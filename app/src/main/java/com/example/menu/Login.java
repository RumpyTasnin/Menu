package com.example.menu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity {


    Button signIn, newAccount;
    Intent intent, intent1,i;
    String phone,address,email,username,order_id,uid;
    ValueEventListener listener;
    DatabaseReference uid_ref;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //status bar transparent
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

                startActivity(intent);
                overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                finish();

            }
        });
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent1 = new Intent(Login.this, newAccount.class);

                startActivity(intent1);
                overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                finish();

            }


        });


    }
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
         //   String p = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().substring(3);
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
           uid_ref = ref.child(uid);
            listener=uid_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   // Toast.makeText(Login.this,"login activity ",Toast.LENGTH_LONG).show();
                    phone = snapshot.child("phone").getValue().toString();
                    address = snapshot.child("deliver_address").getValue().toString();
                    email = snapshot.child("email").getValue().toString();
                    username = snapshot.child("name").getValue().toString();

                    if(snapshot.hasChild("order_history")){
                        if(snapshot.child("order_history").hasChild("recent_order_id")){
                            order_id=snapshot.child("order_history").child("recent_order_id").getValue().toString();
                          //  Toast.makeText(Login.this,"recent order login"+snapshot.child("order_history").child("recent_order_id").getValue().toString(),Toast.LENGTH_SHORT).show();

                        }
                    }
             //   Toast.makeText(Login.this,"Login "+phone,Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Intent intent=new Intent(Login.this,MainMenu.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);// will create new activity by closing all other
            i=getIntent();
            phone=i.getStringExtra("phone");
            username=i.getStringExtra("username");
            email=i.getStringExtra("email");
            address=i.getStringExtra("deliver_address");
            order_id=String.valueOf(i.getIntExtra("order_id",0));
            uid=i.getStringExtra("uid");

            intent.putExtra("phone",phone);
            intent.putExtra("username",username);
            intent.putExtra("email",email);
            intent.putExtra("deliver_address",address);
            intent.putExtra("uid",uid);
            if(order_id!=null){
                intent.putExtra("order_id",Integer.parseInt(order_id));
            }

         // Toast.makeText(Login.this,"going to main menu  when current user not null",Toast.LENGTH_LONG).show();
            // activity in the application

            startActivity(intent);
            overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
            finish();

        }
        else{
            if(!getIntent().getBooleanExtra("open_login_activity",false)){
                final String PREFS_NAME = "MyPrefsFile";
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

                if (settings.getBoolean("my_first_time", true)) {
                    Toast.makeText(Login.this,"First time app installed",Toast.LENGTH_LONG).show();

                    settings.edit().putBoolean("my_first_time", false).commit();
                }
                else{
                    Intent intent=new Intent(Login.this,MainMenu.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                }
            }

        }

    }
    @Override
    protected void onStop() {
        super.onStop();

        if (uid_ref != null && listener != null) {
            uid_ref.removeEventListener(listener);
            //Toast.makeText(Login.this, "removed listener", Toast.LENGTH_LONG).show();
        }

    }

}