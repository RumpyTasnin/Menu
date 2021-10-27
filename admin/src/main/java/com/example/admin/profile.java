package com.example.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class profile extends AppCompatActivity {
    String phone, id, email, username, uid, branch, order_id, delivery_type, token,CLASS;
    EditText editText_username;
    String new_username="";
    TextView textView_email,textView_phone,textView_id,textView_updateOrchange;
    LinearLayout layout_change_update,layout_cancel,delete_account;
    ImageView close;
    Intent intent;
    boolean change=true;
    Dialog internet_connection_dialog;

    DatabaseReference adminReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_profile);

        editText_username=findViewById(R.id.name);
        textView_email=findViewById(R.id.email);
        textView_phone=findViewById(R.id.phone);
        textView_id=findViewById(R.id.employee_id);
        layout_change_update=findViewById(R.id.changeOrUpdate);
        layout_cancel=findViewById(R.id.cancel);
        close=findViewById(R.id.close);
        delete_account=findViewById(R.id.delete_account);
        textView_updateOrchange=findViewById(R.id.update_change);

        phone = getIntent().getStringExtra("phone");
        username = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        uid = getIntent().getStringExtra("uid");
        branch = getIntent().getStringExtra("branch");
        order_id = getIntent().getStringExtra("order_id");
        delivery_type = getIntent().getStringExtra("deliver_type");
        token = getIntent().getStringExtra("token");
        CLASS=getIntent().getStringExtra("class");

        internet_connection_dialog = new Dialog(profile.this);
        internet_connection_dialog.setContentView(R.layout.dialog_no_internet);
        internet_connection_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        internet_connection_dialog.getWindow().setGravity(Gravity.CENTER);
        internet_connection_dialog.setCancelable(true);
        internet_connection_dialog.getWindow().getAttributes().windowAnimations = R.style.CustomActivityAnimation2;
        internet_connection_dialog.findViewById(R.id.try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected(profile.this)){
                    internet_connection_dialog.dismiss();

                }
            }
        });

        editText_username.setText(username);
        textView_email.setText(email);
        textView_phone.setText(phone);
        textView_id.setText(id);

        adminReference= FirebaseDatabase.getInstance().getReference().child("Admin");

        layout_change_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(change){
                    layout_cancel.setVisibility(View.VISIBLE);
                    textView_updateOrchange.setText("Update");
                    editText_username.setFocusable(true);
                    editText_username.setFocusableInTouchMode(true);
                    editText_username.requestFocus();
                    change=false;
                }
                else{
                    ///////////////update///////////////////////////

                    if(!editText_username.getText().toString().isEmpty()){
                        if (isConnected(profile.this)) {
                            username=editText_username.getText().toString();
                            adminReference.child(uid).child("name").setValue(username);
                            editText_username.clearFocus();
                            editText_username.setFocusable(false);
                            editText_username.setFocusableInTouchMode(false);
                            layout_cancel.setVisibility(View.GONE);
                            textView_updateOrchange.setText("Change");
                            change=true;
                        }
                        else{
                            internet_connection_dialog.show();
                        }

                    }
                    else{
                        Toast.makeText(profile.this,"Field can not be empty",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        layout_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_username.clearFocus();
                editText_username.setFocusable(false);
                editText_username.setFocusableInTouchMode(false);
                layout_cancel.setVisibility(View.GONE);
                editText_username.setText(username);
                layout_cancel.setVisibility(View.GONE);
                textView_updateOrchange.setText("Change");
                change=true;

            }
        });

        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected(profile.this)){
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(profile.this);
                        builder.setTitle("Account Delete");
                        builder.setCancelable(false);
                        builder.setMessage("Are you sure you want to delete your account?");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                    stopService(new Intent(profile.this,notificationService.class));
                                    FirebaseAuth.getInstance().getCurrentUser().delete();
                                    adminReference.child(uid).removeValue();
                                    adminReference.child("Registered_Phone").child(phone).removeValue();
                                    adminReference.child("registered_admins").child(id).removeValue();

                                    dialog.dismiss();

                                    Toast.makeText(profile.this, "account deleted", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(profile.this, Login.class);
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
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        negativeButton.setTextColor(getResources().getColor(R.color.green));
                        positiveButton.setTextColor(getResources().getColor(R.color.green));
                    }

                }
                else{
                    internet_connection_dialog.show();
                }

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

    }

    private void close() {
        if(CLASS.equals("Dashboard")){
            intent=new Intent(profile.this,Dashboard.class);
        }
        else{
            if(CLASS.equals("CheckOrder")){
                intent=new Intent(profile.this,Check_orders.class);
            }
            else{
                if(CLASS.equals("Orders")){
                    intent=new Intent(profile.this,Orders.class);
                }
            }
        }
        intent.putExtra("class",CLASS);
        intent.putExtra("order_id",order_id);
        intent.putExtra("phone",phone);
        intent.putExtra("username",username);
        intent.putExtra("email",email);
        intent.putExtra("uid",uid);
        intent.putExtra("id",id);
        intent.putExtra("branch",branch);
        intent.putExtra("deliver_type",delivery_type);
        intent.putExtra("token",token);
        intent.putExtra("order_status", getIntent().getStringExtra("order_status"));
        startActivity(intent);
        overridePendingTransition(0, R.anim.animate_slide_down_exit);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        close();
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
            //Toast.makeText(profile.this, "secure connection", Toast.LENGTH_LONG).show();

            RETURN = true;
        } else {
            Toast.makeText(profile.this, "no connection", Toast.LENGTH_LONG).show();
            /*if (!internet_connection_dialog.isShowing()) {
                internet_connection_dialog.show();
            }*/

            RETURN = false;
        }
        return RETURN;
    }
}