package com.example.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class profile extends AppCompatActivity {

    EditText name,adr;
    TextView mail,phn,name_change,address_change,update1,update2,cancel1,cancel2;
    String uid, order_id, username,phone, email, address,ranAddress,CLASS,ADDRESS,new_username;

    DatabaseReference databaseReference,reference,place_order_reference,user_reference;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_profile);

        name=findViewById(R.id.name);
        adr=findViewById(R.id.address);
        mail=findViewById(R.id.email);
        phn=findViewById(R.id.phone);
        name_change=findViewById(R.id.name_change);
        address_change=findViewById(R.id.address_change);
        update1=findViewById(R.id.save1);
        update2=findViewById(R.id.save2);
        cancel1=findViewById(R.id.cance1);
        cancel2=findViewById(R.id.cancel2);

        name.setFocusable(false);
        adr.setFocusable(false);

        username=getIntent().getStringExtra("username");
        phone=getIntent().getStringExtra("phone");
        email=getIntent().getStringExtra("email");
        uid=getIntent().getStringExtra("uid");
        address=getIntent().getStringExtra("deliver_address");
        order_id=String.valueOf(getIntent().getIntExtra("order_id",0));
        CLASS=getIntent().getStringExtra("class");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user_reference= FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            reference=FirebaseDatabase.getInstance().getReference("cart");
            databaseReference = FirebaseDatabase.getInstance().getReference("orders");
            place_order_reference=FirebaseDatabase.getInstance().getReference().child("placed_order");
        }

        user_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild("Deliver")){
                    if(snapshot.child("Deliver").hasChild(address)){
                        if(snapshot.child("Deliver").child(address).hasChild("full_address")){
                            adr.setText(snapshot.child("Deliver").child(address).child("full_address").getValue().toString());

                        }
                        else {
                            if(snapshot.child("Deliver").child(address).hasChild("location")){
                                adr.setText(snapshot.child("Deliver").child(address).child("location").getValue().toString());

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        name.setText(username);
        phn.setText(phone);
        mail.setText(email);


        update1.setVisibility(View.GONE);
        update2.setVisibility(View.GONE);
        cancel1.setVisibility(View.GONE);
        cancel2.setVisibility(View.GONE);

        name_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update1.setVisibility(View.VISIBLE);
                cancel1.setVisibility(View.VISIBLE);
                name_change.setVisibility(View.GONE);
                name.getText().clear();
                name.setFocusable(true);
                name.setFocusableInTouchMode(true);
                name.requestFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.showSoftInput( name, 0);
            }
        });
        address_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update2.setVisibility(View.VISIBLE);
                cancel2.setVisibility(View.VISIBLE);
                address_change.setVisibility(View.GONE);
                ADDRESS=adr.getText().toString();
                adr.getText().clear();
                adr.setFocusable(true);
                adr.setFocusableInTouchMode(true);
                adr.requestFocus();

            }
        });

        update1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_username=name.getText().toString();
                if(new_username.isEmpty()){
                    Toast.makeText(profile.this,"Field can not be empty",Toast.LENGTH_LONG).show();
                }else{
                    username=new_username;
                    user_reference.child("name").setValue(username);
                    if(!order_id.equals("0")){

                        place_order_reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChild(order_id)){
                                    Boolean confirm= (Boolean) snapshot.child(order_id).child("Confirmed").getValue();
                                    if(!confirm){

                                        databaseReference.child(order_id).child("customer_name").setValue(username);
                                        reference.child(order_id).child("user_info").child("customer_name").setValue(username);
                                        place_order_reference.child(order_id).child("user_info").child("customer_name").setValue(username);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    update1.setVisibility(View.GONE);
                    cancel1.setVisibility(View.GONE);
                    name.clearFocus();
                    name.setFocusable(false);
                    name.setFocusableInTouchMode(false);
                    name_change.setVisibility(View.VISIBLE);
                    name.setFocusable(false);
                }

            }
        });

        update2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!adr.getText().toString().isEmpty()|| !(adr.getText().toString() ==null)){
                    ranAddress=getRandomAddress();
                    address=ranAddress;
                    user_reference.child(uid).child("address").setValue(adr.getText().toString());
                    user_reference.child(uid).child("Deliver").child(ranAddress).child("location").setValue(adr.getText().toString());
                    user_reference.child(uid).child("Deliver").child(ranAddress).child("remarks").setValue("");
                    user_reference.child(uid).child("Deliver").child(ranAddress).child("type").setValue("Home");
                    user_reference.child(uid).child("Deliver").child(ranAddress).child("name").setValue(ranAddress);
                    adr.setVisibility(View.VISIBLE);
                    adr.clearFocus();
                    adr.setFocusable(false);
                    adr.setFocusableInTouchMode(false);
                }
                else{
                    Toast.makeText(profile.this,"Field can not be empty",Toast.LENGTH_LONG).show();
                }

            }
        });

        cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update1.setVisibility(View.GONE);
                cancel1.setVisibility(View.GONE);
                name_change.setVisibility(View.VISIBLE);
                name.setText(username);
                name.clearFocus();
                name.setFocusable(false);
                name.setFocusableInTouchMode(false);

            }
        });
        cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update2.setVisibility(View.GONE);
                cancel2.setVisibility(View.GONE);
                address_change.setVisibility(View.VISIBLE);
                adr.setText(ADDRESS);
                adr.clearFocus();
                adr.setFocusable(false);
                adr.setFocusableInTouchMode(false);
            }
        });
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });




    }

    private void close() {


        if(CLASS.equals("MainMenu")){
            intent =new Intent(profile.this,MainMenu.class);
        }else{
            if(CLASS.equals("Classic")){
                intent =new Intent(profile.this,Classic.class);
            } else{
                if(CLASS.equals("Specials")){
                    intent =new Intent(profile.this,Specials.class);
                }
                else {
                    if(CLASS.equals("HOT")){
                        intent =new Intent(profile.this,HOT.class);
                    }
                    else{
                        if(CLASS.equals("ICED")){
                            intent =new Intent(profile.this,ICED.class);
                        }
                        else{
                            if(CLASS.equals("TEA")){
                                intent =new Intent(profile.this,TEA.class);
                            }
                            else {
                                if(CLASS.equals("Freddo")){
                                    intent =new Intent(profile.this,Freddo.class);
                                }
                                else {
                                    if(CLASS.equals("Dessert")){
                                        intent =new Intent(profile.this,Dessert.class);
                                    }
                                    else{
                                        if(CLASS.equals("Others")){
                                            intent =new Intent(profile.this,Others.class);
                                        }
                                    }

                                }

                            }
                        }

                    }
                }
            }
        }
        intent.putExtra("class",CLASS);
        intent.putExtra("order_id",Integer.parseInt(order_id));
        intent.putExtra("phone",phone);
        intent.putExtra("username",username);
        intent.putExtra("email",email);
        intent.putExtra("uid",uid);
        intent.putExtra("deliver_address",address);
        startActivity(intent);
        overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
        finish();

    }

    private String getRandomAddress() {
        Random random=new Random();
        String random_address= "ADDRESS";
        String characters="abcdefghijklmnopqrstuvwxyz0123456789XYZPQRSUV";
        int i=0;
        StringBuilder string=new StringBuilder();
        while (i<6){
            string.append(characters.charAt(random.nextInt(characters.length())));
            i++;
        }
        random_address=random_address+string.toString();

        return random_address;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        close();
    }
}