package cx.study.auction.app.home;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.FrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.app.auction.AuctionFragment;
import cx.study.auction.app.base.BaseActivity;
import cx.study.auction.app.type.TypeFragment;
import cx.study.auction.app.user.UserFragment;
import cx.study.auction.app.user.UserNotLoginFragment;
import cx.study.auction.bean.User;
import cx.study.auction.event.LogoutEvent;
import cx.study.auction.event.MoreClickEvent;
import cx.study.auction.model.dao.UserDao;

public class HomeActivity extends BaseActivity {
    @Bind(R.id.fragment_container)
    FrameLayout frameLayout;
    @Bind(R.id.bnv_bottom_menu)
    BottomNavigationView bottomMenu;

    Fragment homeFragment;
    Fragment typeFragment;
    Fragment userFragment;
    Fragment auctionFragment;
    User user;
    private void initHomeToolBar(){
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }

    private void initToolBar(String title){
        if (getSupportActionBar() != null){
            getSupportActionBar().show();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.color_blue));
        }
        setTitle(title);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initHomeToolBar();
        EventBus.getDefault().register(this);
        init();
    }

    private void init(){
        user = new UserDao(this).getLocalUser();
        final FragmentManager fm = getSupportFragmentManager();

        homeFragment = fm.findFragmentById(R.id.fragment_container);
        if (homeFragment == null){
            homeFragment = HomeFragment.getInstance();
            fm.beginTransaction()
                    .add(R.id.fragment_container,homeFragment)
                    .commit();
        }
        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fm.beginTransaction();
                switch (item.getItemId()){
                    case R.id.homePage:
                        initHomeToolBar();
                        if (homeFragment == null) {
                            homeFragment = HomeFragment.getInstance();
                        }
                        transaction.replace(R.id.fragment_container,homeFragment);
                        break;
                    case R.id.type:
                        initToolBar("类别");
                        if (typeFragment == null) {
                            typeFragment = TypeFragment.getInstance();
                        }
                        transaction.replace(R.id.fragment_container,typeFragment);
                        break;
                    case R.id.auction:
                        initToolBar("我的拍卖");
                        if (user == null){
                            Fragment fragment = new UserNotLoginFragment();
                            transaction.replace(R.id.fragment_container,fragment);
                        } else {
                            if (auctionFragment == null) {
                                auctionFragment = AuctionFragment.getInstance();
                            }
                            transaction.replace(R.id.fragment_container, auctionFragment);
                        }
                        break;
                    case R.id.user:
                        initToolBar("我的信息");
                        if (user == null){
                            Fragment fragment = new UserNotLoginFragment();
                            transaction.replace(R.id.fragment_container,fragment);
                        } else {
                            if (userFragment == null) {
                                userFragment = UserFragment.getInstance();
                            }
                            transaction.replace(R.id.fragment_container,userFragment);
                        }

                        break;
                }
                transaction.commit();
                return true;
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        //init();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoreClickEvent(MoreClickEvent event){
        initToolBar("类别");
        bottomMenu.getMenu().getItem(1).setChecked(true);
        FragmentManager fm = getSupportFragmentManager();
        if (typeFragment == null) {
            typeFragment = TypeFragment.getInstance();
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("type",event.type);
        typeFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.fragment_container,typeFragment).commit();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutEvent(LogoutEvent event){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
