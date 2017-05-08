package cx.study.auction.app.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cx.study.auction.R;
import cx.study.auction.app.LoginActivity;
import cx.study.auction.app.base.BaseActivity;
import cx.study.auction.event.LogoutEvent;
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
        setTitle("设置");
    }

    @OnClick({R.id.tv_user,R.id.tv_about,R.id.tv_logout})
    @Override
    public void onClick(View v) {
        final Intent intent = new Intent();
        switch (v.getId()){
            case R.id.tv_user:
                intent.setClass(this,UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_about:
                break;
            case R.id.tv_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userDao.logout();
                        intent.setClass(SettingActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                        EventBus.getDefault().post(new LogoutEvent());
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.setMessage("确定要退出登录吗？");
                builder.show();
                break;
        }
    }
}
