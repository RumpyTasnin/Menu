package com.example.menu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Queue;
import java.util.Random;

public class Cart<Firebase> extends AppCompatActivity {

    ImageView back, back2;
    TextView subtotal, delivery_fee, discount, total;
    RadioGroup radioGroup;
    boolean not_order_class = false;
    Spinner spinner;
    String branch ;
    String currentDate, currentTime;
    TextView textView;
    boolean called_reference_once=false;
    boolean called_reference_from_start,refernce_for_spinner=false;
    Intent i;


    RadioButton home_delivery, dine_in, take_away;
    LinearLayout place_order, layout1, layout2;
    Intent intent, intent1;
    String delivery="";
    String token;
    String status = "";
    String pickup_time = "0";
    String order_id, phone, username, address, email, uid;
    int price_total, total_charge, deliver_charge, discount_charge = 0;
    RecyclerView recyclerView;
    boolean address_confirmed, back_to_mainmenu = false;
    Dialog dialog, dialog1;
    int food_count, iced, extra = 0;
    String s = "";
    String layout, type;
    String CLASS = "";
    int extra_amount, iced_amount, food_amount, stotal, food_price, odId;
    ArrayAdapter<CharSequence> adapter;


    ValueEventListener listener,listener1;
    int food_image, layout_no;
    String food_name, food_description, food_price_intent, food_size_regular, food_size_large, activity, regular_price, large_price;
    boolean visibility, radio_group_visibility;

    //ArrayList<dataSet> arrayList;
    FirebaseRecyclerOptions<dataSet> options;
    FirebaseRecyclerAdapter<dataSet, FirebaseViewHolder> firebaseAdapter;
    Query query;
    String key,key1;

