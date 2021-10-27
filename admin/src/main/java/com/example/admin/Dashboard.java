package com.example.admin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {
    String phone, id, email, username, uid;
    TextView toolbar_name;
    Intent i;
    MenuInflater menuInflater;
    MenuBuilder menuBuilder;
    Intent intent;

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //status bar transparent
        requestWindowFeature(1);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_dashboard);


        phone = getIntent().getStringExtra("phone");
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        uid = getIntent().getStringExtra("uid");
       // Toast.makeText(Dashboard.this,id,Toast.LENGTH_LONG).show();

        findViewById(R.id.toolbar).findViewById(R.id.back).setVisibility(View.INVISIBLE);
        toolbar_name = findViewById(R.id.toolbar).findViewById(R.id.name);

        toolbar_name.setText(username);
        i = new Intent(Dashboard.this, Check_orders.class);
        findViewById(R.id.toolbar).findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                    builder.setTitle("Logout");
                    builder.setCancelable(false);
                    builder.setMessage("Are you sure you want to logout?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                FirebaseAuth.getInstance().signOut();
                                stopService(new Intent(Dashboard.this,notificationService.class));
                                dialog.dismiss();

                                Toast.makeText(Dashboard.this, "logged out", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Dashboard.this, Login.class);
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
                    // builder.create();
                    AlertDialog dialog=builder.create();
                    dialog.show();
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    negativeButton.setTextColor(getResources().getColor(R.color.green));
                    positiveButton.setTextColor(getResources().getColor(R.color.green));
                }

            }
        });
        menuBuilder = new MenuBuilder(Dashboard.this);
        //popup.setOnMenuItemClickListener(Dashboard.this);
        menuInflater = new MenuInflater(Dashboard.this);
        menuInflater.inflate(R.menu.popup_menu,menuBuilder);
        findViewById(R.id.toolbar).findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {

            @SuppressLint("RtlHardcoded")
            @Override
            public void onClick(View v) {
                MenuPopupHelper menuPopupHelper = new MenuPopupHelper(Dashboard.this, menuBuilder, v);
                menuPopupHelper.setGravity(Gravity.RIGHT);
                menuPopupHelper.setForceShowIcon(true);
                menuBuilder.setCallback(new MenuBuilder.Callback() {
                    @Override
                    public boolean onMenuItemSelected(@NonNull MenuBuilder menu, @NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.account:
                                intent=new Intent(Dashboard.this,profile.class);
                                intent.putExtra("class","Dashboard");
                                intent.putExtra("phone",phone);
                                intent.putExtra("username",username);
                                intent.putExtra("email",email);
                                intent.putExtra("uid",uid);
                                intent.putExtra("id",id);
                                startActivity(intent);
                                overridePendingTransition(R.anim.animate_slide_up_enter, 0);
                                finish();
                                return true;

                            case R.id.dashboard:
                               menuBuilder.close();
                               return true;

                            case R.id.change_password:
                                intent=new Intent(Dashboard.this,forgotPassword.class);
                                intent.putExtra("class","Dashboard");
                                intent.putExtra("phone",phone);
                                intent.putExtra("username",username);
                                intent.putExtra("email",email);
                                intent.putExtra("uid",uid);
                                intent.putExtra("id",id);
                                intent.putExtra("from_other_activity",true);
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
        findViewById(R.id.mohammadpur).setOnClickListener(this::onClick);
        findViewById(R.id.shyamoli).setOnClickListener(this::onClick);
        findViewById(R.id.dhanmondi).setOnClickListener(this::onClick);
        findViewById(R.id.mirpur).setOnClickListener(this::onClick);
        findViewById(R.id.mohakhali).setOnClickListener(this::onClick);
        findViewById(R.id.bashundhara).setOnClickListener(this::onClick);
        findViewById(R.id.uttara).setOnClickListener(this::onClick);
        findViewById(R.id.gulshan).setOnClickListener(this::onClick);


    }

    @Override
    public void onClick(View v) {
        i.putExtra("phone", phone);
        i.putExtra("username", username);
        i.putExtra("email", email);
        i.putExtra("id", id);
        i.putExtra("uid", uid);

        //Toast.makeText(Dashboard.this,"clicked",Toast.LENGTH_LONG).show();

        switch (v.getId()) {
            case R.id.shyamoli:
                i.putExtra("branch", "Shyamoli");
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.dhanmondi:
                i.putExtra("branch", "Dhanmondi");
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.mirpur:
                i.putExtra("branch", "Mirpur");
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.mohakhali:
                i.putExtra("branch", "Mohakhali");
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.gulshan:
                i.putExtra("branch", "Gulshan");
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.bashundhara:
                i.putExtra("branch", "Bashundhara");
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.uttara:
                i.putExtra("branch", "Uttara");
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            default:
                i.putExtra("branch", "Mohammadpur");
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;


        }
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}