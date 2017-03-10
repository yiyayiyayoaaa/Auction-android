package cx.study.auction.app.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.app.commodity.CommodityActivity;
import cx.study.auction.bean.Commodity;
import cx.study.auction.bean.HomeContentItem;
import cx.study.auction.bean.HomeItem;
import cx.study.auction.bean.HomeTitleItem;
import cx.study.auction.event.ViewPagerChangeEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.greenrobot.eventbus.EventBus.TAG;

/**
 *
 * Created by cheng.xiao on 2017/3/9.
 */

public class HomeFragment extends Fragment{

    public static Fragment getInstance(){
        return new HomeFragment();
    }
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.home_recycle_view)
    RecyclerView recyclerView;
    List<HomeItem> homeItems = Lists.newArrayList();
    List<ImageView> viewList = Lists.newArrayList();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,null);
        ButterKnife.bind(this,view);
        loadDate();
        initViewPager();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),8,GridLayoutManager.VERTICAL,false));
        HomeAdapter adapter = new HomeAdapter(homeItems);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //轮播图片
    public void onViewPagerChange(ViewPagerChangeEvent event){
        Log.d(TAG, "onViewPagerChange: " + event.currentItem);
        if (event.currentItem <viewList.size() -1){
            viewPager.setCurrentItem(event.currentItem + 1);
        }
        if (event.currentItem == viewList.size() -1){
            viewPager.setCurrentItem(0,false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    private void initViewPager(){
        for (int i = 0; i < 5; i ++){
            ImageView view = (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.view_pager_item, null);
            Picasso.with(getActivity())
                    .load("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/newmusic/img/default_8a5b42b2.png")
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
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                EventBus.getDefault().post(new ViewPagerChangeEvent(currentItem));
            }
        },10,10, TimeUnit.SECONDS);
    }

    private void loadDate() {
        HomeItem<String> item1 = new HomeTitleItem();
        item1.setType(HomeItem.TITLE);
        item1.setObj("正在热拍");
        homeItems.add(item1);
        for (int i = 0; i < 8; i ++){
            HomeItem<Commodity> homeItem = new HomeContentItem();
            homeItem.setType(HomeItem.CONTENT);
            Commodity commodity = new Commodity();
            commodity.setCommodityName("Aaa aaa aaa" + i);
            List<String> imgUrls = new ArrayList<>();
            imgUrls.add("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/newmusic/img/default_8a5b42b2.png");
            imgUrls.add("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/newmusic/img/default_8a5b42b2.png");
            imgUrls.add("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/newmusic/img/default_8a5b42b2.png");
            commodity.setImgUrls(imgUrls);
            homeItem.setObj(commodity);
            homeItems.add(homeItem);
        }
        HomeItem<String> item2 = new HomeTitleItem();
        item2.setType(HomeItem.TITLE);
        item2.setObj("即将开始");
        homeItems.add(item2);
        for (int i = 0; i < 8; i ++){
            HomeItem<Commodity> homeItem = new HomeContentItem();
            homeItem.setType(HomeItem.CONTENT);
            Commodity commodity = new Commodity();
            commodity.setCommodityName("Bbb bbb bbb" + i);
            List<String> imgUrls = new ArrayList<>();
            imgUrls.add("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/newmusic/img/default_8a5b42b2.png");
            imgUrls.add("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/newmusic/img/default_8a5b42b2.png");
            imgUrls.add("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/newmusic/img/default_8a5b42b2.png");
            commodity.setImgUrls(imgUrls);
            homeItem.setObj(commodity);
            homeItems.add(homeItem);
        }

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url("http://192.168.0.104:8080/rest/homeInfo.do")
                .build();
        okHttpClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d(TAG, "onResponse: " + response.body().string());
                    }
                });
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
            HomeItem homeItem = items.get(position);
            switch (homeItem.getType()){
                case HomeItem.TITLE:
                    HomeTitleHolder titleHolder = (HomeTitleHolder) holder;
                    HomeItem<String> titleItem = (HomeTitleItem)homeItem;
                    titleHolder.itemTitle.setText(titleItem.getObj());
                    titleHolder.itemMore.setText("更多");
                    break;
                case HomeItem.CONTENT:
                    HomeContentHolder contentHolder = (HomeContentHolder) holder;
                    final HomeItem<Commodity> contentItem = (HomeContentItem)homeItem;
                    final Commodity commodity = contentItem.getObj();
                    Picasso.with(getActivity())
                            .load(commodity.getImgUrls().get(0))
                            .into(contentHolder.itemImage);
                    contentHolder.itemName.setText(commodity.getCommodityName());

                    contentHolder.itemImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(),CommodityActivity.class);
                            intent.putExtra("commodity",commodity);
                            startActivity(intent);
                        }
                    });
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
                                return 8;
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