    DatabaseReference databaseReference, place_order_reference, user_reference, cart_reference, order_reference,reference_push_user_confirmed
            ,branch_reference;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //status bar transparent
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_cart2);
        layout1 = findViewById(R.id.cart_linear_layout);
        layout2 = findViewById(R.id.cart_linear_layout2);

        back = findViewById(R.id.cart_back);
        back2 = findViewById(R.id.cart_back2);
        subtotal = findViewById(R.id.subtotal);
        delivery_fee = findViewById(R.id.delivery_fee);
        discount = findViewById(R.id.discount);
        total = findViewById(R.id.total);
        home_delivery = findViewById(R.id.home_delivery);
        dine_in = findViewById(R.id.dine);
        take_away = findViewById(R.id.take_away);
        place_order = findViewById(R.id.confirm);
        recyclerView = findViewById(R.id.firebase_recyclerview);
        radioGroup = findViewById(R.id.radioGroup);
        spinner = findViewById(R.id.spinner);

        try {
            // Create an ArrayAdapter using the string array and a default spinner layout
            adapter = ArrayAdapter.createFromResource(Cart.this, R.array.branches, R.layout.spinner_textview);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  //  Toast.makeText(Cart.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
                    branch = parent.getItemAtPosition(position).toString();
                    place_order_reference = FirebaseDatabase.getInstance().getReference().child("placed_order");
                    if(!order_id.equals("0")){
                    place_order_reference.child("orders").child(order_id).child("Branch").setValue(branch);}
                    listener1= place_order_reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!refernce_for_spinner) {
                                if (snapshot.child("Branch").hasChild(branch + "_status")) {
                                    status = snapshot.child("Branch").child(branch + "_status").getValue().toString();
                                    refernce_for_spinner=true;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(Cart.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


        //recyclerView.setHasFixedSize(true);
        intent = getIntent();
        order_id = String.valueOf(getIntent().getIntExtra("order_id", 0));

        if (order_id.equals("0")) {
            layout1.setVisibility(View.INVISIBLE);
            layout2.setVisibility(View.VISIBLE);
            back_to_mainmenu = true;
          //  Toast.makeText(Cart.this, "order id null", Toast.LENGTH_LONG).show();
        }
       // Toast.makeText(Cart.this, order_id, Toast.LENGTH_LONG).show();
        ///
        CLASS = getIntent().getStringExtra("class");
        not_order_class = getIntent().getBooleanExtra("not_order_class", false);
/////////////////////
        username = getIntent().getStringExtra("username");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        uid = getIntent().getStringExtra("uid");
        address = getIntent().getStringExtra("deliver_address");

        if (!getIntent().getBooleanExtra("not_order_class", false)) {
            food_image = getIntent().getIntExtra("food_image", 0);
            food_name = getIntent().getStringExtra("food_name");
            food_description = getIntent().getStringExtra("food_description").trim();
            food_price_intent = getIntent().getStringExtra("food_price");
            visibility = getIntent().getBooleanExtra("add_ons_visibility", true);
            radio_group_visibility = getIntent().getBooleanExtra("radio", true);
            food_size_regular = getIntent().getStringExtra("regular");
            food_size_large = getIntent().getStringExtra("large");
            activity = getIntent().getStringExtra("activity");
            regular_price = getIntent().getStringExtra("regular_price");
            large_price = getIntent().getStringExtra("large_price");
            layout_no = getIntent().getIntExtra("layout", 1);
            not_order_class = false;
        } else {
            not_order_class = true;
        }


        dialog = new Dialog(Cart.this);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);

        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_layout));
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomActivityAnimation;
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                try {
                    order_reference = FirebaseDatabase.getInstance().getReference("orders");
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("cart");
                    Intent i = new Intent(Cart.this, MainMenu.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtra("order_id", 0);
                    i.putExtra("username", username);
                    i.putExtra("phone", phone);
                    i.putExtra("uid", uid);
                    i.putExtra("email", email);
                    i.putExtra("deliver_address", address);
                    databaseReference.child(order_id).setValue(null);
                    order_reference.child(order_id).setValue(null);

                    startActivity(i);
                    overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                    finish();
                } catch (Exception e) {

                }
            }
        });
        ////#################################################
        ////########################################## on click listenerpu
        address_confirmed = getIntent().getBooleanExtra("address_confirmed", false);
        if (address_confirmed) {
          //  Toast.makeText(Cart.this, order_id, Toast.LENGTH_SHORT).show();
            radioGroup.check(home_delivery.getId());
            delivery = "home_delivery";
            deliver_charge = 80;
        }


        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected(Cart.this)){
                    currentDate = new SimpleDateFormat("EEE, d MMM, yyyy", Locale.getDefault()).format(new Date());
                    currentTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
                    if (status.equals("rush")) {
                        Toast.makeText(Cart.this, "there's currently a huge rush", Toast.LENGTH_LONG).show();
                    } else {
                        if (price_total != 0) {
                            if (delivery != null) {


                                user_reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                                place_order_reference = FirebaseDatabase.getInstance().getReference().child("placed_order");
                                order_reference = FirebaseDatabase.getInstance().getReference("orders");
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("cart");
                                reference_push_user_confirmed=user_reference.child("confirmed_orders").push();
                                branch_reference=place_order_reference.child("confirmed_orders").child(branch).child(delivery).push();
                                // place_order_reference.child(order_id).child("Date").setValue(currentDate);
                                key=reference_push_user_confirmed.getKey();
                                key1=branch_reference.getKey();

                                place_order_reference.child("orders").child(order_id).child("placed_order_confirmed_key").setValue(key1);
                                place_order_reference.child("orders").child(order_id).child("user_order_confirmed_key").setValue(key);


                                ///////////////////////////////////////////////////////
                                branch_reference.child("Date").setValue(currentDate);
                                branch_reference.child("Time").setValue(currentTime);
                                branch_reference.child("status").setValue("confirmed");
                                branch_reference.child("order_id").setValue(order_id);

                                user_reference.child("order_history").child(order_id).child("Date").setValue(currentDate);
                                user_reference.child("order_history").child(order_id).child("Time").setValue(currentTime);
                                user_reference.child("order_history").child(order_id).child("order_id").setValue(order_id);
                                user_reference.child("order_history").child(order_id).child("status").setValue("confirmed");

                                reference_push_user_confirmed.child("Date").setValue(currentDate);
                                reference_push_user_confirmed.child("Time").setValue(currentTime);
                                reference_push_user_confirmed.child("order_id").setValue(order_id);
                                reference_push_user_confirmed.child("status").setValue("confirmed");


                                token = getToken();
                                if (delivery.equals("home_delivery")) {

                                    if (address_confirmed||address!=null|| !address.isEmpty()) {
                                        try {

                                            user_reference.child("order_history").child("recent_order_id").setValue("0");

                                            // Toast.makeText(Cart.this, token, Toast.LENGTH_SHORT).show();
                                            place_order_reference.child("orders").child(order_id).child("delivery").setValue(delivery);
                                            place_order_reference.child("orders").child(order_id).child("delivery_fee").setValue(deliver_charge);
                                            user_reference.child("order_history").child(order_id).child("Token").setValue(token);

                                            branch_reference.child("delivery").setValue(delivery);
                                            branch_reference.child("Token").setValue(token);
                                            place_order_reference.child("orders").child(order_id).child("Confirmed").setValue(true);
                                            user_reference.child("order_history").child(order_id).child("Confirmed").setValue(true);

                                            reference_push_user_confirmed.child("total_price").setValue(price_total);
                                            branch_reference.child("total_price").setValue(price_total);

                                            reference_push_user_confirmed.child("final_price").setValue(total_charge);
                                            branch_reference.child("final_price").setValue(total_charge);

                                            reference_push_user_confirmed.child("Confirmed").setValue(true);
                                            reference_push_user_confirmed.child("Token").setValue(token);


                                            TextView textView = dialog.findViewById(R.id.token);
                                            textView.setText("#" + token);
                                            dialog.show();

                                        } catch (Exception e) {
                                            Toast.makeText(Cart.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        if(address==null||address.isEmpty()){
                                            Toast.makeText(Cart.this, "select a deliver address", Toast.LENGTH_SHORT).show();

                                        }                                    }

                                }
                                if (delivery.equals("take_away") && pickup_time != null) {
                                    user_reference.child("order_history").child("recent_order_id").setValue("0");
                                    // Toast.makeText(Cart.this, token, Toast.LENGTH_SHORT).show();
                                    place_order_reference.child("orders").child(order_id).child("delivery").setValue(delivery);
                                    place_order_reference.child("orders").child(order_id).child("pickup_in").setValue(pickup_time);
                                    reference_push_user_confirmed.child("total_price").setValue(total_charge);
                                    branch_reference.child("total_price").setValue(total_charge);


                                    branch_reference.child("delivery").setValue(delivery);
                                    branch_reference.child("Token").setValue(token);

                                    user_reference.child("order_history").child(order_id).child("Token").setValue(token);

                                    place_order_reference.child("orders").child(order_id).child("Confirmed").setValue(true);
                                    user_reference.child("order_history").child(order_id).child("Confirmed").setValue(true);

                                    reference_push_user_confirmed.child("Confirmed").setValue(true);
                                    reference_push_user_confirmed.child("Token").setValue(token);


                                    TextView textView = dialog.findViewById(R.id.token);
                                    textView.setText("#" + token);
                                    dialog.show();

                                }
                                if (delivery.equals("dine_in")) {
                                    user_reference.child("order_history").child("recent_order_id").setValue("0");
                                    // Toast.makeText(Cart.this, token, Toast.LENGTH_SHORT).show();
                                    place_order_reference.child("orders").child(order_id).child("delivery").setValue(delivery);

                                    user_reference.child("order_history").child(order_id).child("Token").setValue(token);
                                    branch_reference.child("delivery").setValue(delivery);
                                    branch_reference.child("Token").setValue(token);
                                    reference_push_user_confirmed.child("total_price").setValue(total_charge);
                                    branch_reference.child("total_price").setValue(total_charge);



                                    place_order_reference.child("orders").child(order_id).child("Confirmed").setValue(true);
                                    user_reference.child("order_history").child(order_id).child("Confirmed").setValue(true);

                                    reference_push_user_confirmed.child("Confirmed").setValue(true);
                                    reference_push_user_confirmed.child("Token").setValue(token);
                                    TextView textView = dialog.findViewById(R.id.token);
                                    textView.setText("#" + token);
                                    dialog.show();

                                }
                            } else {
                                Toast.makeText(Cart.this, "Select a deliver method", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(Cart.this, "Empty cart, can not place order", Toast.LENGTH_LONG).show();
                        }

                    }

                }

            }
        });


        dialog1 = new Dialog(Cart.this);
        dialog1.setContentView(R.layout.dialog2);
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setGravity(Gravity.CENTER);
        dialog1.setCancelable(false);
        dialog1.getWindow().getAttributes().windowAnimations = R.style.CustomActivityAnimation;
        take_away.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.show();


                dialog1.findViewById(R.id.take_away_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(delivery.equals("home_delivery")){
                            radioGroup.check(home_delivery.getId());


                        }
                        else{
                            if(delivery.equals("dine_in")){
                                radioGroup.check(dine_in.getId());

                            }
                            else {
                                radioGroup.clearCheck();
                                delivery="";
                            }
                        }

                        pickup_time = "";
                        dialog1.dismiss();
                    }
                });
                dialog1.findViewById(R.id.twenty).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(delivery.isEmpty()&&delivery.equals("home_delivery")){
                            databaseReference.child(order_id).child("final_price").removeValue();
                            place_order_reference.child("orders").child(order_id).child("final_price").removeValue();
                            user_reference.child("order_history").child(order_id).child("final_price").removeValue();
                            databaseReference.child(order_id).child("Delivery_fee").removeValue();
                            place_order_reference.child("orders").child(order_id).child("Delivery_fee").removeValue();
                            deliver_charge=0;
                            set_price();
                        }
                        pickup_time = "20";
                        delivery = "take_away";
                        place_order_reference.child("orders").child(order_id).child("delivery").setValue(delivery);
                        dialog1.dismiss();
                    }
                });
                dialog1.findViewById(R.id.forty).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!delivery.isEmpty()&&delivery.equals("home_delivery")){
                            databaseReference.child(order_id).child("final_price").removeValue();
                            place_order_reference.child("orders").child(order_id).child("final_price").removeValue();
                            user_reference.child("order_history").child(order_id).child("final_price").removeValue();
                            databaseReference.child(order_id).child("Delivery_fee").removeValue();
                            place_order_reference.child("orders").child(order_id).child("Delivery_fee").removeValue();
                            deliver_charge=0;
                            set_price();
                        }
                        pickup_time = "40";
                        delivery = "take_away";
                        place_order_reference.child("orders").child(order_id).child("delivery").setValue(delivery);
                        dialog1.dismiss();
                    }
                });
                dialog1.findViewById(R.id.hour).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!delivery.isEmpty()&&delivery.equals("home_delivery")){
                            databaseReference.child(order_id).child("final_price").removeValue();
                            place_order_reference.child("orders").child(order_id).child("final_price").removeValue();
                            user_reference.child("order_history").child(order_id).child("final_price").removeValue();
                            databaseReference.child(order_id).child("Delivery_fee").removeValue();
                            place_order_reference.child("orders").child(order_id).child("Delivery_fee").removeValue();
                            deliver_charge=0;
                            set_price();
                        }
                        pickup_time = "60";
                        delivery = "take_away";
                        place_order_reference.child("orders").child(order_id).child("delivery").setValue(delivery);
                        dialog1.dismiss();

                    }
                });

            }
        });
        home_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delivery = "home_delivery";
                place_order_reference.child("orders").child(order_id).child("delivery").setValue(delivery);
                deliver_charge = 80;
                databaseReference = FirebaseDatabase.getInstance().getReference().child("cart");
                place_order_reference = FirebaseDatabase.getInstance().getReference().child("placed_order");

                databaseReference.child(order_id).child("delivery").setValue(delivery);
                databaseReference.child(order_id).child("Delivery_fee").setValue(deliver_charge);
                place_order_reference.child("orders").child(order_id).child("Delivery_fee").setValue(deliver_charge);

                intent1 = new Intent(Cart.this, home_delivery.class);
                intent1.putExtra("order_id", Integer.parseInt(order_id));
                intent1.putExtra("phone", phone);
                intent1.putExtra("username", username);
                intent1.putExtra("email", email);
                intent1.putExtra("uid", uid);
                intent1.putExtra("deliver_address", address);
                intent1.putExtra("not_order_class", not_order_class);

                intent1.putExtra("class", CLASS);
                if (!not_order_class) {
                    if(getIntent().getBooleanExtra("from_favourites_class",false)){
                        intent1.putExtra("from_favourites_class",true);
                    }
                    intent1.putExtra("food_image", food_image);
                    intent1.putExtra("food_name", food_name);
                    intent1.putExtra("food_description", food_description);
                    intent1.putExtra("food_price", food_price_intent);
                    intent1.putExtra("regular", food_size_regular);
                    intent1.putExtra("large", food_size_large);
                    intent1.putExtra("add_ons_visibility", visibility);
                    intent1.putExtra("radio", radio_group_visibility);
                    intent1.putExtra("regular_price", regular_price);
                    intent1.putExtra("large_price", large_price);
                    intent1.putExtra("activity", activity);
                    intent1.putExtra("layout", layout_no);
                }

                set_price();
                databaseReference.child(order_id).child("final_price").setValue(total_charge);
                place_order_reference.child("orders").child(order_id).child("final_price").setValue(total_charge);
                user_reference.child("order_history").child(order_id).child("final_price").setValue(total_charge);


                try {
                    if(!total.getText().toString().isEmpty()) {
                        startActivity(intent1);
                        overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                    }
                    else {
                        Toast.makeText(Cart.this, " wait a moment ", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Cart.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                finish();


            }
        });
        dine_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!delivery.isEmpty()&&delivery.equals("home_delivery")){
                    databaseReference.child(order_id).child("final_price").removeValue();
                    place_order_reference.child("orders").child(order_id).child("final_price").removeValue();
                    user_reference.child("order_history").child(order_id).child("final_price").removeValue();
                    databaseReference.child(order_id).child("Delivery_fee").removeValue();
                    place_order_reference.child("orders").child(order_id).child("Delivery_fee").removeValue();
                    deliver_charge=0;
                    set_price();
                }
                delivery = "dine_in";
                place_order_reference.child("orders").child(order_id).child("delivery").setValue(delivery);
