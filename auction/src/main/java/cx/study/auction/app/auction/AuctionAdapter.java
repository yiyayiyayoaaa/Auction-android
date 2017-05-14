package cx.study.auction.app.auction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.app.commodity.CommodityActivity;
import cx.study.auction.bean.Commodity;
import cx.study.auction.bean.Commodity.CommodityStatus;
import cx.study.auction.util.DateUtil;
import cx.study.auction.util.PicassoUtil;

/**
 *
 * Created by chengxiao on 2017/4/23.
 */

public class AuctionAdapter extends RecyclerView.Adapter<AuctionAdapter.AuctionHolder> {
    Activity context;
    List<Commodity> commodities;
    public AuctionAdapter(Activity context, List<Commodity> commodities){
        this.commodities = commodities;
        this.context = context;
    }
    @Override
    public AuctionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.commodity_auction_item, parent, false);
        return new AuctionHolder(view);
    }

    @Override
    public void onBindViewHolder(AuctionHolder holder, int position) {
        final Commodity commodity = commodities.get(position);
        holder.setAuction(commodity,context);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommodityActivity.class);
                intent.putExtra("id",commodity.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commodities.size();
    }

    public static class AuctionHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)TextView tvName;
        @Bind(R.id.tv_price)TextView tvPrice;
        @Bind(R.id.tv_description)TextView tvDescription;
        @Bind(R.id.tv_status)TextView tvStatus;
        @Bind(R.id.tv_time)TextView tvTime;
        @Bind(R.id.item_image)ImageView itemImage;
        public AuctionHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setAuction(Commodity commodity,Activity context){
            tvName.setText(commodity.getCommodityName());
            tvPrice.setText("起拍价(¥)：" + commodity.getStartingPrice()+"元");
            String status = null;
            String time = null;
            Drawable drawable = null;
            tvStatus.setTextColor(context.getResources().getColor(android.R.color.white));
            tvDescription.setText(Html.fromHtml(commodity.getDescription()));
            switch (commodity.getStatus()){
                case CommodityStatus.AUCTION:
                    status = "拍卖中";
                    time = DateUtil.getDateTimeString(commodity.getEndTime()) + "结束";
                    drawable = context.getResources().getDrawable(R.drawable.rectangle_background_blue);
                    break;
                case CommodityStatus.SUCCESS:
                    drawable = context.getResources().getDrawable(R.drawable.rectangle_background_gray);
                    tvStatus.setTextColor(context.getResources().getColor(android.R.color.black));
                    status = "已结束";
                    break;
                case CommodityStatus.UNSOLD:
                    status = "已结束";
                    tvStatus.setTextColor(context.getResources().getColor(android.R.color.black));
                    drawable = context.getResources().getDrawable(R.drawable.rectangle_background_gray);
                    break;
                case CommodityStatus.WAIT_AUCTION:
                    status = "未开始";
                    drawable = context.getResources().getDrawable(R.drawable.rectangle_background_red);
                    time = DateUtil.getDateTimeString(commodity.getStartTime()) + "开始";
                    break;
                default:
                    status = "已结束";
                    tvStatus.setTextColor(context.getResources().getColor(android.R.color.black));
                    drawable = context.getResources().getDrawable(R.drawable.rectangle_background_gray);
                    break;

            }
            PicassoUtil.show(itemImage,commodity.getImageUrls().get(0));
            tvStatus.setText(status);
            tvStatus.setBackground(drawable);
            tvTime.setText(time);
        }
    }
}
