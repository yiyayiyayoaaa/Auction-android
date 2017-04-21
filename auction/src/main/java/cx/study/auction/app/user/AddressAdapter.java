package cx.study.auction.app.user;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.Callable;

import bolts.Task;
import butterknife.Bind;
import butterknife.ButterKnife;
import cx.study.auction.R;
import cx.study.auction.bean.UserAddress;
import cx.study.auction.event.AddressDeleteEvent;
import cx.study.auction.model.rest.UserAddressRest;

/**
 *
 * Created by cheng.xiao on 2017/4/19.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressHolder> {
    List<UserAddress> list;
    Activity context;
    public AddressAdapter(List<UserAddress> list, Activity context){
        this.list = list;
        this.context = context;
    }

    @Override
    public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_address_item, parent, false);
        return new AddressHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressHolder holder, int position) {
        final UserAddress address = list.get(position);
        holder.setAddress(address,context);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog(address.getId());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("address",address.getAddress());
                context.setResult(Activity.RESULT_OK,intent);
                context.finish();
            }
        });
    }


    private void deleteDialog(final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定要删除这条记录吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(id);
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }
    private Task<Void> delete(final int id){
        return Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                UserAddressRest rest = new UserAddressRest();
                rest.delete(id);
                EventBus.getDefault().post(new AddressDeleteEvent());
                return null;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class AddressHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_phone)
        TextView tvPhone;
        @Bind(R.id.tv_address)
        TextView tvAddress;
        @Bind(R.id.btn_edit)
        TextView btnEdit;
        @Bind(R.id.btn_delete)
        TextView btnDelete;
        public AddressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setAddress(final UserAddress address, final Context context){
            String s = address.getAddress();
            String[] split = s.split("@");
            tvName.setText(split[0]);
            tvPhone.setText(split[1]);
            tvAddress.setText(split[2] + " " + split[3]);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,AddressAddActivity.class);
                    intent.putExtra("isAdd",false);
                    intent.putExtra("userAddress",address);
                    context.startActivity(intent);
                }
            });
        }
    }
}
