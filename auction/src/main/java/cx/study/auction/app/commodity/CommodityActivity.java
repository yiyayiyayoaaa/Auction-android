package cx.study.auction.app.commodity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import bolts.Continuation;
import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cx.study.auction.R;
import cx.study.auction.app.LoginActivity;
import cx.study.auction.app.base.BaseActivity;
import cx.study.auction.bean.BidRecord;
import cx.study.auction.bean.Commodity;
import cx.study.auction.bean.Commodity.CommodityStatus;
import cx.study.auction.bean.User;
import cx.study.auction.contants.HttpRest;
import cx.study.auction.model.dao.UserDao;
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

public class CommodityActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.commodity_name)
    TextView commodityName;
    @Bind(R.id.starting_price)
    TextView staringPrice;
    @Bind(R.id.appraised_price)
    TextView appraisedPrice;
    @Bind(R.id.bidding_deposit)
    TextView biddingDeposit;
    @Bind(R.id.reverse_price)
    TextView reversePrice;
    @Bind(R.id.bid_increments)
    TextView bidIncrements;
    @Bind(R.id.customer)
    TextView tvCustomer;
    @Bind(R.id.bid_record)
    ListView bidRecord;
    @Bind(R.id.commodity_description)
    TextView commodityDescription;
    @Bind(R.id.time)
    TextView tvTime;
    @Bind(R.id.btn_bid)
    Button btnBid;
    @Bind(R.id.tv_bid_count)
    TextView tvBidCount;
    @Bind(R.id.btn_back)
    ImageView btnBack;
    CommodityRest commodityRest;
    UserDao userDao;
    private Commodity commodity;
    User user;
    BidRecordAdapter adapter;
    List<BidRecord> records = Lists.newArrayList();
    private List<ImageView> viewList = Lists.newArrayList();
    private ScheduledExecutorService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_commodity);
        ButterKnife.bind(this);
        setTitle("商品详情");
        getSupportActionBar().hide();
    }

    private void initView() {
        getBidRecords(this);
        commodityName.setText(commodity.getCommodityName());
        staringPrice.setText(getString(R.string.start_price,commodity.getStartingPrice()));
        appraisedPrice.setText(getString(R.string.appraised_price,commodity.getAppraisedPrice()));
        biddingDeposit.setText(getString(R.string.bidding_deposit,commodity.getBiddingDeposit()));
        reversePrice.setText(getString(R.string.reverse_price,commodity.getReservePrice()));
        bidIncrements.setText(getString(R.string.bid_increments,commodity.getBidIncrements()));
        tvCustomer.setText(getString(R.string.customer,commodity.getCustomerName()));

    }
    private void init(){
        userDao = new UserDao(this);
        user = userDao.getLocalUser();
        commodityRest = new CommodityRest();
        adapter = new BidRecordAdapter(this,records,user==null?-1:user.getId());
        bidRecord.setAdapter(adapter);
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
                        showTime();
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

    private void showTime(){
        service = Executors.newSingleThreadScheduledExecutor();
        Integer status = commodity.getStatus();
        final Date startTime = commodity.getStartTime();
        final Date endTime = commodity.getEndTime();
        switch (status){
            case CommodityStatus.AUCTION:
                //结束倒计时

                service.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        long interval = endTime.getTime() - System.currentTimeMillis();
                        int[] time = getTime(interval);
                        if (interval > 0){
                            final String content = getString(R.string.end_time,time[0],time[1],time[2],time[3]);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvTime.setText(content);
                                    btnBid.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            stopTime();
                            //结束
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvTime.setText("结束");
                                    btnBid.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                },0,1, TimeUnit.SECONDS);
                break;
            case CommodityStatus.WAIT_AUCTION:
                //开始倒计时

                service.scheduleWithFixedDelay(new Runnable() {
                    @Override
                    public void run() {
                        long interval2 = startTime.getTime() - System.currentTimeMillis();
                        int[] time2 = getTime(interval2);
                        if (interval2 > 0){
                            btnBid.setClickable(false);
                            final String content = getString(R.string.start_time,time2[0],time2[1],time2[2],time2[3]);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvTime.setText(content);
                                    btnBid.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            stopTime();
                            //结束
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    commodity.setStatus(CommodityStatus.AUCTION);
                                    showTime();
                                }
                            });
                        }
                    }
                },0,1, TimeUnit.SECONDS);
                break;
            default:
        }
    }
    private void stopTime(){
        service.shutdownNow();
        service = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }


    private int[] getTime(long time){
        int[] i = new int[4];
        long d = 24 * 60 * 60 * 1000;
        long h = 60 * 60 * 1000;
        long m = 60 * 1000;
        long s = 1000;
        i[0] = (int) (time / d);
        i[1] = (int) ((time - i[0] * d)/h);
        i[2] = (int) ((time - i[0] * d - i[1] * h) /m);
        i[3] = (int) ((time - i[0] * d - i[1] * h - i[2] * m) / s);
        return i;
    }

    @Override
    @OnClick({R.id.btn_bid,R.id.btn_back})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_bid:
                //判断是否登录
                if(user!= null) {
                    refreshCommodity().onSuccess(new Continuation<Commodity, Object>() {
                        @Override
                        public Object then(Task<Commodity> task) throws Exception {
                            commodity = task.getResult();
                            bidPrice();
                            return null;
                        }
                    },Task.UI_THREAD_EXECUTOR);
                } else {
                    Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("isGoHome",false);
                    startActivity(intent);
                }
                break;
            case R.id.btn_back:
                finish();
            default:

        }
    }

    private void bidPrice(){
        final WeakReference<Activity> ref = new WeakReference<Activity>(this);
        final double b;
        double hammerPrice = commodity.getHammerPrice();//当前价
        if (Double.isNaN(hammerPrice)){
            hammerPrice = 0d;
        }
        double startingPrice = commodity.getStartingPrice(); //起始价
        double bidIncrements = commodity.getBidIncrements();//出价增幅
        if (startingPrice > hammerPrice){
            b = startingPrice;
        } else {
            b = hammerPrice + bidIncrements;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入您的出价");
        View view = LayoutInflater.from(this).inflate(R.layout.layout_dialog_bid_price, null);
        final EditText editText = (EditText) view.findViewById(R.id.et_bid_price);
        Log.e(TAG, "onClick: " + b);
        editText.setHint("当前出价不能低于" + b + "元");
        builder.setView(view);
        builder.setCancelable(false);
        builder.setPositiveButton("确定",null);
        builder.setNegativeButton("取消",null);
        final AlertDialog dialog = builder.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (!TextUtils.isEmpty(text) && Double.parseDouble(text)>= b){
                    BidRecord record = new BidRecord(commodity.getId(),user.getId(), Double.parseDouble(text));
                    postBidPrice(record,ref.get());
                    dialog.dismiss();
                } else {
                    Toast.makeText(ref.get(),"请输入正确的出价",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 请求出价记录
     * @return
     */
    private Task<List<BidRecord>> getBidRecords(final Context context){
        return Task.call(new Callable<List<BidRecord>>() {
            @Override
            public List<BidRecord> call() throws Exception {
                return commodityRest.getBidRecords(commodity.getId());
            }
        },Task.BACKGROUND_EXECUTOR).continueWith(new Continuation<List<BidRecord>, List<BidRecord>>() {
            @Override
            public List<BidRecord> then(Task<List<BidRecord>> task) throws Exception {
                if (!task.isFaulted()){
                    //刷新ui
                    records.clear();
                    records.addAll(task.getResult());
                    tvBidCount.setText(String.format(Locale.getDefault(),"该商品已出价%d次",records.size()));
                    adapter.notifyDataSetChanged();
                } else {
                    //错误信息
                    Toast.makeText(context,task.getError().getMessage(),Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    /**
     * 发送价格
     * @return
     */
    private Task<String> postBidPrice(final BidRecord bidRecord, final Context context){
        return Task.call(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return commodityRest.postBidPrice(bidRecord);
            }
        },Task.BACKGROUND_EXECUTOR).continueWith(new Continuation<String, String>() {
            @Override
            public String then(Task<String> task) throws Exception {
                if (!task.isFaulted()){
                    Toast.makeText(context,task.getResult(),Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(context,task.getError().getMessage(),Toast.LENGTH_SHORT).show();
                }
                getBidRecords(context);
                return null;
            }
        },Task.UI_THREAD_EXECUTOR);
    }

    private Task<Commodity> refreshCommodity(){
        return Task.callInBackground(new Callable<Commodity>() {
            @Override
            public Commodity call() throws Exception {
                return commodityRest.getCommodityById(commodity.getId());
            }
        });
    }
}
