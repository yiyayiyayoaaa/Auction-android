package cx.study.auction.app.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cx.study.auction.R;
import cx.study.auction.app.auction.AuctionFragment;
import cx.study.auction.app.base.BaseActivity;
import cx.study.auction.app.type.TypeFragment;
import cx.study.auction.app.user.UserFragment;

public class HomeActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fragment_container)
    FrameLayout frameLayout;
    @Bind(R.id.bnv_bottom_menu)
    BottomNavigationView bottomMenu;
    @Bind(R.id.tv_search)
    TextView tv_search;

    Fragment homeFragment;
    Fragment typeFragment;
    Fragment userFragment;
    Fragment auctionFragment;

    private void initHomeToolBar(){
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
            setSupportActionBar(toolbar);
            toolbar.setTitle(getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void initOtherToolBar(){
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
            initToolbar();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.colorPrimaryDark));
        }
    }
    private void initToolbarByOrange(){
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
            initToolbar();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(android.R.color.holo_orange_light));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initHomeToolBar();
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
                hideAllFragment(transaction);
                switch (item.getItemId()){
                    case R.id.homePage:
                        initHomeToolBar();
                        setSingleAction( 0, "" , null);
                        tv_search.setVisibility(View.VISIBLE);
                        if (homeFragment == null){
                            homeFragment = HomeFragment.getInstance();
                            transaction.add(R.id.fragment_container,homeFragment);
                        } else {
                            transaction.show(homeFragment);
                        }
                        break;
                    case R.id.type:
                        initHomeToolBar();
                        setSingleAction( 0, "" , null);
                        tv_search.setVisibility(View.VISIBLE);
                        if (typeFragment == null){
                            typeFragment = TypeFragment.getInstance();
                            transaction.add(R.id.fragment_container,typeFragment);
                        } else {
                            transaction.show(typeFragment);
                        }
                        break;
                    case R.id.auction:
                        initOtherToolBar();
                        setSingleAction( 0, "" , null);
                        tv_search.setVisibility(View.GONE);
                        if (auctionFragment == null){
                            auctionFragment = AuctionFragment.getInstance();
                            transaction.add(R.id.fragment_container,auctionFragment);
                        } else {
                            transaction.show(auctionFragment);
                        }
                        break;
                    case R.id.user:
                        initToolbarByOrange();
                        setSingleAction(R.drawable.icon_setting, "设置", getResources().getDrawable(R.drawable.icon_setting));
                        tv_search.setVisibility(View.GONE);
                        if (userFragment == null){
                            userFragment = UserFragment.getInstance();
                            transaction.add(R.id.fragment_container,userFragment);
                        } else {
                            transaction.show(userFragment);
                        }
                        break;
                }
                transaction.commit();
                return true;
            }
        });
    }

    private void hideAllFragment(FragmentTransaction transaction) {
        if (homeFragment != null){
            transaction.hide(homeFragment);
        }
        if (typeFragment != null){
            transaction.hide(typeFragment);
        }
        if (auctionFragment != null){
            transaction.hide(auctionFragment);
        }
        if (userFragment != null){
            transaction.hide(userFragment);
        }
    }

    @OnClick(R.id.tv_search)
    public void search(TextView textView){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

}
