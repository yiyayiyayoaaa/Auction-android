package cx.study.auction.app.auction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.app.base.BaseFragment;

/**
 *
 * Created by cheng.xiao on 2017/3/9.
 */

public class AuctionFragment extends BaseFragment{

    public static Fragment getInstance() {
        return new AuctionFragment();
    }

    @Bind(R.id.recycle_view)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auction,null);
        ButterKnife.bind(this,view);
        return view;
    }
}
