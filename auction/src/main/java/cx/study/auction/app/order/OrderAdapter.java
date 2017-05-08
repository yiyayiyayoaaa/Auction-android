package cx.study.auction.app.order;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cx.study.auction.R;
import cx.study.auction.bean.Order;
import cx.study.auction.event.DoOrderEvent;
import cx.study.auction.event.DoOrderEvent.Event;
import cx.study.auction.util.DateUtil;
import cx.study.auction.util.PicassoUtil;

/**
 *
 * Created by cheng.xiao on 2017/4/20.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder>{

    private List<Order> orders;
    private Activity context;

    public OrderAdapter(List<Order> orders,Activity context){
        this.orders = orders;
        this.context = context;
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {
        final Order order = orders.get(position);
        holder.setOrder(order,context);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,OrderActivity.class);
                intent.putExtra("id",order.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.tv_order_num)TextView tvOrderNum;
        @Bind(R.id.tv_status)TextView tvStatus;
        @Bind(R.id.tv_name)TextView tvName;
        @Bind(R.id.tv_price)TextView tvPrice;
        @Bind(R.id.tv_time)TextView tvTime;
        @Bind(R.id.btn_pay)TextView btnPay;
        @Bind(R.id.btn_cancel)TextView btnCancel;
        @Bind(R.id.btn_received)TextView btnReceived;
        @Bind(R.id.image_order)ImageView orderImage;
        @Bind(R.id.layout_tool)LinearLayout layoutTool;
        @Bind(R.id.tv_description)TextView tvDescription;
        private Order order;
        public OrderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setOrder(Order order, Activity context){
            this.order = order;
            tvName.setText(order.getCommodityName());
            tvOrderNum.setText(order.getOrderNum());
            tvPrice.setText("金额："+ order.getPrice() + "元");
            tvTime.setText("创建时间："+ DateUtil.getDateTimeString(order.getStartTime()));
            tvDescription.setText(Html.fromHtml(order.getDescription()));
            PicassoUtil.show(orderImage,order.getUrl());
            switch (order.getStatus()){
                case Order.OrderStatus.WAIT_PAY:
                    tvStatus.setText("待付款");
                    btnReceived.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.VISIBLE);
                    btnPay.setVisibility(View.VISIBLE);
                    layoutTool.setVisibility(View.VISIBLE);
                    break;
                case Order.OrderStatus.WAIT_SEND:
                    tvStatus.setText("待发货");
                    btnReceived.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.VISIBLE);
                    btnPay.setVisibility(View.GONE);
                    layoutTool.setVisibility(View.VISIBLE);
                    break;
                case Order.OrderStatus.WAIT_RECEIVED:
                    tvStatus.setText("待收货");
                    btnPay.setVisibility(View.GONE);
                    btnReceived.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.GONE);
                    layoutTool.setVisibility(View.VISIBLE);
                    break;
                case Order.OrderStatus.FINISH:
                    tvStatus.setText("已完成");
                    layoutTool.setVisibility(View.GONE);
                    break;
                case Order.OrderStatus.CANCEL:
                    tvStatus.setText("已取消");
                    layoutTool.setVisibility(View.GONE);
//                    btnPay.setVisibility(View.GONE);
//                    btnReceived.setVisibility(View.GONE);
//                    btnCancel.setVisibility(View.GONE);
                    break;
            }
        }
        @OnClick({R.id.btn_pay,R.id.btn_cancel,R.id.btn_received})
        @Override
        public void onClick(View v) {
            DoOrderEvent event = new DoOrderEvent();
            event.orderId = order.getId();
            switch (v.getId()){
                case R.id.btn_pay:
                    event.event = Event.PAY;
                    break;
                case R.id.btn_cancel:
                    event.event = Event.CANCEL;
                    break;
                case R.id.btn_received:
                    event.event = Event.RECEIVED;
                    break;
            }
            EventBus.getDefault().post(event);
        }
    }
}
