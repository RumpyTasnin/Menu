package com.example.menu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
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

public class MainActivity extends AppCompatActivity {
    TextView slogan1, slogan2, name;
    ImageView cup;
    Animation cupspin, cupspin2, nameAnim, slide;
    Intent intent;
    //ProgressBar progressBar;
    Dialog internet_connection_dialog;
    LinearLayout layout_progress;
    String phone, address, email, username, order_id, status, uid;
    Handler handler;
    Runnable runnableCode;
    DatabaseReference uid_ref;
    ValueEventListener listener;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // to erase status bar.....
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main);


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


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                intent = new Intent(getApplicationContext(), Login.class);
                intent.putExtra("username", username);
                intent.putExtra("phone", phone);
                intent.putExtra("email", email);
                intent.putExtra("deliver_address", address);
                intent.putExtra("uid", uid);


                if (order_id != null) {
                    // Toast.makeText(MainActivity.this,phone,Toast.LENGTH_LONG).show();
                    intent.putExtra("order_id", Integer.parseInt(order_id));
                }


                if (phone == null) {
                    if (isConnected(MainActivity.this)) {
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            Toast.makeText(MainActivity.this, "wait a moment", Toast.LENGTH_SHORT).show();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                            // String p = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().substring(3);
                            String p = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            layout_progress.setVisibility(View.VISIBLE);
                            uid_ref = ref.child(p);
                            listener = uid_ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    phone = snapshot.child("phone").getValue().toString();
                                    address = snapshot.child("deliver_address").getValue().toString();
                                    email = snapshot.child("email").getValue().toString();
                                    username = snapshot.child("name").getValue().toString();
                                    uid = snapshot.child("UID").getValue().toString();
                                    if (snapshot.hasChild("order_history")) {
                                        if (snapshot.child("order_history").hasChild("recent_order_id")) {

                                            order_id = snapshot.child("order_history").child("recent_order_id").getValue().toString();
                                            intent.putExtra("order_id", Integer.parseInt(order_id));

                                        }
                                    }
                                    //  Toast.makeText(MainActivity.this,"called 2" +phone,Toast.LENGTH_SHORT).show();
                                    //progressBar.setVisibility(View.INVISIBLE);
                                    intent.putExtra("username", username);
                                    intent.putExtra("phone", phone);
                                    intent.putExtra("email", email);
                                    intent.putExtra("uid", uid);
                                    intent.putExtra("deliver_address", address);
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
                            // Toast.makeText(MainActivity.this,"else ",Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                            finish();
                        }
                    } else {
                        internet_connection_dialog.show();
                    }


                } else {
                    startActivity(intent);
                    overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                    finish();
                }


            }
        }, 4000);


    }

    @Override
    protected void onStart() {
        super.onStart();

        intent = new Intent(MainActivity.this, Login.class);
        internet_connection_dialog = new Dialog(MainActivity.this);
        internet_connection_dialog.setContentView(R.layout.dialog_no_internet);
        internet_connection_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        internet_connection_dialog.getWindow().setGravity(Gravity.CENTER);
        internet_connection_dialog.setCancelable(true);
        internet_connection_dialog.getWindow().getAttributes().windowAnimations = R.style.CustomActivityAnimation2;
        internet_connection_dialog.findViewById(R.id.try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected(MainActivity.this)) {
                    internet_connection_dialog.dismiss();

                }
                tryAgain();
            }
        });
        internet_connection_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                handler = new Handler();
// Define the code block to be executed
                runnableCode = new Runnable() {
                    @Override
                    public void run() {
                        // Do something here on the main thread

                        // Repeat this the same runnable code block again another 2 seconds
                        // 'this' is referencing the Runnable object
                        tryAgain();
                        handler.postDelayed(this, 2000);
                    }
                };
