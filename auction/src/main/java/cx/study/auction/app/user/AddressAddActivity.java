package cx.study.auction.app.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Date;
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
import cx.study.auction.cascade.activity.MainActivity;
import cx.study.auction.model.dao.UserDao;
import cx.study.auction.model.rest.UserAddressRest;
import cx.study.auction.util.MCProgress;

/**
 *
 * Created by cheng.xiao on 2017/4/19.
 */

public class AddressAddActivity extends BaseActivity implements View.OnClickListener{
    private static final int ADD = 0;
    private static final int UPDATE = 1;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.et_user)
    EditText etUser;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_address)
    EditText etAddress;
    String address1;
    UserAddress userAddress;
    User localUser;
    UserAddressRest addressRest;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!= null){
            actionBar.show();
        }
        boolean isAdd = getIntent().getBooleanExtra("isAdd", true);
        userAddress = (UserAddress) getIntent().getSerializableExtra("userAddress");
        if (isAdd) {
            setTitle("添加新地址");
            setSingleAction(ADD, "保存", null);
        } else {
            setTitle("编辑地址");
            setSingleAction(UPDATE, "保存", null);
            initView();
        }
        UserDao dao = new UserDao(this);
        localUser = dao.getLocalUser();
        addressRest = new UserAddressRest();
    }

    private void initView() {
        String address = userAddress.getAddress();
        String[] split = address.split("@");
        etUser.setText(split[0]);
        etPhone.setText(split[1]);
        tvAddress.setText(split[2]);
        address1 = split[2];
        etAddress.setText(split[3]);
    }

    @OnClick({R.id.tv_address})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_address:
                startActivityForResult(new Intent(this, MainActivity.class),1);
                break;

        }
    }

    @Override
    public void onActionClick(ActionItem item) {
        super.onActionClick(item);
        if (!checkInput()){
            return;
        }
        switch (item.getItemId()){
            case ADD:
                add();
                break;
            case UPDATE:
                update();
                break;
        }

    }

    private Task<Void> update(){
        MCProgress.show("添加中",this);
        final WeakReference<Activity> ref = new WeakReference<Activity>(this);
        return Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                addressRest.update(userAddress);
                return null;
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                Activity context = ref.get();
                MCProgress.dismiss();
                if (!task.isFaulted()){
                    Toast.makeText(context,"添加成功",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(context,task.getError().getMessage(),Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    private Task<Void> add(){
        MCProgress.show("添加中",this);
        final WeakReference<Activity> ref = new WeakReference<Activity>(this);
        return Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                addressRest.add(userAddress);
                return null;
            }
        }).continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(Task<Void> task) throws Exception {
                Activity context = ref.get();
                MCProgress.dismiss();
                if (!task.isFaulted()){
                    Toast.makeText(context,"添加成功",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(context,task.getError().getMessage(),Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    private boolean checkInput() {
        String phone = etPhone.getText().toString();
        String user = etUser.getText().toString();
        String address2 = etAddress.getText().toString();
        if (TextUtils.isEmpty(user)){
            Toast.makeText(this,"收货人不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this,"手机号不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(address1)){
            Toast.makeText(this,"请选择所在地区",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(address2) || address2.length() < 5){
            Toast.makeText(this,"详细地址不得少于5个字",Toast.LENGTH_SHORT).show();
            return false;
        }
       if (userAddress == null){
           userAddress = new UserAddress();
       }
        userAddress.setUserId(localUser.getId());
        userAddress.setTime(new Date());
        userAddress.setAddress(user + "@" + phone+ "@" + address1 + "@" +address2.replace("@",""));
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (data != null){
           address1 = data.getStringExtra("address");
           tvAddress.setText(address1);

       }
    }
}
