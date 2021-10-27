package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class forgotPassword extends AppCompatActivity {

    LinearLayout next;
    TextInputLayout phone;
    ImageView back;
    String email,phone_no,password;
    ValueEventListener listener;
    Intent intent;
    String PHONE, id, EMAIL, username, uid, branch, order_id, delivery_type, token,CLASS;
    boolean from_other_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_forgot_password);

        Window mRootWindow = getWindow();
        View mRootView = mRootWindow.getDecorView().findViewById(android.R.id.content);

        PHONE = getIntent().getStringExtra("phone");
        username = getIntent().getStringExtra("username");
        EMAIL = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        uid = getIntent().getStringExtra("uid");
        branch = getIntent().getStringExtra("branch");
        order_id = getIntent().getStringExtra("order_id");
        delivery_type = getIntent().getStringExtra("deliver_type");
        token = getIntent().getStringExtra("token");
        CLASS=getIntent().getStringExtra("class");
        from_other_activity=getIntent().getBooleanExtra("from_other_activity",false);

        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                View view = mRootWindow.getDecorView();
                view.getWindowVisibleDisplayFrame(r);
                int screenHeight = mRootView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                //Toast.makeText(signIn.this,"screen height "+screenHeight+ "  visible display "+r.bottom ,Toast.LENGTH_LONG).show();
                if (keypadHeight > screenHeight * 0.15) {
                    // Toast.makeText(forgotPassword.this,"Keyboard is showing",Toast.LENGTH_LONG).show();

                } else {

                    // Toast.makeText(forgotPassword.this,"keyboard closed",Toast.LENGTH_LONG).show();
                }
            }
        });
        next=findViewById(R.id.next);
        phone=findViewById(R.id.phone);

        back=findViewById(R.id.toolbar_back).findViewById(R.id.toolbarback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!phone.getEditText().getText().toString().isEmpty()){
                    if(phone.getEditText().getText().toString().length()==11){
                        phone_no=phone.getEditText().getText().toString();
                        listener= FirebaseDatabase.getInstance().getReference().child("Admin").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child("Registered_Phone").hasChild(phone_no)){
                                    email=snapshot.child("Registered_Phone").child(phone_no).child("email").getValue().toString();
                                    password=snapshot.child("Registered_Phone").child(phone_no).child("password").getValue().toString();
                                    phone.setError(null);
                                    phone.setErrorEnabled(false);
                                    Toast.makeText(forgotPassword.this,"phone number exists",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(forgotPassword.this,Verification.class);
                                    intent.putExtra("phone",phone.getEditText().getText().toString());
                                    intent.putExtra("reset",true);
                                    if(from_other_activity){
                                        intent.putExtra("class", CLASS);
                                        intent.putExtra("order_id", order_id);
                                        intent.putExtra("username", username);
                                        intent.putExtra("email", EMAIL);
                                        intent.putExtra("uid", uid);
                                        intent.putExtra("id", id);
                                        intent.putExtra("branch", branch);
                                        intent.putExtra("deliver_type", delivery_type);
                                        intent.putExtra("token", token);
                                        intent.putExtra("from_other_activity",true);
                                        intent.putExtra("order_status", getIntent().getStringExtra("order_status"));


                                    }
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);

                                }
                                else {
                                    phone.setError("Phone number is not registered");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                    else{
                        phone.setError("Invalid Phone Number");
                    }
                }
                else {
                    phone.setError("Field can not be empty");
                }

            }
        });

    }
    @Override
    protected void onStop() {
        super.onStop();
        /*if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }*/
        if ( FirebaseDatabase.getInstance().getReference().child("Admin") != null && listener != null) {
            FirebaseDatabase.getInstance().getReference().child("Admin").removeEventListener(listener);
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        if ( FirebaseDatabase.getInstance().getReference().child("Admin") != null && listener != null) {
            FirebaseDatabase.getInstance().getReference().child("Admin").removeEventListener(listener);
        }

    }

    // 2) :
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( FirebaseDatabase.getInstance().getReference().child("Admin") != null && listener != null) {
            FirebaseDatabase.getInstance().getReference().child("Admin").removeEventListener(listener);
        }

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        close();
    }
    private void close() {
        if (from_other_activity) {
            if (CLASS.equals("Dashboard")) {
                intent = new Intent(forgotPassword.this, Dashboard.class);
            } else {
                if (CLASS.equals("CheckOrder")) {
                    intent = new Intent(forgotPassword.this, Check_orders.class);
                } else {
                    if (CLASS.equals("Orders")) {
                        intent = new Intent(forgotPassword.this, Orders.class);
                    }
                }
            }
            intent.putExtra("class", CLASS);
            intent.putExtra("order_id", order_id);
            intent.putExtra("phone", PHONE);
            intent.putExtra("username", username);
            intent.putExtra("email", EMAIL);
            intent.putExtra("uid", uid);
            intent.putExtra("id", id);
            intent.putExtra("branch", branch);
            intent.putExtra("deliver_type", delivery_type);
            intent.putExtra("token", token);
            intent.putExtra("from_other_activity",true);
            intent.putExtra("order_status", getIntent().getStringExtra("order_status"));

            startActivity(intent);
            overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
            finish();
        }
        else {
            intent = new Intent(forgotPassword.this, signIn.class);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();

        }
    }

}