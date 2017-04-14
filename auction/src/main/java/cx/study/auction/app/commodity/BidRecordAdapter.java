package cx.study.auction.app.commodity;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cx.study.auction.R;
import cx.study.auction.bean.BidRecord;
import cx.study.auction.util.DateUtil;

/**
 *
 * Created by cheng.xiao on 2017/4/14.
 */

public class BidRecordAdapter extends BaseAdapter{
    private Activity context;
    private List<BidRecord> list;
    private int userId;

    public BidRecordAdapter(Activity context, List<BidRecord> list,int userId){
        this.context = context;
        this.list = list;
        this.userId = userId;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BidRecord record = list.get(position);
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bid_record_list_view,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvBidPrice.setText(String.valueOf(record.getPrice()));
        holder.tvBidTime.setText(DateUtil.getDateTimeString(record.getBidTime()));
        holder.tvId.setText(String.valueOf(position + 1));
        holder.tvIsMe.setTag(record.getUserId());
        holder.tvIsMe.setText(record.getUserId()==userId?"我的出价":"");
        return convertView;
    }

    private static class ViewHolder{
        private TextView tvId;
        private TextView tvBidPrice;
        private TextView tvBidTime;
        private TextView tvIsMe;

        private ViewHolder(View view){
            tvId = (TextView) view.findViewById(R.id.tv_id);
            tvBidPrice = (TextView) view.findViewById(R.id.tv_bid_price);
            tvBidTime = (TextView) view.findViewById(R.id.tv_bid_time);
            tvIsMe = (TextView) view.findViewById(R.id.tv_isMe);
        }
    }
}
