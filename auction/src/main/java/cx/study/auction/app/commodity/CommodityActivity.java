package cx.study.auction.app.commodity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.app.base.BaseActivity;
import cx.study.auction.bean.Commodity;
import cx.study.auction.contants.HttpRest;
import cx.study.auction.model.rest.CommodityRest;
import cx.study.auction.model.rest.http.MCException;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
    CommodityRest commodityRest;
    private Commodity commodity;
    private List<ImageView> viewList = Lists.newArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity);
        ButterKnife.bind(this);
        setTitle("商品详情");
        init();
    }

    private void initView() {
        commodityName.setText(commodity.getCommodityName());
        staringPrice.setText(getString(R.string.start_price,commodity.getStartingPrice()));
    }

    private void init(){
        commodityRest = new CommodityRest();
        final int commodityId = getIntent().getIntExtra("id",0);
        Observable.create(new ObservableOnSubscribe<Commodity>() {

            @Override
            public void subscribe(ObservableEmitter<Commodity> e) throws MCException {
                commodity = commodityRest.getCommodityById(commodityId);
                e.onNext(commodity);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Commodity>() {
                    @Override
                    public void accept(@NonNull Commodity commodity) throws MCException {
                        initView();
                        initViewPager();
                    }
                });

    }


    private static final String TAG = "CommodityActivity";
    private void initViewPager(){
        if (commodity != null){
            for (String url : commodity.getImageUrls()){
                Log.e(TAG, "initViewPager: " + url);
                ImageView view = (ImageView) LayoutInflater.from(this).inflate(R.layout.view_pager_item, null);
                Picasso.with(this)
                        .load(HttpRest.BASE_URL + url)
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
