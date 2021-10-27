package com.example.admin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button signIn, newAccount;
    Intent intent, intent1,i,service_intent;
    String phone,id,email,username,order_id,uid;
    Dialog internet_connection_dialog;
    ValueEventListener listener;
    DatabaseReference uid_ref;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(1);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_login);
        //////////
        signIn = findViewById(R.id.signin);
        newAccount = findViewById(R.id.newaccount);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Login.this, signIn.class);

                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent1 = new Intent(Login.this, newAccount.class);

                startActivityForResult(intent1, 2);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }


        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);


        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Admin");
            //   String p = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().substring(3);
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            uid_ref = ref.child(uid);
            listener=uid_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Toast.makeText(Login.this,"login activity ",Toast.LENGTH_LONG).show();
                    phone = snapshot.child("phone").getValue().toString();
                    id = snapshot.child("id").getValue().toString();
                    email = snapshot.child("email").getValue().toString();
                    username = snapshot.child("name").getValue().toString();
                    service_intent=new Intent(Login.this,notificationService.class);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        service_intent.putExtra("phone",phone);
                        service_intent.putExtra("username",username);
                        service_intent.putExtra("email",email);
                        service_intent.putExtra("id",id);
                        service_intent.putExtra("uid",uid);
                        ContextCompat.startForegroundService(Login.this,service_intent);

                    } else {
                        service_intent=new Intent(Login.this,notificationService.class);
                        service_intent.putExtra("phone",phone);
                        service_intent.putExtra("username",username);
                        service_intent.putExtra("email",email);
                        service_intent.putExtra("id",id);
                        service_intent.putExtra("uid",uid);
                       startService(service_intent);
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Intent intent=new Intent(Login.this,Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);// will create new activity by closing all other
            i=getIntent();
            phone=i.getStringExtra("phone");
            username=i.getStringExtra("username");
            email=i.getStringExtra("email");
            id=i.getStringExtra("id");
            uid=i.getStringExtra("uid");

            intent.putExtra("phone",phone);
            intent.putExtra("username",username);
            intent.putExtra("email",email);
            intent.putExtra("id",id);
            intent.putExtra("uid",uid);

          //  Toast.makeText(Login.this,"going to main menu  when current user not null",Toast.LENGTH_LONG).show();
            // activity in the application

            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();

        }

    }
    @Override
    protected void onStop() {
        super.onStop();

        if (uid_ref != null && listener != null) {
            uid_ref.removeEventListener(listener);
        }

    }

}