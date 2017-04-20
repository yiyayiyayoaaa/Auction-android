package cx.study.auction.app.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cx.study.auction.R;
import cx.study.auction.app.LoginActivity;
import cx.study.auction.app.base.BaseFragment;
import cx.study.auction.app.order.OrderListActivity;
import cx.study.auction.bean.User;
import cx.study.auction.model.dao.UserDao;
import cx.study.auction.model.rest.UserRest;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * Created by cheng.xiao on 2017/3/9.
 */

public class UserFragment extends BaseFragment implements View.OnClickListener{
    public static Fragment getInstance(){
        return new UserFragment();
    }
    @Bind(R.id.btn_portrait)
    CircleImageView imageView;
    @Bind(R.id.btn_user_info)
    TextView btnUserInfo;
    @Bind(R.id.btn_view_all_order)
    TextView btnAllOrder;
    @Bind(R.id.btn_wait_pay)
    TextView btnWaitPay;
    @Bind(R.id.btn_wait_send)
    TextView btnWaitSend;
    @Bind(R.id.btn_wait_received)
    TextView btnWaitReceived;
    @Bind(R.id.btn_address_manager)
    TextView btnAddress;
    @Bind(R.id.btn_account)
    TextView btnAccount;
    @Bind(R.id.btn_recharge)
    TextView btnRecharge;
    @Bind(R.id.layout_not_login)
    TextView layout_not_login;
    @Bind(R.id.user1)
    LinearLayout user1;
    @Bind(R.id.user2)
    LinearLayout user2;
    @Bind(R.id.textView)
    TextView tvNickname;
    UserRest userRest;
    UserDao userDao;
    User user;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRest = new UserRest();
        userDao = new UserDao(getActivity());
        user = userDao.getLocalUser();
    }

    private Task<User> loadUserInfo(){
        return Task.callInBackground(new Callable<User>() {
            @Override
            public User call() throws Exception {
                return userRest.getUserInfo(user.getId());
            }
        }).continueWith(new Continuation<User, User>() {
            @Override
            public User then(Task<User> task) throws Exception {
                if (!task.isFaulted()){
                    user = task.getResult();
                    userDao.saveUser(user);
                    initView();
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    private void initView(){
        tvNickname.setText(user.getNickname());
        // TODO: 2017/4/20 设置头像 待完成
        btnAccount.setText("余额：¥" + user.getAccount()+"元");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (user != null){
            loadUserInfo();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_user,container,false);
        ButterKnife.bind(this,view);
        UserDao userDao = new UserDao(getActivity());
        User user = userDao.getLocalUser();
        if (user == null){
            user1.setVisibility(View.GONE);
            user2.setVisibility(View.GONE);
            layout_not_login.setVisibility(View.VISIBLE);
        } else {
            user1.setVisibility(View.VISIBLE);
            user2.setVisibility(View.VISIBLE);
            layout_not_login.setVisibility(View.GONE);
        }
        return view;
    }

    @OnClick({R.id.btn_portrait,R.id.btn_user_info,R.id.btn_view_all_order,R.id.btn_wait_pay,R.id.btn_wait_send,
            R.id.btn_wait_received,R.id.btn_address_manager,R.id.btn_account,R.id.btn_recharge,R.id.layout_not_login})
    @Override
    public void onClick(View v) {
        Intent orderIntent = new Intent(getActivity(), OrderListActivity.class);
        switch (v.getId()){
            case R.id.btn_user_info:
                Intent i = new Intent(getActivity(),UserInfoActivity.class);
                startActivity(i);
                break;
            case R.id.btn_address_manager:
                Intent intent = new Intent(getActivity(),UserAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_recharge:
                recharge();
                break;
            case R.id.layout_not_login:
                Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent1);
                getActivity().finish();
                break;
            case R.id.btn_wait_pay:
                orderIntent.putExtra("type",1);
                startActivity(orderIntent);
                break;
            case R.id.btn_wait_send:
                orderIntent.putExtra("type",2);
                startActivity(orderIntent);
                break;
            case R.id.btn_wait_received:
                orderIntent.putExtra("type",3);
                startActivity(orderIntent);
                break;
            case R.id.btn_view_all_order:
                startActivity(orderIntent);
                break;
        }
    }

    private void recharge(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请输入要充值的金额");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog_bid_price, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        textView.setText("金额(¥):");
        final EditText editText = (EditText) view.findViewById(R.id.et_bid_price);
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton("确定",null);
        builder.setNegativeButton("取消",null);
        final AlertDialog dialog = builder.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (!TextUtils.isEmpty(text)){
                    //充值
                    postMoney(Double.parseDouble(text));
                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(),"请输入正确的金额",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Task<Boolean> postMoney(final double money){
        return Task.callInBackground(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return userRest.recharge(user.getId(),money);
            }
        }).continueWith(new Continuation<Boolean, Boolean>() {
            @Override
            public Boolean then(Task<Boolean> task) throws Exception {
                if (!task.isFaulted()){
                    loadUserInfo();
                }
                return null;
            }
        });
    }
}
