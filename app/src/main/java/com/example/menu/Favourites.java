package com.example.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Favourites extends AppCompatActivity {

    RecyclerView recyclerView;

    //ArrayList<dataSet> arrayList;
    FirebaseRecyclerOptions<favourites_dataset> options;
    FirebaseRecyclerAdapter<favourites_dataset,FirebaseViewHolder6> firebaseAdapter;
    Query query;
    String uid,phone,username,email,address;
    String order_id,CLASS;
    int count;

    ImageView close;
    String food_name,food_description,food_price,regular,large,regular_price,large_price,activity;
    int layout_no,food_image;
    Boolean radio_visibility,add_ons_visibility;

    Intent intent1;

    DatabaseReference databaseReference,reference,place_order_reference,user_reference;
    ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        setContentView(R.layout.activity_favourites);

        recyclerView=findViewById(R.id.firebase_recyclerview);

        close=findViewById(R.id.close);
        order_id = String.valueOf(getIntent().getIntExtra("order_id", 0));
        username=getIntent().getStringExtra("username");
        phone=getIntent().getStringExtra("phone");
        email=getIntent().getStringExtra("email");
        uid=getIntent().getStringExtra("uid");
        address=getIntent().getStringExtra("deliver_address");
        CLASS=getIntent().getStringExtra("class");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user_reference= FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        }
        query = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("Favourites");
        options = new FirebaseRecyclerOptions.Builder<favourites_dataset>().setQuery(query, favourites_dataset.class).build();
        firebaseAdapter = new FirebaseRecyclerAdapter<favourites_dataset, FirebaseViewHolder6>(options) {
            @NonNull
            @Override
            public FirebaseViewHolder6 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(Favourites.this)
                        .inflate(R.layout.favourites_row_layout, parent, false);
                FirebaseViewHolder6 firebaseViewHolder = new FirebaseViewHolder6(view);
                return firebaseViewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolder6 holder, int position, @NonNull favourites_dataset model) {
                food_name = model.getFood_name();
                food_description=model.getFood_description();
                food_price=model.getFood_price();
                regular=model.getRegular();
                large=model.getLarge();
                regular_price=model.getRegular_price();
                large_price=model.getLarge_price();
                activity=model.getActivity();
                layout_no=model.getLayout_no();
                food_image=model.getFood_image();
                radio_visibility=model.isRadio_visibility();
                add_ons_visibility=model.isAdd_ons_visibility();
                count=1;
                holder.food_image.setImageResource(food_image);
                holder.food_name.setText(food_name);
                if(layout_no==1){
                    holder.food_price.setText(regular+" -"+regular_price+"Tk, "+large+" -"+large_price+"Tk");
                }
                else{
                    if(layout_no==2){
                        holder.food_price.setText("Price -"+food_price+"Tk ");
                    }
                }
                holder.heart_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       listener= user_reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                               // Toast.makeText(Favourites.this, "getting called", Toast.LENGTH_LONG).show();
                                    if(snapshot.child("Favourites").hasChild(model.getFood_name())){
                                        holder.heart_logo.setImageResource(R.drawable.blank_black_heart);
                                        user_reference.child("Favourites").child(model.getFood_name()).removeValue();
                                        count=0;
                                        if (databaseReference != null && listener != null) {
                                            databaseReference.removeEventListener(listener);
                                            //Toast.makeText(Order.this, "removed listener", Toast.LENGTH_LONG).show();
                                        }



                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
                holder.main_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Favourites.this,Order.class);
                        intent.putExtra("food_image",model.getFood_image());
                        intent.putExtra("food_name",model.getFood_name());
                        intent.putExtra("food_description",model.getFood_description());
                        intent.putExtra("food_price",model.getFood_price());
                        intent.putExtra("regular",model.getRegular());
                        intent.putExtra("large",model.getLarge());
                        intent.putExtra("add_ons_visibility",model.isAdd_ons_visibility());
                        intent.putExtra("radio",model.isRadio_visibility());
                        intent.putExtra("regular_price",model.getRegular_price());
                        intent.putExtra("large_price",model.getLarge_price());
                        intent.putExtra("class",CLASS);
                        intent.putExtra("order_id",Integer.parseInt(order_id));
                        intent.putExtra("layout",model.getLayout_no());
                        intent.putExtra("username",username);
                        intent.putExtra("email",email);
                        intent.putExtra("deliver_address",address);
                        intent.putExtra("phone",phone);
                        intent.putExtra("uid",uid);
                        intent.putExtra("from_favourites_class",true);
                        startActivity(intent);
                    }
                });

            }
        };
        try {
            firebaseAdapter.startListening();
            recyclerView.setLayoutManager(new LinearLayoutManager(Favourites.this));
            recyclerView.setAdapter(firebaseAdapter);
        } catch (Exception e) {
            Toast.makeText(Favourites.this, e.getMessage(), Toast.LENGTH_LONG).show();

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
            intent1 =new Intent(Favourites.this,MainMenu.class);
        }else{
            if(CLASS.equals("Classic")){
                intent1 =new Intent(Favourites.this,Classic.class);
            } else{
                if(CLASS.equals("Specials")){
                    intent1 =new Intent(Favourites.this,Specials.class);
                }
                else {
                    if(CLASS.equals("HOT")){
                        intent1 =new Intent(Favourites.this,HOT.class);
                    }
                    else{
                        if(CLASS.equals("ICED")){
                            intent1 =new Intent(Favourites.this,ICED.class);
                        }
                        else{
                            if(CLASS.equals("TEA")){
                                intent1 =new Intent(Favourites.this,TEA.class);
                            }
                            else {
                                if(CLASS.equals("Freddo")){
                                    intent1 =new Intent(Favourites.this,Freddo.class);
                                }
                                else {
                                    if(CLASS.equals("Dessert")){
                                        intent1 =new Intent(Favourites.this,Dessert.class);
                                    }
                                    else{
                                        if(CLASS.equals("Others")){
                                            intent1 =new Intent(Favourites.this,Others.class);
                                        }
                                    }

                                }

                            }
                        }

                    }
                }
            }
        }
        intent1.putExtra("class",CLASS);
        intent1.putExtra("order_id",Integer.parseInt(order_id));
        intent1.putExtra("phone",phone);
        intent1.putExtra("username",username);
        intent1.putExtra("email",email);
        intent1.putExtra("uid",uid);
        intent1.putExtra("deliver_address",address);
        startActivity(intent1);
        overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        close();
    }
    @Override
    protected void onStop() {
        super.onStop();

        if (databaseReference != null && listener != null) {
            databaseReference.removeEventListener(listener);
            //Toast.makeText(Order.this, "removed listener", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseReference != null && listener != null) {
            databaseReference.removeEventListener(listener);
           // Toast.makeText(Favourites.this, "removed listener destroy", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(Verification.this,"resumed",Toast.LENGTH_LONG).show();
        if (databaseReference != null && listener != null) {
            databaseReference.removeEventListener(listener);
            //Toast.makeText(Order.this, "removed listener", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(Verification.this,"paused",Toast.LENGTH_LONG).show();
        if (databaseReference != null && listener != null) {
            databaseReference.removeEventListener(listener);
            //Toast.makeText(Order.this, "removed listener", Toast.LENGTH_LONG).show();
        }
    }



}