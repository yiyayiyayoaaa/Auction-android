package cx.study.auction.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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
import butterknife.OnClick;
import cx.study.auction.R;
import cx.study.auction.app.base.BaseActivity;
import cx.study.auction.bean.User;
import cx.study.auction.bean.UserAddress;
import cx.study.auction.event.AddressDeleteEvent;
import cx.study.auction.model.dao.UserDao;
import cx.study.auction.model.rest.UserAddressRest;

/**
 *
 * Created by cheng.xiao on 2017/4/17.
 */

public class UserAddressActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.recycle_view)
    RecyclerView recyclerView;
    @Bind(R.id.btn_add_address)
    TextView btnAddAddress;
    AddressAdapter adapter;
    List<UserAddress> list = Lists.newArrayList();
    UserAddressRest addressRest;
    User localUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_address);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
        setTitle("管理收货地址");
        addressRest = new UserAddressRest();
        UserDao dao = new UserDao(this);
        localUser = dao.getLocalUser();
        initRecycleView();
    }

    private Task<List<UserAddress>> loadData(){
        return Task.callInBackground(new Callable<List<UserAddress>>() {
            @Override
            public List<UserAddress> call() throws Exception {
                return addressRest.getUserAddressList(localUser.getId());
            }
        }).continueWith(new Continuation<List<UserAddress>, List<UserAddress>>() {
            @Override
            public List<UserAddress> then(Task<List<UserAddress>> task) throws Exception {
                list.clear();
                if (!task.isFaulted()){
                    list.addAll(task.getResult());
                } else {

                }
                adapter.notifyDataSetChanged();
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    private void initRecycleView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressAdapter(list,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @OnClick(R.id.btn_add_address)
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,AddressAddActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddressDeleteEvent(AddressDeleteEvent event){
        loadData();
    }
}
