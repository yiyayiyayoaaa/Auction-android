package cx.study.auction.app.commodity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.app.base.BaseFragment;
import cx.study.auction.bean.Commodity;

/**
 *
 * Created by cheng.xiao on 2017/4/17.
 */

public class CommodityListFragment extends BaseFragment {
    @Bind(R.id.recycle_view)
    RecyclerView recyclerView;
    List<Commodity> commodityList;
    public static CommodityListFragment getInstance(){
        return new CommodityListFragment();
    }
    public void setCommodityList(List<Commodity> commodityList){
        this.commodityList = commodityList;
        CommodityAdapter adapter = new CommodityAdapter(commodityList,getActivity());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_commodity_list, container, false);
        ButterKnife.bind(this,view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        return view;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