//                databaseReference.child(order_id).child("delivery").setValue(delivery);

            }
        });

        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });


        ////#####################################
        /////##################################
        if (order_id != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("cart").child(order_id);
            place_order_reference = FirebaseDatabase.getInstance().getReference().child("placed_order");


            listener=databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if(!called_reference_once) {
                            //Toast.makeText(Cart.this, "called_reference_once "+String.valueOf(called_reference_once), Toast.LENGTH_LONG).show();
                            price_total = Integer.parseInt(snapshot.child("user_info").child("total_price").getValue().toString());

                            if (snapshot.child("Delivery").hasChild("home_delivery")) {
                                deliver_charge = Integer.parseInt(snapshot.child("Delivery").child("home_delivery").child("delivery_fee").getValue().toString());
                            }
                            set_price();
                            if (databaseReference != null && listener != null) {
                                databaseReference.removeEventListener(listener);
                                called_reference_once = true;
                              //  Toast.makeText(Cart.this, "removed", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });}



    }

    private void set_price() {

        total_charge = price_total + discount_charge + deliver_charge;
        delivery_fee.setText("Tk " + String.valueOf(deliver_charge) + ".00");
        discount.setText("Tk " + String.valueOf(discount_charge) + ".00");
        total.setText("Tk " + String.valueOf(total_charge) + ".00");
        subtotal.setText("Tk " + String.valueOf(price_total) + ".00");

    }

    @Override
    protected void onStart() {
        super.onStart();

        user_reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        place_order_reference = FirebaseDatabase.getInstance().getReference().child("placed_order");
        order_reference = FirebaseDatabase.getInstance().getReference("orders");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("cart");
        recyclerView = findViewById(R.id.firebase_recyclerview);


        //recyclerView.setHasFixedSize(true);
        //  intent=getIntent();
        order_id = String.valueOf(getIntent().getIntExtra("order_id", 0));
        username = getIntent().getStringExtra("username");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        uid = getIntent().getStringExtra("uid");
        address = getIntent().getStringExtra("deliver_address");
        query = FirebaseDatabase.getInstance().getReference().child("cart").child(order_id).child("food");
       // Toast.makeText(Cart.this, "order id " + order_id, Toast.LENGTH_LONG).show();


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            textView = findViewById(R.id.place_order_text);


            if (order_id.equals("0")) {
                layout1.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.VISIBLE);
                back_to_mainmenu = true;
               // Toast.makeText(Cart.this, "order id null", Toast.LENGTH_LONG).show();
            } else {
                try {
                   listener1= place_order_reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!called_reference_from_start){
                                if (snapshot.child("orders").child(order_id).hasChild("Branch")) {
                                    branch = snapshot.child("orders").child(order_id).child("Branch").getValue().toString();

                                    adapter = ArrayAdapter.createFromResource(Cart.this, R.array.branches, R.layout.spinner_textview);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                 //   Toast.makeText(Cart.this,"branch "+branch,Toast.LENGTH_LONG).show();

                                    spinner.setAdapter(adapter);
                                    spinner.setSelection(adapter.getPosition(branch));

                                }
                                if(snapshot.child("orders").child(order_id).hasChild("delivery")){
                                    delivery=snapshot.child("orders").child(order_id).child("delivery").getValue().toString();
                                    if(delivery.equals("home_delivery")){
                                        radioGroup.check(home_delivery.getId());
                                    }
                                    else {
                                        if(delivery.equals("take_away")){
                                            radioGroup.check(take_away.getId());
                                        }
                                        else{
                                            if(delivery.equals("dine_in")){
                                                radioGroup.check(dine_in.getId());
                                            }
                                        }
                                    }
                                }
                                if (snapshot.child("Branch").hasChild(branch + "_status")) {
                                    status = snapshot.child("Branch").child(branch + "_status").getValue().toString();
                                }
                                called_reference_from_start=true;
                            }



                            //Toast.makeText(Cart.this,"status checked "+status,Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Cart.this, "status check error ", Toast.LENGTH_LONG).show();

                        }
                    });
                } catch (Exception e) {
                }

            }

            try {
                options = new FirebaseRecyclerOptions.Builder<dataSet>().setQuery(query, dataSet.class).build();
                firebaseAdapter = new FirebaseRecyclerAdapter<dataSet, FirebaseViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FirebaseViewHolder holder, int position, @NonNull dataSet model) {
                   //  allowed_minus=true;
                   //  Toast.makeText(Cart.this,String.valueOf(allowed_minus),Toast.LENGTH_LONG).show();
                        holder.food_name.setText(model.getFood_name());

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

                            holder.count.setText(String.valueOf(model.getFood_amount()));
                        } catch (Exception e) {
                            Toast.makeText(Cart.this, "error amount ", Toast.LENGTH_LONG).show();
                        }


                        holder.add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(isConnected(Cart.this)){
                                    if(Integer.parseInt(holder.count.getText().toString())==model.getFood_amount()){
                                        layout = model.getLayout();
                                        type = model.getFood_type();
                                        extra_amount = model.getExtra_shot_amount();
                                        iced_amount = model.getIced_amount();
                                        food_amount = model.getFood_amount();
                                        stotal = price_total;
                                        food_price = Integer.parseInt(model.getFood_price());


                                        try {
                                            try {
                                                food_amount = food_amount + 1;
                                                stotal = food_price + stotal;
                                                //  Toast.makeText(Cart.this, "after adding food amount " + food_amount + " total price " + stotal, Toast.LENGTH_LONG).show();
                                                holder.count.setText(String.valueOf(food_amount));
                                                if (layout.equals("1") && extra_amount != 0) {
                                                    stotal = stotal + (extra_amount * 35);
                                                }
                                                if (layout.equals("1") && iced_amount != 0) {
                                                    stotal = stotal + (iced_amount * 25);
                                                }
                                                price_total = stotal;
                                                subtotal.setText(String.valueOf(price_total));
                                                set_price();

                                            } catch (Exception e) {
                                                Toast.makeText(Cart.this, "error 2", Toast.LENGTH_SHORT).show();
                                            }

                                            // Toast.makeText(Cart.this,model.getFood_name()+"("+model.getFood_type()+", estra shot amount= "+model.getExtra_shot_amount()+" ,iced amount= "+model.getIced_amount(),Toast.LENGTH_LONG).show();
                                            if (layout.equals("1")) {

                                                user_reference.child("order_history").child(order_id).child("food").child(model.getFood_name() + "(" + model.getFood_type() + ", extra_shot_amount=" + model.getExtra_shot_amount() + " ,iced_amount= " + model.getIced_amount() + ")").child("food_amount").setValue(food_amount);
                                                user_reference.child("order_history").child(order_id).child("total_price").setValue(stotal);

                                                place_order_reference.child("orders").child(order_id).child("food").child(model.getFood_name() + "(" + model.getFood_type() + ", extra_shot_amount=" + model.getExtra_shot_amount() + " ,iced_amount= " + model.getIced_amount() + ")").child("food_amount").setValue(food_amount);
                                                place_order_reference.child("orders").child(order_id).child("total_price").setValue(stotal);

                                                databaseReference.child(order_id).child("food").child(model.getFood_name() + "(" + model.getFood_type() + ", extra_shot_amount=" + model.getExtra_shot_amount() + " ,iced_amount= " + model.getIced_amount() + ")").child("food_amount").setValue(food_amount);
                                                databaseReference.child(order_id).child("user_info").child("total_price").setValue(stotal);

                                                order_reference.child(order_id).child("food").child(model.getFood_name() + "(" + model.getFood_type() + ", extra_shot_amount=" + model.getExtra_shot_amount() + " ,iced_amount= " + model.getIced_amount() + ")").child("food_amount").setValue(food_amount);
                                                order_reference.child(order_id).child("total_price").setValue(stotal);
                                            } else {
                                                user_reference.child("order_history").child(order_id).child("food").child(model.getFood_name()).child("food_amount").setValue(food_amount);
                                                user_reference.child("order_history").child(order_id).child("total_price").setValue(stotal);

                                                place_order_reference.child("orders").child(order_id).child("food").child(model.getFood_name()).child("food_amount").setValue(food_amount);
                                                place_order_reference.child("orders").child(order_id).child("total_price").setValue(stotal);

                                                databaseReference.child(order_id).child("food").child(model.getFood_name()).child("food_amount").setValue(food_amount);
                                                databaseReference.child(order_id).child("user_info").child("total_price").setValue(stotal);

                                                order_reference.child(order_id).child("food").child(model.getFood_name()).child("food_amount").setValue(food_amount);
                                                order_reference.child(order_id).child("total_price").setValue(stotal);
                                            }


                                        } catch (Exception e) {
                                            Toast.makeText(Cart.this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }



                            }
                        });


                        holder.minus.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(isConnected(Cart.this)){
                                    if(Integer.parseInt(holder.count.getText().toString())==model.getFood_amount()){
                                      //  Toast.makeText(Cart.this,"count: "+ holder.count.getText().toString()+" amount "+model.getFood_amount(),Toast.LENGTH_LONG).show();
                                        try {
                                            layout = model.getLayout();
                                            type = model.getFood_type();
                                            extra_amount = model.getExtra_shot_amount();
                                            iced_amount = model.getIced_amount();
                                            food_amount = model.getFood_amount();
                                            stotal = price_total;
                                            food_price = Integer.parseInt(model.getFood_price());
                                            if (food_amount == 1) {

                                               // Toast.makeText(Cart.this, "food amount  1 " + food_amount + " " + food_price, Toast.LENGTH_SHORT).show();
                                                food_amount = food_amount - 1;
                                                stotal = stotal - food_price;


                                                if (layout.equals("1") && extra_amount != 0) {
                                                    //Toast.makeText(Cart.this, "food amount 1 extra amount not zero", Toast.LENGTH_SHORT).show();
                                                    stotal = stotal - (extra_amount * 35);
                                                }
                                                if (layout.equals("1") && iced_amount != 0) {
                                                    //   Toast.makeText(Cart.this, "food amount 1 iced amount not zero", Toast.LENGTH_SHORT).show();

                                                    stotal = stotal - (iced_amount * 25);
                                                }
                                                if (food_amount == 0) {
                                                    //  Toast.makeText(Cart.this, "food amount 0 ", Toast.LENGTH_SHORT).show();

                                                    if (stotal == 0) {
                                                        //   Toast.makeText(Cart.this, "stotal " + stotal, Toast.LENGTH_SHORT).show();
                                                        try {
                                                            layout1.setVisibility(View.INVISIBLE);
                                                            layout2.setVisibility(View.VISIBLE);
                                                            back_to_mainmenu = true;
                                                        } catch (Exception e) {
                                                            Toast.makeText(Cart.this, "layout visibility error " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                        }

                                                        user_reference.child("order_history").child(order_id).setValue(null);
                                                        user_reference.child("order_history").child("recent_order_id").setValue("0");
                                                        place_order_reference.child("orders").child(order_id).setValue(null);
                                                        databaseReference.child(order_id).setValue(null);
                                                        order_reference.child(order_id).removeValue();
                                                        deliver_charge = 0;
                                                        order_id = "0";


                                                    } else {

                                                        //  Toast.makeText(Cart.this, " total price not equals to zero ", Toast.LENGTH_LONG).show();
                                                        if (layout.equals("1")) {
                                                            user_reference.child("order_history").child(order_id).child("food").child(model.getFood_name() + "(" + model.getFood_type() + ", extra_shot_amount=" + model.getExtra_shot_amount() + " ,iced_amount= " + model.getIced_amount() + ")").removeValue();
                                                            user_reference.child("order_history").child(order_id).child("total_price").setValue(stotal);

                                                            place_order_reference.child("orders").child(order_id).child("food").child(model.getFood_name() + "(" + model.getFood_type() + ", extra_shot_amount=" + model.getExtra_shot_amount() + " ,iced_amount= " + model.getIced_amount() + ")").removeValue();
                                                            place_order_reference.child("orders").child(order_id).child("total_price").setValue(stotal);

                                                            databaseReference.child(order_id).child("food").child(model.getFood_name() + "(" + model.getFood_type() + ", extra_shot_amount=" + model.getExtra_shot_amount() + " ,iced_amount= " + model.getIced_amount() + ")").removeValue();
                                                            databaseReference.child(order_id).child("user_info").child("total_price").setValue(stotal);

                                                            order_reference.child(order_id).child("food").child(model.getFood_name() + "(" + model.getFood_type() + ", extra_shot_amount=" + model.getExtra_shot_amount() + " ,iced_amount= " + model.getIced_amount() + ")").removeValue();
                                                            order_reference.child(order_id).child("total_price").setValue(stotal);
                                                        } else {
                                                            user_reference.child("order_history").child(order_id).child("food").child(model.getFood_name()).removeValue();
                                                            user_reference.child("order_history").child(order_id).child("total_price").setValue(stotal);

                                                            place_order_reference.child("orders").child(order_id).child("food").child(model.getFood_name()).removeValue();
                                                            place_order_reference.child("orders").child(order_id).child("total_price").setValue(stotal);

                                                            databaseReference.child(order_id).child("food").child(model.getFood_name()).removeValue();
                                                            databaseReference.child(order_id).child("user_info").child("total_price").setValue(stotal);

                                                            order_reference.child(order_id).child("food").child(model.getFood_name()).removeValue();
                                                            order_reference.child(order_id).child("total_price").setValue(stotal);
                                                        }
                                                    }
                                                }


                                            } else {
                                                if (food_amount > 1) {
                                                    //  Toast.makeText(Cart.this, "food amount greater than 1", Toast.LENGTH_SHORT).show();

                                                    food_amount = food_amount - 1;
                                                    stotal = stotal - food_price;
                                                    //*  holder.count.setText(String.valueOf(food_amount));*//*


                                                    if (layout.equals("1") && extra_amount != 0) {
                                                        stotal = stotal - (extra_amount * 35);
                                                    }
                                                    if (layout.equals("1") && iced_amount != 0) {
                                                        stotal = stotal - (iced_amount * 25);
                                                    }

                                                    if (layout.equals("1")) {
                                                        user_reference.child("order_history").child(order_id).child("food").child(model.getFood_name() + "(" + model.getFood_type() + ", extra_shot_amount=" + model.getExtra_shot_amount() + " ,iced_amount= " + model.getIced_amount() + ")").child("food_amount").setValue(food_amount);
                                                        user_reference.child("order_history").child(order_id).child("total_price").setValue(stotal);

                                                        place_order_reference.child("orders").child(order_id).child("food").child(model.getFood_name() + "(" + model.getFood_type() + ", extra_shot_amount=" + model.getExtra_shot_amount() + " ,iced_amount= " + model.getIced_amount() + ")").child("food_amount").setValue(food_amount);
                                                        place_order_reference.child("orders").child(order_id).child("total_price").setValue(stotal);

                                                        databaseReference.child(order_id).child("food").child(model.getFood_name() + "(" + model.getFood_type() + ", extra_shot_amount=" + model.getExtra_shot_amount() + " ,iced_amount= " + model.getIced_amount() + ")").child("food_amount").setValue(food_amount);
                                                        databaseReference.child(order_id).child("user_info").child("total_price").setValue(stotal);

                                                        order_reference.child(order_id).child("food").child(model.getFood_name() + "(" + model.getFood_type() + ", extra_shot_amount=" + model.getExtra_shot_amount() + " ,iced_amount= " + model.getIced_amount() + ")").child("food_amount").setValue(food_amount);
                                                        order_reference.child(order_id).child("total_price").setValue(stotal);
                                                    } else {
                                                        user_reference.child("order_history").child(order_id).child("food").child(model.getFood_name()).child("food_amount").setValue(food_amount);
                                                        user_reference.child("order_history").child(order_id).child("total_price").setValue(stotal);

                                                        place_order_reference.child("orders").child(order_id).child("food").child(model.getFood_name()).child("food_amount").setValue(food_amount);
                                                        place_order_reference.child("orders").child(order_id).child("total_price").setValue(stotal);

                                                        databaseReference.child(order_id).child("food").child(model.getFood_name()).child("food_amount").setValue(food_amount);
                                                        databaseReference.child(order_id).child("user_info").child("total_price").setValue(stotal);

                                                        order_reference.child(order_id).child("food").child(model.getFood_name()).child("food_amount").setValue(food_amount);
                                                        order_reference.child(order_id).child("total_price").setValue(stotal);
                                                    }
                                                }

                                            }


                                            price_total = stotal;
                                            holder.count.setText(String.valueOf(food_amount));

                                            set_price();


                                        } catch (Exception e) {
                                            Toast.makeText(Cart.this, "error" +price_total+"  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }





                                }

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(Cart.this)
                                .inflate(R.layout.cart_row_layout, parent, false);
                        FirebaseViewHolder firebaseViewHolder = new FirebaseViewHolder(view);
                        return firebaseViewHolder;
                    }
                };
            } catch (Exception e) {
            }

            try {
                firebaseAdapter.startListening();
                recyclerView.setLayoutManager(new LinearLayoutManager(Cart.this));
                recyclerView.setAdapter(firebaseAdapter);
            } catch (Exception e) {
                Toast.makeText(Cart.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }


        } else {
            layout1.setVisibility(View.INVISIBLE);
            layout2.setVisibility(View.VISIBLE);
            back_to_mainmenu = true;
        }
    }

    private String getToken() {
        Random random = new Random();
        String random_address = "";
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZefghijktuvwxyz0123456789";
        int i = 0;
        StringBuilder string = new StringBuilder();
        while (i < 7) {
            string.append(characters.charAt(random.nextInt(characters.length())));
            i++;
        }
        random_address = random_address + string.toString();

        return random_address;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    private void back() {

        if (back_to_mainmenu) {
            if (intent.getBooleanExtra("not_order_class", false)) {

                CLASS = getIntent().getStringExtra("class");
                if (CLASS.contains("MainMenu")) {
                    i = new Intent(Cart.this, MainMenu.class);
                }
                if (CLASS.contains("Classic")) {
                    i = new Intent(Cart.this, Classic.class);
                }
                if (CLASS.contains("Dessert")) {
                    i = new Intent(Cart.this, Dessert.class);
                }
                if (CLASS.contains("Freddo")) {
                    i = new Intent(Cart.this, Freddo.class);
                }
                if (CLASS.contains("HOT")) {
                    i = new Intent(Cart.this, HOT.class);
                }
                if (CLASS.contains("ICED")) {
                    i = new Intent(Cart.this, ICED.class);
                }
                if (CLASS.contains("Others")) {
                    i = new Intent(Cart.this, Others.class);
                }
                if (CLASS.contains("Specials")) {
                    i = new Intent(Cart.this, Specials.class);
                }
                if (CLASS.contains("TEA")) {
                    i = new Intent(Cart.this, TEA.class);
                }
            } else {
                i = new Intent(Cart.this, MainMenu.class);
            }
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("order_id", Integer.parseInt(order_id));
            i.putExtra("phone", phone);
            i.putExtra("username", username);
            i.putExtra("email", email);
            i.putExtra("uid", uid);
            i.putExtra("deliver_address", address);
            startActivity(i);
            overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
            finish();
        } else {
            if (intent.getBooleanExtra("not_order_class", false)) {

                CLASS = getIntent().getStringExtra("class");
                if (CLASS.contains("MainMenu")) {
                    i = new Intent(Cart.this, MainMenu.class);
                }
                if (CLASS.contains("Classic")) {
                    i = new Intent(Cart.this, Classic.class);
                }
                if (CLASS.contains("Dessert")) {
                    i = new Intent(Cart.this, Dessert.class);
                }
                if (CLASS.contains("Freddo")) {
                    i = new Intent(Cart.this, Freddo.class);
                }
                if (CLASS.contains("HOT")) {
                    i = new Intent(Cart.this, HOT.class);
                }
                if (CLASS.contains("ICED")) {
                    i = new Intent(Cart.this, ICED.class);
                }
                if (CLASS.contains("Others")) {
                    i = new Intent(Cart.this, Others.class);
                }
                if (CLASS.contains("Specials")) {
                    i = new Intent(Cart.this, Specials.class);
                }
                if (CLASS.contains("TEA")) {
                    i = new Intent(Cart.this, TEA.class);
                }
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("order_id", Integer.parseInt(order_id));
                i.putExtra("phone", phone);
                i.putExtra("username", username);
                i.putExtra("email", email);
                i.putExtra("uid", uid);
                i.putExtra("deliver_address", address);
                startActivity(i);
                overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
                finish();
            } else {
                i = new Intent(Cart.this, Order.class);
                if(getIntent().getBooleanExtra("from_favourites_class",false)){
                    i.putExtra("class",getIntent().getStringExtra("class"));
                    i.putExtra("from_favourites_class",true);
                }
                i.putExtra("order_id", Integer.parseInt(order_id));
                i.putExtra("phone", phone);
                i.putExtra("username", username);
                i.putExtra("email", email);
                i.putExtra("uid", uid);
                i.putExtra("deliver_address", address);

                i.putExtra("food_image", food_image);
                i.putExtra("food_name", food_name);
                i.putExtra("food_description", food_description);
                i.putExtra("food_price", food_price_intent);
                i.putExtra("regular", food_size_regular);
                i.putExtra("large", food_size_large);
                i.putExtra("add_ons_visibility", visibility);
                i.putExtra("radio", radio_group_visibility);
                i.putExtra("regular_price", regular_price);
                i.putExtra("large_price", large_price);
                i.putExtra("activity", activity);
                i.putExtra("layout", layout_no);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
                finish();
            }


        }


    }
    @Override
    protected void onStop() {
        super.onStop();

        if (place_order_reference != null && listener1 != null) {
            place_order_reference.removeEventListener(listener1);
        }
        if (databaseReference != null && listener != null) {
            databaseReference.removeEventListener(listener);
        }


    }
    private boolean isConnected(Context context) {
        boolean RETURN = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi_connection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile_connection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi_connection != null && wifi_connection.isConnected()) || (mobile_connection != null && mobile_connection.isConnected())) {

            RETURN = true;
        } else {
            Toast.makeText(Cart.this, "no connection", Toast.LENGTH_LONG).show();
            RETURN = false;
        }
        return RETURN;
    }
}