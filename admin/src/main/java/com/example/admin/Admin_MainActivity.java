package com.example.admin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_MainActivity extends AppCompatActivity {
    TextView slogan1, slogan2, name;
    ImageView cup;
    Animation cupspin, cupspin2, nameAnim, slide;
    Intent intent;
    String phone, id, email, username, uid;
    Dialog internet_connection_dialog;
    LinearLayout layout_progress;
    DatabaseReference ref;

    Handler handler;
    Runnable runnableCode;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // to erase status bar.....
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_admin__main);

        slogan1 = findViewById(R.id.slogan1);
        slogan2 = findViewById(R.id.slogan2);
        name = findViewById(R.id.name);
        cup = findViewById(R.id.cup);
        layout_progress = findViewById(R.id.progressbar_main);

        cupspin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cup_animation);
        nameAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.name_animation);

        // cupspin2=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.cup_animation2);
        cup.startAnimation(cupspin);
        name.startAnimation(nameAnim);
        slogan1.startAnimation(nameAnim);
        slogan2.startAnimation(nameAnim);
        intent = new Intent(getApplicationContext(), Login.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                    if (isConnected(Admin_MainActivity.this)) {
                       intent.putExtra("username", username);
                        intent.putExtra("phone", phone);
                        intent.putExtra("email", email);
                        intent.putExtra("id", id);
                        intent.putExtra("uid", uid);
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            if (phone == null) {
                         //   Toast.makeText(Admin_MainActivity.this, "wait a moment", Toast.LENGTH_SHORT).show();
                            layout_progress.setVisibility(View.VISIBLE);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Admin");
                            // String p = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().substring(3);
                            String p = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            DatabaseReference uid_ref = ref.child(p);
                            uid_ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    phone = snapshot.child("phone").getValue().toString();
                                    id = snapshot.child("id").getValue().toString();
                                    email = snapshot.child("email").getValue().toString();
                                    username = snapshot.child("name").getValue().toString();
                                    uid = snapshot.child("UID").getValue().toString();


                                    intent.putExtra("username", username);
                                    intent.putExtra("phone", phone);
                                    intent.putExtra("email", email);
                                    intent.putExtra("uid", uid);
                                    intent.putExtra("id", id);
                                    //     Toast.makeText(MainActivity.this, " 2nd " + phone+username, Toast.LENGTH_SHORT).show();
                                    layout_progress.setVisibility(View.GONE);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                                    finish();


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        } else {
                                layout_progress.setVisibility(View.GONE);
                                startActivity(intent);
                                overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                                finish();
                            }
                    }else{
                            layout_progress.setVisibility(View.GONE);
                            startActivity(intent);
                            overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                            finish();
                        }




                }else{
                        internet_connection_dialog.show();
                    }
                    /*else {
                    isConnected(Admin_MainActivity.this);
                }
*/

            }
        }, 4000);

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
           // Toast.makeText(Admin_MainActivity.this, "secure connection", Toast.LENGTH_LONG).show();

            RETURN = true;
        } else {
          //  Toast.makeText(Admin_MainActivity.this, "no connection", Toast.LENGTH_LONG).show();
            /*if (!internet_connection_dialog.isShowing()) {
                internet_connection_dialog.show();
            }*/

            RETURN = false;
        }
        return RETURN;
    }

    private void tryAgain() {
        intent = new Intent(getApplicationContext(), Login.class);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ref = FirebaseDatabase.getInstance().getReference("Admin");
            String p = FirebaseAuth.getInstance().getCurrentUser().getUid();
            layout_progress.setVisibility(View.VISIBLE);
           // Toast.makeText(Admin_MainActivity.this, "start p " + p, Toast.LENGTH_LONG).show();
            DatabaseReference uid_ref = ref.child(p);
            uid_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    phone = snapshot.child("phone").getValue().toString();
                    id = snapshot.child("id").getValue().toString();
                    email = snapshot.child("email").getValue().toString();
                    username = snapshot.child("name").getValue().toString();
                    uid = snapshot.child("UID").getValue().toString();

                    intent.putExtra("username", username);
                    intent.putExtra("phone", phone);
                    intent.putExtra("email", email);
                    intent.putExtra("id", id);
                    intent.putExtra("uid", uid);
                    layout_progress.setVisibility(View.GONE);
                    if(handler!=null){
                       // Toast.makeText(Admin_MainActivity.this,"handler cancelled",Toast.LENGTH_SHORT).show();
                        handler.removeCallbacks(runnableCode);}
                    startActivity(intent);
                    overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                    finish();

                    //  Toast.makeText(MainActivity.this,"1st "+phone,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            if(handler!=null){
                //Toast.makeText(Admin_MainActivity.this,"handler cancelled",Toast.LENGTH_SHORT).show();
                handler.removeCallbacks(runnableCode);}
            startActivity(intent);
            overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
            finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        intent = new Intent(Admin_MainActivity.this, Login.class);
        internet_connection_dialog = new Dialog(Admin_MainActivity.this);
        internet_connection_dialog.setContentView(R.layout.dialog_no_internet);
        internet_connection_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        internet_connection_dialog.getWindow().setGravity(Gravity.CENTER);
        internet_connection_dialog.setCancelable(true);
        internet_connection_dialog.getWindow().getAttributes().windowAnimations = R.style.CustomActivityAnimation2;
        internet_connection_dialog.findViewById(R.id.try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected(Admin_MainActivity.this)){
                    internet_connection_dialog.dismiss();
                    tryAgain();
                }
            }
        });
        internet_connection_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                handler = new Handler();

                runnableCode = new Runnable() {
                    @Override
                    public void run() {
                        tryAgain();
                        handler.postDelayed(this, 2000);
                    }
                };
                handler.post(runnableCode);
            }
        });



        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ref = FirebaseDatabase.getInstance().getReference("Admin");
            String p = FirebaseAuth.getInstance().getCurrentUser().getUid();
           // Toast.makeText(Admin_MainActivity.this, "start p " + p, Toast.LENGTH_LONG).show();
            DatabaseReference uid_ref = ref.child(p);
            uid_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    phone = snapshot.child("phone").getValue().toString();
                    id = snapshot.child("id").getValue().toString();
                    email = snapshot.child("email").getValue().toString();
                    username = snapshot.child("name").getValue().toString();
                    uid = snapshot.child("UID").getValue().toString();

                    intent.putExtra("username", username);
                    intent.putExtra("phone", phone);
                    intent.putExtra("email", email);
                    intent.putExtra("id", id);
                    intent.putExtra("uid", uid);




                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

}