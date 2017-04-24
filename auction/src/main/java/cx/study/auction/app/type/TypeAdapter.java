package cx.study.auction.app.type;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.List;

import cx.study.auction.R;
import cx.study.auction.bean.CommodityType;

/**
 *
 * Created by chengxiao on 2017/4/16.
 */

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeViewHolder>{
    interface OnItemClickListener{
        void onItemClick(View view, CommodityType type);
    }
    OnItemClickListener listener;
    List<CommodityType> types;

    private Activity context;
    public TypeAdapter(List<CommodityType> types,Activity context){
        this.types = types;
        this.context = context;
    }
    @Override
    public TypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.commodity_type_item, parent, false);
        return new TypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TypeViewHolder holder, int position) {
        final CommodityType type = types.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onItemClick(v,type);
                }
            }
        });
        holder.setType(type);
    }

    @Override
    public int getItemCount() {
        return types.size();
    }
    public void changeState(CommodityType type){
        for(CommodityType commodityType : types){
            commodityType.setSelect(false);
        }
        type.setSelect(true);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(TypeAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    static class TypeViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        public TypeViewHolder(View itemView) {
            super(itemView);
            radioButton = (RadioButton) itemView;
        }

        public void setType(CommodityType type){
            radioButton.setChecked(type.isSelect());
            radioButton.setText(type.getTypeName());
        }
    }

}
