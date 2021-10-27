package com.example.menu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TEA extends AppCompatActivity {
    RecyclerView recyclerView2;
    ArrayList<textview> arrayList2;
    Adapter2 adapter2;
    LinearLayout card1,card2;

    String address,email,phone,username,uid;

    int order_id=0;
    TextView tea;

    TextView special;
    DrawerLayout drawerLayout;
    ImageView log_image;
    TextView log_text,guest;
    LinearLayout order, addresses, favourites, profile,call;
    Intent i;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //status bar transparent
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_t_e);


        Intent intent=getIntent();
        order_id=intent.getIntExtra("order_id",0);

        card1=findViewById(R.id.layout_card_1);
        card2=findViewById(R.id.layout_card_2);

        username=getIntent().getStringExtra("username");
        phone=getIntent().getStringExtra("phone");
        email=getIntent().getStringExtra("email");
        uid=getIntent().getStringExtra("uid");
        address=getIntent().getStringExtra("deliver_address");
        order=findViewById(R.id.navigation_drawer).findViewById(R.id.order);
        profile=findViewById(R.id.navigation_drawer).findViewById(R.id.profile);
        addresses=findViewById(R.id.navigation_drawer).findViewById(R.id.address);
        favourites=findViewById(R.id.navigation_drawer).findViewById(R.id.favourite);
        call=findViewById(R.id.navigation_drawer).findViewById(R.id.call);
        drawerLayout=findViewById(R.id.drawerlayout);
        log_image= findViewById(R.id.navigation_drawer).findViewById(R.id.log_image);
        log_text= findViewById(R.id.navigation_drawer).findViewById(R.id.text);
        guest=findViewById(R.id.navigation_drawer).findViewById(R.id.username);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            log_image.setImageResource(R.drawable.ic_sharp_logout_24);
            log_text.setText("Logout");
            order.setVisibility(View.VISIBLE);
            profile.setVisibility(View.VISIBLE);
            addresses.setVisibility(View.VISIBLE);
            favourites.setVisibility(View.VISIBLE);
            guest.setText(getIntent().getStringExtra("username"));
        }
        else{
            log_image.setImageResource(R.drawable.ic_login);
            log_text.setText("Login");
            order.setVisibility(View.GONE);
            profile.setVisibility(View.GONE);
            addresses.setVisibility(View.GONE);
            favourites.setVisibility(View.GONE);
            guest.setText("Guest");

        }


        findViewById(R.id.toolbar).findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
        findViewById(R.id.navigation_drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        findViewById(R.id.navigation_drawer).findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // home(v);

                i=new Intent(TEA.this,MainMenu.class);
                set_Intent();
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // logout(MainMenu.this);
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    AlertDialog.Builder builder=new AlertDialog.Builder(TEA.this);
                    builder.setTitle("Logout");
                    builder.setCancelable(false);
                    builder.setMessage("Are you sure you want to logout?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                                FirebaseAuth.getInstance().signOut();
                                dialog.dismiss();
                                log_image.setImageResource(R.drawable.ic_login);
                                log_text.setText("Login");
                                Toast.makeText(TEA.this,"logged out",Toast.LENGTH_LONG).show();
                                order.setVisibility(View.GONE);
                                profile.setVisibility(View.GONE);
                                addresses.setVisibility(View.GONE);
                                favourites.setVisibility(View.GONE);
                                guest.setText("Guest");

                            }

                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();


                        }
                    });
                    // builder.create();
                    AlertDialog dialog=builder.create();
                    dialog.show();
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    negativeButton.setTextColor(getResources().getColor(R.color.green));
                    positiveButton.setTextColor(getResources().getColor(R.color.green));
                }
                else{
                    Intent intent=new Intent(TEA.this,signIn.class);
                    startActivity(intent);
                    Toast.makeText(TEA.this,"logged in",Toast.LENGTH_LONG).show();

                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }

            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{  i=new Intent(TEA.this,nav_orders.class);
                    set_Intent();

                }
                catch(Exception e){
                    Toast.makeText(TEA.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{ i=new Intent(TEA.this,profile.class);
                    set_Intent();}
                catch(Exception e){
                    Toast.makeText(TEA.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(TEA.this,Addresses.class);
                set_Intent();
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.favourite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(TEA.this,Favourites.class);
                set_Intent();
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call(v);
                AlertDialog.Builder builder=new AlertDialog.Builder(TEA.this);
                builder.setCancelable(true);
                builder.setMessage("Call +88"+phone);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int permissionCheck = ContextCompat.checkSelfPermission(TEA.this, Manifest.permission.CALL_PHONE);

                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                    TEA.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    123);
                        } else {
                            String s="tel:"+"+88"+phone;
                            Intent i1=new Intent(Intent.ACTION_CALL);
                            i1.setData(Uri.parse(s));
                            dialog.dismiss();
                            startActivity(i1);                        }


                    }


                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();


                    }
                });
                // builder.create();
                AlertDialog dialog=builder.create();
                dialog.show();
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setTextColor(getResources().getColor(R.color.green));
                positiveButton.setTextColor(getResources().getColor(R.color.green));

            }
        });
        Dialog dialog=new Dialog(TEA.this);
        dialog.setContentView(R.layout.dialog_about_us);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.CustomActivityAnimation;
        dialog.findViewById(R.id.got_it).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        findViewById(R.id.toolbar).findViewById(R.id.cart2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(TEA.this,Cart.class);
                i.putExtra("not_order_class",true);
                i.putExtra("class","TEA");
                i.putExtra("order_id",order_id);
                i.putExtra("phone",phone);
                i.putExtra("username",username);
                i.putExtra("email",email);
                i.putExtra("uid",uid);
                i.putExtra("deliver_address",address);
                startActivity(i);
                finish();
            }
        });


        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(TEA.this,Order.class);
                //holder.title.setTransitionName("text_transition7");
                intent1.putExtra("food_image",R.drawable.greentea);
                intent1.putExtra("food_name","Green Tea");
                intent1.putExtra("food_description","");
                intent1.putExtra("food_price","30");
                intent1.putExtra("regular","");
                intent1.putExtra("large","");
                intent1.putExtra("add_ons_visibility",false);
                intent1.putExtra("radio_group_visibility",false);
                intent1.putExtra("regular_price","30");
                intent1.putExtra("large_price","");
                intent1.putExtra("activity","com.example.menu.TEA");
                intent1.putExtra("layout",1);
                intent1.putExtra("order_id",order_id);
                intent1.putExtra("username",username);
                intent1.putExtra("email",email);
                intent1.putExtra("uid",uid);
                intent1.putExtra("deliver_address",address);
                intent1.putExtra("phone",phone);
                startActivity(intent1);
                overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                finish();


            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(TEA.this,Order.class);
                //holder.title.setTransitionName("text_transition7");
                intent1.putExtra("food_image",R.drawable.balcktea);
                intent1.putExtra("food_name","Black Tea");
                intent1.putExtra("food_description","");
                intent1.putExtra("food_price","20");
                intent1.putExtra("regular","");
                intent1.putExtra("large","");
                intent1.putExtra("add_ons_visibility",false);
                intent1.putExtra("radio_group_visibility",false);
                intent1.putExtra("regular_price","20");
                intent1.putExtra("large_price","");
                intent1.putExtra("activity","com.example.menu.TEA");
                intent1.putExtra("layout",1);
                intent1.putExtra("order_id",order_id);
                intent1.putExtra("username",username);
                intent1.putExtra("email",email);
                intent1.putExtra("uid",uid);
                intent1.putExtra("deliver_address",address);
                intent1.putExtra("phone",phone);
                startActivity(intent1);
                overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                finish();


            }
        });




        setRecyclerView2();

        tea = findViewById(R.id.tea);
        getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transation));
        tea.setTransitionName("text_transition6");


    }
    public void set_Intent(){
        // Toast.makeText(MainMenu.this," nothing set intent",Toast.LENGTH_LONG).show();

        i.putExtra("class","TEA");
        i.putExtra("order_id",order_id);
        i.putExtra("phone",phone);
        i.putExtra("username",username);
        i.putExtra("email",email);
        i.putExtra("uid",uid);
        i.putExtra("deliver_address",address);
        startActivity(i);
        overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
        finish();
    }
    private void setRecyclerView2() {

        recyclerView2 = findViewById(R.id.recyclertext6);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(TEA.this, LinearLayoutManager.HORIZONTAL, false));


        arrayList2 = new ArrayList<>();
        arrayList2.add(new textview("All"));
        arrayList2.add(new textview("Classic"));
        arrayList2.add(new textview("Specials"));
        arrayList2.add(new textview("Hot Drinks"));
        arrayList2.add(new textview("Iced Drinks"));
        arrayList2.add(new textview("Tea"));
        arrayList2.add(new textview("Freddos"));
        arrayList2.add(new textview("Waffles"));
        arrayList2.add(new textview("Others"));

        adapter2 = new Adapter2(getApplicationContext(), arrayList2, TEA.this,username,phone,email,address,uid);
        recyclerView2.setAdapter(adapter2);
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(TEA.this, MainMenu.class);
        intent.putExtra("order_id",order_id);
        intent.putExtra("phone",phone);
        intent.putExtra("username",username);
        intent.putExtra("email",email);
        intent.putExtra("uid",uid);
        intent.putExtra("deliver_address",address);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, R.anim.animate_slide_down_exit);
        finish();
    }
}