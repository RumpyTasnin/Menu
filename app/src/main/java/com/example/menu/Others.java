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

public class Others extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView2;
    ArrayList<textview> arrayList2;
    Adapter2 adapter2;
    String phone,username,email,address,uid;
    TextView pizza_sandwich,bbq_sandwich,tandoori_sandwich,delight_burger,kingkong_burger,fatty_burger,alfredo_pasta,bolognese_pasta;

    int order_id=0;
    TextView other;
    Intent intent;

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
        setContentView(R.layout.activity_others);


        Intent intent=getIntent();
        order_id=intent.getIntExtra("order_id",0);
        username=getIntent().getStringExtra("username");
        phone=getIntent().getStringExtra("phone");
        uid=getIntent().getStringExtra("uid");
        email=getIntent().getStringExtra("email");
        address=getIntent().getStringExtra("deliver_address");

        setRecyclerView2();


        other=findViewById(R.id.other);
        getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transation));
        other.setTransitionName("text_transition5");


        pizza_sandwich=findViewById(R.id.pizza_sandwich);
        bbq_sandwich=findViewById(R.id.bbq_sandwich);
        tandoori_sandwich=findViewById(R.id.tandoori_sandwich);
        delight_burger=findViewById(R.id.delight_burger);
        kingkong_burger=findViewById(R.id.kingkong_burger);
        fatty_burger=findViewById(R.id.fatty_burger);
        alfredo_pasta=findViewById(R.id.alfredo_pasta);
        bolognese_pasta=findViewById(R.id.bolognese_pasta);



        pizza_sandwich.setOnClickListener(this::onClick);
        bbq_sandwich.setOnClickListener(this::onClick);
        tandoori_sandwich.setOnClickListener(this::onClick);
        delight_burger.setOnClickListener(this::onClick);
        kingkong_burger.setOnClickListener(this::onClick);
        fatty_burger.setOnClickListener(this::onClick);
        alfredo_pasta.setOnClickListener(this::onClick);
        bolognese_pasta.setOnClickListener(this::onClick);

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
                i=new Intent(Others.this,MainMenu.class);
                set_Intent();
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // logout(MainMenu.this);
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    AlertDialog.Builder builder=new AlertDialog.Builder(Others.this);
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
                                Toast.makeText(Others.this,"logged out",Toast.LENGTH_LONG).show();
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
                    Intent intent=new Intent(Others.this,signIn.class);
                    startActivity(intent);
                    Toast.makeText(Others.this,"logged in",Toast.LENGTH_LONG).show();

                    overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                    finish();
                }

            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{  i=new Intent(Others.this,nav_orders.class);
                    set_Intent();

                }
                catch(Exception e){
                    Toast.makeText(Others.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{ i=new Intent(Others.this,profile.class);
                    set_Intent();}
                catch(Exception e){
                    Toast.makeText(Others.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(Others.this,Addresses.class);
                set_Intent();
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.favourite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(Others.this,Favourites.class);
                set_Intent();
            }
        });
        findViewById(R.id.navigation_drawer).findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Others.this);
                builder.setCancelable(true);
                builder.setMessage("Call +8801968568656");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int permissionCheck = ContextCompat.checkSelfPermission(Others.this, Manifest.permission.CALL_PHONE);

                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                    Others.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    123);
                        } else {
                            String s="tel:"+"+8801968568656";
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
        Dialog dialog=new Dialog(Others.this);
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
                Intent i= new Intent(Others.this,Cart.class);
                i.putExtra("not_order_class",true);
                i.putExtra("class","Others");
                i.putExtra("order_id",order_id);
                i.putExtra("phone",phone);
                i.putExtra("uid",uid);
                i.putExtra("username",username);
                i.putExtra("email",email);
                i.putExtra("deliver_address",address);
                startActivity(i);
                overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                finish();
            }
        });

    }

    private void setRecyclerView2() {
        recyclerView2=findViewById(R.id.recyclertextothers);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(Others.this,LinearLayoutManager.HORIZONTAL,false));

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


        adapter2=new Adapter2(getApplicationContext(),arrayList2,Others.this,username,phone,email,address,uid);
        recyclerView2.setAdapter(adapter2);


    }




    @Override
    public void onClick(View view) {
        intent=new Intent(Others.this,Order.class);

        switch (view.getId()){
            case R.id.pizza_sandwich:
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("food_image",R.drawable.pizzasand2);
                intent.putExtra("food_name","House Special Pizza Sandwich");
                intent.putExtra("food_description","A unique fusion recipe with the combination of pizza recipe to the popular sandwich recipe.");
                intent.putExtra("food_price","350");
                setIntent();
                break;
            case R.id.bbq_sandwich:
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("food_image",R.drawable.bbqsand);
                intent.putExtra("food_name","Bbq Chicken Cheese ");
                intent.putExtra("food_description","Chicken breast grilled with onions, smothered in bbq sauce and melted paper jack, topped with bacon and mushroom");
                intent.putExtra("food_price","310");
                setIntent();
                break;
            case R.id.tandoori_sandwich:
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("food_image",R.drawable.tandoori1);
                intent.putExtra("food_name","Tandoori chicken cheese sandwich");
                intent.putExtra("food_description","The chicken is served on toasted bread spread with a mayonnaise spiked with mint, cilantro and chili, flavors common in Indian relishes.");
                intent.putExtra("food_price","320");
                setIntent();
                break;
            case R.id.delight_burger:
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("food_image",R.drawable.delight2);
                intent.putExtra("food_name","Mainstream Delight");
                intent.putExtra("food_description","100gm chickenpatty,beef salami, jalapeno, mushroom, egg, pickle, honey mustard sauce, mayonnaise." );
                intent.putExtra("food_price","330");
                setIntent();
                break;
            case R.id.kingkong_burger:
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("food_image",R.drawable.kingkongburger);
                intent.putExtra("food_name","Kingkong Burger");
                intent.putExtra("food_description","100gm chickenpatty 2X, cheese slice 2X, beef salami, beef bacon, jalapeno, mushroom, egg, pickle, honey mustard sauce, mayonnaise.");
                intent.putExtra("food_price","350");
                setIntent();
                break;
            case R.id.fatty_burger:
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("food_image",R.drawable.fattyburger);
                intent.putExtra("food_name","Fatty Burger");
                intent.putExtra("food_description","100gm chickenpatty 2X, cheese slice 3X, 2layers of beef bacon, bbq sauce, honey mustard sauce.");
                intent.putExtra("food_price","430");
                setIntent();
                break;
            case R.id.alfredo_pasta:
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("food_image",R.drawable.alfredo);
                intent.putExtra("food_name","Fettuccine Alfredo Pasta");
                intent.putExtra("food_description","Our fettuccine pasta tastes its best when served in a rich, creamy Parmesan cheese sauce made with real cream and butter.");
                intent.putExtra("food_price","430");
                setIntent();
                break;
            case R.id.bolognese_pasta:
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("food_image",R.drawable.bolognese1);
                intent.putExtra("food_name","Pasta Bolognese");
                intent.putExtra("food_description","A bowl of steaming hot pasta tangled with a beautifully rich and smooth bolognese sauce exploding with so much flavour ");
                intent.putExtra("food_price","340");
                setIntent();
                break;



        }

    }
    public void set_Intent(){
        // Toast.makeText(MainMenu.this," nothing set intent",Toast.LENGTH_LONG).show();

        i.putExtra("class","Others");
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
    private void setIntent() {


        intent.putExtra("activity","com.example.menu.Others");
        intent.putExtra("layout",2);
        intent.putExtra("order_id",order_id);
        intent.putExtra("username",username);
        intent.putExtra("email",email);
        intent.putExtra("deliver_address",address);
        intent.putExtra("phone",phone);
        intent.putExtra("uid",uid);
        startActivity(intent);
        overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
        finish();


    }
    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(Others.this, MainMenu.class);
        intent.putExtra("order_id",order_id);
        intent.putExtra("phone",phone);
        intent.putExtra("uid",uid);
        intent.putExtra("username",username);
        intent.putExtra("email",email);
        intent.putExtra("deliver_address",address);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, R.anim.animate_slide_down_exit);
        finish();}

}