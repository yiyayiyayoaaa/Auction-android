package cx.study.auction.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cx.study.auction.R;
import cx.study.auction.app.LoginActivity;
import cx.study.auction.app.base.BaseFragment;
import cx.study.auction.bean.User;
import cx.study.auction.model.dao.UserDao;
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
        switch (v.getId()){
            case R.id.btn_address_manager:
                Intent intent = new Intent(getActivity(),UserAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_not_login:
                Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent1);
                getActivity().finish();
                break;
        }
    }
}
