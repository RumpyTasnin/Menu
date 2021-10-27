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
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<helperClass> arrayList;
    RecyclerView recyclerView2;
    ArrayList<textview> arrayList2;
    Adapter2 adapter2;
    Adapter adapter;
    int order_id=0;
    String previousactivity,phone,username,email,address,uid;
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
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

       // getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_main_menu);



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


        Intent intent=getIntent();
        order_id=intent.getIntExtra("order_id",0);
        i=getIntent();
        username=i.getStringExtra("username");
        phone=i.getStringExtra("phone");
        email=i.getStringExtra("email");
        uid=i.getStringExtra("uid");
        address=i.getStringExtra("deliver_address");
        setRecyclerView();
        setRecyclerView2();

        //Toast.makeText(MainMenu.this,"Main menu"+phone,Toast.LENGTH_LONG).show();
      //  Toast.makeText(MainMenu.this,"Main menu recent order id "+order_id,Toast.LENGTH_LONG).show();

        previousactivity=intent.getStringExtra("previousactivity");




        ////////////////////////////////////////////////////////////////////////////////


        findViewById(R.id.toolbar).findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
//        findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                }
//            }
//        });
        findViewById(R.id.navigation_drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //  Toast.makeText(MainMenu.this,"nothing",Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.navigation_drawer).findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // home(v);
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // logout(MainMenu.this);
                Toast.makeText(MainMenu.this,"logged out clicked",Toast.LENGTH_LONG).show();


                if(FirebaseAuth.getInstance().getCurrentUser()!=null){

                    // builder.create();
                    AlertDialog.Builder builder=new AlertDialog.Builder(MainMenu.this);
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
                                Toast.makeText(MainMenu.this,"logged out",Toast.LENGTH_LONG).show();
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
                    builder.show();
                }
                else{
                    Intent intent=new Intent(MainMenu.this,signIn.class);
                    startActivity(intent);
                    Toast.makeText(MainMenu.this,"logged in",Toast.LENGTH_LONG).show();

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
                try{  i=new Intent(MainMenu.this,nav_orders.class);
                    // Toast.makeText(MainMenu.this," nothing order  v",Toast.LENGTH_LONG).show();
                    set_Intent();

                }
                catch(Exception e){
                    Toast.makeText(MainMenu.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // profile(v);
                try{ i=new Intent(MainMenu.this,profile.class);
                    set_Intent();}
                catch(Exception e){
                    Toast.makeText(MainMenu.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   address(v);
                i=new Intent(MainMenu.this,Addresses.class);
                set_Intent();
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.favourite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // favourite(v);
                i=new Intent(MainMenu.this,Favourites.class);
                set_Intent();
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call(v);
                AlertDialog.Builder builder=new AlertDialog.Builder(MainMenu.this);
                builder.setCancelable(true);
                builder.setMessage("Call +88"+phone);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int permissionCheck = ContextCompat.checkSelfPermission(MainMenu.this, Manifest.permission.CALL_PHONE);

                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                    MainMenu.this,
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
        Dialog dialog=new Dialog(MainMenu.this);
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
                i= new Intent(MainMenu.this,Cart.class);
                i.putExtra("not_order_class",true);
               /* i.putExtra("class","MainMenu");
                i.putExtra("order_id",order_id);
                i.putExtra("phone",phone);
                i.putExtra("username",username);
                i.putExtra("email",email);
                i.putExtra("uid",uid);
                i.putExtra("deliver_address",address);
                startActivity(i);
                finish();*/
                set_Intent();
            }
        });




    }


    private void setRecyclerView2() {

        recyclerView2=findViewById(R.id.recyclertext);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(MainMenu.this,LinearLayoutManager.HORIZONTAL,false));

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

        adapter2=new Adapter2(getApplicationContext(),arrayList2,MainMenu.this,username,phone,email,address,uid);
        recyclerView2.setAdapter(adapter2);

    }

    private void setRecyclerView() {
        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainMenu.this,LinearLayoutManager.HORIZONTAL,false));

        arrayList=new ArrayList<>();
        arrayList.add(new helperClass(R.drawable.classic,"CLASSICS","Espresso drinks." +
                "Regular and large size available","","","","",false,false,order_id));
        arrayList.add(new helperClass(R.drawable.specials,"SPECIALS","Espresso drinks." +
                "Regular and large size available","","","","",false,false,order_id));
        arrayList.add(new helperClass(R.drawable.hot,"HOT DRINKS","Regular and large size available","","","","",false,false,order_id));
        arrayList.add(new helperClass(R.drawable.iced,"ICED DRINKS","Regular and large size available","","","","",false,false,order_id));
        arrayList.add(new helperClass(R.drawable.tea,"TEA","Regular and large size available","","","","",false,false,order_id));
        arrayList.add(new helperClass(R.drawable.cafefredo,"FREDDOS","Regular and large size available","","","","",false,false,order_id));
        arrayList.add(new helperClass(R.drawable.dessert,"WAFFLES"," ","","","","",false,false,order_id));
        arrayList.add(new helperClass(R.drawable.snacks,"OTHERS","Snack items","","","","",false,false,order_id));

        adapter=new Adapter(getApplicationContext(),MainMenu.this,arrayList,phone,email,address,username,uid);
        recyclerView.setAdapter(adapter);
    }

   /* public void home(View view){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

    }
    public void order(View view){
        try{  i=new Intent(MainMenu.this,nav_orders.class);
           // Toast.makeText(MainMenu.this," nothing order  v",Toast.LENGTH_LONG).show();
            set_Intent();

        }
        catch(Exception e){
            Toast.makeText(MainMenu.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }


    }*/
   /* public void address(View view){
        i=new Intent(MainMenu.this,Addresses.class);
      //  i.putExtra("from_nav_drawer",true);
       *//* i.putExtra("class","MainMenu");
        i.putExtra("order_id",order_id);
        i.putExtra("phone",phone);
        i.putExtra("username",username);
        i.putExtra("email",email);
        i.putExtra("uid",uid);
        i.putExtra("deliver_address",address);
        startActivity(i);
        finish();*//*
        set_Intent();

    }
    public void profile(View view){

        try{ i=new Intent(MainMenu.this,profile.class);
           *//* i.putExtra("class","MainMenu");
        i.putExtra("order_id",order_id);
        i.putExtra("phone",phone);
        i.putExtra("username",username);
        i.putExtra("email",email);
        i.putExtra("uid",uid);
        i.putExtra("deliver_address",address);
        startActivity(i);
        finish();*//*
        set_Intent();}
        catch(Exception e){
            Toast.makeText(MainMenu.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public void favourite(View view){
        i=new Intent(MainMenu.this,Favourites.class);
        set_Intent();
    }
    public void about(View view){

    }
    public void log_activity(View view){
       // logout(MainMenu.this);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            AlertDialog.Builder builder=new AlertDialog.Builder(MainMenu.this);
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
                        Toast.makeText(MainMenu.this,"logged out",Toast.LENGTH_LONG).show();
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
            builder.show();
        }
        else{
            Intent intent=new Intent(MainMenu.this,signIn.class);
            startActivity(intent);
            Toast.makeText(MainMenu.this,"logged in",Toast.LENGTH_LONG).show();

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }

    }

    private void logout(Activity activity) {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            AlertDialog.Builder builder=new AlertDialog.Builder(activity);
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
                        Toast.makeText(MainMenu.this,"logged out",Toast.LENGTH_LONG).show();
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
            builder.show();
        }
        else{
            Intent intent=new Intent(MainMenu.this,signIn.class);
            startActivity(intent);
            Toast.makeText(MainMenu.this,"logged in",Toast.LENGTH_LONG).show();

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }

    }

    public void call(View view){

    }*/
    public void set_Intent(){
       // Toast.makeText(MainMenu.this," nothing set intent",Toast.LENGTH_LONG).show();

        i.putExtra("class","MainMenu");
        i.putExtra("order_id",order_id);
        i.putExtra("phone",phone);
        i.putExtra("username",username);
        i.putExtra("email",email);
        i.putExtra("uid",uid);
        i.putExtra("deliver_address",address);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}