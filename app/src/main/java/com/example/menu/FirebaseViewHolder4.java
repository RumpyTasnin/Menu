package com.example.menu;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class FirebaseViewHolder4 extends RecyclerView.ViewHolder {
   TextView  food_type, quantity, food_name, price;
    public FirebaseViewHolder4(@NonNull View itemView) {
        super(itemView);

        food_type=itemView.findViewById(R.id.food_type);
        food_name=itemView.findViewById(R.id.food_name);
        quantity=itemView.findViewById(R.id.quantity);
        price=itemView.findViewById(R.id.price);




    }
}
