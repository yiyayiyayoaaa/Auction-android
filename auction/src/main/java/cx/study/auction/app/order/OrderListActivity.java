package cx.study.auction.app.order;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.google.common.collect.Lists;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.app.base.BaseActivity;
import cx.study.auction.event.DoOrderEvent;
import cx.study.auction.event.RefreshEvent;
import cx.study.auction.model.rest.OrderRest;
import cx.study.auction.model.rest.http.MCException;
import cx.study.auction.util.MCProgress;

import static cx.study.auction.event.DoOrderEvent.Event;

/**
 *
 * Created by cheng.xiao on 2017/4/20.
 */

public class OrderListActivity extends BaseActivity{
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    List<Fragment> fragments = Lists.newArrayList();
    List<String> titles = Lists.newArrayList();

    OrderRest orderRest;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        setTitle("我的订单");
        int type = getIntent().getIntExtra("type", 0);
        init(type);
        orderRest = new OrderRest();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDoOrderEvent(DoOrderEvent event){
        switch (event.event){
            case Event.PAY:
                doPay(event.orderId);
                break;
            case Event.CANCEL:
                doCancel(event.orderId);
                break;
            case Event.RECEIVED:
                break;
        }
    }

    private Task<Void> doPay(final int id){
        final WeakReference<Activity> ref = new WeakReference<Activity>(this);
        MCProgress.show("操作中",this);
        return Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                orderRest.pay(id);
                return null;
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws MCException {
                MCProgress.dismiss();
                if (!task.isFaulted()){
                    //geOrderList();
                    EventBus.getDefault().post(new RefreshEvent());
                } else {
                    Exception error = task.getError();
                    Toast.makeText(ref.get(),error.getMessage(),Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    private Task<Void> doCancel(final int id){
        final WeakReference<Activity> ref = new WeakReference<Activity>(this);
        MCProgress.show("操作中",this);
        return Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() throws MCException {
                orderRest.cancel(id);
                return null;
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws MCException {
                MCProgress.dismiss();
                if (!task.isFaulted()){
                    //geOrderList();
                    EventBus.getDefault().post(new RefreshEvent());
                } else {
                    Exception error = task.getError();
                    Toast.makeText(ref.get(),error.getMessage(),Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    private void init(int type){
        titles.add("全部");
        titles.add("待支付");
        titles.add("待发货");
        titles.add("待接收");
        titles.add("已完成");
        titles.add("已取消");
        Fragment fragmentAll = OrderListFragment.getInstance(titles.get(0));
        Fragment fragmentWaitPay = OrderListFragment.getInstance(titles.get(1));
        Fragment fragmentWaitSend = OrderListFragment.getInstance(titles.get(2));
        Fragment fragmentWaitReceived = OrderListFragment.getInstance(titles.get(3));
        Fragment fragmentIsFinish = OrderListFragment.getInstance(titles.get(4));
        Fragment fragmentIsCancel = OrderListFragment.getInstance(titles.get(5));
        fragments.add(fragmentAll);
        fragments.add(fragmentWaitPay);
        fragments.add(fragmentWaitSend);
        fragments.add(fragmentWaitReceived);
        fragments.add(fragmentIsFinish);
        fragments.add(fragmentIsCancel);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(3)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(4)));
        OrderViewPagerAdapter adapter = new OrderViewPagerAdapter(getSupportFragmentManager(),fragments,titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(type);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
