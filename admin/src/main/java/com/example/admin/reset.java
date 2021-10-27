package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Pattern;

public class reset extends AppCompatActivity {
    String PHONE, ID, EMAIL, username, UID, branch, order_id, delivery_type, token,CLASS;
    boolean from_other_activity;

    LinearLayout gone;
    TextInputLayout new_password, confirm_password;
    LinearLayout update, linearLayout;
    String password1, password2, uid, phone, mail, name, id,old_password;

    LinearLayout login;
    ImageView back;
    boolean show = false;
    DatabaseReference ref;
    AlertDialog.Builder builder;
    AlertDialog alert_dialog;
    ValueEventListener listener;
    Dialog dialog;
    Button positiveButton;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.white));

        }

        //Toast.makeText(newAccount.this,"navigation ba color "+getWindow().getNavigationBarColor(),Toast.LENGTH_LONG).show();
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_reset);

        gone = findViewById(R.id.gone);
        confirm_password = findViewById(R.id.confirm_password);
        new_password = findViewById(R.id.new_password);
        update = findViewById(R.id.update);
        linearLayout = findViewById(R.id.linearLayout1);
        back = findViewById(R.id.toolbar_back).findViewById(R.id.toolbarback);

        if(getIntent().getBooleanExtra("from_other_activity",false)) {
            PHONE = getIntent().getStringExtra("phone");
            username = getIntent().getStringExtra("username");
            EMAIL = getIntent().getStringExtra("email");
            ID = getIntent().getStringExtra("id");
            UID = getIntent().getStringExtra("uid");
            branch = getIntent().getStringExtra("branch");
            order_id = getIntent().getStringExtra("order_id");
            delivery_type = getIntent().getStringExtra("deliver_type");
            token = getIntent().getStringExtra("token");
            CLASS = getIntent().getStringExtra("class");
            from_other_activity = getIntent().getBooleanExtra("from_other_activity", false);
            //Toast.makeText(reset.this, ID,Toast.LENGTH_LONG).show();
        }

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
                if (keypadHeight > screenHeight * 0.15) {
                    gone.setVisibility(View.GONE);
                } else {
                    gone.setVisibility(View.VISIBLE);
                }
            }
        });
        dialog=new Dialog(reset.this);
        dialog.setContentView(R.layout.verified);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations=R.style.CustomActivityAnimation2;
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected(reset.this)) {
                    // showCustomDialog();
                    Toast.makeText(reset.this, "no internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    password1 = new_password.getEditText().getText().toString().trim();
                    password2 = confirm_password.getEditText().getText().toString().trim();
                    if (PasswordValidation(password1)) {
                        if (password1.equals(password2)) {
                            // linearLayout.setVisibility(View.VISIBLE);
                            confirm_password.setError(null);
                            confirm_password.setErrorEnabled(false);
                            try{
                                uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                                phone = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()).substring(3);
                                mail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                              //  Toast.makeText(reset.this, uid, Toast.LENGTH_LONG).show();

                            }catch (Exception e){
                                Toast.makeText(reset.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            try {
                                ref = FirebaseDatabase.getInstance().getReference("Admin");

                                try {
                                    listener=ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            name = Objects.requireNonNull(snapshot.child(uid).child("name").getValue()).toString();
                                            id = Objects.requireNonNull(snapshot.child(uid).child("id").getValue()).toString();
                                            old_password=Objects.requireNonNull(snapshot.child(uid).child("password").getValue()).toString();
                                            if(getIntent().getBooleanExtra("from_other_activity",false)) {
                                                if (CLASS.equals("Dashboard")) {
                                                    intent = new Intent(reset.this, Dashboard.class);
                                                } else {
                                                    if (CLASS.equals("CheckOrder")) {
                                                        intent = new Intent(reset.this, Check_orders.class);
                                                    } else {
                                                        if (CLASS.equals("Orders")) {
                                                            intent = new Intent(reset.this, Orders.class);
                                                        }
                                                    }
                                                }
                                                intent.putExtra("class", CLASS);
                                                intent.putExtra("order_id", order_id);
                                                intent.putExtra("username", username);
                                                intent.putExtra("email", EMAIL);
                                                intent.putExtra("phone",PHONE);
                                                intent.putExtra("uid", UID);
                                                intent.putExtra("id", ID);
                                                intent.putExtra("branch", branch);
                                                intent.putExtra("deliver_type", delivery_type);
                                                intent.putExtra("token", token);
                                                intent.putExtra("from_other_activity",true);
                                                intent.putExtra("order_status", getIntent().getStringExtra("order_status"));

                                            }
                                            else {
                                                intent = new Intent(reset.this, Dashboard.class);
                                                intent.putExtra("username", name);
                                                intent.putExtra("phone", phone);
                                                intent.putExtra("email", mail);
                                                intent.putExtra("uid", uid);
                                                intent.putExtra("id", id);
                                            }
                                           // Toast.makeText(reset.this,name+old_password+phone,Toast.LENGTH_LONG).show();
                                            try{
                                                AuthCredential credential = EmailAuthProvider.getCredential(mail,old_password);
                                               // Toast.makeText(reset.this,phone,Toast.LENGTH_LONG).show();
                                                Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).updatePassword(password1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            try{
                                                                ref.child(uid).child("password").setValue(password1);
                                                                FirebaseDatabase.getInstance().getReference("Admin").child("Registered_Phone").child(phone).child("password").setValue(password1);

                                                                show_permission();
                                                            }catch(Exception e){
                                                                Toast.makeText(reset.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                                            }



                                                        }
                                                        else{
                                                            Toast.makeText(reset.this,"Something went wrong,try again1",Toast.LENGTH_LONG).show();
                                                            show=false;
                                                        }

                                                    }
                                                });
                                            }catch (Exception e){
                                                Toast.makeText(reset.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }




                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(reset.this, "on cancelled ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (Exception e) {
                                    Toast.makeText(reset.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }




                            } catch (Exception e) {
                                Toast.makeText(reset.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            //linearLayout.setVisibility(View.INVISIBLE);
                            confirm_password.setError("Password does not match! Please try again.");
                        }
                    }

                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(reset.this, forgotPassword.class);
                if(getIntent().getBooleanExtra("from_other_activity",false)) {
                    intent.putExtra("class", CLASS);
                    intent.putExtra("order_id", order_id);
                    intent.putExtra("username", username);
                    intent.putExtra("email", EMAIL);
                    intent.putExtra("phone",PHONE);
                    intent.putExtra("uid", UID);
                    intent.putExtra("id", ID);
                    intent.putExtra("branch", branch);
                    intent.putExtra("deliver_type", delivery_type);
                    intent.putExtra("token", token);
                    intent.putExtra("from_other_activity",true);
                    intent.putExtra("order_status", getIntent().getStringExtra("order_status"));

                }

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
    private void show_permission() {


        try{

            if(!isFinishing())
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            builder=new AlertDialog.Builder(reset.this);
                            builder.setTitle("Password Updated")
                                    .setCancelable(false)
                                    .setMessage("Your password has been verified")
                                    .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //do nothing
                                            Toast.makeText(reset.this," logged in",Toast.LENGTH_LONG).show();
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            dialog.dismiss();
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                                           // finish();


                                        }
                                    })
                                    .setIcon(R.drawable.ic_sharp_verified_24);
                            AlertDialog dialog1=builder.create();
                            dialog1.show();
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); //create a new one
                            layoutParams.weight = 10;
                            layoutParams.gravity = Gravity.CENTER; //this is layout_gravity
                            dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setLayoutParams(layoutParams);
                            dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.green));
                        } catch (WindowManager.BadTokenException e) {
                            Toast.makeText(reset.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }


                    }
                });

                if (ref != null && listener != null) {
                 //   Toast.makeText(reset.this,"removed",Toast.LENGTH_LONG).show();

                    ref.removeEventListener(listener);
                }

            }
        }catch(Exception e){
            Toast.makeText(reset.this,"Something went wrong showing dialog,try again1 "+e.getMessage(),Toast.LENGTH_LONG).show();

        }
    }
    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi_connection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile_connection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi_connection != null && wifi_connection.isConnected()) || (mobile_connection != null && mobile_connection.isConnected())) {
            //Toast.makeText(reset.this,"secure connection",Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(reset.this,"no connection",Toast.LENGTH_LONG).show();

            return false;
        }

    }
    private void showCustomDialog() {
    }

    public Boolean PasswordValidation(String pass) {
        Pattern pattern = Pattern.compile("^" +

                "(?=.*[a-zA-Z0-9])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 4 characters
                "$");

        if (pass.trim().isEmpty()) {
            new_password.setError("Field can't be empty");
            return false;
        } else if (!pattern.matcher(pass).matches()) {
            new_password.setError("Password too weak");
            return false;
        } else {
            new_password.setError(null);
            new_password.setErrorEnabled(false);
            //  Toast.makeText(getApplicationContext(), "password validation", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(reset.this, forgotPassword.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if(getIntent().getBooleanExtra("from_other_activity",false)) {
            intent.putExtra("class", CLASS);
            intent.putExtra("order_id", order_id);
            intent.putExtra("username", username);
            intent.putExtra("email", EMAIL);
            intent.putExtra("phone",PHONE);
            intent.putExtra("uid", UID);
            intent.putExtra("id", ID);
            intent.putExtra("branch", branch);
            intent.putExtra("deliver_type", delivery_type);
            intent.putExtra("token", token);
            intent.putExtra("from_other_activity",true);
            intent.putExtra("order_status", getIntent().getStringExtra("order_status"));

        }

        startActivity(intent);
        overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
        finish();
    }


    @Override
    protected void onStop() {
        super.onStop();
        /*if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }*/
        if (ref != null && listener != null) {
            ref.removeEventListener(listener);
        }
        if ( alert_dialog!=null && alert_dialog.isShowing() )
        {
            Toast.makeText(reset.this, "cancelled", Toast.LENGTH_LONG).show();
            alert_dialog.cancel();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (ref != null && listener != null) {
            ref.removeEventListener(listener);
        }
        if ( alert_dialog!=null && alert_dialog.isShowing() )
        {
            //Toast.makeText(reset.this, "cancelled1", Toast.LENGTH_LONG).show();
            alert_dialog.cancel();

        }
    }

    // 2) :
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ref != null && listener != null) {
            ref.removeEventListener(listener);
        }
        if ( alert_dialog!=null && alert_dialog.isShowing())
        {
           // Toast.makeText(reset.this, "cancelled2", Toast.LENGTH_LONG).show();
            alert_dialog.cancel();
        }
    }

}