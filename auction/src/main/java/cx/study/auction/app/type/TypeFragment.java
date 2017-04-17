package cx.study.auction.app.type;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.app.base.BaseFragment;
import cx.study.auction.app.commodity.CommodityListFragment;
import cx.study.auction.bean.Commodity;
import cx.study.auction.bean.CommodityType;
import cx.study.auction.model.rest.CommodityRest;

/**
 *
 * Created by cheng.xiao on 2017/3/9.
 */

public class TypeFragment extends BaseFragment{
    @Bind(R.id.fragment_container)
    FrameLayout frameLayout;
    @Bind(R.id.rv_type)
    RecyclerView rvType;
    CommodityListFragment commodityListFragment;
    List<CommodityType> typeList = new ArrayList<>();
    List<Commodity> commodityList = new ArrayList<>();
    CommodityRest commodityRest;
    TypeAdapter typeAdapter;
    public static Fragment getInstance(){
        return new TypeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commodityRest = new CommodityRest();
    }
    private void initType(){
        typeList.clear();
        CommodityType typeAll = new CommodityType(-1,"全部");
        typeAll.setSelect(true);
        CommodityType typeAuction = new CommodityType(-2,"正在拍卖");
        CommodityType typeWaitAuction = new CommodityType(-3,"即将开始");
        typeList.add(typeAll);
        typeList.add(typeAuction);
        typeList.add(typeWaitAuction);
        loadType().continueWith(new Continuation<List<CommodityType>, Object>() {
            @Override
            public Object then(Task<List<CommodityType>> task) throws Exception {
                if (!task.isFaulted()){
                    typeList.addAll(task.getResult());
                }
                typeAdapter.notifyDataSetChanged();
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type,container,false);
        ButterKnife.bind(this,view);
        rvType.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        typeAdapter = new TypeAdapter(typeList,getActivity());
        typeAdapter.setOnItemClickListener(new TypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, CommodityType type) {
                if (!type.isSelect()){
                    typeAdapter.changeState(type);
                    loadData(type.getId());
                }
            }
        });
        rvType.setAdapter(typeAdapter);
        initFrameLayout();
        return view;
    }

    private void initFrameLayout(){
        FragmentManager fm = getChildFragmentManager();
        commodityListFragment = (CommodityListFragment) fm.findFragmentById(R.id.fragment_container);
        if (commodityListFragment == null){
            commodityListFragment = CommodityListFragment.getInstance();
            fm.beginTransaction()
                    .add(R.id.fragment_container,commodityListFragment)
                    .commit();
        }
        loadData(-1);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            initType();
            loadData(-1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initType();
    }

    private Task<List<CommodityType>> loadType(){
        return Task.callInBackground(new Callable<List<CommodityType>>() {
            @Override
            public List<CommodityType> call() throws Exception {
                return commodityRest.getCommodityType();
            }
        });
    }

    private Task<List<Commodity>> loadData(final int typeId){
        return Task.callInBackground(new Callable<List<Commodity>>() {
            @Override
            public List<Commodity> call() throws Exception {
                return commodityRest.getCommodities(typeId);
            }
        }).continueWith(new Continuation<List<Commodity>, List<Commodity>>() {
            @Override
            public List<Commodity> then(Task<List<Commodity>> task) throws Exception {
                if (!task.isFaulted()){
                    commodityListFragment.setCommodityList(task.getResult());
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
