package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Check_orders extends AppCompatActivity {

    String phone, id, email, username, uid, branch;
    TextView toolbar_name, branch_name;
    ValueEventListener valueEventListener;
    Intent i;
    MenuInflater menuInflater, inflater;
    MenuBuilder menuBuilder, Builder;

    TextView status, change;
    String changed_status = "";
    String firebase_branch_status;
    RecyclerView recyclerView1, recyclerView2, recyclerView3;
    int change_status_color;

    int c1, c2, c3 = 0;
    String order_id, STATUS, delivery;
    ImageView back, drop_down, drop_down1, drop_down2;
    DatabaseReference placed_order_reference;
    Intent intent;
    Dialog internet_connection_dialog;

    LinearLayout layout, layout1, layout2;
    LinearLayoutManager layoutManager, layoutManager1, layoutManager2;
    FirebaseRecyclerOptions<check_order_dataset> options, options1, options2;
    Query query, query1, query2;
    FirebaseRecyclerAdapter<check_order_dataset, FirebaseViewHolder> firebaseAdapter, firebaseAdapter2, firebaseAdapter3;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_check_orders);

        phone = getIntent().getStringExtra("phone");
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        uid = getIntent().getStringExtra("uid");
        branch = getIntent().getStringExtra("branch");

        back = findViewById(R.id.toolbar).findViewById(R.id.back);
        toolbar_name = findViewById(R.id.toolbar).findViewById(R.id.name);
        status = findViewById(R.id.status);
        change = findViewById(R.id.change_status);
        branch_name = findViewById(R.id.branch);
        drop_down = findViewById(R.id.drop_down1);
        drop_down1 = findViewById(R.id.drop_down2);
        drop_down2 = findViewById(R.id.drop_down3);
        recyclerView1 = findViewById(R.id.firebase_recycler1);
        recyclerView2 = findViewById(R.id.firebase_recycler2);
        recyclerView3 = findViewById(R.id.firebase_recycler3);
        layout = findViewById(R.id.layout);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);

        internet_connection_dialog = new Dialog(Check_orders.this);
        internet_connection_dialog.setContentView(R.layout.dialog_no_internet);
        internet_connection_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        internet_connection_dialog.getWindow().setGravity(Gravity.CENTER);
        internet_connection_dialog.setCancelable(true);
        internet_connection_dialog.getWindow().getAttributes().windowAnimations = R.style.CustomActivityAnimation2;
        internet_connection_dialog.findViewById(R.id.try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected(Check_orders.this)) {
                    internet_connection_dialog.dismiss();

                }
            }
        });

        toolbar_name.setText(username);
        branch_name.setText(branch + " Branch");

        layoutManager = new LinearLayoutManager(Check_orders.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        layoutManager1 = new LinearLayoutManager(Check_orders.this);
        layoutManager1.setReverseLayout(true);
        layoutManager1.setStackFromEnd(true);

        layoutManager2 = new LinearLayoutManager(Check_orders.this);
        layoutManager2.setReverseLayout(true);
        layoutManager2.setStackFromEnd(true);
        query = FirebaseDatabase.getInstance().getReference().child("placed_order").child("confirmed_orders").child(branch).child("home_delivery");
        options = new FirebaseRecyclerOptions.Builder<check_order_dataset>().setQuery(query, check_order_dataset.class).build();

        try {
            firebaseAdapter = new FirebaseRecyclerAdapter<check_order_dataset, FirebaseViewHolder>(options) {
                @NonNull
                @Override
                public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(Check_orders.this)
                            .inflate(R.layout.row_check_order_recycler, parent, false);
                    FirebaseViewHolder firebaseViewHolder = new FirebaseViewHolder(view);
                    return firebaseViewHolder;
                }

                @Override
                protected void onBindViewHolder(@NonNull FirebaseViewHolder holder, int position, @NonNull check_order_dataset model) {

                    if (model.getDelivery().equals("home_delivery")) {
                        setHolder(holder, model);
                    }
                }
            };
        } catch (Exception e) {
            Toast.makeText(Check_orders.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }

        try {
            firebaseAdapter.startListening();
            recyclerView1.setLayoutManager(layoutManager);
            recyclerView1.setAdapter(firebaseAdapter);
        } catch (Exception e) {
            Toast.makeText(Check_orders.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }


        query1 = FirebaseDatabase.getInstance().getReference().child("placed_order").child("confirmed_orders").child(branch).child("take_away");
        options1 = new FirebaseRecyclerOptions.Builder<check_order_dataset>().setQuery(query1, check_order_dataset.class).build();

        firebaseAdapter2 = new FirebaseRecyclerAdapter<check_order_dataset, FirebaseViewHolder>(options1) {
            @NonNull
            @Override
            public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(Check_orders.this)
                        .inflate(R.layout.row_check_order_recycler, parent, false);
                FirebaseViewHolder firebaseViewHolder = new FirebaseViewHolder(view);
                return firebaseViewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolder holder, int position, @NonNull check_order_dataset model) {
                if (model.getDelivery().equals("take_away")) {
                    setHolder(holder, model);

                }
            }
        };

        try {
            firebaseAdapter2.startListening();
            recyclerView2.setLayoutManager(layoutManager1);
            recyclerView2.setAdapter(firebaseAdapter2);

        } catch (Exception e) {
            Toast.makeText(Check_orders.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }

        try {
            query2 = FirebaseDatabase.getInstance().getReference().child("placed_order").child("confirmed_orders").child(branch).child("dine_in");
            options2 = new FirebaseRecyclerOptions.Builder<check_order_dataset>().setQuery(query2, check_order_dataset.class).build();
            firebaseAdapter3 = new FirebaseRecyclerAdapter<check_order_dataset, FirebaseViewHolder>(options2) {
                @NonNull
                @Override
                public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(Check_orders.this)
                            .inflate(R.layout.row_check_order_recycler, parent, false);
                    return new FirebaseViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull FirebaseViewHolder holder, int position, @NonNull check_order_dataset model) {
                    try {
                        if (model.getDelivery().equals("dine_in")) {

                            setHolder(holder, model);
                        }


                    } catch (Exception e) {
                        Toast.makeText(Check_orders.this, "error1 " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            };

        } catch (Exception e) {

        }
        try {
            firebaseAdapter3.startListening();
            recyclerView3.setLayoutManager(layoutManager2);
            recyclerView3.setAdapter(firebaseAdapter3);

        } catch (Exception e) {
            Toast.makeText(Check_orders.this, "error" + e.getMessage(), Toast.LENGTH_LONG).show();

        }
        placed_order_reference = FirebaseDatabase.getInstance().getReference("placed_order");
        valueEventListener = placed_order_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(branch + "_status")) {
                    firebase_branch_status = snapshot.child("Branch").child(branch + "_status").getValue().toString();
                    if (firebase_branch_status == "free") {
                        status.setTextColor(getResources().getColor(R.color.black));
                        status.setText("Cafe's free at the moment");
                    }
                    if (firebase_branch_status == "rush") {
                        status.setTextColor(getResources().getColor(R.color.black));
                        status.setText("Cafe's a little busy at the moment");
                    }
                    if (firebase_branch_status == "busy") {
                        status.setTextColor(getResources().getColor(R.color.RED));
                        status.setText("Cafe's very busy at the moment");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        findViewById(R.id.toolbar).findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Check_orders.this);
                    builder.setTitle("Logout");
                    builder.setCancelable(false);
                    builder.setMessage("Are you sure you want to logout?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                FirebaseAuth.getInstance().signOut();
                                stopService(new Intent(Check_orders.this,notificationService.class));
                                dialog.dismiss();

                                Toast.makeText(Check_orders.this, "logged out", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Check_orders.this, Login.class);
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
        menuBuilder = new MenuBuilder(Check_orders.this);
        //popup.setOnMenuItemClickListener(Dashboard.this);
        menuInflater = new MenuInflater(Check_orders.this);
        menuInflater.inflate(R.menu.popup_menu, menuBuilder);
        findViewById(R.id.toolbar).findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {

            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View v) {
                MenuPopupHelper menuPopupHelper = new MenuPopupHelper(Check_orders.this, menuBuilder, v);
                menuPopupHelper.setGravity(Gravity.RIGHT);
                menuPopupHelper.setForceShowIcon(true);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.account:
                                intent = new Intent(Check_orders.this, profile.class);
                                intent.putExtra("class", "CheckOrder");
                                intent.putExtra("order_id", order_id);
                                intent.putExtra("phone", phone);
                                intent.putExtra("username", username);
                                intent.putExtra("email", email);
                                intent.putExtra("uid", uid);
                                intent.putExtra("id", id);
                                intent.putExtra("branch", branch);
                                startActivity(intent);
                                overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                                finish();
                                return true;

                            case R.id.dashboard:
                                intent = new Intent(Check_orders.this, Dashboard.class);
                                intent.putExtra("class", "CheckOrder");
                                intent.putExtra("order_id", order_id);
                                intent.putExtra("phone", phone);
                                intent.putExtra("username", username);
                                intent.putExtra("email", email);
                                intent.putExtra("uid", uid);
                                intent.putExtra("id", id);
                                intent.putExtra("branch", branch);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
                                return true;

                            case R.id.change_password:
                                intent = new Intent(Check_orders.this, forgotPassword.class);
                                intent.putExtra("class", "CheckOrder");
                                intent.putExtra("order_id", order_id);
                                intent.putExtra("phone", phone);
                                intent.putExtra("username", username);
                                intent.putExtra("email", email);
                                intent.putExtra("uid", uid);
                                intent.putExtra("id", id);
                                intent.putExtra("branch", branch);
                                intent.putExtra("from_other_activity", true);
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
        Builder = new MenuBuilder(Check_orders.this);
        inflater = new MenuInflater(Check_orders.this);
        inflater.inflate(R.menu.status_popup_menu, Builder);
        change.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View v) {
                change.setTextColor(getResources().getColor(R.color.blue));
                MenuPopupHelper menuPopupHelper = new MenuPopupHelper(Check_orders.this, Builder, v);
                menuPopupHelper.setGravity(Gravity.LEFT);
                menuPopupHelper.setForceShowIcon(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(Check_orders.this);
                builder.setTitle("Change Cafe's Status");
                builder.setCancelable(false);
                builder.setMessage("Are you sure you want to change the status of ElCafe-" + branch + "?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        status.setTextColor(change_status_color);
                        status.setText(changed_status);
                        change.setTextColor(getResources().getColor(R.color.light_blue));
                        placed_order_reference.child("Branch").child(branch + "_status").setValue(firebase_branch_status);

                        dialog.dismiss();
                        Builder.close();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        change.setTextColor(getResources().getColor(R.color.light_blue));
                        dialog.dismiss();
                        Builder.close();
                    }
                });
                menuPopupHelper.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        change.setTextColor(getResources().getColor(R.color.light_blue));
                    }
                });

                Builder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.free:
                                change_status_color = getResources().getColor(R.color.black);
                                changed_status = "Cafe's free at the moment";
                                firebase_branch_status = "free";
                                builder.show();
                                return true;
                            case R.id.busy:
                                change_status_color = getResources().getColor(R.color.black);
                                changed_status = "Cafe's a little busy at the moment";
                                firebase_branch_status = "busy";
                                builder.show();
                                return true;
                            case R.id.rush:
                                change_status_color = getResources().getColor(R.color.RED);
                                changed_status = "Cafe's very busy at the moment";
                                firebase_branch_status = "rush";
                                builder.show();
                                return true;


                        }
                        return false;
                    }

                    @Override
                    public void onMenuModeChange(@NonNull MenuBuilder menu) {

                    }
                });
                menuPopupHelper.show();


            }
        });

        valueEventListener = placed_order_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Branch").hasChild(branch + "_status")) {
                    firebase_branch_status = snapshot.child("Branch").child(branch + "_status").getValue().toString();
                    //Toast.makeText(Check_orders.this, branch + "_status" + firebase_branch_status, Toast.LENGTH_LONG).show();
                    if (firebase_branch_status.equals("free")) {
                        status.setTextColor(getResources().getColor(R.color.black));
                        status.setText("Cafe's free at the moment");
                      //  Toast.makeText(Check_orders.this, "Cafe's free at the moment", Toast.LENGTH_LONG).show();

                    }
                    if (firebase_branch_status.equals("busy")) {
                        status.setTextColor(getResources().getColor(R.color.black));
                        status.setText("Cafe's a little busy at the moment");
                       //// Toast.makeText(Check_orders.this, "Cafe's busy at the moment", Toast.LENGTH_LONG).show();

                    }
                    if (firebase_branch_status.equals("rush")) {
                        status.setTextColor(getResources().getColor(R.color.RED));
                        status.setText("Cafe's very busy at the moment");
                       // Toast.makeText(Check_orders.this, "Cafe's very busy at the moment", Toast.LENGTH_LONG).show();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        try {
            drop_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (c1 == 0) {
                        drop_down.setImageResource(R.drawable.ic_sharp_arrow_drop_down);
                        layout.setVisibility(View.VISIBLE);
                        c1 = 1;
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (!snapshot.exists()) {
                                    Toast.makeText(Check_orders.this, " no order has been placed", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        drop_down.setImageResource(R.drawable.ic_sharp_arrow_drop_up_24);
                        layout.setVisibility(View.GONE);
                        c1 = 0;
                    }
                }
            });

        } catch (Exception e) {
            Toast.makeText(Check_orders.this, "drop down error " + e.getMessage(), Toast.LENGTH_LONG).show();

        }

        drop_down1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c2 == 0) {
                    drop_down1.setImageResource(R.drawable.ic_sharp_arrow_drop_down);
                    layout1.setVisibility(View.VISIBLE);
                    c2 = 1;
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                Toast.makeText(Check_orders.this, " no order has been placed", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    drop_down1.setImageResource(R.drawable.ic_sharp_arrow_drop_up_24);
                    layout1.setVisibility(View.GONE);
                    c2 = 0;
                }
            }
        });
        drop_down2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c3 == 0) {
                    drop_down2.setImageResource(R.drawable.ic_sharp_arrow_drop_down);
                    layout2.setVisibility(View.VISIBLE);
                    c3 = 1;
                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                Toast.makeText(Check_orders.this, " no order has been placed", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    drop_down2.setImageResource(R.drawable.ic_sharp_arrow_drop_up_24);
                    layout2.setVisibility(View.GONE);
                    c3 = 0;
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    private void setHolder(FirebaseViewHolder holder, check_order_dataset model) {
        order_id = model.getOrder_id();
        STATUS = model.getStatus();
        delivery = model.getDelivery();

        holder.order_id.setText(model.getToken());
        holder.date.setText(model.getDate());
        holder.time.setText(model.getTime());
        holder.price.setText("TK " + model.getTotal_price());
        holder.order_id.setText("Order #" + model.getToken());
        if (STATUS.equals("confirmed")) {

            holder.status.setImageResource(R.drawable.ic_yellow_outline_check_circle_24);
        } else {
            if (STATUS.equals("cancel")) {
                holder.status.setImageResource(R.drawable.ic_yellow_outline_cancel_24);
            } else {
                if (STATUS.equals("picked_up")) {
                    holder.status.setImageResource(R.drawable.ic_yellow_baseline_delivery_dining_24);
                } else {
                    if (STATUS.equals("processing")) {
                        holder.status.setImageResource(R.drawable.ic_yellow_baseline_soup_kitchen_24);
                    } else {
                        if (STATUS.equals("delivered")) {
                            holder.status.setImageResource(R.drawable.ic_yellow_sharp_home_24);
                        }
                    }

                }
            }
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(Check_orders.this, Orders.class);
                i.putExtra("phone", phone);
                i.putExtra("username", username);
                i.putExtra("email", email);
                i.putExtra("id", id);
                i.putExtra("uid", uid);
                i.putExtra("branch", branch);
                i.putExtra("deliver_type", model.getDelivery());
                i.putExtra("order_id", model.getOrder_id());
                i.putExtra("token", model.getToken());
                i.putExtra("order_status", model.getStatus());
                //Toast.makeText(Check_orders.this, "clicked", Toast.LENGTH_LONG).show();
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }

    private void close() {
        intent = new Intent(Check_orders.this, Dashboard.class);
        intent.putExtra("phone", phone);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("id", id);
        intent.putExtra("uid", uid);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        ;
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        close();
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
           // Toast.makeText(Check_orders.this, "secure connection", Toast.LENGTH_LONG).show();

            RETURN = true;
        } else {
            Toast.makeText(Check_orders.this, "no connection", Toast.LENGTH_LONG).show();
            /*if (!internet_connection_dialog.isShowing()) {
                internet_connection_dialog.show();
            }*/

            RETURN = false;
        }
        return RETURN;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (placed_order_reference != null && valueEventListener != null) {
            placed_order_reference.removeEventListener(valueEventListener);
        }

    }

}