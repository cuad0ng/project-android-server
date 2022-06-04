package com.example.project_android_server.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project_android_server.R;

public class FoodViewHolder extends RecyclerView.ViewHolder {
    public TextView food_name, food_price, food_discount, food_description;
    public ImageView food_image;
    public Button btnFoodUpdate, btnFoodDelete;

    public FoodViewHolder(View itemView) {
        super(itemView);

        food_name = itemView.findViewById(R.id.food_name);
        food_price = itemView.findViewById(R.id.food_price);
        food_discount = itemView.findViewById(R.id.food_discount);
        food_description = itemView.findViewById(R.id.food_description);
        food_image = itemView.findViewById(R.id.food_image);
        btnFoodUpdate = itemView.findViewById(R.id.food_update);
        btnFoodDelete = itemView.findViewById(R.id.food_delete);
    }

}

