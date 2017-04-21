package cx.study.auction.app.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.app.base.BaseFragment;
import cx.study.auction.bean.Order;
import cx.study.auction.bean.User;
import cx.study.auction.event.RefreshEvent;
import cx.study.auction.model.dao.UserDao;
import cx.study.auction.model.rest.OrderRest;

/**
 *
 * Created by cheng.xiao on 2017/4/20.
 */

public class OrderListFragment extends BaseFragment{
    private String type ;
    public static Fragment getInstance(String type){
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        Fragment fragment = new OrderListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Bind(R.id.recycle_view)
    RecyclerView recyclerView;
    private int status;
    List<Order> orders = Lists.newArrayList();
    OrderAdapter adapter;
    OrderRest orderRest;
    User user;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
        orderRest = new OrderRest();
        UserDao userDao = new UserDao(getActivity());
        user = userDao.getLocalUser();
        if (type != null){
            switch (type){
                case "全部":
                    status = -1;
                    break;
                case "待支付":
                    status = Order.OrderStatus.WAIT_PAY;
                    break;
                case "待发货":
                    status = Order.OrderStatus.WAIT_SEND;
                    break;
                case "待接收":
                    status = Order.OrderStatus.WAIT_RECEIVED;
                    break;
                case "已完成":
                    status = Order.OrderStatus.FINISH;
                    break;
                case "已取消":
                    status = Order.OrderStatus.CANCEL;
                    break;
            }
        }
        adapter  = new OrderAdapter(orders,getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragmen_order_list, container, false);
        ButterKnife.bind(this,view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        geOrderList();
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    private Task<List<Order>> geOrderList(){
        return Task.callInBackground(new Callable<List<Order>>() {
            @Override
            public List<Order> call() throws Exception {
                return orderRest.getOrderList(user.getId(),status);
            }
        }).continueWith(new Continuation<List<Order>, List<Order>>() {
            @Override
            public List<Order> then(Task<List<Order>> task) throws Exception {
                if (!task.isFaulted()){
                    orders.clear();
                    orders.addAll(task.getResult());
                }
                adapter.notifyDataSetChanged();
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(RefreshEvent event){
        geOrderList();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
