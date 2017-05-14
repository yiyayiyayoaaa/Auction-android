package cx.study.auction.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cx.study.auction.R;
import cx.study.auction.app.base.BaseActivity;
import cx.study.auction.app.home.HomeActivity;
import cx.study.auction.bean.User;
import cx.study.auction.event.RegisterEvent;
import cx.study.auction.model.dao.UserDao;
import cx.study.auction.model.rest.UserRest;

/**
 *
 * Created by cheng.xiao on 2017/4/6.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;

    private UserRest userRest;
    private UserDao userDao;
    private boolean isGoHome = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        userRest = new UserRest();
        userDao = new UserDao(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.show();
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        Intent intent = getIntent();
        if (intent != null){
            isGoHome = intent.getBooleanExtra("isGoHome",true);
            if (!isGoHome){
                if (actionBar != null){
                    actionBar.show();
                    actionBar.setHomeButtonEnabled(true);
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
            }
        }
    }

    @Override
    @OnClick({R.id.btn_login,R.id.btn_to_register})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                login(username,password);
                break;
            case R.id.btn_to_register:
                toRegister();
                break;
        }
    }

    private void login(final String username, final String password){
        final WeakReference<Activity> ref = new WeakReference<Activity>(this);
        Task.call(new Callable<User>() {
            @Override
            public User call() throws Exception {
                return userRest.login(username,password);
            }
        },Task.BACKGROUND_EXECUTOR).continueWith(new Continuation<User, Object>() {
            @Override
            public Object then(Task<User> task) throws Exception {
                if (!task.isFaulted() && task.getResult() != null){
                    User user = task.getResult();
                    if (Double.isNaN(user.getAccount())){
                        user.setAccount(0d);
                    }
                    userDao.saveUser(user);
                    if (isGoHome){
                        Intent intent = new Intent(ref.get(), HomeActivity.class);
                        startActivity(intent);
                    }
                    finish();
                } else {
                    Exception error = task.getError();
                    error.printStackTrace();
                    Toast.makeText(ref.get(),error.getMessage(),Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    private void toRegister(){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterEvent(RegisterEvent registerEvent){
        finish();
    }
}
