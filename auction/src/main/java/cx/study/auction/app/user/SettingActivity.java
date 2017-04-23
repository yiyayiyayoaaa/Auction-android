package cx.study.auction.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cx.study.auction.R;
import cx.study.auction.app.LoginActivity;
import cx.study.auction.app.base.BaseActivity;
import cx.study.auction.model.dao.UserDao;

/**
 *
 * Created by chedngxiao on 2017/4/23.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.tv_user)TextView tvUser;
    @Bind(R.id.tv_about)TextView tvAbout;
    @Bind(R.id.tv_logout)TextView tvLogout;
    UserDao userDao;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        userDao = new UserDao(this);
    }

    @OnClick({R.id.tv_user,R.id.tv_about,R.id.tv_logout})
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.tv_user:
                intent.setClass(this,UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_about:
                break;
            case R.id.tv_logout:
                userDao.logout();
                intent.setClass(this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
