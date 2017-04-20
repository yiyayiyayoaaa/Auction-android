package cx.study.auction.app.order;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.bean.Order;

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

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_order_num)TextView tvOrderNum;
        @Bind(R.id.tv_status)TextView tvStatus;
        @Bind(R.id.tv_name)TextView tvName;
        @Bind(R.id.tv_price)TextView tvPrice;
        @Bind(R.id.tv_time)TextView tvTime;
        @Bind(R.id.btn_pay)TextView btnPay;
        @Bind(R.id.btn_cancel)TextView btnCancel;
        @Bind(R.id.btn_received)TextView btnReceived;

        public OrderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setOrder(Order order){

        }
    }
}
