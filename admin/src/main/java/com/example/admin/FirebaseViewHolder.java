package com.example.admin;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseViewHolder extends RecyclerView.ViewHolder {
    TextView order_id,date,time,price;
    ImageView status;
    CardView layout;
    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);
        order_id=itemView.findViewById(R.id.order_id);
        date=itemView.findViewById(R.id.date);
        time=itemView.findViewById(R.id.time);
        status=itemView.findViewById(R.id.status);
        price=itemView.findViewById(R.id.price);
        layout=itemView.findViewById(R.id.layout);
    }
}
