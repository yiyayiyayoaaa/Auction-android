package cx.study.auction.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import cx.study.auction.model.dao.UserDao;
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
    @Bind(R.id.et_rePassword)
    EditText etRePassword;
    @Bind(R.id.rg_gender)
    RadioGroup radioGroup;
    UserRest userRest;
    String username;
    String password;
    User user;
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
                if (checkInput()) {
                    doRegister();
                }
                break;
        }
    }

    private void doRegister(){
        final WeakReference<Activity> ref = new WeakReference<Activity>(this);
        register().continueWithTask(new Continuation<Boolean, Task<User>>() {
            @Override
            public Task<User> then(Task<Boolean> task) throws MCException {
                if (!task.isFaulted()) {
                    if (task.getResult()) {
                        Toast.makeText(ref.get(),"注册成功",Toast.LENGTH_SHORT).show();
                        return login();
                    }
                }else {
                    Exception error = task.getError();
                    error.printStackTrace();
                    Toast.makeText(ref.get(),error.getMessage(),Toast.LENGTH_SHORT).show();
                    throw new MCException(error);
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }
    private boolean checkInput(){
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
        int id = radioGroup.getCheckedRadioButtonId();
        Log.d("radio", "checkInput: " + id);
        String rePassword = etRePassword.getText().toString();
        if (TextUtils.isEmpty(username.trim())){
            Toast.makeText(this,"用户名不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password.trim())){
            Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(rePassword)){
            Toast.makeText(this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (id == -1){
            Toast.makeText(this,"请选择性别",Toast.LENGTH_SHORT).show();
            return false;
        }
        user = new User();
        user.setUsername(username);
        user.setPassword(password);
        switch (id){
            case R.id.rb_nan:
                user.setGender(0);
                break;
            case R.id.rb_nv:
                user.setGender(1);
                break;
        }
        return true;
    }

    private Task<Boolean> register(){
        return Task.callInBackground(new Callable<Boolean>() {
            @Override
            public Boolean call() throws MCException {
                return userRest.register(user);
            }
        });
    }

    private Task<User> login(){
        final WeakReference<Activity> ref = new WeakReference<Activity>(this);
        return Task.callInBackground(new Callable<User>() {
            @Override
            public User call() throws Exception {
                return userRest.login(username,password);
            }
        }).continueWith(new Continuation<User, User>() {
            @Override
            public User then(Task<User> task) throws Exception {
                if (!task.isFaulted()){
                    if (task.getResult() != null) {
                        UserDao userDao = new UserDao(ref.get());
                        userDao.saveUser(task.getResult());
                        Intent intent = new Intent(ref.get(), HomeActivity.class);
                        EventBus.getDefault().post(new RegisterEvent());
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Exception error = task.getError();
                    error.printStackTrace();
                    Toast.makeText(ref.get(),error.getMessage(),Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }
}
