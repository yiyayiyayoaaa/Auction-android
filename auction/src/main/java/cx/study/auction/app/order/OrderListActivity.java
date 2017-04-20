package cx.study.auction.app.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.google.common.collect.Lists;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.app.base.BaseActivity;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        setTitle("我的订单");
        int type = getIntent().getIntExtra("type", 0);
        init(type);
    }



    private void init(int type){
        titles.add("全部");
        titles.add("待支付");
        titles.add("待发货");
        titles.add("待接收");
        titles.add("已取消");
        Fragment fragmentAll = OrderListFragment.getInstance(titles.get(0));
        Fragment fragmentWaitPay = OrderListFragment.getInstance(titles.get(1));
        Fragment fragmentWaitSend = OrderListFragment.getInstance(titles.get(2));
        Fragment fragmentWaitReceived = OrderListFragment.getInstance(titles.get(3));
        Fragment fragmentIsCancel = OrderListFragment.getInstance(titles.get(4));
        fragments.add(fragmentAll);
        fragments.add(fragmentWaitPay);
        fragments.add(fragmentWaitSend);
        fragments.add(fragmentWaitReceived);
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
}
