package com.example.menu;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;

public class FirebaseViewHolder2 extends RecyclerView.ViewHolder {
    TextView location1,location2;
    LinearLayout row;
    ImageView location,done,close;
    public FirebaseViewHolder2(@NonNull View itemView) {
        super(itemView);

        location=itemView.findViewById(R.id.work_home);
        done=itemView.findViewById(R.id.done);
        close=itemView.findViewById(R.id.close);
        location1=itemView.findViewById(R.id.location);
        location2=itemView.findViewById(R.id.location1);
        row=itemView.findViewById(R.id.row);




    }
   /* public  void deleteItem(){
        listener.handleDeleteItem( getSnapshots().getSnapshot(getAdapterPosition()))
    }
    interface listener<DocumentSnapshot> {
       public void handleDeleteItem(DocumentSnapshot snapshot);

    }*/

}
