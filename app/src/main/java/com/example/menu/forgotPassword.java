package com.example.menu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                Intent intent = new Intent(forgotPassword.this, signIn.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!phone.getEditText().getText().toString().isEmpty()){
                    if(phone.getEditText().getText().toString().length()==11){
                       phone_no=phone.getEditText().getText().toString();
                       listener= FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
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
        if ( FirebaseDatabase.getInstance().getReference().child("users") != null && listener != null) {
            FirebaseDatabase.getInstance().getReference().child("users").removeEventListener(listener);
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        if ( FirebaseDatabase.getInstance().getReference().child("users") != null && listener != null) {
            FirebaseDatabase.getInstance().getReference().child("users").removeEventListener(listener);
        }

    }

    // 2) :
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( FirebaseDatabase.getInstance().getReference().child("users") != null && listener != null) {
            FirebaseDatabase.getInstance().getReference().child("users").removeEventListener(listener);
        }

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(forgotPassword.this, signIn.class);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
        finish();
    }

}