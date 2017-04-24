package cx.study.auction.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.app.LoginActivity;
import cx.study.auction.app.base.BaseFragment;

/**
 *
 * Created by cheng.xiao on 2017/4/24.
 */

public class UserNotLoginFragment extends BaseFragment implements View.OnClickListener{
    @Bind(R.id.layout_not_login)
    TextView layoutNotLogin;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_not_login, container, false);
        ButterKnife.bind(this,view);
        layoutNotLogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra("isGoHome",false);
        startActivity(intent);
    }
}
