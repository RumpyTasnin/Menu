package com.example.menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class Order extends AppCompatActivity implements View.OnClickListener {

    ImageView back, heart, add1, add2, add3, minus1, minus2, minus3, go_to_cart;
    ShapeableImageView imageView;
    // RatingBar ratingBar;
    TextInputLayout instuctions;
    LinearLayout add_ons_layout, cart;
    TextView drink_name, full_decsription, price, add_to_cart, count1, count2, count3, description;
    String regular_price, large_price;
    RadioButton regular, large;
    RadioGroup radioGroup;
    String activity, order_id;
    DatabaseReference databaseReference,databaseReference1, reference, place_order_reference, user_reference;
    String FOOD_TYPE, FOOD_TYPE_PRICE, currentDate, currentTime, food_instructions;
    int layout_no;
    boolean REGULAR = true;
    int food_image;
    int add1_count, add2_count, add3_count, add_cart_price, PRICE = 0;
    int odId = 0;
    int c = 0;
    boolean first_time = true;
    String food_name, food_description, food_price, food_size_regular, food_size_large;
    Boolean visibility, radio_group_visibility;
    String phone, address, username, email, uid;
    int k = 0;
    int a1, a2, a3;
    int count = 0;
    Dialog internet_connection_dialog;

    ValueEventListener listener, listener1,listener2;

    ////////////////////////////////#########################################????\


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //status bar transparent
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        Intent i = getIntent();
        layout_no = i.getIntExtra("layout", 1);
        username = getIntent().getStringExtra("username");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        uid = getIntent().getStringExtra("uid");
        address = getIntent().getStringExtra("deliver_address");
        odId = getIntent().getIntExtra("order_id", 0);
        order_id = String.valueOf(odId);
        internet_connection_dialog = new Dialog(Order.this);
        internet_connection_dialog.setContentView(R.layout.dialog_no_internet);
        internet_connection_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        internet_connection_dialog.getWindow().setGravity(Gravity.CENTER);
        internet_connection_dialog.setCancelable(true);
        internet_connection_dialog.getWindow().getAttributes().windowAnimations = R.style.CustomActivityAnimation2;
        internet_connection_dialog.findViewById(R.id.try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected(Order.this)) {
                    internet_connection_dialog.dismiss();

                }
            }
        });

        if (layout_no == 1) {

            setContentView(R.layout.activity_order);
            setLayout();
            add1_count = Integer.parseInt(count1.getText().toString());
            add2_count = Integer.parseInt(count2.getText().toString());
            add3_count = Integer.parseInt(count3.getText().toString());

            add1.setOnClickListener(this);
            add2.setOnClickListener(this);
            add3.setOnClickListener(this);
            minus1.setOnClickListener(this);
            minus2.setOnClickListener(this);
            minus3.setOnClickListener(this);
            regular.setOnClickListener(this);
            large.setOnClickListener(this);

        } else {
            setContentView(R.layout.activity_order_2);
            setLayout2();
            add1_count = Integer.parseInt(count1.getText().toString());
            add1.setOnClickListener(this);
            minus1.setOnClickListener(this);
        }


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            place_order();
            user_reference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            reference = FirebaseDatabase.getInstance().getReference("cart");
            databaseReference = FirebaseDatabase.getInstance().getReference("orders");
            place_order_reference = FirebaseDatabase.getInstance().getReference().child("placed_order");
        }



        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

             user_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("Favourites").hasChild(food_name)) {

                        count = 1;
                        heart.setImageResource(R.drawable.ic_baseline_favorite_24);
                        //Toast.makeText(Order.this, "added", Toast.LENGTH_LONG).show();

                    }
                    if (user_reference != null && listener1 != null) {
                        user_reference.removeEventListener(listener1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }


        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    if (count == 0) {
                      //  Toast.makeText(Order.this, food_name + " " + food_description + " c " + count, Toast.LENGTH_LONG).show();
                        heart.setImageResource(R.drawable.ic_baseline_favorite_24);
                        user_reference.child("Favourites").child(food_name).child("food_name").setValue(food_name);
                        user_reference.child("Favourites").child(food_name).child("food_image").setValue(food_image);
                        user_reference.child("Favourites").child(food_name).child("food_description").setValue(food_description);
                        user_reference.child("Favourites").child(food_name).child("food_price").setValue(food_price);
                        user_reference.child("Favourites").child(food_name).child("regular").setValue(food_size_regular);
                        user_reference.child("Favourites").child(food_name).child("large").setValue(food_size_large);
                        user_reference.child("Favourites").child(food_name).child("activity").setValue(activity);
                        user_reference.child("Favourites").child(food_name).child("layout_no").setValue(layout_no);
                        if (layout_no == 1) {
                            user_reference.child("Favourites").child(food_name).child("add_ons_visibility").setValue(visibility);
                            user_reference.child("Favourites").child(food_name).child("radio_visibility").setValue(radio_group_visibility);
                            user_reference.child("Favourites").child(food_name).child("regular_price").setValue(regular_price);
                            user_reference.child("Favourites").child(food_name).child("large_price").setValue(large_price);
                        } else {
                            if (layout_no == 2) {
                                user_reference.child("Favourites").child(food_name).child("add_ons_visibility").setValue(false);
                                user_reference.child("Favourites").child(food_name).child("radio_visibility").setValue(false);
                                user_reference.child("Favourites").child(food_name).child("regular_price").setValue("");
                                user_reference.child("Favourites").child(food_name).child("large_price").setValue("");
                            }
                        }
                        count = 1;

                    } else {
                        listener1 = user_reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.child("Favourites").hasChild(food_name)) {
                                    heart.setImageResource(R.drawable.blank_love);
                                    user_reference.child("Favourites").child(food_name).removeValue();
                                    count = 0;
                                    //Toast.makeText(Order.this, "heart removed " + count, Toast.LENGTH_LONG).show();
                                }
                                if (user_reference != null && listener1 != null) {
                                    user_reference.removeEventListener(listener1);
                                   // Toast.makeText(Order.this, "removed", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }

            }
        });


        try {
            cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isConnected(Order.this)) {
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            c = 0;
                            try {
                                food_instructions = instuctions.getEditText().getText().toString();
                                if (REGULAR) {
                                    FOOD_TYPE = food_size_regular;
                                    FOOD_TYPE_PRICE = regular_price;
                                } else {
                                    FOOD_TYPE = food_size_large;
                                    FOOD_TYPE_PRICE = large_price;
                                }
                                if (add1_count != 0) {

                                    if (odId == 0) {
                                        first_time = false;
                                        Random random = new Random();
                                        odId = random.nextInt();
                                        if (odId < 0) {
                                            odId = odId * (-1);
                                        }
                                        order_id = String.valueOf(odId);

                                        databaseReference.child(order_id).child("customer_name").setValue(username);
                                        databaseReference.child(order_id).child("customer_phone").setValue(phone);
                                        databaseReference.child(order_id).child("customer_email").setValue(email);
                                        databaseReference.child(order_id).child("customer_uid").setValue(uid);
                                        databaseReference.child(order_id).child("confirmed").setValue(false);

                                        user_reference.child("order_history").child("recent_order_id").setValue(order_id);


                                        reference.child(order_id).child("user_info").child("customer_name").setValue(username);
                                        reference.child(order_id).child("user_info").child("customer_phone").setValue(phone);
                                        reference.child(order_id).child("user_info").child("customer_email").setValue(email);
                                        reference.child(order_id).child("user_info").child("customer_uid").setValue(uid);

                                        place_order_reference.child("orders").child(order_id).child("user_info").child("customer_name").setValue(username);
                                        place_order_reference.child("orders").child(order_id).child("user_info").child("customer_phone").setValue(phone);
                                        place_order_reference.child("orders").child(order_id).child("user_info").child("customer_email").setValue(email);
                                        place_order_reference.child("orders").child(order_id).child("user_info").child("customer_uid").setValue(uid);
                                        place_order_reference.child("orders").child(order_id).child("Confirmed").setValue(false);

                                        if (layout_no == 1) {
                                            // Toast.makeText(Order.this, "1 " + order_id + " food name " + food_name + " food type " + FOOD_TYPE + " add1 " + add1_count + " add2 " + add2_count + " add3  " + add3_count, Toast.LENGTH_LONG).show();

                                            layout1(databaseReference, order_id, food_name, FOOD_TYPE, add1_count, add2_count, add3_count);
                                            try {
                                                add1_count = 0;
                                                add2_count = 0;
                                                add3_count = 0;
                                                count1.setText("0");
                                                count2.setText("0");
                                                count3.setText("0");
                                            } catch (Exception e) {
                                                Toast.makeText(Order.this, "1 " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            }


                                        } else {
                                            if (layout_no == 2) {
                                                layout2(databaseReference, order_id, food_name, add1_count);
                                                add1_count = 0;
                                                count1.setText("0");

                                            }
                                        }


                                    } else {
                                        order_id = String.valueOf(odId);
                                        // Toast.makeText(Order.this,"order class new order id is "+order_id,Toast.LENGTH_LONG).show();

                                        listener = databaseReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (c == 0) {
                                                    try {
                                                        if (layout_no == 1) {
                                                            //Toast.makeText(Order.this, "2 " + order_id + " food name " + food_name + " food type " + FOOD_TYPE + " add1 " + add1_count + " add2 " + add2_count + " add3  " + add3_count, Toast.LENGTH_LONG).show();
                                                            if (snapshot.child(order_id).child("food").hasChild(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")")) {
                                                                a1 = Integer.parseInt(snapshot.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("food_amount").getValue().toString());
                                                                add1_count = a1 + add1_count;
                                                            }

                                                            try {
                                                                layout1(databaseReference, order_id, food_name, FOOD_TYPE, add1_count, add2_count, add3_count);
                                                                add1_count = 0;
                                                                add2_count = 0;
                                                                add3_count = 0;
                                                                count1.setText("0");
                                                                count2.setText("0");
                                                                count3.setText("0");
                                                            } catch (Exception e) {
                                                                Toast.makeText(Order.this, "layout 1 setting error " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                            }

                                                        } else {
                                                            if (layout_no == 2) {
                                                                if (snapshot.child(order_id).child("food").hasChild(food_name)) {
                                                                    a1 = Integer.parseInt(snapshot.child(order_id).child("food").child(food_name).child("food_amount").getValue().toString());
                                                                    add1_count = a1 + add1_count;
                                                                }
                                                                try {
                                                                    layout2(databaseReference, order_id, food_name, add1_count);
                                                                    add1_count = 0;
                                                                    count1.setText("0");
                                                                } catch (Exception e) {
                                                                    Toast.makeText(Order.this, "layout 2 setting error " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                                }

                                                            }
                                                        }
                                                        c = 1;
                                                        first_time = false;
                                                        if (databaseReference != null && listener != null) {
                                                            databaseReference.removeEventListener(listener);
                                                        }

                                                    } catch (Exception e) {
                                                        Toast.makeText(Order.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                                if (databaseReference != null && listener != null) {
                                                    databaseReference.removeEventListener(listener);
                                                }

                                            }


                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                            }
                                        });


                                    }
                                    Toast.makeText(Order.this, "item added to cart, visit cart to confirm your order", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(Order.this, " Cart can not be empty, add an item  ", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(Order.this, "order class error  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Order.this, R.style.AlertDialogTheme);
                            builder.setTitle("Not logged in");
                            builder.setCancelable(false);
                            builder.setMessage("You have to login to add items to your cart");
                            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();

                                }
                            });
                            builder.show();
                        }
                    } else {
                        internet_connection_dialog.show();
                    }

                }
            });
        } catch (Exception e) {
            Toast.makeText(Order.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


////////////////////////////////////////////////////////////////////////
        go_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected(Order.this)) {
                    try {
                        //Toast.makeText(Order.this,"order_id "+order_id,Toast.LENGTH_SHORT).show();
                        if (order_id != null) {
                            Intent i = new Intent(Order.this, Cart.class);

                            i.putExtra("order_id", Integer.parseInt(order_id));
                            i.putExtra("phone", phone);
                            i.putExtra("username", username);
                            i.putExtra("email", email);
                            i.putExtra("uid", uid);
                            i.putExtra("deliver_address", address);
                            if (getIntent().getBooleanExtra("from_favourites_class", false)) {
                                i.putExtra("class", getIntent().getStringExtra("class"));
                                i.putExtra("from_favourites_class", true);
                            }
                            i.putExtra("food_image", food_image);
                            i.putExtra("food_name", food_name);
                            i.putExtra("food_description", food_description);
                            i.putExtra("food_price", food_price);
                            i.putExtra("regular", food_size_regular);
                            i.putExtra("large", food_size_large);
                            i.putExtra("add_ons_visibility", visibility);
                            i.putExtra("radio", radio_group_visibility);
                            i.putExtra("regular_price", regular_price);
                            i.putExtra("large_price", large_price);
                            i.putExtra("activity", activity);
                            i.putExtra("layout", layout_no);
                            //Toast.makeText(Order.this, "cart clicked", Toast.LENGTH_LONG).show();
                            startActivity(i);
                            overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                        }

                        if (order_id == "0") {
                            try {
                                Intent i = new Intent(Order.this, Cart.class);
                                i.putExtra("order_id", Integer.parseInt(order_id));
                                i.putExtra("phone", phone);
                                i.putExtra("username", username);
                                i.putExtra("email", email);
                                i.putExtra("uid", uid);
                                i.putExtra("deliver_address", address);
                                if (getIntent().getBooleanExtra("from_favourites_class", false)) {
                                    i.putExtra("class", getIntent().getStringExtra("class"));
                                    i.putExtra("from_favourites_class", true);
                                }

                                i.putExtra("food_image", food_image);
                                i.putExtra("food_name", food_name);
                                i.putExtra("food_description", food_description);
                                i.putExtra("food_price", food_price);
                                i.putExtra("regular", food_size_regular);
                                i.putExtra("large", food_size_large);
                                i.putExtra("add_ons_visibility", visibility);
                                i.putExtra("radio", radio_group_visibility);
                                i.putExtra("regular_price", regular_price);
                                i.putExtra("large_price", large_price);
                                i.putExtra("activity", activity);
                                i.putExtra("layout", layout_no);
                                startActivity(i);
                                overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                            } catch (Exception e) {

                                Toast.makeText(Order.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(Order.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    internet_connection_dialog.show();
                }


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backPressed();
            }
        });
        ////////////////////////////////////////////////
    }

    private void layout2(DatabaseReference databaseReference, String order_id, String food_name, int add1_count) {

        //  databaseReference.child(order_id).child("food").child(food_name).child("Time").setValue(currentTime);
        databaseReference.child(order_id).child("food").child(food_name).child("food_name").setValue(food_name);
        databaseReference.child(order_id).child("food").child(food_name).child("food_amount").setValue(add1_count);
        databaseReference.child(order_id).child("food").child(food_name).child("food_price").setValue(food_price);
        databaseReference.child(order_id).child("food").child(food_name).child("instructions").setValue(food_instructions);
        databaseReference.child(order_id).child("food").child(food_name).child("food_type").setValue("");
        databaseReference.child(order_id).child("food").child(food_name).child("extra_shot_amount").setValue(0);
        databaseReference.child(order_id).child("food").child(food_name).child("extra_shot_price").setValue("");
        databaseReference.child(order_id).child("food").child(food_name).child("iced_amount").setValue(0);
        databaseReference.child(order_id).child("food").child(food_name).child("iced_price").setValue("");
        databaseReference.child(order_id).child("food").child(food_name).child("layout").setValue("2");
        databaseReference.child(order_id).child("total_price").setValue(add_cart_price);

        //reference.child(order_id).child("food").child(food_name).child("time").setValue(currentTime);
        reference.child(order_id).child("food").child(food_name).child("food_name").setValue(food_name);
        reference.child(order_id).child("food").child(food_name).child("food_amount").setValue(add1_count);
        reference.child(order_id).child("food").child(food_name).child("food_price").setValue(food_price);
        reference.child(order_id).child("food").child(food_name).child("instructions").setValue(food_instructions);
        reference.child(order_id).child("food").child(food_name).child("food_type").setValue("");
        reference.child(order_id).child("food").child(food_name).child("extra_shot_amount").setValue(0);
        reference.child(order_id).child("food").child(food_name).child("extra_shot_price").setValue("");
        reference.child(order_id).child("food").child(food_name).child("iced_amount").setValue(0);
        reference.child(order_id).child("food").child(food_name).child("iced_price").setValue("");
        reference.child(order_id).child("food").child(food_name).child("layout").setValue("2");
        reference.child(order_id).child("user_info").child("total_price").setValue(add_cart_price);


        // place_order_reference.child(order_id).child(food_name).child("time").setValue(currentTime);
        place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("food_name").setValue(food_name);
        place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("food_amount").setValue(add1_count);
        place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("food_price").setValue(food_price);
        place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("instructions").setValue(food_instructions);
        place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("food_type").setValue("");
        place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("extra_shot_amount").setValue(0);
        place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("extra_shot_price").setValue("");
        place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("iced_amount").setValue(0);
        place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("iced_price").setValue("");
        place_order_reference.child("orders").child(order_id).child("food").child(food_name).child("layout").setValue("2");

/*
        place_order_reference.child("orders").child(order_id).child("order_id").setValue(order_id);
*/
        place_order_reference.child("orders").child(order_id).child("total_price").setValue(add_cart_price);
/*
        place_order_reference.child("orders").child(order_id).child("Branch").setValue("");
*/


        user_reference.child("order_history").child(order_id).child("food").child(food_name).child("food_name").setValue(food_name);
        user_reference.child("order_history").child(order_id).child("food").child(food_name).child("food_amount").setValue(add1_count);
        user_reference.child("order_history").child(order_id).child("food").child(food_name).child("food_price").setValue(food_price);
        user_reference.child("order_history").child(order_id).child("total_price").setValue(add_cart_price);
        user_reference.child("order_history").child(order_id).child("food").child(food_name).child("food_type").setValue("");
        user_reference.child("order_history").child(order_id).child("food").child(food_name).child("extra_shot_amount").setValue(0);
        user_reference.child("order_history").child(order_id).child("food").child(food_name).child("extra_shot_price").setValue("");
        user_reference.child("order_history").child(order_id).child("food").child(food_name).child("iced_amount").setValue(0);
        user_reference.child("order_history").child(order_id).child("food").child(food_name).child("iced_price").setValue("");
        user_reference.child("order_history").child(order_id).child("food").child(food_name).child("instructions").setValue(food_instructions);
        user_reference.child("order_history").child(order_id).child("food").child(food_name).child("layout").setValue("2");


    }

    private void layout1(DatabaseReference databaseReference, String order_id, String food_name, String FOOD_TYPE, int add1_count, int add2_count, int add3_count) {

        try {
            databaseReference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("food_name").setValue(food_name);
            databaseReference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("food_amount").setValue(add1_count);
            databaseReference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("food_type").setValue(FOOD_TYPE);
            databaseReference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("food_price").setValue(FOOD_TYPE_PRICE);
            databaseReference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("extra_shot_amount").setValue(add2_count);
            databaseReference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("iced_amount").setValue(add3_count);
            databaseReference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("extra_shot_price").setValue("35.00TK");
            databaseReference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("iCED_price").setValue("25.00TK");
            databaseReference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("instructions").setValue(food_instructions);
            databaseReference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("layout").setValue("1");
            databaseReference.child(order_id).child("total_price").setValue(add_cart_price);


            //reference.child(order_id).child("food").child(food_name+"("+FOOD_TYPE+", extra_shot_amount="+add2_count+" ,iced_amount= "+add3_count+")").child("time ").setValue(currentTime);
            reference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("food_name").setValue(food_name);
            reference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("food_amount").setValue(add1_count);
            reference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("food_type").setValue(FOOD_TYPE);
            reference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("food_price").setValue(FOOD_TYPE_PRICE);
            reference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("extra_shot_amount").setValue(add2_count);
            reference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("iced_amount").setValue(add3_count);
            reference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("extra_shot_price").setValue("35.00TK");
            reference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("iced_price").setValue("25.00TK");
            reference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("instructions").setValue(food_instructions);
            reference.child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("layout").setValue("1");
            reference.child(order_id).child("user_info").child("total_price").setValue(add_cart_price);

            user_reference.child("order_history").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("food_name").setValue(food_name);
            user_reference.child("order_history").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("food_amount").setValue(add1_count);
            user_reference.child("order_history").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("food_type").setValue(FOOD_TYPE);
            user_reference.child("order_history").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("food_price").setValue(FOOD_TYPE_PRICE);
            user_reference.child("order_history").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("extra_shot_amount").setValue(add2_count);
            user_reference.child("order_history").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("iced_amount").setValue(add3_count);
            user_reference.child("order_history").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("extra_shot_price").setValue("35.00TK");
            user_reference.child("order_history").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("iced_price").setValue("25.00TK");
            user_reference.child("order_history").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("instructions").setValue(food_instructions);
            user_reference.child("order_history").child(order_id).child("total_price").setValue(add_cart_price);
            user_reference.child("order_history").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("layout").setValue("1");


            place_order_reference.child("orders").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("food_name").setValue(food_name);
            place_order_reference.child("orders").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("food_amount").setValue(add1_count);
            place_order_reference.child("orders").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("food_type").setValue(FOOD_TYPE);
            place_order_reference.child("orders").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("food_price").setValue(FOOD_TYPE_PRICE);
            place_order_reference.child("orders").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("extra_shot_amount").setValue(add2_count);
            place_order_reference.child("orders").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("iced_amount").setValue(add3_count);
            place_order_reference.child("orders").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("extra_shot_price").setValue("35.00TK");
            place_order_reference.child("orders").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("iced_price").setValue("25.00TK");
            place_order_reference.child("orders").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("instructions").setValue(food_instructions);
            place_order_reference.child("orders").child(order_id).child("food").child(food_name + "(" + FOOD_TYPE + ", extra_shot_amount=" + add2_count + " ,iced_amount= " + add3_count + ")").child("layout").setValue("1");

            place_order_reference.child("orders").child(order_id).child("total_price").setValue(add_cart_price);
            //  place_order_reference.child("orders").child(order_id).child("Branch").setValue("");


        } catch (Exception e) {
            Toast.makeText(Order.this, "error  layout " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void place_order() {
        databaseReference1= FirebaseDatabase.getInstance().getReference("orders");

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (odId != 0) {
                databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                      //  Toast.makeText(Order.this, "outside order id "+order_id+" price " +snapshot.child(String.valueOf(odId)).child("total_price").getValue().toString(), Toast.LENGTH_LONG).show();

                        if (snapshot.hasChild(String.valueOf(odId))) {
                            add_cart_price = Integer.parseInt(snapshot.child(String.valueOf(odId)).child("total_price").getValue().toString());
                          //  Toast.makeText(Order.this, "order id "+order_id+" price " +snapshot.child(String.valueOf(odId)).child("total_price").getValue().toString(), Toast.LENGTH_LONG).show();
                            if (k == 0) {

                                price.setText(add_cart_price + ".00Tk");
                                k = 1;

                            }


                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } else {
                add_cart_price = 0;
                price.setText(add_cart_price + ".00Tk");
            }
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setLayout2() {
        imageView = findViewById(R.id.imageView);
        back = findViewById(R.id.order_back);
        heart = findViewById(R.id.heart);
        add1 = findViewById(R.id.add1);
        minus1 = findViewById(R.id.minus1);
        count1 = findViewById(R.id.count1);
        //ratingBar=findViewById(R.id.rating_bar);
        instuctions = findViewById(R.id.instructions);
        drink_name = findViewById(R.id.drink_name);
        full_decsription = findViewById(R.id.description_full);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        add_to_cart = findViewById(R.id.add_to_cart);
        regular = findViewById(R.id.regular);
        large = findViewById(R.id.large);
        cart = findViewById(R.id.cart);
        go_to_cart = findViewById(R.id.go_to_cart);


        Intent intent = getIntent();
        food_image = intent.getIntExtra("food_image", 0);
        food_name = intent.getStringExtra("food_name");
        food_description = intent.getStringExtra("food_description").trim();
        food_price = intent.getStringExtra("food_price");
        food_size_regular = intent.getStringExtra("regular");
        food_size_large = intent.getStringExtra("large");
        activity = intent.getStringExtra("activity");
        odId = intent.getIntExtra("order_id", 0);
        PRICE = Integer.parseInt(food_price);
        phone = intent.getStringExtra("phone");
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        uid = intent.getStringExtra("uid");
        address = intent.getStringExtra("deliver_address");
        // Toast.makeText(Order.this,"order layout 2  uid "+uid,Toast.LENGTH_LONG).show();


        try {
            imageView.setImageResource(food_image);
        } catch (Exception e) {
            Toast.makeText(Order.this, e.getMessage() + "set layout2", Toast.LENGTH_LONG).show();
        }
        drink_name.setText(food_name);
        full_decsription.setText(food_description);
        //price.setText(food_price);


    }

    @SuppressLint("SetTextI18n")
    private void setLayout() {
        imageView = findViewById(R.id.imageView);
        back = findViewById(R.id.order_back);
        heart = findViewById(R.id.heart);
        add1 = findViewById(R.id.add1);
        add2 = findViewById(R.id.add2);
        add3 = findViewById(R.id.add3);
        minus1 = findViewById(R.id.minus1);
        minus2 = findViewById(R.id.minus2);
        minus3 = findViewById(R.id.minus3);
        count1 = findViewById(R.id.count1);
        count2 = findViewById(R.id.count2);
        count3 = findViewById(R.id.count3);
        //ratingBar=findViewById(R.id.rating_bar);
        instuctions = findViewById(R.id.instructions);
        add_ons_layout = findViewById(R.id.add_ons_layout);
        drink_name = findViewById(R.id.drink_name);
        full_decsription = findViewById(R.id.description_full);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        add_to_cart = findViewById(R.id.add_to_cart);
        radioGroup = findViewById(R.id.radioGroup);
        regular = findViewById(R.id.regular);
        large = findViewById(R.id.large);
        cart = findViewById(R.id.cart);
        go_to_cart = findViewById(R.id.go_to_cart);


        Intent intent = getIntent();
        food_image = intent.getIntExtra("food_image", 0);
        food_name = intent.getStringExtra("food_name");
        food_description = intent.getStringExtra("food_description").trim();
        food_price = intent.getStringExtra("food_price");
        visibility = intent.getBooleanExtra("add_ons_visibility", true);
        radio_group_visibility = intent.getBooleanExtra("radio", true);
        food_size_regular = intent.getStringExtra("regular");
        food_size_large = intent.getStringExtra("large");
        activity = intent.getStringExtra("activity");
        regular_price = intent.getStringExtra("regular_price");
        large_price = intent.getStringExtra("large_price");
        odId = intent.getIntExtra("order_id", 0);
        PRICE = Integer.parseInt(regular_price);
        phone = intent.getStringExtra("phone");
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        uid = intent.getStringExtra("uid");
        address = intent.getStringExtra("deliver_address");
        // Toast.makeText(Order.this,"order layout 1  phone "+phone,Toast.LENGTH_LONG).show();


        try {
            imageView.setImageResource(food_image);
        } catch (Exception e) {
            Toast.makeText(Order.this, e.getMessage() + "set layout", Toast.LENGTH_LONG).show();
        }
        drink_name.setText(food_name);
        full_decsription.setText(food_description);
        //price.setText(food_price);
        regular.setText(food_size_regular + "  -" + regular_price + "TK");
        large.setText(food_size_large + "  -" + large_price + "TK");


        if (visibility == false) {
            add_ons_layout.setVisibility(View.GONE);
        } else {
            add_ons_layout.setVisibility(View.VISIBLE);
        }
        if (food_size_large.isEmpty()) {
            radioGroup.setVisibility(View.GONE);
        } else {
            radioGroup.setVisibility(View.VISIBLE);
        }

        if (food_description.isEmpty()) {
            description.setVisibility(View.GONE);
        } else {
        }


    }


    @Override
    public void onBackPressed() {
        backPressed();
    }

    private void backPressed() {

        try {
            Intent intent;

            if (getIntent().getBooleanExtra("from_favourites_class", false)) {
                intent = new Intent(Order.this, Favourites.class);
                intent.putExtra("order_id", odId);
                intent.putExtra("phone", phone);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                intent.putExtra("class", getIntent().getStringExtra("class"));
                intent.putExtra("uid", uid);
                intent.putExtra("deliver_address", address);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
                finish();
            } else {
                if (activity.contains("com.example.menu.Classic")) {
                    intent = new Intent(Order.this, Classic.class);
                    intent.putExtra("order_id", odId);
                    intent.putExtra("phone", phone);
                    intent.putExtra("username", username);
                    intent.putExtra("email", email);
                    intent.putExtra("uid", uid);
                    intent.putExtra("deliver_address", address);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
                    finish();
                } else {
                    if (activity.contains("com.example.menu.Specials")) {
                        intent = new Intent(Order.this, Specials.class);
                        intent.putExtra("order_id", odId);
                        intent.putExtra("phone", phone);
                        intent.putExtra("username", username);
                        intent.putExtra("email", email);
                        intent.putExtra("uid", uid);
                        intent.putExtra("deliver_address", address);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
                        finish();
                    } else {
                        if (activity.contains("com.example.menu.HOT")) {
                            intent = new Intent(Order.this, HOT.class);
                            intent.putExtra("order_id", odId);
                            intent.putExtra("phone", phone);
                            intent.putExtra("username", username);
                            intent.putExtra("email", email);
                            intent.putExtra("uid", uid);
                            intent.putExtra("deliver_address", address);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
                            finish();
                        } else {
                            if (activity.contains("com.example.menu.ICED")) {
                                intent = new Intent(Order.this, ICED.class);
                                intent.putExtra("order_id", odId);
                                intent.putExtra("phone", phone);
                                intent.putExtra("username", username);
                                intent.putExtra("email", email);
                                intent.putExtra("uid", uid);
                                intent.putExtra("deliver_address", address);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
                                finish();
                            } else {
                                if (activity.contains("com.example.menu.Dessert")) {
                                    intent = new Intent(Order.this, Dessert.class);
                                    intent.putExtra("order_id", odId);
                                    intent.putExtra("phone", phone);
                                    intent.putExtra("username", username);
                                    intent.putExtra("email", email);
                                    intent.putExtra("uid", uid);
                                    intent.putExtra("deliver_address", address);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
                                    finish();
                                } else {
                                    if (activity.contains("com.example.menu.Freddo")) {
                                        intent = new Intent(Order.this, Freddo.class);
                                        intent.putExtra("order_id", odId);
                                        intent.putExtra("phone", phone);
                                        intent.putExtra("username", username);
                                        intent.putExtra("email", email);
                                        intent.putExtra("uid", uid);
                                        intent.putExtra("deliver_address", address);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
                                        finish();
                                    } else {
                                        if (activity.contains("com.example.menu.TEA")) {
                                            intent = new Intent(Order.this, TEA.class);
                                            intent.putExtra("order_id", odId);
                                            intent.putExtra("phone", phone);
                                            intent.putExtra("username", username);
                                            intent.putExtra("email", email);
                                            intent.putExtra("uid", uid);
                                            intent.putExtra("deliver_address", address);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
                                            finish();
                                        } else {
                                            if (activity.contains("com.example.menu.Others")) {
                                                intent = new Intent(Order.this, Others.class);
                                                intent.putExtra("order_id", odId);
                                                intent.putExtra("phone", phone);
                                                intent.putExtra("username", username);
                                                intent.putExtra("email", email);
                                                intent.putExtra("uid", uid);
                                                intent.putExtra("deliver_address", address);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
                                                finish();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage() + " order java exception " + activity, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View view) {
        int cases = view.getId();
        switch (cases) {
            case R.id.add1:
                //   Toast.makeText(Order.this,"before add1 "+add1_count+ add2_count+add3_count,Toast.LENGTH_SHORT).show();
                add1_count = add1_count + 1;
                if (add2_count == 0 && add3_count == 0) {
                    add_cart_price = add_cart_price + PRICE;
                } else {
                    if (add2_count > 0) {
                        add_cart_price = add_cart_price + PRICE;
                        add_cart_price = add_cart_price + (35 * add2_count);
                    }
                    if (add3_count > 0) {
                        add_cart_price = add_cart_price + (25 * add3_count);
                    }
                }
                // Toast.makeText(Order.this,"after add1 "+add1_count,Toast.LENGTH_SHORT).show();
                count1.setText(String.valueOf(add1_count));
                price.setText(add_cart_price + ".00TK");
                break;
            case R.id.add2:
                add2_count = add2_count + 1;
                count2.setText(String.valueOf(add2_count));
                add_cart_price = add_cart_price + (add1_count * 35);
                price.setText(add_cart_price + ".00TK");
                break;
            case R.id.add3:
                add3_count = add3_count + 1;
                count3.setText(String.valueOf(add3_count));
                add_cart_price = add_cart_price + (add1_count * 25);
                price.setText(add_cart_price + ".00TK");
                break;
            case R.id.minus1:
                if (add1_count != 0) {
                    // Toast.makeText(Order.this,"before minus "+String.valueOf(add1_count),Toast.LENGTH_LONG).show();
                    add1_count = add1_count - 1;
                    if (add2_count == 0 && add3_count == 0) {
                        add_cart_price = add_cart_price - PRICE;
                    } else {
                        add_cart_price = add_cart_price - PRICE;
                        if (add2_count > 0) {
                            add_cart_price = add_cart_price - (35 * add2_count);
                        }
                        if (add3_count > 0) {
                            add_cart_price = add_cart_price - (25 * add3_count);
                        }
                    }
                    count1.setText(String.valueOf(add1_count));
                    price.setText(add_cart_price + ".00TK");
                    //  Toast.makeText(Order.this,"after minus "+String.valueOf(add1_count),Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.minus2:
                if (add2_count != 0) {

                    add2_count = add2_count - 1;
                    count2.setText(String.valueOf(add2_count));
                    add_cart_price = add_cart_price - (add1_count * 35);
                    price.setText(add_cart_price + ".00TK");
                }
                break;
            case R.id.minus3:
                if (add3_count != 0) {
                    add3_count = add3_count - 1;
                    count3.setText(String.valueOf(add3_count));
                    add_cart_price = add_cart_price - (add1_count * 25);
                    price.setText(add_cart_price + ".00TK");
                }
                break;

            case R.id.regular:
                if (REGULAR) {
                    PRICE = Integer.parseInt(regular_price);
                } else {
                    if (add1_count != 0 && !REGULAR) {
                        add_cart_price = add_cart_price - (add1_count * Integer.parseInt(large_price));
                        add_cart_price = add_cart_price + (add1_count * Integer.parseInt(regular_price));
                        price.setText(add_cart_price + ".00TK");

                    }
                    REGULAR = true;
                    PRICE = Integer.parseInt(regular_price);
                }
                break;
            case R.id.large:
                if (REGULAR) {
                    if (add1_count != 0) {
                        add_cart_price = add_cart_price - (add1_count * Integer.parseInt(regular_price));
                        add_cart_price = add_cart_price + (add1_count * Integer.parseInt(large_price));
                        price.setText(add_cart_price + ".00TK");
                    }
                    REGULAR = false;
                    PRICE = Integer.parseInt(large_price);
                } else {
                    PRICE = Integer.parseInt(large_price);
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (databaseReference != null && listener != null) {
            databaseReference.removeEventListener(listener);
            //Toast.makeText(Order.this, "removed listener", Toast.LENGTH_LONG).show();
        }
        if (user_reference != null && listener1 != null) {
            user_reference.removeEventListener(listener1);
        }
        if(databaseReference1!=null&& listener2!=null){
            databaseReference1.removeEventListener(listener2);
        }

    }

    private boolean isConnected(Context context) {
        boolean RETURN = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi_connection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile_connection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi_connection != null && wifi_connection.isConnected()) || (mobile_connection != null && mobile_connection.isConnected())) {
            if (internet_connection_dialog.isShowing()) {
                internet_connection_dialog.dismiss();
            }

            RETURN = true;
        } else {
            Toast.makeText(Order.this, "no connection", Toast.LENGTH_LONG).show();

            RETURN = false;
        }
        return RETURN;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseReference != null && listener != null) {
            databaseReference.removeEventListener(listener);
            //Toast.makeText(Order.this, "removed listener", Toast.LENGTH_LONG).show();
        }
        if (user_reference != null && listener1 != null) {
            user_reference.removeEventListener(listener1);
        }
        if(databaseReference1!=null&& listener2!=null){
            databaseReference1.removeEventListener(listener2);
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
        if (user_reference != null && listener1 != null) {
            user_reference.removeEventListener(listener1);
        }
        if(databaseReference1!=null&& listener2!=null){
            databaseReference1.removeEventListener(listener2);
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
        if (user_reference != null && listener1 != null) {
            user_reference.removeEventListener(listener1);
        }
        if(databaseReference1!=null&& listener2!=null){
            databaseReference1.removeEventListener(listener2);
        }
    }

}

