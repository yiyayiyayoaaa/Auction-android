package cx.study.auction.app.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.bean.HomeItem;

/**
 *
 * Created by cheng.xiao on 2017/3/9.
 */

public class HomeFragment extends Fragment{

    public static Fragment getInstance(){
        return new HomeFragment();
    }

    @Bind(R.id.home_recycle_view)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,null);
        ButterKnife.bind(this,view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),6,GridLayoutManager.VERTICAL,false));
        return view;
    }


    private class HomeHolder extends RecyclerView.ViewHolder {
        public HomeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class HomeAdapter extends RecyclerView.Adapter<HomeHolder>{
        private List<HomeItem> items;

        public HomeAdapter(List<HomeItem> items){
            this.items = items;
        }
        @Override
        public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case HomeItem.TITLE:
                    break;
                case HomeItem.CONTENT:
                    break;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(HomeHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
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
                        }
                        return 0;
                    }
                });
            }
        }
    }
}