// Start the initial runnable task by posting through the handler
                handler.post(runnableCode);
            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {


            try {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                // String p = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().substring(3);
                String p = FirebaseAuth.getInstance().getCurrentUser().getUid();

                // Toast.makeText(MainActivity.this,"start p "+p,Toast.LENGTH_LONG).show();
                DatabaseReference uid_ref = ref.child(p);
                uid_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        phone = snapshot.child("phone").getValue().toString();
                        address = snapshot.child("deliver_address").getValue().toString();
                        email = snapshot.child("email").getValue().toString();
                        username = snapshot.child("name").getValue().toString();
                        uid = snapshot.child("UID").getValue().toString();
                        if (snapshot.hasChild("order_history")) {
                            if (snapshot.child("order_history").hasChild("recent_order_id")) {
                                order_id = snapshot.child("order_history").child("recent_order_id").getValue().toString();
                            }
                            //  Toast.makeText(MainActivity.this,"called1"+ phone+"    "+order_id,Toast.LENGTH_SHORT).show();

                        }
                        intent = new Intent(getApplicationContext(), Login.class);
                        intent.putExtra("username", username);
                        intent.putExtra("phone", phone);
                        intent.putExtra("email", email);
                        intent.putExtra("deliver_address", address);
                        intent.putExtra("uid", uid);


                        //  Toast.makeText(MainActivity.this,"1st "+phone,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } catch (Exception e) {
            }


        }
    }

    private void tryAgain() {
        // Toast.makeText(MainActivity.this,"try again called   ",Toast.LENGTH_SHORT).show();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            try {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                // String p = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().substring(3);
                String p = FirebaseAuth.getInstance().getCurrentUser().getUid();
                layout_progress.setVisibility(View.VISIBLE);
                //  Toast.makeText(MainActivity.this,"on start p "+p+ String.valueOf(isConnected(MainActivity.this)),Toast.LENGTH_LONG).show();
                uid_ref = ref.child(p);
                listener = uid_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        phone = snapshot.child("phone").getValue().toString();
                        address = snapshot.child("deliver_address").getValue().toString();
                        email = snapshot.child("email").getValue().toString();
                        username = snapshot.child("name").getValue().toString();
                        uid = snapshot.child("UID").getValue().toString();
                        if (snapshot.hasChild("order_history")) {
                            if (snapshot.child("order_history").hasChild("recent_order_id")) {
                                order_id = snapshot.child("order_history").child("recent_order_id").getValue().toString();
                            }
                            //  Toast.makeText(MainActivity.this,"called1"+ phone+"    "+order_id,Toast.LENGTH_SHORT).show();

                        }
                        layout_progress.setVisibility(View.GONE);
                        intent = new Intent(getApplicationContext(), Login.class);
                        intent.putExtra("username", username);
                        intent.putExtra("phone", phone);
                        intent.putExtra("email", email);
                        intent.putExtra("deliver_address", address);
                        intent.putExtra("uid", uid);
                        if (handler != null) {
                            // Toast.makeText(MainActivity.this,"handler cancelled",Toast.LENGTH_SHORT).show();
                            handler.removeCallbacks(runnableCode);
                        }
                        startActivity(intent);
                        overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                        finish();

                        //  Toast.makeText(MainActivity.this,"1st "+phone,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } catch (Exception e) {
            }

        } else {
            if (handler != null) {
                //  Toast.makeText(MainActivity.this,"handler cancelled",Toast.LENGTH_SHORT).show();
                handler.removeCallbacks(runnableCode);
            }
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }

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
           // Toast.makeText(MainActivity.this, "secure connection", Toast.LENGTH_LONG).show();

            RETURN = true;
        } else {
            Toast.makeText(MainActivity.this, "no connection", Toast.LENGTH_LONG).show();
           /* if (!internet_connection_dialog.isShowing()) {
                internet_connection_dialog.show();
            }*/

            RETURN = false;
        }
        return RETURN;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (uid_ref != null && listener != null) {
            uid_ref.removeEventListener(listener);
            //Toast.makeText(Login.this, "removed listener", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (uid_ref != null && listener != null) {
            uid_ref.removeEventListener(listener);
            //Toast.makeText(Login.this, "removed listener", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(Verification.this,"resumed",Toast.LENGTH_LONG).show();
        if (uid_ref != null && listener != null) {
            uid_ref.removeEventListener(listener);
            //Toast.makeText(Login.this, "removed listener", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(Verification.this,"paused",Toast.LENGTH_LONG).show();
        if (uid_ref != null && listener != null) {
            uid_ref.removeEventListener(listener);
            //Toast.makeText(Login.this, "removed listener", Toast.LENGTH_LONG).show();
        }
    }


}

