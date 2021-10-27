package com.example.menu;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseViewHolder6 extends RecyclerView.ViewHolder {
    ImageView food_image,heart_logo;
    LinearLayout heart_layout,main_layout;
    TextView food_name, food_price;
    public FirebaseViewHolder6(@NonNull View itemView) {
        super(itemView);
        food_image=itemView.findViewById(R.id.food_image);
        heart_logo=itemView.findViewById(R.id.heart_logo);
        heart_layout=itemView.findViewById(R.id.fav);
        food_name=itemView.findViewById(R.id.food_name);
        food_price=itemView.findViewById(R.id.food_price);
        main_layout=itemView.findViewById(R.id.main_layout);
    }
}
