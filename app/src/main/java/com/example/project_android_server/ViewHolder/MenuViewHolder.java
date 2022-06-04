package com.example.project_android_server.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android_server.R;

public class MenuViewHolder extends RecyclerView.ViewHolder {
    public TextView txtMenuName;
    public ImageView imageView;
    public Button btnUpdateCategory, btnDeleteCategory, btnDetailCategory;

    public MenuViewHolder(View itemView) {
        super(itemView);

        txtMenuName = (TextView) itemView.findViewById(R.id.menu_name);
        imageView = (ImageView) itemView.findViewById(R.id.menu_image);
        btnUpdateCategory = itemView.findViewById(R.id.menu_update);
        btnDeleteCategory = itemView.findViewById(R.id.menu_delete);
        btnDetailCategory = itemView.findViewById(R.id.menu_detail);


    }

}
