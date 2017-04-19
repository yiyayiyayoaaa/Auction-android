package cx.study.auction.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cx.study.auction.R;
import cx.study.auction.app.base.BaseFragment;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_user,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick({R.id.btn_portrait,R.id.btn_user_info,R.id.btn_view_all_order,R.id.btn_wait_pay,R.id.btn_wait_send,
            R.id.btn_wait_received,R.id.btn_address_manager,R.id.btn_account,R.id.btn_recharge})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_address_manager:
                Intent intent = new Intent(getActivity(),UserAddressActivity.class);
                startActivity(intent);
                break;
        }
    }
}
