package cx.study.auction.app.auction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.app.base.BaseFragment;
import cx.study.auction.bean.Commodity;
import cx.study.auction.bean.User;
import cx.study.auction.model.dao.UserDao;
import cx.study.auction.model.rest.CommodityRest;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

/**
 *
 * Created by cheng.xiao on 2017/3/9.
 */

public class AuctionFragment extends BaseFragment{

    public static Fragment getInstance() {
        return new AuctionFragment();
    }
    @Bind(R.id.store_house_ptr_frame)
    PtrFrameLayout ptrFrameLayout;
    @Bind(R.id.recycle_view)
    RecyclerView recyclerView;
    AuctionAdapter adapter;

    CommodityRest commodityRest;
    User user;
    List<Commodity> commodities = Lists.newArrayList();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AuctionAdapter(getActivity(),commodities);
        commodityRest = new CommodityRest();
        user = new UserDao(getActivity()).getLocalUser();
    }

    private void initRefreshView(){
        final MaterialHeader header = new MaterialHeader(getActivity());
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(15));
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.disableWhenHorizontalMove(true);

        ptrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                loadData().continueWith(new Continuation<List<Commodity>, Object>() {
                    @Override
                    public Object then(Task<List<Commodity>> task) throws Exception {
                        frame.post(new Runnable() {
                            @Override
                            public void run() {
                                ptrFrameLayout.refreshComplete();
                            }
                        });
                        return null;
                    }
                });
            }
        });
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auction,container,false);
        ButterKnife.bind(this,view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        initRefreshView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ptrFrameLayout.autoRefresh();
    }

    private Task<List<Commodity>> loadData(){
        return Task.callInBackground(new Callable<List<Commodity>>() {
            @Override
            public List<Commodity> call() throws Exception {
                return commodityRest.getAuction(user.getId());
            }
        }).continueWith(new Continuation<List<Commodity>, List<Commodity>>() {
            @Override
            public List<Commodity> then(Task<List<Commodity>> task) throws Exception {
                if (!task.isFaulted()){
                    commodities.clear();
                    commodities.addAll(task.getResult());
                    adapter.notifyDataSetChanged();
                } else {
                    Exception error = task.getError();
                    Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }
}
