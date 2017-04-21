package cx.study.auction.app.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.app.base.BaseActivity;
import cx.study.auction.app.user.UserAddressActivity;
import cx.study.auction.bean.Order;
import cx.study.auction.contants.HttpRest;
import cx.study.auction.model.rest.OrderRest;
import cx.study.auction.util.DateUtil;

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
    Order order;
    OrderRest orderRest;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        ButterKnife.bind(this);
        int id = getIntent().getIntExtra("id", -1);
        orderRest = new OrderRest();
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
               String[] strings = order.getAddress().split("@");
               address = "收货人：" + strings[0] + "\n\n手机号：" + strings[1] + "\n\n收货地址：" + strings[2] + " " + strings[3];
           }
           StringBuilder orderInfo = new StringBuilder("订单编号：")
                   .append(order.getOrderNum())
                   .append("\n\n").append("创建时间：")
                   .append(DateUtil.getDateTimeString(order.getStartTime()));
           tvAddress.setText(address);
           tvName.setText(order.getCommodityName());
           tvPrice.setText("成交价："+ order.getPrice() + "元");
           tvDescription.setText(order.getDescription());
           Picasso.with(this)
                   .load(HttpRest.BASE_URL + order.getUrl())
                   .into(orderImage);
           switch (order.getStatus()){
               case Order.OrderStatus.WAIT_PAY:
                   tvStatus.setText("待付款");
                   btnReceived.setVisibility(View.GONE);
                   btnCancel.setVisibility(View.VISIBLE);
                   btnPay.setVisibility(View.VISIBLE);
                   tvAddress.setOnClickListener(this);
                   tvAddress.setText("点击设置收货地址");
                   break;
               case Order.OrderStatus.WAIT_SEND:
                   tvStatus.setText("待发货");
                   btnReceived.setVisibility(View.GONE);
                   btnCancel.setVisibility(View.VISIBLE);
                   btnPay.setVisibility(View.GONE);
                   orderInfo.append("\n\n").append("支付时间：")
                           .append(DateUtil.getDateTimeString(order.getPayTime()));
                   break;
               case Order.OrderStatus.WAIT_RECEIVED:
                   tvStatus.setText("待收货");
                   btnPay.setVisibility(View.GONE);
                   btnReceived.setVisibility(View.VISIBLE);
                   btnCancel.setVisibility(View.GONE);
                   orderInfo.append("\n").append("支付时间：")
                           .append(DateUtil.getDateTimeString(order.getPayTime()));
                   break;
               case Order.OrderStatus.FINISH:
                   tvStatus.setText("交易完成");
                   btnReceived.setVisibility(View.GONE);
                   btnCancel.setVisibility(View.VISIBLE);
                   btnPay.setVisibility(View.GONE);
                   orderInfo.append("\n\n").append("支付时间：")
                           .append(DateUtil.getDateTimeString(order.getPayTime()))
                           .append("\n\n").append("结束时间：")
                           .append(DateUtil.getDateTimeString(order.getEndTime()));
                   break;
               case Order.OrderStatus.CANCEL:
                   tvStatus.setText("交易取消");
                   btnPay.setVisibility(View.GONE);
                   btnReceived.setVisibility(View.GONE);
                   btnCancel.setVisibility(View.GONE);
                   orderInfo.append("\n\n").append("支付时间：")
                           .append(DateUtil.getDateTimeString(order.getPayTime()))
                           .append("\n\n").append("结束时间：")
                           .append(DateUtil.getDateTimeString(order.getEndTime()));
                   break;
           }
           tvOrderInfo.setText(orderInfo.toString());
       }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_address:
                Intent intent = new Intent(this, UserAddressActivity.class);
                startActivityForResult(intent,1);
                break;
        }
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
