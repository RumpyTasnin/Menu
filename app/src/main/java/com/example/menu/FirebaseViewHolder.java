package com.example.menu;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseViewHolder extends RecyclerView.ViewHolder {
    TextView food_type,food_name,count,price;
    ImageView add,minus;
    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);
        food_type=itemView.findViewById(R.id.food_type);
        food_name=itemView.findViewById(R.id.food_name);
        count=itemView.findViewById(R.id.count);
        add=itemView.findViewById(R.id.add);
        minus=itemView.findViewById(R.id.minus);
        price=itemView.findViewById(R.id.row_price);




    }
}
