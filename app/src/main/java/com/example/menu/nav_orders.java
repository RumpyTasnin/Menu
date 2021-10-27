package com.example.menu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class nav_orders extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayAdapter<CharSequence> adapter;
    //ArrayList<dataSet> arrayList;
    FirebaseRecyclerOptions<navOrderDataSet> options;
    FirebaseRecyclerAdapter<navOrderDataSet,FirebaseViewHolder3> firebaseAdapter;
    Query query;
    String uid,ORDER_ID,status,order_id,phone,username,email,address,CLASS;
    Intent intent;
    ImageView close;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_nav_orders);

        recyclerView=findViewById(R.id.firebase_recyclerview);
        close=findViewById(R.id.close);
        username=getIntent().getStringExtra("username");
        phone=getIntent().getStringExtra("phone");
        email=getIntent().getStringExtra("email");
        uid=getIntent().getStringExtra("uid");
        address=getIntent().getStringExtra("deliver_address");
        CLASS=getIntent().getStringExtra("class");
        order_id = String.valueOf(getIntent().getIntExtra("order_id", 0));

        query = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("confirmed_orders");

    try {
            options = new FirebaseRecyclerOptions.Builder<navOrderDataSet>().setQuery(query, navOrderDataSet.class).build();
            firebaseAdapter = new FirebaseRecyclerAdapter<navOrderDataSet, FirebaseViewHolder3>(options) {
                @Override
                protected void onBindViewHolder(@NonNull FirebaseViewHolder3 holder, int position, @NonNull navOrderDataSet model) {
                   if(model.isConfirmed()){
                        ORDER_ID=model.getOrder_id();
                        status=model.getStatus();
                        holder.order_id.setText(model.getToken());
                        holder.date.setText(model.getDate());
                        holder.time.setText(model.getTime());
                        holder.price.setText("TK "+model.getTotal_price());
                        holder.order_id.setText("Order #"+model.getToken());

                       if (status.equals("confirmed")) {

                           holder.status.setImageResource(R.drawable.ic_outline_check_circle_24);
                       } else {
                           if (status.equals("cancel")) {
                               holder.status.setImageResource(R.drawable.ic_outline_cancel_24);
                           } else {
                               if (status.equals("picked_up")) {
                                   holder.status.setImageResource(R.drawable.yellow_ic_sharp_delivery_dining_24);
                               } else {
                                   if (status.equals("processing")) {
                                       holder.status.setImageResource(R.drawable.yellow_ic_sharp_soup_kitchen_24);
                                   } else {
                                       if (status.equals("delivered")) {
                                           holder.status.setImageResource(R.drawable.yellow_home);
                                       }
                                   }

                               }
                           }
                       }
                        holder.reorder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i=new Intent(nav_orders.this,reorder.class);
                                i.putExtra("class",CLASS);
                                i.putExtra("order_id",Integer.parseInt(order_id));
                                i.putExtra("reorder_id",Integer.parseInt(model.getOrder_id()));
                                i.putExtra("phone",phone);
                                i.putExtra("username",username);
                                i.putExtra("email",email);
                                i.putExtra("uid",uid);
                                i.putExtra("deliver_address",address);
                                i.putExtra("total_price",model.getTotal_price());
                                startActivity(i);
                                overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                                finish();
                            }
                        });
                    }



                }

                @NonNull
                @Override
                public FirebaseViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(nav_orders.this)
                            .inflate(R.layout.nav_order_recycler_layout, parent, false);
                    FirebaseViewHolder3 firebaseViewHolder = new FirebaseViewHolder3(view);
                    return firebaseViewHolder;
                }
            };
        } catch (Exception e) {
        }
        try {
            firebaseAdapter.startListening();
            LinearLayoutManager layoutManager = new LinearLayoutManager(nav_orders.this);
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(firebaseAdapter);
        } catch (Exception e) {
            Toast.makeText(nav_orders.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });


    }
    private void close(){
        if(CLASS.equals("MainMenu")){
            intent =new Intent(nav_orders.this,MainMenu.class);
        }else{
            if(CLASS.equals("Classic")){
                intent =new Intent(nav_orders.this,Classic.class);
            } else{
                if(CLASS.equals("Specials")){
                    intent =new Intent(nav_orders.this,Specials.class);
                }
                else {
                    if(CLASS.equals("HOT")){
                        intent =new Intent(nav_orders.this,HOT.class);
                    }
                    else{
                        if(CLASS.equals("ICED")){
                            intent =new Intent(nav_orders.this,ICED.class);
                        }
                        else{
                            if(CLASS.equals("TEA")){
                                intent =new Intent(nav_orders.this,TEA.class);
                            }
                            else {
                                if(CLASS.equals("Freddo")){
                                    intent =new Intent(nav_orders.this,Freddo.class);
                                }
                                else {
                                    if(CLASS.equals("Dessert")){
                                        intent =new Intent(nav_orders.this,Dessert.class);
                                    }
                                    else{
                                        if(CLASS.equals("Others")){
                                            intent =new Intent(nav_orders.this,Others.class);
                                        }
                                    }

                                }

                            }
                        }

                    }
                }
            }
        }



        intent.putExtra("class",CLASS);
        intent.putExtra("order_id",Integer.parseInt(order_id));
        intent.putExtra("phone",phone);
        intent.putExtra("username",username);
        intent.putExtra("email",email);
        intent.putExtra("uid",uid);
        intent.putExtra("deliver_address",address);
        startActivity(intent);
        overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        close();
    }
}