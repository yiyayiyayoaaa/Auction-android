package cx.study.auction.app.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import bolts.Continuation;
import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.app.commodity.CommodityActivity;
import cx.study.auction.app.type.TypeFragment;
import cx.study.auction.bean.Commodity;
import cx.study.auction.bean.Commodity.CommodityStatus;
import cx.study.auction.bean.HomeItem;
import cx.study.auction.contants.HttpRest;
import cx.study.auction.event.ViewPagerChangeEvent;
import cx.study.auction.model.rest.HomeRest;
import cx.study.auction.util.DateUtil;

import static org.greenrobot.eventbus.EventBus.TAG;

/**
 *
 * Created by cheng.xiao on 2017/3/9.
 */

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public static Fragment getInstance(){
        return new HomeFragment();
    }
    @Bind(R.id.swipe)
    SwipeRefreshLayout swipe;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.home_recycle_view)
    RecyclerView recyclerView;

    List<ImageView> viewList = Lists.newArrayList();
    HomeAdapter adapter;
    HomeRest homeRest;
    ScheduledExecutorService executorService;
    FragmentManager fm;
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeRest = new HomeRest();
        fm = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,null);
        ButterKnife.bind(this,view);
        swipe.setOnRefreshListener(this);

        initViewPager();
        //loadData();
        swipe.post(new Runnable() {
            @Override
            public void run() {
                swipe.setRefreshing(true);
                loadData();
            }
        });
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //轮播图片
    public void onViewPagerChange(ViewPagerChangeEvent event){
        Log.d(TAG, "onViewPagerChange: " + event.currentItem);
        if (event.currentItem <viewList.size() -1){
            viewPager.setCurrentItem(event.currentItem + 1);
        } else if (event.currentItem == viewList.size() -1){
            viewPager.setCurrentItem(0,false);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        swipe.setRefreshing(false);
        EventBus.getDefault().unregister(this);
        if (executorService != null){
            executorService.shutdown();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        if (executorService == null){
            startViewPagerAuto();
        }
    }

    private void initViewPager(){
        for (int i = 0; i < 5; i ++){
            ImageView view = (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.view_pager_item, null);
            Picasso.with(getActivity())
                    .load(HttpRest.BASE_URL + "/file/9e979c9d4e3f40018a65d48c2a3590c1.jpg")
                    .into(view);
            viewList.add(view);
        }
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewList.get(position));
            }
        });

    }
    private void startViewPagerAuto(){
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                EventBus.getDefault().post(new ViewPagerChangeEvent(currentItem));
            }
        },10,10, TimeUnit.SECONDS);
    }

    private Task<List<HomeItem>> loadData() {
        final WeakReference<Activity> ref = new WeakReference<Activity>(getActivity());
        return Task.callInBackground(new Callable<List<HomeItem>>() {
            @Override
            public List<HomeItem> call() throws Exception {
                return homeRest.getHomeInfo();
            }
        }).continueWith(new Continuation<List<HomeItem>, List<HomeItem>>() {
            @Override
            public List<HomeItem> then(Task<List<HomeItem>> task) throws Exception {
                swipe.post(new Runnable() {
                    @Override
                    public void run() {
                        swipe.setRefreshing(false);
                    }
                });
                Activity context = ref.get();
                if (context == null || context.isFinishing()){
                    return null;
                }
                if (!task.isFaulted()){
                    adapter = new HomeAdapter(task.getResult());
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(),6,GridLayoutManager.VERTICAL,false));
                    recyclerView.setAdapter(adapter);
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    @Override
    public void onRefresh() {
        loadData();
    }


    private class HomeTitleHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        TextView itemMore;

        HomeTitleHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById(R.id.item_title);
            itemMore = (TextView) itemView.findViewById(R.id.item_more);

            }
        }

    private class HomeContentHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName;
        TextView itemTextLeft;
        TextView itemTextRight;
        HomeContentHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            itemTextLeft = (TextView) itemView.findViewById(R.id.item_text_left);
            itemTextRight = (TextView) itemView.findViewById(R.id.item_text_right);
        }

        public void setViewValue(HomeItem item){
            final Commodity commodity = (Commodity) item.getObj();
            Integer status = commodity.getStatus();

            String imageUrl = commodity.getImageUrls().get(0);
            Log.d(TAG, "onBindViewHolder: " + imageUrl);
            Picasso.with(getActivity())
                    .load(HttpRest.SERVER_URL + imageUrl)
                    //.resize(200,200)
                    .into(itemImage);
            switch (status){
                case CommodityStatus.AUCTION:
                    itemTextLeft.setText(DateUtil.getDateString(commodity.getEndTime()) + " 结束");
                    break;
                case CommodityStatus.WAIT_AUCTION:
                    itemTextLeft.setText(DateUtil.getDateString(commodity.getStartTime()) + " 开始");
                    break;
            }
            itemTextRight.setText("起拍价：¥" + commodity.getStartingPrice()+"元");
            itemName.setText(commodity.getCommodityName());
            itemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),CommodityActivity.class);
                    Log.d(TAG, "onClick: " + commodity.getId());
                    intent.putExtra("id",commodity.getId());
                    startActivity(intent);
                }
            });
        }
    }

    private class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<HomeItem> items;

        HomeAdapter(List<HomeItem> items){
            this.items = items;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            switch (viewType){
                case HomeItem.TITLE:
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.home_title_item,parent,false);
                    return new HomeTitleHolder(view);
                case HomeItem.CONTENT:
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.home_content_item,parent,false);

                    return new HomeContentHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final HomeItem homeItem = items.get(position);
            final int i = position;
            switch (homeItem.getType()){
                case HomeItem.TITLE:
                    final HomeTitleHolder titleHolder = (HomeTitleHolder) holder;
                    //HomeItem<String> titleItem = (HomeTitleItem) homeItem;
                    titleHolder.itemTitle.setText((String) homeItem.getObj());
                    titleHolder.itemMore.setText("更多");
                    titleHolder.itemMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentTransaction ft = fm.beginTransaction();
                            Fragment fragment = TypeFragment.getInstance();
                            Bundle bundle = new Bundle();
                            bundle.putInt("typeId",i);
                            fragment.setArguments(bundle);
                            ft.replace(R.id.fragment_container,fragment);
                            ft.commit();
                        }
                    });
                    break;
                case HomeItem.CONTENT:
                    HomeContentHolder contentHolder = (HomeContentHolder) holder;
                   // final HomeItem<Commodity> contentItem = (HomeContentItem)homeItem;
                    contentHolder.setViewValue(homeItem);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemViewType(int position) {
            return items.get(position).getType();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager){
                GridLayoutManager manager = (GridLayoutManager) layoutManager;
                manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int type = getItemViewType(position);
                        switch (type){
                            case HomeItem.TITLE:
                                return 6;
                            case HomeItem.CONTENT:
                                return 2;
                        }
                        return 0;
                    }
                });
            }
        }
    }
}
