package com.yiful.couriorprojectfirebase.util;

import android.content.Context;
import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiful.couriorprojectfirebase.R;
import com.yiful.couriorprojectfirebase.model.MyParcel;

import java.util.List;

/**
 * Created by Yifu on 12/17/2017.
 */

public class ParcelAdapter extends RecyclerView.Adapter<ParcelAdapter.ViewHolder> {
    private List<MyParcel> parcels;
    private Context context;
    public ParcelAdapter(Context context, List<MyParcel> parcels){
        this.context = context;
        this.parcels = parcels;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.parcel_list_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(parcels.get(position).getName());
        holder.tvStatus.setText(parcels.get(position).getStatus());
        holder.tvVendor.setText(parcels.get(position).getVendor());
        holder.tvDate.setText(parcels.get(position).getDeliveryTime().toString());
    }

    @Override
    public int getItemCount() {
        return parcels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvStatus, tvVendor,tvDate,tvDelieverAddress;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvVendor = itemView.findViewById(R.id.tvVendor);
        }
    }
}
