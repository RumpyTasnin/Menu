package com.example.menu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Random;

public class reorder extends AppCompatActivity {

    RecyclerView recyclerView;

    //ArrayList<dataSet> arrayList;
    FirebaseRecyclerOptions<reorder_dataset> options;
    FirebaseRecyclerAdapter<reorder_dataset,FirebaseViewHolder4> firebaseAdapter;
    Query query;
    String uid,phone,username,email,address;
    String name,order_id,reorder_id,CLASS,REORDERID;
    int odId;
    LinearLayout addCart;
    ImageView close;


    DatabaseReference databaseReference,reference,place_order_reference,user_reference;

    String extra_shot_price,iced_price,food_price,food_type,layout,instructions,food_name,s;
    int food_amount,extra_shot_amount,iced_amount,total_price;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_reorder);

        recyclerView=findViewById(R.id.firebase_recyclerview);
        addCart=findViewById(R.id.addCart);
        close=findViewById(R.id.close);
        order_id = String.valueOf(getIntent().getIntExtra("order_id", 0));
        REORDERID=String.valueOf(getIntent().getIntExtra("reorder_id", 0));
        username=getIntent().getStringExtra("username");
        phone=getIntent().getStringExtra("phone");
        email=getIntent().getStringExtra("email");
        uid=getIntent().getStringExtra("uid");
        address=getIntent().getStringExtra("deliver_address");
        CLASS=getIntent().getStringExtra("class");
        total_price=getIntent().getIntExtra("total_price",0);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user_reference=FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            reference=FirebaseDatabase.getInstance().getReference("cart");
            databaseReference = FirebaseDatabase.getInstance().getReference("orders");
            place_order_reference=FirebaseDatabase.getInstance().getReference().child("placed_order");
        }


        Random random = new Random();
        odId = random.nextInt();
        if (odId < 0) {
            odId = odId * (-1);
        }
        reorder_id = String.valueOf(odId);
        databaseReference.child(reorder_id).child("customer_name").setValue(username);
        databaseReference.child(reorder_id).child("customer_phone").setValue(phone);
        databaseReference.child(reorder_id).child("customer_email").setValue(email);
        databaseReference.child(reorder_id).child("customer_uid").setValue(uid);
        databaseReference.child(reorder_id).child("confirmed").setValue(false);

        user_reference.child("order_history").child("recent_order_id").setValue(reorder_id);


        reference.child(reorder_id).child("user_info").child("customer_name").setValue(username);
        reference.child(reorder_id).child("user_info").child("customer_phone").setValue(phone);
        reference.child(reorder_id).child("user_info").child("customer_email").setValue(email);
        reference.child(reorder_id).child("user_info").child("customer_uid").setValue(uid);
        //reference.child(order_id).child("user_info").child("customer_address").setValue(address);
        // reference.child(order_id).child("user_info").child("date").setValue(currentDate);

        place_order_reference.child("orders").child(reorder_id).child("user_info").child("customer_name").setValue(username);
        place_order_reference.child("orders").child(reorder_id).child("user_info").child("customer_phone").setValue(phone);
        place_order_reference.child("orders").child(reorder_id).child("user_info").child("customer_email").setValue(email);
        place_order_reference.child("orders").child(reorder_id).child("user_info").child("customer_uid").setValue(uid);
        place_order_reference.child("orders").child(reorder_id).child("Confirmed").setValue(false);

       // Toast.makeText(reorder.this,"current order id "+ order_id+" reorderid "+REORDERID,Toast.LENGTH_LONG).show();

        query = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("order_history").child(REORDERID).child("food");


        try {
            options = new FirebaseRecyclerOptions.Builder<reorder_dataset>().setQuery(query, reorder_dataset.class).build();
            firebaseAdapter = new FirebaseRecyclerAdapter<reorder_dataset, FirebaseViewHolder4>(options) {
                @Override
                protected void onBindViewHolder(@NonNull FirebaseViewHolder4  holder, int position, @NonNull reorder_dataset model) {
                    extra_shot_amount=model.getExtra_shot_amount();
                    extra_shot_price=model.getExtra_shot_price();
                    iced_amount=model.getIced_amount();
                    iced_price=model.getIced_price();
                    food_name=model.getFood_name();
                    food_price=model.getFood_price();
                    food_amount=model.getFood_amount();
                    food_type=model.food_type;
                    instructions=model.getInstructions();
                    layout=model.getLayout();

                    holder.food_name.setText(food_name);
                    try {
                        if (model.getFood_type().contains("Regular")) {
                            holder.food_type.setText("(R)");
                        } else {
                            if (model.getFood_type().contains("Large")) {
                                holder.food_type.setText("(L)");
                            } else {
                                if (model.getFood_type().contains("Single")) {
                                    holder.food_type.setText("(S)");
                                } else {
                                    if (model.getFood_type().contains("Double")) {
                                        holder.food_type.setText("(D)");
                                    } else {
                                        if (model.getFood_type().contains("Half")) {
                                            holder.food_type.setText("(H)");
                                        } else {
                                            if (model.getFood_type().contains("Full")) {
                                                holder.food_type.setText("(D)");
                                            } else {
                                                holder.food_type.setText("(R)");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (model.getIced_amount() == 0 && model.getExtra_shot_amount() == 0) {
                            holder.price.setText("TK " + model.getFood_price() + ".00");
                        } else {

                            if (model.getExtra_shot_amount() != 0) {
                                s = "Extra Shot";
                                if (model.getIced_amount() != 0) {
                                    s = s + "+ Iced";
                                }
                            } else {
                                if (model.getIced_amount() != 0) {
                                    s = " Iced";
                                }
                            }
                            holder.price.setText("TK " + model.getFood_price() + ".00" + " " + s);

                        }

                        holder.quantity.setText(String.valueOf(model.getFood_amount())+"X");
                    } catch (Exception e) {
                        Toast.makeText(reorder.this, "error amount ", Toast.LENGTH_LONG).show();
                    }
                    if(layout.equals("1")){
                        name=food_name+"("+food_type+", extra_shot_amount="+extra_shot_amount+" ,iced_amount= "+iced_amount+")";
                        layout(databaseReference,reorder_id,name,food_type,food_amount,extra_shot_amount,iced_amount,layout,food_name);
                    }
                    else{
                        if(layout.equals("2")){
                            name=food_name;
                            layout(databaseReference,reorder_id,name,food_type,food_amount,extra_shot_amount,iced_amount,layout,food_name);
                        }
                    }

                }

                @NonNull
                @Override
                public FirebaseViewHolder4 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(reorder.this)
                            .inflate(R.layout.reorder_row_layout, parent, false);
                    FirebaseViewHolder4 firebaseViewHolder = new FirebaseViewHolder4(view);
                    return firebaseViewHolder;
                }


            };
        } catch (Exception e) {
        }


        try {
            firebaseAdapter.startListening();

            recyclerView.setLayoutManager(new LinearLayoutManager(reorder.this));
            recyclerView.setAdapter(firebaseAdapter);
        } catch (Exception e) {
            Toast.makeText(reorder.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(reorder.this,Cart.class);
                i.putExtra("not_order_class",true);
                i.putExtra("from_navigation",true);
                i.putExtra("class",CLASS);
                i.putExtra("order_id",Integer.parseInt(reorder_id));
                i.putExtra("phone",phone);
                i.putExtra("username",username);
                i.putExtra("email",email);
                i.putExtra("uid",uid);
                i.putExtra("deliver_address",address);
                startActivity(i);
                overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                finish();

            }
        });

    }

    private void layout(DatabaseReference databaseReference, String order_id, String food_name, String food_type,int food_amount,int extra_shot_amount,int iced_amount,String layout,String FOODNAME) {

        try{
            databaseReference.child(order_id).child("food").child(food_name).child("food_name").setValue(FOODNAME);
            databaseReference.child(order_id).child("food").child(food_name).child("food_amount").setValue(food_amount);
            databaseReference.child(order_id).child("food").child(food_name).child("food_type").setValue(food_type);
            databaseReference.child(order_id).child("food").child(food_name).child("food_price").setValue(food_price);
            databaseReference.child(order_id).child("food").child(food_name).child("extra_shot_amount").setValue(extra_shot_amount);
            databaseReference.child(order_id).child("food").child(food_name).child("iced_amount").setValue(iced_amount);
            databaseReference.child(order_id).child("food").child(food_name).child("extra_shot_price").setValue("35.00TK");
            databaseReference.child(order_id).child("food").child(food_name).child("iCED_price").setValue("25.00TK");
            databaseReference.child(order_id).child("food").child(food_name).child("instructions").setValue(instructions);
            databaseReference.child(order_id).child("food").child(food_name).child("layout").setValue(layout);
            databaseReference.child(order_id).child("total_price").setValue(total_price);


            //reference.child(order_id).child("food").child(food_name+"("+FOOD_TYPE+", extra_shot_amount="+add2_count+" ,iced_amount= "+add3_count+")").child("time ").setValue(currentTime);
            reference.child(order_id).child("food").child(food_name).child("food_name").setValue(FOODNAME);
            reference.child(order_id).child("food").child(food_name).child("food_amount").setValue(food_amount);
            reference.child(order_id).child("food").child(food_name).child("food_type").setValue(food_type);
            reference.child(order_id).child("food").child(food_name).child("food_price").setValue(food_price);
            reference.child(order_id).child("food").child(food_name).child("extra_shot_amount").setValue(extra_shot_amount);
            reference.child(order_id).child("food").child(food_name).child("iced_amount").setValue(iced_amount);
            reference.child(order_id).child("food").child(food_name).child("extra_shot_price").setValue("35.00TK");
            reference.child(order_id).child("food").child(food_name).child("iced_price").setValue("25.00TK");
            reference.child(order_id).child("food").child(food_name).child("instructions").setValue(instructions);
            reference.child(order_id).child("food").child(food_name).child("layout").setValue(layout);
            reference.child(order_id).child("user_info").child("total_price").setValue(total_price);

            user_reference.child("order_history").child(order_id).child("food").child(food_name).child("food_name").setValue(FOODNAME);
            user_reference.child("order_history").child(order_id).child("food").child(food_name).child("food_amount").setValue(food_amount);
            user_reference.child("order_history").child(order_id).child("food").child(food_name).child("food_type").setValue(food_type);
            user_reference.child("order_history").child(order_id).child("food").child(food_name).child("food_price").setValue(food_price);
            user_reference.child("order_history").child(order_id).child("food").child(food_name).child("extra_shot_amount").setValue(extra_shot_amount);
            user_reference.child("order_history").child(order_id).child("food").child(food_name).child("iced_amount").setValue(iced_amount);
            user_reference.child("order_history").child(order_id).child("food").child(food_name).child("extra_shot_price").setValue("35.00TK");
            user_reference.child("order_history").child(order_id).child("food").child(food_name).child("iced_price").setValue("25.00TK");
            user_reference.child("order_history").child(order_id).child("food").child(food_name).child("instructions").setValue(instructions);
            user_reference.child("order_history").child(order_id).child("total_price").setValue(total_price);
            user_reference.child("order_history").child(order_id).child("food").child(food_name).child("layout").setValue(layout);



            place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("food_name").setValue(FOODNAME);
            place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("food_amount").setValue(food_amount);
            place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("food_type").setValue(food_type);
            place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("food_price").setValue(food_price);
            place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("extra_shot_amount").setValue(extra_shot_amount);
            place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("iced_amount").setValue(iced_amount);
            place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("extra_shot_price").setValue("35.00TK");
            place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("iced_price").setValue("25.00TK");
            place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("instructions").setValue(instructions);
            place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("layout").setValue(layout);

            place_order_reference.child("orders").child(order_id).child("total_price").setValue(total_price);
           // place_order_reference.child("orders").child(order_id).child("Branch").setValue("");



        }catch (Exception e){
            Toast.makeText(reorder.this,"error  layout "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
    private void close(){
        databaseReference.child(reorder_id).removeValue();
        reference.child(reorder_id).removeValue();
        user_reference.child("order_history").child(reorder_id).removeValue();
        user_reference.child("order_history").child("recent_order_id").setValue(String.valueOf(getIntent().getIntExtra("order_id", 0)));
        place_order_reference.child("orders").child(reorder_id).removeValue();
        Intent i =new Intent(reorder.this,nav_orders.class);
        i.putExtra("class",CLASS);
        i.putExtra("order_id",order_id);
        i.putExtra("phone",phone);
        i.putExtra("username",username);
        i.putExtra("email",email);
        i.putExtra("uid",uid);
        i.putExtra("deliver_address",address);
        startActivity(i);
        overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        close();
    }
}