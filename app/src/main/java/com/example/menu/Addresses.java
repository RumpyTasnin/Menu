package com.example.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Addresses extends AppCompatActivity {

    ImageView close;
    LinearLayout add_address;

    RecyclerView recyclerView;
    String uid,phone,username,email,address,order_id,CLASS,current_address,ranAddress,apartment_details,street_details,landmarks_details;
    Intent intent;

    Dialog dialog;
    EditText road_dialog,apartment_dialog,remark_dialog;
    TextView textView_address,textView_apartment,textView_street,textView_remark;
    String  label1;
    AutoCompleteTextView autoCompleteTextView;


    FirebaseRecyclerOptions<home_delivery_dataSet> options;
    FirebaseRecyclerAdapter<home_delivery_dataSet, FirebaseViewHolder5> firebaseAdapter;
    Query query;

    ValueEventListener listener;
    DatabaseReference databaseReference,adapter_reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        requestWindowFeature(1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_addresses);

        close=findViewById(R.id.close);
        add_address=findViewById(R.id.add_address);
        recyclerView=findViewById(R.id.firebase_recyclerview);

        order_id = String.valueOf(getIntent().getIntExtra("order_id", 0));
        username=getIntent().getStringExtra("username");
        phone=getIntent().getStringExtra("phone");
        email=getIntent().getStringExtra("email");
        uid=getIntent().getStringExtra("uid");
        address=getIntent().getStringExtra("deliver_address");
        CLASS=getIntent().getStringExtra("class");


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        dialog=new Dialog(Addresses.this);
        dialog.setContentView(R.layout.dialog3);
      //  dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations=R.style.CustomActivityAnimation2;
        road_dialog=dialog.findViewById(R.id.road1_dialog3);
        apartment_dialog=dialog.findViewById(R.id.apartment1_dialog3);
        remark_dialog=dialog.findViewById(R.id.remarks1_dialog3);
        textView_address=dialog.findViewById(R.id.address_dialog3);
        textView_apartment=dialog.findViewById(R.id.appartment2_dialog3);
        textView_street=dialog.findViewById(R.id.street_dialog3);
        textView_remark=dialog.findViewById(R.id.remarks_dialog3);

        textView_address.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        textView_apartment.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        textView_street.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        textView_remark.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        dialog.findViewById(R.id.home1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label1="Home";
            }
        });
        dialog.findViewById(R.id.work1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label1="Work";
            }
        });
        dialog.findViewById(R.id.others1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label1="Others";
            }
        });

        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                label1="";
                autoCompleteTextView=dialog.findViewById(R.id.autocomplete_fragment2);
                road_dialog.setText("");
                apartment_dialog.setText("");
                remark_dialog.setText("");
                autoCompleteTextView.setAdapter(new PlaceAutoSuggestAdapter(Addresses.this,android.R.layout.simple_list_item_1));
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        current_address=autoCompleteTextView.getText().toString();
                    }
                });
                dialog.findViewById(R.id.add_address).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isConnected(Addresses.this)){
                            ranAddress=getRandomAddress();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                            if(!autoCompleteTextView.getText().toString().isEmpty()&& !apartment_dialog.getText().toString().isEmpty()&&!road_dialog.getText().toString().isEmpty()){

                                textView_address.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                textView_apartment.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                textView_street.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                textView_remark.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                                current_address=autoCompleteTextView.getText().toString();
                                apartment_details=apartment_dialog.getText().toString();
                                street_details=road_dialog.getText().toString();
                                if(!remark_dialog.getText().toString().isEmpty()){
                                    landmarks_details=remark_dialog.getText().toString();
                                }
                                databaseReference.child("deliver_address").setValue(ranAddress);
                                databaseReference.child("Deliver").child(ranAddress).child("location").setValue(current_address);
                                databaseReference.child("Deliver").child(ranAddress).child("name").setValue(ranAddress);
                                databaseReference.child("Deliver").child(ranAddress).child("landmark").setValue(landmarks_details);
                                databaseReference.child("Deliver").child(ranAddress).child("apartment").setValue(apartment_details);
                                databaseReference.child("Deliver").child(ranAddress).child("road_no").setValue(street_details);
                                if(label1.isEmpty()){
                                    databaseReference.child("Deliver").child(ranAddress).child("type").setValue("Others");
                                }
                                else {
                                    databaseReference.child("Deliver").child(ranAddress).child("type").setValue(label1);
                                }
                               // Toast.makeText(Addresses.this,"label"+label1 , Toast.LENGTH_LONG).show();
                                dialog.dismiss();

                            }
                            else{

                                if(autoCompleteTextView.getText().toString().isEmpty()){
                                    textView_address.setTextColor(getResources().getColor(R.color.red));

                                }
                                if(apartment_dialog.getText().toString().isEmpty()){
                                    textView_apartment.setTextColor(getResources().getColor(R.color.red));

                                }
                                if(road_dialog.getText().toString().isEmpty()){
                                    textView_street.setTextColor(getResources().getColor(R.color.red));

                                }

                            }
                        }

                    }
                });
                dialog.findViewById(R.id.close_dialog3).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

            }



        });

        query = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("Deliver");
        options = new FirebaseRecyclerOptions.Builder<home_delivery_dataSet>().setQuery(query, home_delivery_dataSet.class).build();


        firebaseAdapter = new FirebaseRecyclerAdapter<home_delivery_dataSet, FirebaseViewHolder5>(options) {
            @NonNull
            @Override
            public FirebaseViewHolder5 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(Addresses.this)
                        .inflate(R.layout.addresses_row_layout, parent, false);
                FirebaseViewHolder5 firebaseViewHolder = new FirebaseViewHolder5(view);
                return firebaseViewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull FirebaseViewHolder5 holder, int position, @NonNull home_delivery_dataSet model) {
                // Toast.makeText(home_delivery.this, "bind view holder", Toast.LENGTH_LONG).show();

                try {
                    holder.location2.setText(model.getType());
                    holder.location1.setText(model.getLocation());

                } catch (Exception e) {
                    Toast.makeText(Addresses.this, "data change add value event listener" + e.getMessage(), Toast.LENGTH_LONG).show();

                }
              try {
              //   Toast.makeText(Addresses.this,model.getType() , Toast.LENGTH_LONG).show();

                    if (model.getType().contains("Home")) {
                        holder.logo.setImageResource(R.drawable.yellow_home);
                       // Toast.makeText(Addresses.this,model.getType() , Toast.LENGTH_LONG).show();
                    } else {
                        if (model.getType().contains("Work")) {
                            holder.logo.setImageResource(R.drawable.yellow_work);
                          //  Toast.makeText(Addresses.this,model.getType() , Toast.LENGTH_LONG).show();
                        } else {
                            if (model.getType().contains("Others")) {
                                holder.logo.setImageResource(R.drawable.yellow_others);
                                //Toast.makeText(Addresses.this,model.getType() , Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(Addresses.this, "type error" + e.getMessage(), Toast.LENGTH_LONG).show();


                }
                holder.row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i=new Intent(Addresses.this,edit_address.class);
                        i.putExtra("address",model.getLocation());
                        i.putExtra("order_id",Integer.parseInt(order_id));
                        i.putExtra("username",username);
                        i.putExtra("phone",phone);
                        i.putExtra("email",email);
                        i.putExtra("uid",uid);
                        i.putExtra("deliver_address",model.getName());
                        i.putExtra("not_order_class",true);
                        i.putExtra("from_addresses_class",true);
                        i.putExtra("class",CLASS);
                       // Toast.makeText(Addresses.this, " going to edit class " , Toast.LENGTH_LONG).show();
                        startActivity(i);

                    }
                });


            }

        };
        try {
            firebaseAdapter.startListening();
            recyclerView.setLayoutManager(new LinearLayoutManager(Addresses.this));
            recyclerView.setAdapter(firebaseAdapter);

            ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        } catch (Exception e) {
            Toast.makeText(Addresses.this,"address"+ e.getMessage(), Toast.LENGTH_LONG).show();
        }




    }

    ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            if(direction==ItemTouchHelper.LEFT){
                //Toast.makeText(Addresses.this,"swiped left to delete "+  firebaseAdapter.getRef(viewHolder.getAdapterPosition()),Toast.LENGTH_LONG).show();

                adapter_reference=firebaseAdapter.getRef(viewHolder.getAdapterPosition());
                listener=adapter_reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try{
                            if(snapshot.hasChild("name")) {
                                if (address.equals(snapshot.child("name").getValue().toString())) {
                                    databaseReference.child("deliver_address").setValue("");
                                }
                            }
                            adapter_reference.removeValue();
                            if ( adapter_reference!= null && listener != null) {
                                adapter_reference.removeEventListener(listener);
                                // Toast.makeText(Addresses.this,"removed value event listener ",Toast.LENGTH_LONG).show();

                            }
                        } catch (Exception e){
                            Toast.makeText(Addresses.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(Addresses.this, R.color.yellow))
                    .addSwipeLeftLabel("Delete")
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void close(){


        if(CLASS.equals("MainMenu")){
            intent =new Intent(Addresses.this,MainMenu.class);
        }else{
            if(CLASS.equals("Classic")){
                intent =new Intent(Addresses.this,Classic.class);
            } else{
                if(CLASS.equals("Specials")){
                    intent =new Intent(Addresses.this,Specials.class);
                }
                else {
                    if(CLASS.equals("HOT")){
                        intent =new Intent(Addresses.this,HOT.class);
                    }
                    else{
                        if(CLASS.equals("ICED")){
                            intent =new Intent(Addresses.this,ICED.class);
                        }
                        else{
                            if(CLASS.equals("TEA")){
                                intent =new Intent(Addresses.this,TEA.class);
                            }
                            else {
                                if(CLASS.equals("Freddo")){
                                    intent =new Intent(Addresses.this,Freddo.class);
                                }
                                else {
                                    if(CLASS.equals("Dessert")){
                                        intent =new Intent(Addresses.this,Dessert.class);
                                    }
                                    else{
                                        if(CLASS.equals("Others")){
                                            intent =new Intent(Addresses.this,Others.class);
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
    @Override
    protected void onStop() {
        super.onStop();
        if ( adapter_reference!= null && listener != null) {
            adapter_reference.removeEventListener(listener);
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( adapter_reference!= null && listener != null) {
            adapter_reference.removeEventListener(listener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( adapter_reference!= null && listener != null) {
            adapter_reference.removeEventListener(listener);
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        if ( adapter_reference!= null && listener != null) {
            adapter_reference.removeEventListener(listener);
        }
    }
    private boolean isConnected(Context context) {
        boolean RETURN = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi_connection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile_connection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi_connection != null && wifi_connection.isConnected()) || (mobile_connection != null && mobile_connection.isConnected())) {

            RETURN = true;
        } else {
            Toast.makeText(Addresses.this, "no connection", Toast.LENGTH_LONG).show();

            RETURN = false;
        }
        return RETURN;
    }

}
