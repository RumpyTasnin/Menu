package com.example.menu;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseViewHolder5 extends RecyclerView.ViewHolder {
    TextView location1,location2;
    LinearLayout row;
    ImageView logo,edit;
    public FirebaseViewHolder5(@NonNull View itemView) {
        super(itemView);
        logo=itemView.findViewById(R.id.logo);
        edit=itemView.findViewById(R.id.edit);
        location1=itemView.findViewById(R.id.location);
        location2=itemView.findViewById(R.id.location1);
        row=itemView.findViewById(R.id.row);


    }
}

