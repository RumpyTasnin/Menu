package com.example.menu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class home_delivery extends AppCompatActivity {

    ImageView close;
    LinearLayout edit;
    LinearLayout current_location, add_address, select_location, layout, add_current_loc;
    RecyclerView recyclerView;
    TextView currentLocation;
    Dialog dialog;
    String ranAddress;
    int count = 0;

    TextView heading, button_text;

    String CLASS;

    boolean not_order_class = false;

    EditText road_dialog, apartment_dialog, remark_dialog;
    AutoCompleteTextView autoCompleteTextView;

    View view;
    String street_details, apartment_details, full_address_details, full_address1, landmarks_details, label, label1;
    String order_id, phone, deliver_address, current_address, address, email, username, type, uid;

    TextView textView_address, textView_apartment, textView_street, textView_remark;

    FusedLocationProviderClient fusedLocationProviderClient;

    int food_image, layout_no;
    String food_name, food_description, food_price_intent, food_size_regular, food_size_large, activity, regular_price, large_price;
    boolean visibility, radio_group_visibility;

    boolean isDialog3 = false;

    FirebaseRecyclerOptions<home_delivery_dataSet> options;
    FirebaseRecyclerAdapter<home_delivery_dataSet, FirebaseViewHolder2> firebaseAdapter;
    Query query;

    DatabaseReference databaseReference, orderReference, cartReference, place_order_reference;
    ValueEventListener listener;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //status bar transparent
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_home_delivery);


        close = findViewById(R.id.close);
        current_location = findViewById(R.id.current_location);
        add_address = findViewById(R.id.add_address);
        select_location = findViewById(R.id.place_order);
        recyclerView = findViewById(R.id.home_delivery_recyclervew);
        layout = findViewById(R.id.linear_layout);
        view = findViewById(R.id.view);
        edit = findViewById(R.id.edit);
        heading = findViewById(R.id.heading);
        button_text = findViewById(R.id.button_text);


        add_current_loc = findViewById(R.id.add_current_loc);
        currentLocation = findViewById(R.id.currentLoc);
        order_id = String.valueOf(getIntent().getIntExtra("order_id", 0));
        username = getIntent().getStringExtra("username");
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        uid = getIntent().getStringExtra("uid");
        address = getIntent().getStringExtra("deliver_address");

        not_order_class = getIntent().getBooleanExtra("not_order_class", false);
        CLASS = getIntent().getStringExtra("class");
        if (!not_order_class) {
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
        }


        if (getIntent().getBooleanExtra("from_edit_address", false)) {

            //  Toast.makeText(home_delivery.this,"from edit class",Toast.LENGTH_SHORT).show();
            if (getIntent().getBooleanExtra("from_current_location_layout", false)) {
                layout.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                count = 1;
            } else {
                layout.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                count = 0;
            }

            street_details = getIntent().getStringExtra("street_details");
            apartment_details = getIntent().getStringExtra("apartment_details");
            full_address_details = getIntent().getStringExtra("full_address_details");
            full_address1 = getIntent().getStringExtra("address");
            landmarks_details = getIntent().getStringExtra("landmark_details");
            type = getIntent().getStringExtra("type");
            label = getIntent().getStringExtra("label");
            currentLocation.setText(full_address1);
            current_address = full_address1;
            //   Toast.makeText(home_delivery.this, "back home street details apartment no :  "+apartment_details,Toast.LENGTH_LONG).show();


        } else {
            setUp();
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count == 0) {
                    try {

                        if (ActivityCompat.checkSelfPermission(home_delivery.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            getLocation();


                        } else {
                            ActivityCompat.requestPermissions(home_delivery.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                            // Toast.makeText(home_delivery.this,"try again",Toast.LENGTH_LONG).show();


                        }
                        count = 1;
                    } catch (Exception e) {
                        Toast.makeText(home_delivery.this, "current location error" + e.getMessage(), Toast.LENGTH_LONG).show();

                    }

                } else {
                    layout.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
                    count = 0;
                }

            }
        });
        add_current_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    isDialog3 = false;
                    layout.setVisibility(View.GONE);
                    view.setVisibility(View.GONE);
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                    cartReference = FirebaseDatabase.getInstance().getReference("cart");
                    orderReference = FirebaseDatabase.getInstance().getReference("orders");

                    ranAddress = getRandomAddress();
                    if (getIntent().getBooleanExtra("from_edit_address", false)) {
                        // Toast.makeText(home_delivery.this,"from edit class",Toast.LENGTH_SHORT).show();

                        try {
                            databaseReference.child("Deliver").child(ranAddress).child("name").setValue(ranAddress);
                            databaseReference.child("deliver_address").setValue(ranAddress);
                            databaseReference.child("Deliver").child(ranAddress).child("location").setValue(current_address);
                        } catch (Exception e) {
                            Toast.makeText(home_delivery.this, "add error name location error", Toast.LENGTH_SHORT).show();
                        }
                        try {
                            if (landmarks_details != null) {
                                // Toast.makeText(home_delivery.this, " landmark details from add " + landmarks_details, Toast.LENGTH_LONG).show();
                                databaseReference.child("Deliver").child(ranAddress).child("landmark").setValue(landmarks_details);
                            }
                        } catch (Exception e) {
                            Toast.makeText(home_delivery.this, "landmark details error", Toast.LENGTH_SHORT).show();
                        }

                       /* orderReference.child(order_id).child("landmark").setValue(landmarks_details);
                        cartReference.child(order_id).child("user_info").child("landmark").setValue(landmarks_details);
                        try{}


*/
                        try {
                            if (type != null && street_details != null) {

                                databaseReference.child("Deliver").child(ranAddress).child(type).setValue(street_details);
                                //  Toast.makeText(home_delivery.this," add type "+type+" details "+street_details,Toast.LENGTH_SHORT).show();

                         /*   orderReference.child(order_id).child(type).setValue(street_details);
                            cartReference.child(order_id).child("user_info").child(type).setValue(street_details);
*/
                            } else {
                                Toast.makeText(home_delivery.this, "type empty on add click", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(home_delivery.this, "type add error error", Toast.LENGTH_SHORT).show();
                        }

         /*               orderReference.child(order_id).child("apartment").setValue(apartment_details);
                        cartReference.child(order_id).child("user_info").child("apartment").setValue(apartment_details);*/
                        databaseReference.child("Deliver").child(ranAddress).child("apartment").setValue(apartment_details);
                        databaseReference.child("Deliver").child(ranAddress).child("type").setValue(label);
                        try {

                        } catch (Exception e) {
                            Toast.makeText(home_delivery.this, "full address details add error error", Toast.LENGTH_SHORT).show();
                            if (!full_address_details.isEmpty()) {
                                databaseReference.child("Deliver").child(ranAddress).child("full_address").setValue(full_address_details);
                           /* orderReference.child(order_id).child("full_address").setValue(full_address_details);
                            cartReference.child(order_id).child("user_info").child("full_address").setValue(full_address_details);
*/
                            }
                        }


                           /* orderReference.child(order_id).child("customer_address").setValue(current_address);
                            cartReference.child(order_id).child("user_info").child("customer_address").setValue(current_address);
*/
                    } else {
                        databaseReference.child("deliver_address").setValue(ranAddress);
                        databaseReference.child("Deliver").child(ranAddress).child("location").setValue(current_address);
                        databaseReference.child("Deliver").child(ranAddress).child("name").setValue(ranAddress);
                        databaseReference.child("Deliver").child(ranAddress).child("landmark").setValue("");
                        databaseReference.child("Deliver").child(ranAddress).child("type").setValue("Others");
                       /* orderReference.child(order_id).child("customer_address").setValue(current_address);
                        cartReference.child(order_id).child("user_info").child("customer_address").setValue(current_address);
*/
                    }

                    setAdapter();

                } catch (Exception e) {
                    Toast.makeText(home_delivery.this, "   add method   error " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        });

        dialog = new Dialog(home_delivery.this);
        dialog.setContentView(R.layout.dialog3);
        // dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomActivityAnimation2;
        road_dialog = dialog.findViewById(R.id.road1_dialog3);
        apartment_dialog = dialog.findViewById(R.id.apartment1_dialog3);
        remark_dialog = dialog.findViewById(R.id.remarks1_dialog3);
        textView_address = dialog.findViewById(R.id.address_dialog3);
        textView_apartment = dialog.findViewById(R.id.appartment2_dialog3);
        textView_street = dialog.findViewById(R.id.street_dialog3);
        textView_remark = dialog.findViewById(R.id.remarks_dialog3);

        textView_address.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        textView_apartment.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        textView_street.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        textView_remark.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        dialog.findViewById(R.id.home1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label1 = "Home";
            }
        });
        dialog.findViewById(R.id.work1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label1 = "Work";
            }
        });
        dialog.findViewById(R.id.others1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label1 = "Others";
            }
        });

        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                label1 = "";
                autoCompleteTextView = dialog.findViewById(R.id.autocomplete_fragment2);
                autoCompleteTextView.setText("");
                road_dialog.setText("");
                apartment_dialog.setText("");
                remark_dialog.setText("");
                autoCompleteTextView.setAdapter(new PlaceAutoSuggestAdapter(home_delivery.this, android.R.layout.simple_list_item_1));
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        current_address = autoCompleteTextView.getText().toString();
                    }
                });
                dialog.findViewById(R.id.add_address).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isConnected(home_delivery.this)) {
                            ranAddress = getRandomAddress();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                            if (!autoCompleteTextView.getText().toString().isEmpty() && !apartment_dialog.getText().toString().isEmpty() && !road_dialog.getText().toString().isEmpty()) {
                                isDialog3 = true;
                                textView_address.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                textView_apartment.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                textView_street.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                textView_remark.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                                current_address = autoCompleteTextView.getText().toString();
                                apartment_details = apartment_dialog.getText().toString();
                                street_details = road_dialog.getText().toString();
                                if (!remark_dialog.getText().toString().isEmpty()) {
                                    landmarks_details = remark_dialog.getText().toString();
                                }
                                databaseReference.child("deliver_address").setValue(ranAddress);
                                databaseReference.child("Deliver").child(ranAddress).child("location").setValue(current_address);
                                databaseReference.child("Deliver").child(ranAddress).child("name").setValue(ranAddress);
                                databaseReference.child("Deliver").child(ranAddress).child("landmark").setValue(landmarks_details);
                                databaseReference.child("Deliver").child(ranAddress).child("apartment").setValue(apartment_details);
                                databaseReference.child("Deliver").child(ranAddress).child("road_no").setValue(street_details);
                                if (label1.isEmpty()) {
                                    databaseReference.child("Deliver").child(ranAddress).child("type").setValue("Others");

                                } else {
                                    databaseReference.child("Deliver").child(ranAddress).child("type").setValue(label1);
                                }
                                dialog.dismiss();
                            } else {
                                isDialog3 = false;
                                if (autoCompleteTextView.getText().toString().isEmpty()) {
                                    textView_address.setTextColor(getResources().getColor(R.color.red));

                                }
                                if (apartment_dialog.getText().toString().isEmpty()) {
                                    textView_apartment.setTextColor(getResources().getColor(R.color.red));

                                }
                                if (road_dialog.getText().toString().isEmpty()) {
                                    textView_street.setTextColor(getResources().getColor(R.color.red));

                                }

                            }
                        }

                    }
                });
                dialog.findViewById(R.id.close_dialog3).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        isDialog3 = false;
                        dialog.dismiss();
                    }
                });

            }
        });
        select_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_order_reference = FirebaseDatabase.getInstance().getReference().child("placed_order").child("orders");
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                cartReference = FirebaseDatabase.getInstance().getReference("cart");
                orderReference = FirebaseDatabase.getInstance().getReference("orders");
                if (isDialog3) {
                    if (landmarks_details != null) {
                        orderReference.child(order_id).child("landmark").setValue(landmarks_details);
                        cartReference.child(order_id).child("user_info").child("landmark").setValue(landmarks_details);
                        place_order_reference.child(order_id).child("user_info").child("landmark").setValue(landmarks_details);
                    }
                    orderReference.child(order_id).child("deliver_location").setValue(current_address);
                    cartReference.child(order_id).child("user_info").child("deliver_location").setValue(current_address);
                    place_order_reference.child(order_id).child("user_info").child("deliver_location").setValue(current_address);

                    orderReference.child(order_id).child("deliver_apartment").setValue(apartment_details);
                    cartReference.child(order_id).child("user_info").child("deliver_apartment").setValue(apartment_details);
                    place_order_reference.child(order_id).child("user_info").child("deliver_apartment").setValue(apartment_details);

                    orderReference.child(order_id).child("deliver_road_no").setValue(street_details);
                    cartReference.child(order_id).child("user_info").child("deliver_road_no").setValue(street_details);
                    place_order_reference.child(order_id).child("user_info").child("deliver_road_no").setValue(street_details);

                    Intent i = new Intent(home_delivery.this, Cart.class);
                    i.putExtra("address_confirmed", true);
                    i.putExtra("address", current_address);
                    i.putExtra("order_id", Integer.parseInt(order_id));
                    i.putExtra("username", username);
                    i.putExtra("phone", phone);
                    i.putExtra("email", email);
                    i.putExtra("uid", uid);
                    i.putExtra("deliver_address", address);
                    i.putExtra("not_order_class", not_order_class);

                    i.putExtra("class", CLASS);
                    if (!not_order_class) {
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
                    }


                    startActivity(i);
                    overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
                } else {
                    if (current_address != null && apartment_details != null) {

                        if (getIntent().getBooleanExtra("from_edit_address", false)) {
                            if (landmarks_details != null) {
                                orderReference.child(order_id).child("landmark").setValue(landmarks_details);
                                cartReference.child(order_id).child("user_info").child("landmark").setValue(landmarks_details);
                                place_order_reference.child(order_id).child("user_info").child("landmark").setValue(landmarks_details);
                            }

                            orderReference.child(order_id).child("deliver_location").setValue(current_address);
                            cartReference.child(order_id).child("user_info").child("deliver_location").setValue(current_address);
                            place_order_reference.child(order_id).child("user_info").child("deliver_location").setValue(current_address);
                            try {
                                if (type != null && street_details != null) {
                                    //  Toast.makeText(home_delivery.this,"inside try catch"+street_details,Toast.LENGTH_SHORT).show();
                                    orderReference.child(order_id).child(type).setValue(street_details);
                                    cartReference.child(order_id).child("user_info").child(type).setValue(street_details);
                                    place_order_reference.child(order_id).child("user_info").child(type).setValue(street_details);

                                } else {
                                    //  Toast.makeText(home_delivery.this," null ",Toast.LENGTH_SHORT).show();
                                }
                                if (apartment_details != null) {
                                    orderReference.child(order_id).child("deliver_apartment").setValue(apartment_details);
                                    cartReference.child(order_id).child("user_info").child("deliver_apartment").setValue(apartment_details);
                                    place_order_reference.child(order_id).child("user_info").child("deliver_apartment").setValue(apartment_details);


                                } else {
                                    // Toast.makeText(home_delivery.this," full _address null ",Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(home_delivery.this, "street details error ", Toast.LENGTH_SHORT).show();
                            }
                            try {
                                if (full_address_details != null) {
                                    orderReference.child(order_id).child("full_address").setValue(full_address_details);
                                    cartReference.child(order_id).child("user_info").child("full_address").setValue(full_address_details);
                                    place_order_reference.child(order_id).child("user_info").child("full_address").setValue(full_address_details);

                                } else {
                                    // Toast.makeText(home_delivery.this," full _address null ",Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(home_delivery.this, "select location error", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            try {
                                if (landmarks_details != null) {
                                    orderReference.child(order_id).child("landmark").setValue(landmarks_details);
                                    cartReference.child(order_id).child("user_info").child("landmark").setValue(landmarks_details);
                                    place_order_reference.child(order_id).child("user_info").child("landmark").setValue(landmarks_details);
                                }
                                if (type != null && street_details != null) {
                                    // Toast.makeText(home_delivery.this,"inside try catch"+street_details,Toast.LENGTH_SHORT).show();
                                    orderReference.child(order_id).child(type).setValue(street_details);
                                    cartReference.child(order_id).child("user_info").child(type).setValue(street_details);
                                    place_order_reference.child(order_id).child("user_info").child(type).setValue(street_details);

                                }
                                if (apartment_details != null) {
                                    orderReference.child(order_id).child("deliver_apartment").setValue(apartment_details);
                                    cartReference.child(order_id).child("user_info").child("deliver_apartment").setValue(apartment_details);
                                    place_order_reference.child(order_id).child("user_info").child("deliver_apartment").setValue(apartment_details);


                                }
                                if (full_address_details != null) {
                                    orderReference.child(order_id).child("full_address").setValue(full_address_details);
                                    cartReference.child(order_id).child("user_info").child("full_address").setValue(full_address_details);
                                    place_order_reference.child(order_id).child("user_info").child("full_address").setValue(full_address_details);

                                }
                                orderReference.child(order_id).child("deliver_location").setValue(current_address);
                                cartReference.child(order_id).child("user_info").child("deliver_location").setValue(current_address);
                                place_order_reference.child(order_id).child("user_info").child("deliver_location").setValue(current_address);
                            } catch (Exception e) {
                                Toast.makeText(home_delivery.this, "current address error", Toast.LENGTH_SHORT).show();
                            }

                        }
                        Intent i = new Intent(home_delivery.this, Cart.class);
                        i.putExtra("address_confirmed", true);
                        i.putExtra("order_id", Integer.parseInt(order_id));
                        i.putExtra("username", username);
                        i.putExtra("phone", phone);
                        i.putExtra("email", email);
                        i.putExtra("uid", uid);
                        i.putExtra("not_order_class", not_order_class);
                        i.putExtra("class", CLASS);
                        if (!not_order_class) {
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
                        }

                        //  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
                        finish();

                    } else {
                        Intent i = new Intent(home_delivery.this, edit_address.class);
                        i.putExtra("address_confirmed", true);
                        i.putExtra("address", current_address);
                        i.putExtra("order_id", Integer.parseInt(order_id));
                        i.putExtra("username", username);
                        i.putExtra("phone", phone);
                        i.putExtra("email", email);
                        i.putExtra("uid", uid);
                        i.putExtra("deliver_address", address);

                        i.putExtra("not_order_class", not_order_class);
                        i.putExtra("class", CLASS);
                        if (!not_order_class) {
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
                        }


                        startActivity(i);
                        overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                    }

                }


            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home_delivery.this, Cart.class);
                // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("order_id", Integer.parseInt(order_id));
                i.putExtra("username", username);
                i.putExtra("phone", phone);
                i.putExtra("email", email);
                i.putExtra("uid", uid);
                i.putExtra("deliver_address", deliver_address);
                i.putExtra("not_order_class", not_order_class);
                i.putExtra("class", CLASS);
                if (!not_order_class) {
                    if (getIntent().getBooleanExtra("from_favourites_class", false)) {
                        i.putExtra("from_favourites_class", true);
                    }
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
                }

                startActivity(i);
                overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
                finish();

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(home_delivery.this, edit_address.class);
                i.putExtra("address", current_address);
                i.putExtra("order_id", Integer.parseInt(order_id));
                i.putExtra("username", username);
                i.putExtra("phone", phone);
                i.putExtra("email", email);
                i.putExtra("uid", uid);
                i.putExtra("deliver_address", address);
                i.putExtra("not_order_class", not_order_class);
                i.putExtra("class", CLASS);
                i.putExtra("from_current_location_layout", true);
                if (!not_order_class) {
                    i.putExtra("food_image", food_image);
                    if (getIntent().getBooleanExtra("from_favourites_class", false)) {
                        i.putExtra("from_favourites_class", true);
                    }
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
                }

                startActivity(i);
                overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);


            }
        });


        setAdapter();

    }


    private void setAdapter() {
        // Toast.makeText(home_delivery.this,"set adater  "+phone,Toast.LENGTH_LONG).show();

        try {
            try {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                query = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("Deliver");
                options = new FirebaseRecyclerOptions.Builder<home_delivery_dataSet>().setQuery(query, home_delivery_dataSet.class).build();
            } catch (Exception e) {
                Toast.makeText(home_delivery.this, "  error   here            ", Toast.LENGTH_LONG).show();
            }

            firebaseAdapter = new FirebaseRecyclerAdapter<home_delivery_dataSet, FirebaseViewHolder2>(options) {
                @NonNull
                @Override
                public FirebaseViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(home_delivery.this)
                            .inflate(R.layout.home_delivery_row, parent, false);
                    FirebaseViewHolder2 firebaseViewHolder = new FirebaseViewHolder2(view);
                    return firebaseViewHolder;
                }

                @Override
                protected void onBindViewHolder(@NonNull FirebaseViewHolder2 holder, int position, @NonNull home_delivery_dataSet model) {
                    // Toast.makeText(home_delivery.this, "bind view holder", Toast.LENGTH_LONG).show();

                    try {
                        holder.location2.setText(model.getType());
                        holder.location1.setText(model.getLocation());
                        listener = databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                deliver_address = snapshot.child("deliver_address").getValue().toString();
                                // Toast.makeText(home_delivery.this, "deliver address   " + deliver_address+" model.getname() "+model.getName(), Toast.LENGTH_LONG).show();
                                try {
                                    if (model.getName().contains(deliver_address)) {
                                        holder.done.setVisibility(View.VISIBLE);
                                    } else {
                                        holder.done.setVisibility(View.INVISIBLE);
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(home_delivery.this, "bind  " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } catch (Exception e) {
                        Toast.makeText(home_delivery.this, "data change add value event listener" + e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                    try {
                        //   Toast.makeText(home_delivery.this,model.getType() , Toast.LENGTH_LONG).show();

                        if (model.getType().contains("Home")) {
                            holder.location.setImageResource(R.drawable.home);
                        } else {
                            if (model.getType().contains("Work")) {
                                holder.location.setImageResource(R.drawable.work);
                            } else {
                                if (model.getType().contains("Others")) {
                                    holder.location.setImageResource(R.drawable.location);
                                }
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(home_delivery.this, "type error" + e.getMessage(), Toast.LENGTH_LONG).show();


                    }
                    holder.row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //  Toast.makeText(home_delivery.this, "click", Toast.LENGTH_LONG).show();

                            isDialog3 = false;
                            holder.done.setVisibility(View.VISIBLE);
                            databaseReference.child("deliver_address").setValue(model.getName());
                        }
                    });


                }

            };
        } catch (Exception e) {
            Toast.makeText(home_delivery.this, "Adapter error " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        try {
            firebaseAdapter.startListening();
            recyclerView.setLayoutManager(new LinearLayoutManager(home_delivery.this));
            recyclerView.setAdapter(firebaseAdapter);

        } catch (Exception e) {
            Toast.makeText(home_delivery.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String getRandomAddress() {
        Random random = new Random();
        String random_address = "ADDRESS";
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789XYZPQRSUV";
        int i = 0;
        StringBuilder string = new StringBuilder();
        while (i < 6) {
            string.append(characters.charAt(random.nextInt(characters.length())));
            i++;
        }
        random_address = random_address + string.toString();

        return random_address;

    }


    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //  Toast.makeText(home_delivery.this, " get location  " , Toast.LENGTH_LONG).show();

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                try {
                    Location location = task.getResult();
                    if (location != null) {

                        //Toast.makeText(home_delivery.this, " location not null " , Toast.LENGTH_LONG).show();


                        layout.setVisibility(View.VISIBLE);
                        view.setVisibility(View.VISIBLE);
                        Geocoder geocoder = new Geocoder(home_delivery.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        //  Toast .makeText(home_delivery.this, Html.fromHtml(addresses.get(0).getAddressLine(0)+"  "+addresses.get(0).getFeatureName()), Toast.LENGTH_LONG).show();

                        String loc = addresses.get(0).getSubLocality() + "," + addresses.get(0).getAddressLine(0);
                        if (addresses.get(0).getAddressLine(1) != null) {
                            loc = loc + "," + addresses.get(0).getAddressLine(1);
                        }

                        current_address = loc;
                        currentLocation.setText(Html.fromHtml(loc));
                    } else {
                        Toast.makeText(home_delivery.this, "can not find your current location, check your location service ", Toast.LENGTH_LONG).show();

                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                // super.onLocationResult(locationResult);
                                Toast.makeText(home_delivery.this, "call back ", Toast.LENGTH_LONG).show();
                                Location location1 = locationResult.getLastLocation();

                                try {
                                    Geocoder geocoder = new Geocoder(home_delivery.this, Locale.getDefault());

                                    List<Address> addresses = geocoder.getFromLocation(location1.getLatitude(), location1.getLongitude(), 1);
                                    String loc = addresses.get(0).getSubLocality() + "," + addresses.get(0).getAddressLine(0);
                                    if (addresses.get(0).getAddressLine(1) != null) {
                                        loc = loc + "," + addresses.get(0).getAddressLine(1);
                                    }

                                    current_address = loc;
                                    currentLocation.setText(Html.fromHtml(loc));
                                } catch (IOException e) {
                                    Toast.makeText(home_delivery.this, "getcurrent location   currentlocation=loc error", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }


                            }
                        };

                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    }


                } catch (Exception e) {
                    Toast.makeText(home_delivery.this, "getcurrent location   error", Toast.LENGTH_LONG).show();

                    e.printStackTrace();
                }

            }
        });


    }

    public void setUp() {

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        //*****************************************************************************************************//////////////////////////////
         databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String d = snapshot.child("deliver_address").getValue().toString();
              //  Toast.makeText(home_delivery.this,"deliver address "+d,Toast.LENGTH_LONG).show();

                try {

                    try {
                        if (snapshot.child("Deliver").child(d).hasChild("location")) {
                            current_address = snapshot.child("Deliver").child(d).child("location").getValue().toString();
                           // Toast.makeText(home_delivery.this,"address "+current_address,Toast.LENGTH_LONG).show();
                        }

                        // Toast.makeText(home_delivery.this," set up method on data changed called "+current_address,Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(home_delivery.this, " set up method on data changed called current address error " + current_address, Toast.LENGTH_LONG).show();

                    }
                    try {
                        if (snapshot.child("Deliver").child(d).hasChild("street")) {
                            type = "street";
                            street_details = snapshot.child("Deliver").child(d).child(type).getValue().toString();
                        }
                        if (snapshot.child("Deliver").child(d).hasChild("area")) {
                            type = "area";
                            street_details = snapshot.child("Deliver").child(d).child(type).getValue().toString();
                        }
                       /* if (type != null) {
                            //Toast.makeText(home_delivery.this," current_ address on data changed  type "+type,Toast.LENGTH_LONG).show();

                            street_details=snapshot.child("Deliver").child(d).child(type).getValue().toString();
                        }*/
                    } catch (Exception e) {

                        Toast.makeText(home_delivery.this, " set up method on data changed called type  error " + current_address, Toast.LENGTH_LONG).show();

                    }
                    try {
                        if (snapshot.child("Deliver").child(d).hasChild("full_address")) {
                            full_address_details = snapshot.child("Deliver").child(d).child("full_address").getValue().toString();
                            //Toast.makeText(home_delivery.this," current_ address on data changed  full_address "+full_address_details,Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(home_delivery.this, " set up method on data changed called full address  error " + current_address, Toast.LENGTH_LONG).show();

                    }
                    try {

                        if (snapshot.child("Deliver").child(d).hasChild("apartment")) {
                            apartment_details = snapshot.child("Deliver").child(d).child("apartment").getValue().toString();
                            // Toast.makeText(home_delivery.this, " current_ address on data changed  apartment " + apartment_details, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(home_delivery.this, " set up method on data changed called apartment  error " + current_address, Toast.LENGTH_LONG).show();

                    }
                    try {
                        if (snapshot.child("Deliver").child(d).hasChild("landmark")) {
                            landmarks_details = snapshot.child("Deliver").child(d).child("landmark").getValue().toString();
                            //Toast.makeText(home_delivery.this," current_ address on data changed  landmark "+landmarks_details,Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(home_delivery.this, " set up method on data changed called landmark  error " + current_address, Toast.LENGTH_LONG).show();

                    }


                } catch (Exception e) {
                    Toast.makeText(home_delivery.this, " current_ address error " + current_address, Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(home_delivery.this, Cart.class);
        // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("order_id", Integer.parseInt(order_id));
        i.putExtra("username", username);
        i.putExtra("phone", phone);
        i.putExtra("email", email);
        i.putExtra("uid", uid);
        i.putExtra("deliver_address", deliver_address);

        i.putExtra("not_order_class", not_order_class);
        i.putExtra("class", CLASS);
        if (!not_order_class) {
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
        }


        startActivity(i);
        overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
        finish();
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
            //Toast.makeText(Order.this, "removed listener", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (databaseReference != null && listener != null) {
            databaseReference.removeEventListener(listener);
            //Toast.makeText(Order.this, "removed listener", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (databaseReference != null && listener != null) {
            databaseReference.removeEventListener(listener);
            //Toast.makeText(Order.this, "removed listener", Toast.LENGTH_LONG).show();
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
            Toast.makeText(home_delivery.this, "no connection", Toast.LENGTH_LONG).show();

            RETURN = false;
        }
        return RETURN;
    }
}