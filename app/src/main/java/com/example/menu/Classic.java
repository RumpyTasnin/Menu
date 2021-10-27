package com.example.menu;

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
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Classic extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<helperClass> arrayList;
    Adapter adapter;

    Toolbar toolbar;
    TextView classic;
    int order_id=0;

    RecyclerView recyclerView2;
    ArrayList<textview> arrayList2;
    Adapter2 adapter2;
    String username,address,email,phone,uid;
    Intent i;

    DrawerLayout drawerLayout;
    ImageView log_image;
    TextView log_text,guest;
    LinearLayout order, addresses, favourites, profile,call;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //status bar transparent
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        setContentView(R.layout.activity_classic);

//        toolbar=findViewById(R.id.toolbar);


        Intent intent=getIntent();
        order_id=intent.getIntExtra("order_id",0);
        username=getIntent().getStringExtra("username");
        phone=getIntent().getStringExtra("phone");
        email=getIntent().getStringExtra("email");
        address=getIntent().getStringExtra("deliver_address");
        uid=getIntent().getStringExtra("uid");

        setRecyclerView();
        setRecyclerView2();

         classic=findViewById(R.id.classic);
        getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transation));
        classic.setTransitionName("text_transition");

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





        order_id=getIntent().getIntExtra("order_id",0);
        username=getIntent().getStringExtra("username");
        phone=getIntent().getStringExtra("phone");
        email=getIntent().getStringExtra("email");
        uid=getIntent().getStringExtra("uid");
        address=getIntent().getStringExtra("deliver_address");
        setRecyclerView();
        setRecyclerView2();



        ////////////////////////////////////////////////////////////////////////////////
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
                i=new Intent(Classic.this,MainMenu.class);
                set_Intent();
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // logout(MainMenu.this);
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    AlertDialog.Builder builder=new AlertDialog.Builder(Classic.this);
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
                                Toast.makeText(Classic.this,"logged out",Toast.LENGTH_LONG).show();
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
                    Intent intent=new Intent(Classic.this,signIn.class);
                    startActivity(intent);
                    Toast.makeText(Classic.this,"logged in",Toast.LENGTH_LONG).show();

                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }

            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(MainMenu.this," nothing order ",Toast.LENGTH_LONG).show();
                // order(v);
                try{  i=new Intent(Classic.this,nav_orders.class);
                    // Toast.makeText(MainMenu.this," nothing order  v",Toast.LENGTH_LONG).show();
                    set_Intent();

                }
                catch(Exception e){
                    Toast.makeText(Classic.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // profile(v);
                try{ i=new Intent(Classic.this,profile.class);
                    set_Intent();}
                catch(Exception e){
                    Toast.makeText(Classic.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   address(v);
                i=new Intent(Classic.this,Addresses.class);
                set_Intent();
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.favourite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // favourite(v);
                i=new Intent(Classic.this,Favourites.class);
                set_Intent();
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call(v);
                AlertDialog.Builder builder=new AlertDialog.Builder(Classic.this);
                builder.setCancelable(true);
                builder.setMessage("Call +88"+phone);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int permissionCheck = ContextCompat.checkSelfPermission(Classic.this, Manifest.permission.CALL_PHONE);

                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                    Classic.this,
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
        Dialog dialog=new Dialog(Classic.this);
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
                Intent i= new Intent(Classic.this,Cart.class);
                i.putExtra("not_order_class",true);
                i.putExtra("class","Classic");
                i.putExtra("order_id",order_id);
                i.putExtra("phone",phone);
                i.putExtra("username",username);
                i.putExtra("email",email);
                i.putExtra("deliver_address",address);
                i.putExtra("uid",uid);
                startActivity(i);
                finish();
            }
        });


    }
    public void set_Intent(){
        // Toast.makeText(MainMenu.this," nothing set intent",Toast.LENGTH_LONG).show();

        i.putExtra("class","Classic");
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

        recyclerView2=findViewById(R.id.recyclertext2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(Classic.this,LinearLayoutManager.HORIZONTAL,false));

        arrayList2=new ArrayList<>();
        arrayList2.add(new textview("All"));
        arrayList2.add(new textview("Classic"));
        arrayList2.add(new textview("Specials"));
        arrayList2.add(new textview("Hot Drinks"));
        arrayList2.add(new textview("Iced Drinks"));
        arrayList2.add(new textview("Tea"));
        arrayList2.add(new textview("Freddos"));
        arrayList2.add(new textview("Waffles"));
        arrayList2.add(new textview("Others"));

        adapter2=new Adapter2(getApplicationContext(),arrayList2,Classic.this,username,phone,email,address,uid);
        recyclerView2.setAdapter(adapter2);
    }

    private void setRecyclerView() {
        recyclerView=findViewById(R.id.recycler1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Classic.this,LinearLayoutManager.HORIZONTAL,false));

        arrayList=new ArrayList<>();
        arrayList.add(new helperClass(R.drawable.classic1,"ESPRESSO","A single shot of espresso","130","150","Single","Double",true,true,order_id));

        arrayList.add(new helperClass(R.drawable.machiato,"MACCHIATO","A single or double serving of espresso, stained with milky foam","150","175","Single","Double",true,true,order_id));
        arrayList.add(new helperClass(R.drawable.americano,"AMERICANO","A shot of espresso top with hot water","150","250","Regular","Large",true,true,order_id));
        arrayList.add(new helperClass(R.drawable.cappuccino,"CAPPUCCINO","A double shot of espresso with equal parts steamed milk and foam","175","300","Regular","Large",true,true,order_id));
        arrayList.add(new helperClass(R.drawable.latte,"LATTE","A double shot of espresso with steamed milk and a small layer of foam","175","300","Regular","Large",true,true,order_id));

        adapter=new Adapter(getApplicationContext(),Classic.this,arrayList,phone,email,address,username,uid);
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(Classic.this, MainMenu.class);
        intent.putExtra("order_id",order_id);
        intent.putExtra("phone",phone);
        intent.putExtra("username",username);
        intent.putExtra("email",email);
        intent.putExtra("deliver_address",address);
        intent.putExtra("uid",uid);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, R.anim.animate_slide_down_exit);
        finish();
    }
}