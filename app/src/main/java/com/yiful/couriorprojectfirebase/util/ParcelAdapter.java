package com.yiful.couriorprojectfirebase.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
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
        holder.tvPickUpDate.setText(parcels.get(position).getPickupTime().toString());
        holder.tvPickAddress.setText(parcels.get(position).getPickupAddress().toString());
        holder.tvDeliveryDate.setText(parcels.get(position).getDeliveryTime().toString());
        holder.tvDelieverAddress.setText(parcels.get(position).getDeliveryAddress().toString());

        String bitMapString=parcels.get(position).getUserId();
        Bitmap bitmap=generateBarCode(bitMapString);
        holder.mImageView.setImageBitmap(bitmap);
    }

    private Bitmap StringToBitMap(String bitMap) {
        try{
            byte [] encodeByte= Base64.decode(bitMap,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    private Bitmap generateBarCode(String bitMapString) {
        MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
        Bitmap bitmap=StringToBitMap(bitMapString);
        try {
            BitMatrix bitMatrix=multiFormatWriter.encode(bitMapString, BarcodeFormat.QR_CODE,100,100);
            BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
            bitmap= barcodeEncoder.createBitmap(bitMatrix);
        }
        catch (WriterException e){
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public int getItemCount() {
        return parcels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvStatus, tvVendor,tvPickUpDate,tvPickAddress, tvDeliveryDate,tvDelieverAddress;
        ImageView mImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvVendor = itemView.findViewById(R.id.tvVendor);
            tvPickUpDate=itemView.findViewById(R.id.tvPickUpDate);
            tvPickAddress=itemView.findViewById(R.id.tvPickUpAddress);
            tvDeliveryDate =itemView.findViewById(R.id.tvDeliveryDate);
            tvDelieverAddress=itemView.findViewById(R.id.tvDeliveryAddress);
            mImageView=itemView.findViewById(R.id.imageViewBarCode);
        }
    }
}
