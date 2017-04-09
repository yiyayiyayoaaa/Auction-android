package cx.study.auction.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

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
import cx.study.auction.model.rest.UserRest;
import cx.study.auction.model.rest.http.MCException;

/**
 *
 * Created by cheng.xiao on 2017/4/6.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    UserRest userRest;
    String username;
    String password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.show();
        }
        userRest = new UserRest();
    }

    @Override
    @OnClick({R.id.btn_register})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                doRegister();
                break;
        }
    }

    private void doRegister(){
        final WeakReference<Activity> ref = new WeakReference<Activity>(this);
        register().continueWithTask(new Continuation<Boolean, Task<User>>() {
            @Override
            public Task<User> then(Task<Boolean> task) throws Exception {
                if (task.getResult()) {
                    return login();
                }else{
                    return null;
                }
            }
        }).continueWith(new Continuation<User, Object>() {

            @Override
            public Object then(Task<User> task) throws Exception {
                if (!task.isFaulted()){
                    Intent intent = new Intent(ref.get(), HomeActivity.class);
                    startActivity(intent);
                    EventBus.getDefault().post(new RegisterEvent());
                } else {
                    Exception error = task.getError();
                    error.printStackTrace();
                }
                return null;
            }
        });
    }

    private Task<Boolean> register(){
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
        return Task.callInBackground(new Callable<Boolean>() {
            @Override
            public Boolean call() throws MCException {
                return userRest.register(username,password);
            }
        });
    }

    private Task<User> login(){
        return Task.callInBackground(new Callable<User>() {
            @Override
            public User call() throws Exception {
                return userRest.login(username,password);
            }
        });
    }
}
