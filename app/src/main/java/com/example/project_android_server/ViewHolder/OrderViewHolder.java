package com.example.project_android_server.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android_server.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    public TextView txtOrderId, txtOrderStatus, txtOrderPhone, txtOrderAddress;
    public Button btnOrderUpdate, btnOrderDelete, btnOrderDetail;

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderAddress = itemView.findViewById(R.id.order_address);
        txtOrderId = itemView.findViewById(R.id.order_id);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderPhone = itemView.findViewById(R.id.order_phone);

        btnOrderUpdate = itemView.findViewById(R.id.btnOrderUpdate);
        btnOrderDelete = itemView.findViewById(R.id.btnOrderDelete);
        btnOrderDetail = itemView.findViewById(R.id.btnOrderDetail);
    }


}
