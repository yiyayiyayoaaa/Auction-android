package cx.study.auction.app.order;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cx.study.auction.R;
import cx.study.auction.app.base.BaseActivity;
import cx.study.auction.app.user.UserAddressActivity;
import cx.study.auction.bean.Order;
import cx.study.auction.event.RefreshEvent;
import cx.study.auction.model.rest.OrderRest;
import cx.study.auction.model.rest.http.MCException;
import cx.study.auction.util.DateUtil;
import cx.study.auction.util.MCProgress;
import cx.study.auction.util.PicassoUtil;

/**
 *
 * Created by cheng.xiao on 2017/4/21.
 */

public class OrderActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.tv_order_info)TextView tvOrderInfo;
    @Bind(R.id.tv_description)TextView tvDescription;
    @Bind(R.id.tv_status)TextView tvStatus;
    @Bind(R.id.tv_address)TextView tvAddress;
    @Bind(R.id.tv_name)TextView tvName;
    @Bind(R.id.tv_price)TextView tvPrice;
    @Bind(R.id.btn_pay)TextView btnPay;
    @Bind(R.id.btn_cancel)TextView btnCancel;
    @Bind(R.id.btn_received)TextView btnReceived;
    @Bind(R.id.image_order)ImageView orderImage;
    @Bind(R.id.layout_tool)LinearLayout layoutTool;
    Order order;
    OrderRest orderRest;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        ButterKnife.bind(this);
        int id = getIntent().getIntExtra("id", -1);
        orderRest = new OrderRest();
        setTitle("订单详情");
        getOrderInfo(id);
    }

    private Task<Order> getOrderInfo(final int id){
        return Task.callInBackground(new Callable<Order>() {
            @Override
            public Order call() throws Exception {
                return orderRest.getOrderById(id);
            }
        }).continueWith(new Continuation<Order, Order>() {
            @Override
            public Order then(Task<Order> task) throws Exception {
                if (!task.isFaulted()){
                    order = task.getResult();
                    initView();
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    private void initView() {
       if (order != null){
           String address = null;
           if (!TextUtils.isEmpty(order.getAddress())) {
               String[] strings;
               if (order.getAddress().contains("@")){
                strings = order.getAddress().split("@");
               } else {
                   strings = order.getAddress().split(" ");
               }
               address = "收货人：" + strings[0] + "\n\n手机号：" + strings[1] + "\n\n收货地址：" + strings[2] + " " + strings[3];
           }
           StringBuilder orderInfo = new StringBuilder("订单编号：")
                   .append(order.getOrderNum())
                   .append("\n\n").append("创建时间：")
                   .append(DateUtil.getDateTimeString(order.getStartTime()));
           tvAddress.setText(address);
           tvName.setText(order.getCommodityName());
           tvPrice.setText("成交价："+ order.getPrice() + "元");
           tvDescription.setText(Html.fromHtml(order.getDescription()));
           PicassoUtil.show(orderImage,order.getUrl());
           switch (order.getStatus()){
               case Order.OrderStatus.WAIT_PAY:
                   tvStatus.setText("待付款");
                   btnReceived.setVisibility(View.GONE);
                   btnCancel.setVisibility(View.VISIBLE);
                   btnPay.setVisibility(View.VISIBLE);
                   tvAddress.setOnClickListener(this);
                   tvAddress.setText("点击设置收货地址");
                   layoutTool.setVisibility(View.VISIBLE);
                   break;
               case Order.OrderStatus.WAIT_SEND:
                   tvStatus.setText("待发货");
                   btnReceived.setVisibility(View.GONE);
                   btnCancel.setVisibility(View.VISIBLE);
                   btnPay.setVisibility(View.GONE);
                   layoutTool.setVisibility(View.VISIBLE);
                   orderInfo.append("\n\n").append("支付时间：")
                           .append(DateUtil.getDateTimeString(order.getPayTime()));
                   break;
               case Order.OrderStatus.WAIT_RECEIVED:
                   tvStatus.setText("待收货");
                   btnPay.setVisibility(View.GONE);
                   btnReceived.setVisibility(View.VISIBLE);
                   btnCancel.setVisibility(View.GONE);
                   layoutTool.setVisibility(View.VISIBLE);
                   orderInfo.append("\n\n").append("支付时间：")
                           .append(DateUtil.getDateTimeString(order.getPayTime()));
                   break;
               case Order.OrderStatus.FINISH:
                   tvStatus.setText("交易完成");
//                   btnReceived.setVisibility(View.GONE);
//                   btnCancel.setVisibility(View.GONE);
//                   btnPay.setVisibility(View.GONE);
                   layoutTool.setVisibility(View.GONE);
                   orderInfo.append("\n\n").append("支付时间：")
                           .append(DateUtil.getDateTimeString(order.getPayTime()))
                           .append("\n\n").append("结束时间：")
                           .append(DateUtil.getDateTimeString(order.getEndTime()));
                   break;
               case Order.OrderStatus.CANCEL:
                   tvStatus.setText("交易取消");
//                   btnPay.setVisibility(View.GONE);
//                   btnReceived.setVisibility(View.GONE);
//                   btnCancel.setVisibility(View.GONE);
                   layoutTool.setVisibility(View.GONE);
                   orderInfo.append("\n\n").append("支付时间：")
                           .append(DateUtil.getDateTimeString(order.getPayTime()))
                           .append("\n\n").append("结束时间：")
                           .append(DateUtil.getDateTimeString(order.getEndTime()));
                   break;
           }
           tvOrderInfo.setText(orderInfo.toString());
       }
    }
    @OnClick({R.id.btn_pay,R.id.btn_cancel,R.id.btn_received})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_address:
                Intent intent = new Intent(this, UserAddressActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.btn_pay:
                if (TextUtils.isEmpty(order.getAddress())){
                    Toast.makeText(this,"请选择收货地址",Toast.LENGTH_SHORT).show();
                    return;
                }
                doPay(order.getId(),order.getAddress());
                break;
            case R.id.btn_cancel:
                doCancel(order.getId());
                break;
            case R.id.btn_received:
                doReceived(order.getId());
                break;
        }
    }

    private Task<Void> doCancel(final int id){
        final WeakReference<Activity> ref = new WeakReference<Activity>(this);
        MCProgress.show("操作中",this);
        return Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() throws MCException, MCException {
                orderRest.cancel(id);
                return null;
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws MCException {
                MCProgress.dismiss();
                if (!task.isFaulted()){
                    //geOrderList();
                    Toast.makeText(ref.get(),"取消成功",Toast.LENGTH_SHORT).show();
                    getOrderInfo(order.getId());
                    EventBus.getDefault().post(new RefreshEvent());
                } else {
                    Exception error = task.getError();
                    Toast.makeText(ref.get(),error.getMessage(),Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }


    private Task<Void> doReceived(final int id){
        final WeakReference<Activity> ref = new WeakReference<Activity>(this);
        MCProgress.show("操作中",this);
        return Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() throws MCException, MCException {
                orderRest.finish(id);
                return null;
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws MCException {
                MCProgress.dismiss();
                if (!task.isFaulted()){
                    //geOrderList();
                    Toast.makeText(ref.get(),"交易完成",Toast.LENGTH_SHORT).show();
                    getOrderInfo(order.getId());
                    EventBus.getDefault().post(new RefreshEvent());
                } else {
                    Exception error = task.getError();
                    Toast.makeText(ref.get(),error.getMessage(),Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    private Task<Void> doPay(final int id, final String address){
        final WeakReference<Activity> ref = new WeakReference<Activity>(this);
        MCProgress.show("操作中",this);
        return Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                orderRest.pay(id,address);
                return null;
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws MCException {
                MCProgress.dismiss();
                if (!task.isFaulted()){
                    //geOrderList();
                    Toast.makeText(ref.get(),"付款成功",Toast.LENGTH_SHORT).show();
                    getOrderInfo(order.getId());
                    EventBus.getDefault().post(new RefreshEvent());
                } else {
                    Exception error = task.getError();
                    Toast.makeText(ref.get(),error.getMessage(),Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            order.setAddress(data.getStringExtra("address"));
            String[] strings = order.getAddress().split("@");
            String address = "收货人：" + strings[0] + "\n\n手机号：" + strings[1] + "\n\n收货地址：" + strings[2] + " " + strings[3];
            tvAddress.setText(address);
        }
    }
}
