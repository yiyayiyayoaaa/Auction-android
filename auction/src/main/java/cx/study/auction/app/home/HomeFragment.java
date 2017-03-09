package cx.study.auction.app.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import cx.study.auction.R;

/**
 *
 * Created by cheng.xiao on 2017/3/9.
 */

public class HomeFragment extends Fragment{

    public static Fragment getInstance(){
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,null);
        ButterKnife.bind(this,view);
        return view;
    }
}
