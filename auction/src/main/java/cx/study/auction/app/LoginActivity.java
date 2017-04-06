package cx.study.auction.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cx.study.auction.R;
import cx.study.auction.app.base.BaseActivity;

/**
 *
 * Created by cheng.xiao on 2017/4/6.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.show();
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    @OnClick({R.id.btn_login,R.id.btn_to_register})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_to_register:
                toRegister();
                break;
        }
    }

    private void login(){
//        Intent intent = new Intent(this,RegisterActivity.class);
//        startActivity(intent);
    }

    private void toRegister(){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}
