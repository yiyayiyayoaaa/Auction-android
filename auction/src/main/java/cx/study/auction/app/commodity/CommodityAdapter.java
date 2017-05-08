package cx.study.auction.app.commodity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cx.study.auction.R;
import cx.study.auction.bean.Commodity;
import cx.study.auction.bean.Commodity.CommodityStatus;
import cx.study.auction.util.DateUtil;
import cx.study.auction.util.PicassoUtil;

/**
 *
 * Created by cheng.xiao on 2017/4/17.
 */

public class CommodityAdapter extends RecyclerView.Adapter<CommodityAdapter.CommodityViewHolder>{

    private List<Commodity> list;
    private Activity context;
    public CommodityAdapter(List<Commodity> list,Activity context){
        this.list = list;
        this.context = context;
    }

    @Override
    public CommodityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_content_item, parent, false);
        return new CommodityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommodityViewHolder holder, int position) {
        final Commodity commodity = list.get(position);
        holder.setCommodity(context,commodity);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,CommodityActivity.class);
                intent.putExtra("id",commodity.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

//    public static class CommodityViewHolder extends RecyclerView.ViewHolder{
//        @Bind(R.id.item_image)
//        ImageView imageView;
//        @Bind(R.id.tv_name)
//        TextView tvName;
//        @Bind(R.id.tv_time)
//        TextView tvTime;
//        @Bind(R.id.tv_price)
//        TextView tvPrice;
//        public CommodityViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this,itemView);
//        }
//
//        public void setCommodity(Activity context,Commodity commodity){
//            Picasso.with(context)
//                    .load(HttpRest.BASE_URL + commodity.getImageUrls().get(0))
//                    .into(imageView);
//            tvName.setText(commodity.getCommodityName());
//            switch (commodity.getStatus()){
//                case CommodityStatus.AUCTION:
//                    tvTime.setText(DateUtil.getDateString(commodity.getEndTime()) + " 结束");
//                    break;
//                case CommodityStatus.WAIT_AUCTION:
//                    tvTime.setText(DateUtil.getDateString(commodity.getStartTime()) + " 开始");
//                    break;
//            }
//            tvPrice.setText("¥" + commodity.getStartingPrice()+"元");
//        }
//    }

    public static class CommodityViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName;
        TextView itemTextLeft;
        TextView itemTextRight;
        CommodityViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.item_image);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            itemTextLeft = (TextView) itemView.findViewById(R.id.item_text_left);
            itemTextRight = (TextView) itemView.findViewById(R.id.item_text_right);
        }

        public void setCommodity(final Activity context , final Commodity commodity){
            Integer status = commodity.getStatus();
            String imageUrl = commodity.getImageUrls().get(0);
            PicassoUtil.show(itemImage,imageUrl);
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
//            itemImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context,CommodityActivity.class);
//                    intent.putExtra("id",commodity.getId());
//                    context.startActivity(intent);
//                }
//            });
        }
    }
}
