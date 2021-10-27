package com.example.admin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Orders extends AppCompatActivity implements View.OnClickListener {
    FusedLocationProviderClient fusedLocationProviderClient;
    Intent intent;
    Geocoder geocoder;
    String status, ORDER_STATUS = "";
    boolean cancel = false;
    boolean show_bold_text = true;
    String admin_current_address, destination;
    private GoogleMap mMap;
    Location location;
    Marker marker1;
    MarkerOptions markerOptions;
    LatLng start_latLang, destination_latLng;
    Address address1;
    String currentDate, currentTime;


    String extra_shot_price, iced_price, food_price, food_type, layout, instructions, food_name, s;
    int food_amount, extra_shot_amount, iced_amount, total_price, delivery_fee;

    String FOOD_TYPE, STRING;
    int FOOD_AMOUNT, EXTRA_SHOT_AMOUNT, ICED_AMOUNT, FOOD_PRICE, pricetotal, pricefinal, calculation;

    String customer_phone, customer_email, customer_address, customer_street, customer_road, customer_landmark, customer_full_address,
            customer_name, customer_uid, customer_apartment;

    String phone, id, email, username, uid, branch, order_id, delivery_type, token, order_status;
    EditText TO, SUBJECT, FROM, EMAIL;
    TextView toolbar_name;
    TextView textView_phone, textView_email, textView_address, textView_street_details, textView_road_no, textView_landmark, textView_name, textView_apartment;
    LinearLayout call_now, email_now, see_on_map, street_layout, road_layout, landmark_layout, apartment_layout, order_layout;
    CardView user_info;

    Dialog email_dialog, set_order_status_dialog, internet_connection_dialog;
    LinearLayout dialog_received, dialog_preparing, dialog_pickedUp, dialog_delivered, dialog_confirm, layout_order_status;
    TextView dialog_status, textView_dialog_received, textView_dialog_preparing,
            textView_dialog_pickedUp, textView_dialog_delivered, textView_order_status, textView_pickUp_time;
    View view_order_status;
    LinearLayout linearLayout_set_order_status, linearLayout_cancel_order, order_dropdown, layout_takeaway;

    String placed_order_key, user_key, pickUp_time;

    int c1 = 0;
    int c2 = 1;

    RecyclerView recyclerView;
    MenuInflater menuInflater, inflater;
    MenuBuilder menuBuilder, Builder;

    ImageView drop_down1, drop_down2, back;

    ValueEventListener listener, listener1;
    DatabaseReference place_order_reference, adapter_reference, user_reference, push_reference, delivered_push_reference;
    FirebaseRecyclerOptions<orders_dataset> options;
    Query query;
    FirebaseRecyclerAdapter<orders_dataset, FirebaseViewHolder1> firebaseAdapter;

    ///####?///////////////////send message casting///////////////////////////

    LinearLayout message_layout, send_message;
    EditText message;
    String SMS;
    boolean request=false;
    Intent i;
    String title, body;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_orders);


        phone = getIntent().getStringExtra("phone");
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        uid = getIntent().getStringExtra("uid");
        branch = getIntent().getStringExtra("branch");
        order_id = getIntent().getStringExtra("order_id");
        delivery_type = getIntent().getStringExtra("deliver_type");
        token = getIntent().getStringExtra("token");
        order_status = getIntent().getStringExtra("order_status");

        back = findViewById(R.id.toolbar).findViewById(R.id.back);
        toolbar_name = findViewById(R.id.toolbar).findViewById(R.id.name);
        textView_phone = findViewById(R.id.phone);
        textView_email = findViewById(R.id.email);
        textView_address = findViewById(R.id.address);
        textView_street_details = findViewById(R.id.street_or_area_details);
        textView_road_no = findViewById(R.id.road_no);
        textView_landmark = findViewById(R.id.landmark);
        textView_name = findViewById(R.id.customer_name);
        textView_apartment = findViewById(R.id.apartment);

        email_now = findViewById(R.id.email_now);
        call_now = findViewById(R.id.call_now);
        see_on_map = findViewById(R.id.see_on_map);
        street_layout = findViewById(R.id.street_layout);
        road_layout = findViewById(R.id.road_layout);
        landmark_layout = findViewById(R.id.landmark_layout);
        apartment_layout = findViewById(R.id.apartment_layout);

        message_layout = findViewById(R.id.message_layout);
        send_message = findViewById(R.id.send);
        message = findViewById(R.id.message);

        drop_down1 = findViewById(R.id.drop_down1);
        drop_down2 = findViewById(R.id.drop_down2);
        user_info = findViewById(R.id.user_info);
        order_layout = findViewById(R.id.order_layout);
        recyclerView = findViewById(R.id.firebase_recyclerView);
        layout_order_status = findViewById(R.id.order_status);
        textView_order_status = findViewById(R.id.order_status_textview);
        view_order_status = findViewById(R.id.order_status_view);
        linearLayout_set_order_status = findViewById(R.id.layout_set_order_status);
        linearLayout_cancel_order = findViewById(R.id.cancel_order);
        order_dropdown = findViewById(R.id.order_dropdown);
        layout_takeaway = findViewById(R.id.take_away_layout);
        textView_pickUp_time = findViewById(R.id.pickUp_time);

        set_order_status_dialog = new Dialog(Orders.this);
        set_order_status_dialog.setContentView(R.layout.dialog_order_status);
        set_order_status_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        set_order_status_dialog.getWindow().setGravity(Gravity.CENTER);
        set_order_status_dialog.setCancelable(true);
        set_order_status_dialog.getWindow().getAttributes().windowAnimations = R.style.CustomActivityAnimation2;

        dialog_received = set_order_status_dialog.findViewById(R.id.received);
        dialog_preparing = set_order_status_dialog.findViewById(R.id.preparing);
        dialog_pickedUp = set_order_status_dialog.findViewById(R.id.pickedup);
        dialog_delivered = set_order_status_dialog.findViewById(R.id.deliveredOrder);
        dialog_confirm = set_order_status_dialog.findViewById(R.id.confirm);
        dialog_status = set_order_status_dialog.findViewById(R.id.status);
        textView_dialog_received = set_order_status_dialog.findViewById(R.id.received_order);
        textView_dialog_preparing = set_order_status_dialog.findViewById(R.id.preparingOrder);
        textView_dialog_pickedUp = set_order_status_dialog.findViewById(R.id.picked_up);
        textView_dialog_delivered = set_order_status_dialog.findViewById(R.id.delivered);

        set_order_status_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                textView_order_status.setTextColor(getResources().getColor(R.color.light_blue));
                view_order_status.setBackgroundColor(getResources().getColor(R.color.light_blue));
            }
        });

        internet_connection_dialog = new Dialog(Orders.this);
        internet_connection_dialog.setContentView(R.layout.dialog_no_internet);
        internet_connection_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        internet_connection_dialog.getWindow().setGravity(Gravity.CENTER);
        internet_connection_dialog.setCancelable(true);
        internet_connection_dialog.getWindow().getAttributes().windowAnimations = R.style.CustomActivityAnimation2;
        internet_connection_dialog.findViewById(R.id.try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected(Orders.this)) {
                    internet_connection_dialog.dismiss();

                }
            }
        });
        dialog_status_setText(order_status);
        dialog_received.setOnClickListener(this::onClick);
        dialog_preparing.setOnClickListener(this::onClick);
        dialog_pickedUp.setOnClickListener(this::onClick);
        dialog_delivered.setOnClickListener(this::onClick);
        dialog_confirm.setOnClickListener(this::onClick);


        email_dialog = new Dialog(Orders.this);
        email_dialog.setContentView(R.layout.dialog_email);
        email_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        email_dialog.getWindow().setGravity(Gravity.CENTER);
        email_dialog.setCancelable(false);
        email_dialog.getWindow().getAttributes().windowAnimations = R.style.CustomActivityAnimation2;

        FROM = email_dialog.findViewById(R.id.from);
        TO = email_dialog.findViewById(R.id.to);
        EMAIL = email_dialog.findViewById(R.id.dialog_email);
        SUBJECT = email_dialog.findViewById(R.id.subject);

        email_dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_dialog.dismiss();
            }
        });
        email_dialog.findViewById(R.id.sendEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("mailto:?subject=" + SUBJECT.getText().toString().trim() + "&body=" + EMAIL.getText().toString().trim() + "&to=" + customer_email));
                //Toast.makeText(Orders.this, SUBJECT.getText().toString().trim(), Toast.LENGTH_SHORT).show();


                try {
                    startActivity(Intent.createChooser(intent, "choose one application"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Orders.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
                email_dialog.dismiss();

            }
        });
        findViewById(R.id.toolbar).findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Orders.this);
                    builder.setTitle("Logout");
                    builder.setCancelable(false);
                    builder.setMessage("Are you sure you want to logout?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                FirebaseAuth.getInstance().signOut();
                                stopService(new Intent(Orders.this, notificationService.class));
                                dialog.dismiss();

                                Toast.makeText(Orders.this, "logged out", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Orders.this, Login.class);
                                intent.putExtra("phone", phone);
                                intent.putExtra("username", username);
                                intent.putExtra("email", email);
                                intent.putExtra("id", id);
                                intent.putExtra("uid", uid);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                                startActivity(intent);
                                finish();

                            }

                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();


                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    negativeButton.setTextColor(getResources().getColor(R.color.green));
                    positiveButton.setTextColor(getResources().getColor(R.color.green));
                }

            }
        });

        findViewById(R.id.toolbar).findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                close();
            }
        });

        place_order_reference = FirebaseDatabase.getInstance().getReference("placed_order");
        listener = place_order_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("orders").hasChild(order_id)) {
                    placed_order_key = snapshot.child("orders").child(order_id).child("placed_order_confirmed_key").getValue().toString();
                    user_key = snapshot.child("orders").child(order_id).child("user_order_confirmed_key").getValue().toString();

                    customer_name = Objects.requireNonNull(snapshot.child("orders").child(order_id).child("user_info").child("customer_name").getValue()).toString();
                    textView_name.setText(customer_name);
                    customer_phone = Objects.requireNonNull(snapshot.child("orders").child(order_id).child("user_info").child("customer_phone").getValue()).toString();
                    textView_phone.setText("+88" + customer_phone);
                    customer_email = Objects.requireNonNull(snapshot.child("orders").child(order_id).child("user_info").child("customer_email").getValue()).toString();
                    textView_email.setText(customer_email);
                    TO.setText(customer_email);
                    customer_uid = Objects.requireNonNull(snapshot.child("orders").child(order_id).child("user_info").child("customer_uid").getValue()).toString();

                    user_reference = FirebaseDatabase.getInstance().getReference().child("users").child(customer_uid);

                    if (delivery_type.equals("home_delivery")) {

                        if (snapshot.child("orders").child(order_id).child("user_info").hasChild("deliver_location")) {
                            customer_address = Objects.requireNonNull(snapshot.child("orders").child(order_id).child("user_info").child("deliver_location").getValue()).toString();
                            if (snapshot.child("orders").child(order_id).child("user_info").hasChild("full_address")) {
                                customer_full_address = Objects.requireNonNull(snapshot.child("orders").child(order_id).child("user_info").child("full_address").getValue()).toString();
                                textView_address.setText(customer_full_address);
                                destination = customer_full_address;

                            } else {
                                textView_address.setText(customer_address);
                                destination = customer_address;
                            }
                            // Toast.makeText(Orders.this, customer_full_address + " : address", Toast.LENGTH_LONG).show();
                        }

                        ///////////////////////////////////////
                        if (snapshot.child("orders").child(order_id).child("user_info").hasChild("area")) {
                            customer_street = snapshot.child("orders").child(order_id).child("user_info").child("area").getValue().toString();
                            textView_street_details.setText(customer_street);
                            if (snapshot.child("orders").child(order_id).child("user_info").hasChild("street")) {
                                customer_street = snapshot.child("orders").child(order_id).child("user_info").child("area").getValue().toString() + "," +
                                        snapshot.child("orders").child(order_id).child("user_info").child("street").getValue().toString();
                                textView_street_details.setText(customer_street);
                            }

                        } else {
                            if (snapshot.child("orders").child(order_id).child("user_info").hasChild("street")) {
                                customer_street = snapshot.child("orders").child(order_id).child("user_info").child("street").getValue().toString();
                                textView_street_details.setText(customer_street);
                            } else {
                                street_layout.setVisibility(View.GONE);
                            }
                        }
                        ////////////////////////////////////
                        if (snapshot.child("orders").child(order_id).child("user_info").hasChild("deliver_road_no")) {
                            customer_road = snapshot.child("orders").child(order_id).child("user_info").child("deliver_road_no").getValue().toString();
                            textView_road_no.setText(customer_road);
                        } else {
                            road_layout.setVisibility(View.GONE);
                        }
                        if (snapshot.child("orders").child(order_id).child("user_info").hasChild("landmark")) {
                            customer_landmark = snapshot.child("orders").child(order_id).child("user_info").child("landmark").getValue().toString();
                            textView_landmark.setText(customer_landmark);
                        } else {
                            landmark_layout.setVisibility(View.GONE);
                        }
                        if (snapshot.child("orders").child(order_id).child("user_info").hasChild("deliver_apartment")) {
                            customer_apartment = snapshot.child("orders").child(order_id).child("user_info").child("deliver_apartment").getValue().toString();
                            textView_apartment.setText(customer_apartment);
                        } else {
                            apartment_layout.setVisibility(View.GONE);
                        }

                    } else {
                        findViewById(R.id.deliver_address1).setVisibility(View.GONE);
                        findViewById(R.id.deliver_address2).setVisibility(View.GONE);
                    }
                    if (delivery_type.equals("take_away")) {
                        layout_takeaway.setVisibility(View.VISIBLE);
                        if (snapshot.child("orders").child(order_id).hasChild("pickup_in")) {
                            pickUp_time = Objects.requireNonNull(snapshot.child("orders").child(order_id).child("pickup_in").getValue()).toString();
                            textView_pickUp_time.setText(pickUp_time + " mins");
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        title = "Warning!!!";
        body = "Notify customer about the changes";
        toolbar_name.setText(username);


        query = FirebaseDatabase.getInstance().getReference().child("placed_order").child("orders").child(order_id).child("food");
        options = new FirebaseRecyclerOptions.Builder<orders_dataset>().setQuery(query, orders_dataset.class).build();
        firebaseAdapter = new FirebaseRecyclerAdapter<orders_dataset, FirebaseViewHolder1>(options) {
            @NonNull
            @Override
            public FirebaseViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(Orders.this)
                        .inflate(R.layout.row_order_layout, parent, false);
                FirebaseViewHolder1 firebaseViewHolder = new FirebaseViewHolder1(view);
                return firebaseViewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolder1 holder, int position, @NonNull orders_dataset model) {
                extra_shot_amount = model.getExtra_shot_amount();
                extra_shot_price = model.getExtra_shot_price();
                iced_amount = model.getIced_amount();
                iced_price = model.getIced_price();
                food_name = model.getFood_name();
                food_price = model.getFood_price();
                food_amount = model.getFood_amount();
                food_type = model.food_type;
                instructions = model.getInstructions();
                layout = model.getLayout();

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

                    holder.quantity.setText(String.valueOf(model.getFood_amount()) + "X");
                } catch (Exception e) {
                    Toast.makeText(Orders.this, "error amount ", Toast.LENGTH_LONG).show();
                }
            }
        };
        try {
            firebaseAdapter.startListening();

            recyclerView.setLayoutManager(new LinearLayoutManager(Orders.this));
            recyclerView.setAdapter(firebaseAdapter);
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);

        } catch (Exception e) {
            Toast.makeText(Orders.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }

        message.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Drawable drawable = message.getCompoundDrawables()[2];
                if (drawable == null) {
                    return false;
                }
                //drawleft is less than, drawright is greater than
                //The left, right, up and down correspond to 0 1 2 3
                if (event.getX() > message.getWidth() - message.getCompoundDrawables()[2].getBounds().width()) {

                    //Event executed after click
                    // Toast.makeText(Orders.this, "The drawright on the right was clicked", Toast.LENGTH_SHORT).show();
                    message.setText("");

                    //                    Intent intent =new Intent(DrawableClickActivity.this,CountDownActivity.class);
                    //                    startActivity(intent);
                    return false;
                }
                return false;
            }
        });


        menuBuilder = new MenuBuilder(Orders.this);
        menuInflater = new MenuInflater(Orders.this);
        menuInflater.inflate(R.menu.popup_menu, menuBuilder);
        findViewById(R.id.toolbar).findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {

            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View v) {
                @SuppressLint("RestrictedApi") MenuPopupHelper menuPopupHelper = new MenuPopupHelper(Orders.this, menuBuilder, v);
                menuPopupHelper.setGravity(Gravity.RIGHT);
                menuPopupHelper.setForceShowIcon(true);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.account:
                                intent = new Intent(Orders.this, profile.class);
                                intent.putExtra("class", "Orders");
                                intent.putExtra("order_id", order_id);
                                intent.putExtra("phone", phone);
                                intent.putExtra("username", username);
                                intent.putExtra("email", email);
                                intent.putExtra("uid", uid);
                                intent.putExtra("id", id);
                                intent.putExtra("branch", branch);
                                intent.putExtra("deliver_type", delivery_type);
                                intent.putExtra("token", token);
                                intent.putExtra("order_status", order_status);
                                startActivity(intent);
                                overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                                finish();
                                return true;

                            case R.id.dashboard:
                                intent = new Intent(Orders.this, Dashboard.class);
                                intent.putExtra("class", "Orders");
                                intent.putExtra("order_id", order_id);
                                intent.putExtra("phone", phone);
                                intent.putExtra("username", username);
                                intent.putExtra("email", email);
                                intent.putExtra("uid", uid);
                                intent.putExtra("id", id);
                                intent.putExtra("branch", branch);
                                intent.putExtra("deliver_type", delivery_type);
                                intent.putExtra("token", token);

                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
                                return true;

                            case R.id.change_password:
                                intent = new Intent(Orders.this, forgotPassword.class);
                                intent.putExtra("class", "Orders");
                                intent.putExtra("order_id", order_id);
                                intent.putExtra("phone", phone);
                                intent.putExtra("username", username);
                                intent.putExtra("email", email);
                                intent.putExtra("uid", uid);
                                intent.putExtra("id", id);
                                intent.putExtra("branch", branch);
                                intent.putExtra("deliver_type", delivery_type);
                                intent.putExtra("token", token);
                                intent.putExtra("from_other_activity", true);
                                intent.putExtra("order_status", order_status);

                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
                                return true;
                            default:
                                return false;


                        }
                    }

                    @Override
                    public void onMenuModeChange(@NonNull MenuBuilder menu) {

                    }
                });
                menuPopupHelper.show();
            }
        });

        drop_down1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c1 == 0) {
                    user_info.setVisibility(View.VISIBLE);
                    c1 = 1;
                    drop_down1.setImageResource(R.drawable.ic_sharp_arrow_drop_down);
                } else {
                    c1 = 0;
                    user_info.setVisibility(View.GONE);
                    drop_down1.setImageResource(R.drawable.ic_sharp_arrow_drop_up_24);
                }
            }
        });

        drop_down2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cancel) {
                    if (c2 == 0) {
                        order_layout.setVisibility(View.VISIBLE);
                        c2 = 1;
                        drop_down2.setImageResource(R.drawable.ic_sharp_arrow_drop_down);
                    } else {
                        c2 = 0;
                        order_layout.setVisibility(View.GONE);
                        drop_down2.setImageResource(R.drawable.ic_sharp_arrow_drop_up_24);
                    }
                }
            }
        });
        send_message.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        messageSend();
                    } else {
                        ActivityCompat.requestPermissions(Orders.this,
                                new String[]{Manifest.permission.SEND_SMS},
                                100);

                    }
                }
            }


        });

        call_now.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // call(v);
                AlertDialog.Builder builder = new AlertDialog.Builder(Orders.this);
                builder.setCancelable(true);
                builder.setMessage("Call +88" + customer_phone);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int permissionCheck = ContextCompat.checkSelfPermission(Orders.this, Manifest.permission.CALL_PHONE);

                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                    Orders.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    123);
                        } else {
                            if (!customer_phone.equals("")) {
                                String s = "tel:" + "+88" + customer_phone;
                                Intent i1 = new Intent(Intent.ACTION_CALL);
                                i1.setData(Uri.parse(s));
                                dialog.dismiss();
                                startActivity(i1);
                            }
                        }


                    }


                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();


                    }
                });
                // builder.create();
                AlertDialog dialog = builder.create();
                dialog.show();
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setTextColor(getResources().getColor(R.color.green));
                positiveButton.setTextColor(getResources().getColor(R.color.green));

            }
        });


        email_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email_dialog.show();

            }
        });

        layout_order_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cancel) {
                    set_order_status_dialog.show();
                    ORDER_STATUS = order_status;
                    dialog_status_setText(ORDER_STATUS);
                    textView_order_status.setTextColor(getResources().getColor(R.color.blue));
                    view_order_status.setBackgroundColor(getResources().getColor(R.color.blue));
                } else {
                    Toast.makeText(Orders.this, "Order is cancelled", Toast.LENGTH_LONG).show();
                }
            }

        });

        linearLayout_cancel_order.setOnClickListener(this::onClick);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this);
        //check permission


        see_on_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(Orders.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // when permission granted


                    setMap();
                    // init();
                } else {
                    ActivityCompat.requestPermissions(Orders.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                    //  Toast.makeText(Orders.this, "try again", Toast.LENGTH_LONG).show();
                }

                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocationName(destination, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                address1 = addresses.get(0);
                if (addresses.size() > 0) {
                    destination_latLng = new LatLng(address1.getLatitude(), address1.getLongitude());
                }


                if (ActivityCompat.checkSelfPermission(Orders.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // when permission granted
                    setMap();
                    if (admin_current_address == null || admin_current_address.equals("") || destination == null || destination.equals("")) {

                    } else {
                        displayTrack(admin_current_address, destination, start_latLang, destination_latLng);
                    }

                } else {

                    ActivityCompat.requestPermissions(Orders.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

                }

            }
        });


    }


    private void close() {
        Intent intent = new Intent(Orders.this, Check_orders.class);
        intent.putExtra("phone", phone);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("uid", uid);
        intent.putExtra("id", id);
        intent.putExtra("branch", branch);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        ;
        finish();
    }

    private void displayTrack(String admin_current_address, String destination, LatLng start_latLang, LatLng destination_latLng) {
        try {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + start_latLang.latitude + "," + start_latLang.longitude + "&daddr=" + destination_latLng.latitude
                    + "," + destination_latLng.longitude));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(Orders.this, "map error" + e.getMessage(), Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            i = new Intent(Intent.ACTION_VIEW, uri);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

    }

    private void setMap() {

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
                        Geocoder geocoder = new Geocoder(Orders.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        //  Toast .makeText(home_delivery.this, Html.fromHtml(addresses.get(0).getAddressLine(0)+"  "+addresses.get(0).getFeatureName()), Toast.LENGTH_LONG).show();
                        start_latLang = new LatLng(location.getLatitude(), location.getLongitude());
                        String loc = addresses.get(0).getSubLocality() + "," + addresses.get(0).getAddressLine(0);
                        if (addresses.get(0).getAddressLine(1) != null) {
                            loc = loc + "," + addresses.get(0).getAddressLine(1);
                        }
                        admin_current_address = loc;


                    } else {
                        Toast.makeText(Orders.this, "can not find your current location, check your location service ", Toast.LENGTH_LONG).show();

                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                // super.onLocationResult(locationResult);
                                Toast.makeText(Orders.this, "call back ", Toast.LENGTH_LONG).show();
                                Location location1 = locationResult.getLastLocation();

                                try {
                                    Geocoder geocoder = new Geocoder(Orders.this, Locale.getDefault());

                                    List<Address> addresses = geocoder.getFromLocation(location1.getLatitude(), location1.getLongitude(), 1);
                                    String loc = addresses.get(0).getSubLocality() + "," + addresses.get(0).getAddressLine(0);
                                    if (addresses.get(0).getAddressLine(1) != null) {
                                        loc = loc + "," + addresses.get(0).getAddressLine(1);
                                    }

                                    admin_current_address = loc;
                                    //  currentLocation.setText(Html.fromHtml(loc));
                                } catch (IOException e) {
                                    //  Toast.makeText(Orders.this, "getcurrent location   currentlocation=loc error", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }


                            }
                        };

                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                    }


                } catch (Exception e) {
                    //Toast.makeText(Orders.this, "get current location   error", Toast.LENGTH_LONG).show();

                    e.printStackTrace();
                }

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void messageSend() {

        try {

            if (isSimExists()) {
                STRING = message.getText().toString();
                if (!phone.equals("") && !STRING.equals("")) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("01911770785", null, STRING, null, null);
                    Toast.makeText(Orders.this, "Message sent successfully", Toast.LENGTH_LONG).show();
                    message_layout.setVisibility(View.GONE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                            findViewById(R.id.scrollView).getLayoutParams();
                    params.weight = 1.9f;
                    findViewById(R.id.scrollView).setLayoutParams(params);
                } else {
                    Toast.makeText(Orders.this, "Enter value first ", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(Orders.this, "Failed to send message", Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Orders.this, " Permission Granted ", Toast.LENGTH_LONG).show();
                messageSend();
            }

        } else {
            if (requestCode == 44 && grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Orders.this, " MAP Permission Granted ", Toast.LENGTH_LONG).show();
                    setMap();
                    setMap();
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocationName(destination, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    address1 = addresses.get(0);
                    if (addresses.size() > 0) {
                        destination_latLng = new LatLng(address1.getLatitude(), address1.getLongitude());
                    }
                    setMap();
                    if (admin_current_address == null || admin_current_address.equals("") || destination == null || destination.equals("")) {

                    } else {
                        displayTrack(admin_current_address, destination, start_latLang, destination_latLng);
                    }

                }
            }

        }


    }

    public boolean isSimExists() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int SIM_STATE = telephonyManager.getSimState();

        if (SIM_STATE == TelephonyManager.SIM_STATE_READY) {
            // Toast.makeText(Orders.this, " Sim Found!", Toast.LENGTH_LONG).show();

            return true;
        } else {
            switch (SIM_STATE) {
                case TelephonyManager.SIM_STATE_ABSENT: //SimState = "No Sim Found!";
                    Toast.makeText(Orders.this, "No Sim Found!", Toast.LENGTH_LONG).show();
                    break;
                case TelephonyManager.SIM_STATE_NETWORK_LOCKED: //SimState = "Network Locked!";
                    Toast.makeText(Orders.this, "Network Locked!", Toast.LENGTH_LONG).show();

                    break;
                case TelephonyManager.SIM_STATE_PIN_REQUIRED: //SimState = "PIN Required to access SIM!";
                    Toast.makeText(Orders.this, "PIN Required to access SIM!", Toast.LENGTH_LONG).show();

                    break;
                case TelephonyManager.SIM_STATE_PUK_REQUIRED: //SimState = "PUK Required to access SIM!"; // Personal Unblocking Code
                    Toast.makeText(Orders.this, "PUK Required to access SIM!", Toast.LENGTH_LONG).show();

                    break;
                case TelephonyManager.SIM_STATE_UNKNOWN: //SimState = "Unknown SIM State!";
                    Toast.makeText(Orders.this, "Unknown SIM State!", Toast.LENGTH_LONG).show();

                    break;
            }
            return false;
        }
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            if (direction == ItemTouchHelper.LEFT) {
                if (ORDER_STATUS.equals("processing") || ORDER_STATUS.equals("picked_up") || ORDER_STATUS.equals("delivered")) {
                    Toast.makeText(Orders.this, "Order can not be changed now", Toast.LENGTH_LONG).show();
                } else {
                    if (isConnected(Orders.this)) {
                        STRING = "Dear " + customer_name + ", Your order #" + token + " from elCafe has been changed. Visit your elCafe app to see the changes.";
                        message.setText(STRING);
                        message_layout.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                                findViewById(R.id.scrollView).getLayoutParams();
                        params.weight = 1.4f;
                        findViewById(R.id.scrollView).setLayoutParams(params);
                        adapter_reference = firebaseAdapter.getRef(viewHolder.getAdapterPosition());

                        listener1 = adapter_reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //  message_layout.setVisibility(View.VISIBLE);
                                listener = place_order_reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                        pricetotal = Integer.parseInt(snapshot1.child("orders").child(order_id).child("total_price").getValue().toString());
                                        if (snapshot1.child("orders").child(order_id).hasChild("final_price")) {
                                            pricefinal = Integer.parseInt(snapshot1.child("orders").child(order_id).child("final_price").getValue().toString());
                                            delivery_fee = Integer.parseInt(snapshot1.child("orders").child(order_id).child("Delivery_fee").getValue().toString());
                                        }


                                        placed_order_key = snapshot1.child("orders").child(order_id).child("placed_order_confirmed_key").getValue().toString();
                                        user_key = snapshot1.child("orders").child(order_id).child("user_order_confirmed_key").getValue().toString();
                                        user_reference = FirebaseDatabase.getInstance().getReference().child("users").child(customer_uid);

                                        // Toast.makeText(Orders.this, customer_uid + "  test1  " + order_id + " " + user_reference, Toast.LENGTH_LONG).show();
                                        if (snapshot.child("layout").getValue().toString().equals("1")) {

                                            STRING = snapshot.child("food_name").getValue().toString() + "(" + snapshot.child("food_type").getValue().toString() + ", extra_shot_amount=" + snapshot.child("extra_shot_amount").getValue().toString()
                                                    + " ,iced_amount= " + snapshot.child("iced_amount").getValue().toString() + ")";


                                            FOOD_AMOUNT = Integer.parseInt(snapshot.child("food_amount").getValue().toString());
                                            FOOD_PRICE = Integer.parseInt(snapshot.child("food_price").getValue().toString());
                                            ICED_AMOUNT = Integer.parseInt(snapshot.child("iced_amount").getValue().toString());
                                            EXTRA_SHOT_AMOUNT = Integer.parseInt(snapshot.child("extra_shot_amount").getValue().toString());
                                            calculation = (FOOD_PRICE * FOOD_AMOUNT) + (ICED_AMOUNT * FOOD_AMOUNT * 25) + (EXTRA_SHOT_AMOUNT * FOOD_AMOUNT * 35);
                                            pricetotal = pricetotal - calculation;
                                            if (delivery_type.equals("home_delivery")) {
                                                pricefinal = pricetotal + delivery_fee;
                                            }

                                        } else {
                                            STRING = snapshot.child("food_name").getValue().toString();
                                            FOOD_AMOUNT = Integer.parseInt(snapshot.child("food_amount").getValue().toString());
                                            calculation = (FOOD_PRICE * FOOD_AMOUNT);
                                            pricetotal = pricetotal - calculation;
                                            if (delivery_type.equals("home_delivery")) {
                                                pricefinal = pricetotal + delivery_fee;
                                            }

                                        }
///////////////###################################################?//////////////////////////////////////////////
                                        if (pricetotal == 0) {
                                            place_order_reference.child("orders").child(order_id).removeValue();
                                            place_order_reference.child(branch).child(delivery_type).child(placed_order_key).removeValue();
                                            user_reference.child("confirmed_orders").child(user_key).removeValue();
                                            user_reference.child("order_history").child(order_id).removeValue();
                                            cancel = true;
                                           /* Toast.makeText(Orders.this, "price total is 0 " + place_order_reference.child(branch).child(delivery_type).child(placed_order_key).toString()
                                                    + user_reference.child("confirmed_orders").child(user_key).toString(), Toast.LENGTH_LONG).show();*/
                                        } else {
                                            user_reference.child("order_history").child(order_id).child("food").child(STRING).removeValue();
                                            place_order_reference.child("orders").child(order_id).child("total_price").setValue(pricetotal);
                                            user_reference.child("order_history").child(order_id).child("total_price").setValue(pricetotal);

                                            // Toast.makeText(Orders.this, delivery_type + placed_order_key + " " + user_key, Toast.LENGTH_LONG).show();

                                            place_order_reference.child(branch).child(delivery_type).child(placed_order_key).child("total_price").setValue(pricetotal);
                                            user_reference.child("confirmed_orders").child(user_key).child("total_price").setValue(pricetotal);
                                            if (delivery_type.equals("home_delivery")) {
                                                place_order_reference.child("orders").child(order_id).child("final_price").setValue(pricefinal);
                                                user_reference.child("order_history").child(order_id).child("final_price").setValue(pricefinal);
                                                place_order_reference.child(branch).child(delivery_type).child(placed_order_key).child("final_price").setValue(pricefinal);
                                                user_reference.child("confirmed_orders").child(user_key).child("final_price").setValue(pricefinal);
                                                //  Toast.makeText(Orders.this, pricefinal + " price final ", Toast.LENGTH_LONG).show();

                                            }
                                            adapter_reference.removeValue();
                                        }
                                        // Toast.makeText(Orders.this, STRING + "" + snapshot.child("layout").getValue().toString() + "  price " + pricetotal + " final " + pricefinal, Toast.LENGTH_LONG).show();
                                        if (adapter_reference != null && listener1 != null) {
                                            adapter_reference.removeEventListener(listener1);
                                            //  Toast.makeText(Orders.this, "removed value event listener ", Toast.LENGTH_LONG).show();

                                            try {
                                                notification();
                                            } catch (Exception e) {
                                                Toast.makeText(Orders.this, "notification error " + e.getMessage(), Toast.LENGTH_LONG).show();

                                            }
                                        }
                                        if (place_order_reference != null && listener != null) {
                                            place_order_reference.removeEventListener(listener);
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    } else {
                        internet_connection_dialog.show();
                    }
                }


            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(Orders.this, R.color.yellow))
                    .addSwipeLeftLabel("Delete")
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Warning!!", "Warning", NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(Orders.this, "Warning!!");
        builder.setContentTitle("Warning!!");
        builder.setContentText("Notify customer about the order changes");
        builder.setSmallIcon(R.drawable.ic_sharp_warning_24);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setAutoCancel(true);


        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(Orders.this);
        managerCompat.notify(1, builder.build());
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (adapter_reference != null && listener1 != null) {
            adapter_reference.removeEventListener(listener1);
        }
        if (place_order_reference != null && listener != null) {
            place_order_reference.removeEventListener(listener);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        close();
    }

    @Override
    public void onClick(View v) {
        if (isConnected(Orders.this)) {
            switch (v.getId()) {
                case R.id.received:
                    ORDER_STATUS = "confirmed";
                    dialog_status_setText("confirmed");
                    break;
                case R.id.preparing:
                    ORDER_STATUS = "processing";
                    dialog_status_setText("processing");

                    break;
                case R.id.pickedup:
                    ORDER_STATUS = "picked_up";
                    dialog_status_setText("picked_up");

                    break;
                case R.id.deliveredOrder:
                    ORDER_STATUS = "delivered";
                    dialog_status_setText("delivered");
                    break;
                case R.id.confirm:
                    delivered_push_reference = place_order_reference.child("delivered_orders").push();
                    currentDate = new SimpleDateFormat("EEE, d MMM, yyyy", Locale.getDefault()).format(new Date());
                    currentTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
                    user_reference.child("confirmed_orders").child(user_key).child("status").setValue(ORDER_STATUS);
                    user_reference.child("order_history").child(order_id).child("status").setValue(ORDER_STATUS);
                    place_order_reference.child("orders").child(order_id).child("status").setValue(ORDER_STATUS);

                    if (ORDER_STATUS.equals("delivered")) {
                        place_order_reference.child("confirmed_orders").child(branch).child(delivery_type).child(placed_order_key).removeValue();
                        delivered_push_reference.child(order_id).setValue(order_id);
                        delivered_push_reference.child(token).setValue(token);
                        delivered_push_reference.child("DATE").setValue(currentDate);
                        delivered_push_reference.child("TIME").setValue(currentTime);
                    } else {
                        place_order_reference.child("confirmed_orders").child(branch).child(delivery_type).child(placed_order_key).child("status").setValue(ORDER_STATUS);
                    }
                    order_status = ORDER_STATUS;
                    set_order_status_dialog.dismiss();

                    textView_order_status.setTextColor(getResources().getColor(R.color.light_blue));
                    view_order_status.setBackgroundColor(getResources().getColor(R.color.light_blue));
                    break;
                case R.id.cancel_order:
                    if (!cancel) {
                        if (ORDER_STATUS.equals("processing") || ORDER_STATUS.equals("picked_up") || ORDER_STATUS.equals("delivered")) {
                            Toast.makeText(Orders.this, "Order can not be cancelled now", Toast.LENGTH_LONG).show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Orders.this);
                            builder.setTitle("Cancel Order");
                            builder.setCancelable(false);
                            builder.setMessage("Are you sure you want to cancel the order?");
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    ORDER_STATUS = "cancel";
                                    currentDate = new SimpleDateFormat("EEE, d MMM, yyyy", Locale.getDefault()).format(new Date());
                                    currentTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
                                    push_reference = place_order_reference.child("canceled_orders").push();
                                    place_order_reference.child("confirmed_orders").child(branch).child(delivery_type).child(placed_order_key).removeValue();
                                    place_order_reference.child("orders").child(order_id).child("status").setValue("canceled");
                                    push_reference.child(order_id).setValue(order_id);
                                    push_reference.child(token).setValue(token);
                                    push_reference.child("DATE").setValue(currentDate);
                                    push_reference.child("TIME").setValue(currentTime);
                                    user_reference.child("confirmed_orders").child(user_key).child("status").setValue(ORDER_STATUS);
                                    user_reference.child("order_history").child(order_id).child("status").setValue(ORDER_STATUS);
                                    STRING = "Dear " + customer_name + ", Your order #" + token + " from elCafe has been canceled.";
                                    order_layout.setVisibility(View.GONE);
                                    order_dropdown.setVisibility(View.GONE);
                                    STRING = "Dear " + customer_name + ", Your order #" + token + " from elCafe has been canceled.";
                                    message.setText(STRING);
                                    message_layout.setVisibility(View.VISIBLE);
                                    cancel = true;
                                    notification();
                                    Toast.makeText(Orders.this, "Order cancelled successfully", Toast.LENGTH_LONG).show();
                                }
                            });
                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                            negativeButton.setTextColor(getResources().getColor(R.color.green));
                            positiveButton.setTextColor(getResources().getColor(R.color.green));
                        }
                    } else {
                        Toast.makeText(Orders.this, "Already cancelled order", Toast.LENGTH_LONG).show();
                    }

                    break;

            }

        } else {
            internet_connection_dialog.show();
        }

    }

    private void dialog_status_setText(String order_status) {

        if (order_status.equals("confirmed")) {
            status = "Order has been received";
            textView_dialog_received.setTypeface(null, Typeface.BOLD);
            textView_dialog_preparing.setTypeface(null, Typeface.NORMAL);
            textView_dialog_pickedUp.setTypeface(null, Typeface.NORMAL);
            textView_dialog_delivered.setTypeface(null, Typeface.NORMAL);
        } else {
            if (order_status.equals("picked_up")) {
                status = "Order has been picked up";
                textView_dialog_received.setTypeface(null, Typeface.NORMAL);
                textView_dialog_preparing.setTypeface(null, Typeface.NORMAL);
                textView_dialog_pickedUp.setTypeface(null, Typeface.BOLD);
                textView_dialog_delivered.setTypeface(null, Typeface.NORMAL);

            } else {
                if (order_status.equals("processing")) {
                    status = "Order is being prepare";
                    textView_dialog_received.setTypeface(null, Typeface.NORMAL);
                    textView_dialog_preparing.setTypeface(null, Typeface.BOLD);
                    textView_dialog_pickedUp.setTypeface(null, Typeface.NORMAL);
                    textView_dialog_delivered.setTypeface(null, Typeface.NORMAL);
                } else {
                    if (order_status.equals("delivered")) {
                        status = "Order has been delivered";
                        textView_dialog_received.setTypeface(null, Typeface.NORMAL);
                        textView_dialog_preparing.setTypeface(null, Typeface.NORMAL);
                        textView_dialog_pickedUp.setTypeface(null, Typeface.NORMAL);
                        textView_dialog_delivered.setTypeface(null, Typeface.BOLD);
                    }
                }
            }
        }
        dialog_status.setText(status);
    }

    private boolean isConnected(Context context) {
        boolean RETURN = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi_connection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile_connection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi_connection != null && wifi_connection.isConnected()) || (mobile_connection != null && mobile_connection.isConnected())) {
            if (internet_connection_dialog.isShowing()) {
                internet_connection_dialog.dismiss();
                // layout_progress.setVisibility(View.GONE);
            }
            // Toast.makeText(Orders.this, "secure connection", Toast.LENGTH_LONG).show();

            RETURN = true;
        } else {
            Toast.makeText(Orders.this, "no connection", Toast.LENGTH_LONG).show();
            /*if (!internet_connection_dialog.isShowing()) {
                internet_connection_dialog.show();
            }*/

            RETURN = false;
        }
        return RETURN;
    }

}