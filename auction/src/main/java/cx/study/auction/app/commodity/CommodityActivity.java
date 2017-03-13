package cx.study.auction.app.commodity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.app.base.BaseActivity;
import cx.study.auction.bean.Commodity;

/**
 *
 * Created by cheng.xiao on 2017/3/10.
 */

public class CommodityActivity extends BaseActivity{
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.commodity_name)
    TextView commodityName;
    @Bind(R.id.starting_price)
    TextView staringPrice;
    @Bind(R.id.appraised_price)
    TextView appraisedPrice;
    @Bind(R.id.reverse_price)
    TextView reversePrice;
    @Bind(R.id.bid_increments)
    TextView bidIncrements;
    @Bind(R.id.customer)
    TextView customer;
    @Bind(R.id.bid_record)
    ListView bidRecord;
    @Bind(R.id.commodity_description)
    TextView commodityDescription;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.et_price)
    EditText etPrice;
    @Bind(R.id.btn_bid)
    Button btnBid;

    private Commodity commodity;
    private List<ImageView> viewList = Lists.newArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity);
        ButterKnife.bind(this);
        setTitle("商品详情");
        commodity = getCommodity();
        initViewPager();
        initView();
    }

    private void initView() {
        commodityName.setText(commodity.getCommodityName());
    }

    private Commodity getCommodity(){
        Serializable commodity = getIntent().getSerializableExtra("commodity");
        if (commodity instanceof Commodity){
            return (Commodity) commodity;
        }
        return null;
    }




    private void initViewPager(){
        if (commodity != null){
            for (String url : commodity.getImageUrls()){
                ImageView view = (ImageView) LayoutInflater.from(this).inflate(R.layout.view_pager_item, null);
                Picasso.with(this)
                        .load(url)
                        .into(view);
                viewList.add(view);
            }
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
}
