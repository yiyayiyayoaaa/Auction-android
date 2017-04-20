package cx.study.auction.app.order;

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
 * Created by cheng.xiao on 2017/4/20.
 */

public class OrderListFragment extends BaseFragment{
    private String type ;
    public static Fragment getInstance(String type){
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        Fragment fragment = new OrderListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Bind(R.id.recycle_view)
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragmen_order_list, container, false);
        ButterKnife.bind(this,view);
        return view;
    }
}
