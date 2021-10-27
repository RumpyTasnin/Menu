package com.example.menu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;/*
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
*/
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;
import static com.google.android.material.transition.MaterialSharedAxis.Y;

public class edit_address extends AppCompatActivity implements View.OnTouchListener, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    String address,s1,order_id,username,phone,email,deliver_address,uid,type,street_address,apartment_details,landamrk_details,full_address_details,s2;

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    Geocoder geocoder;
    private GoogleMap mMap;
    private int _yDelta;
    Marker marker1;
    MarkerOptions markerOptions;
    LatLng latLng;
    Address address1;
    List<Address> addresses;
    int height;
    AutoCompleteTextView address_search,autoCompleteTextView;
    private static final String TAG = "edit_address";
    TextView road,missing,text_full_address;
    TextInputLayout street,apartment,landmark,full_address;
    LinearLayout save_changes;
    boolean gone,gone1;
    ImageView close1;
    Intent intent1;
    Button home,work,others;
    String label="Home";

    Intent intent;

    String CLASS;
    boolean not_order_class;


    int food_image,layout_no;
    String food_name,food_description,food_price_intent,food_size_regular,food_size_large,activity,regular_price,large_price;
    boolean visibility,radio_group_visibility;

    DatabaseReference databaseReference,reference,place_order_reference,user_reference;
    PlacesClient placesClient;
    AutocompleteSupportFragment autocompleteSupportFragment;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //status bar transparent
        requestWindowFeature(1);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_edit_address);



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String apiKey = "Yor API key";

   // Setup Places Client
    if (!Places.isInitialized()) {
           Places.initialize(edit_address.this, apiKey);
        }
      // Retrieve a PlacesClient (previously initialized - see MainActivity)
        placesClient =Places.createClient(this);



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);

         height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

      road=findViewById(R.id.road);
      missing=findViewById(R.id.missing);
      street=findViewById(R.id.street);
      apartment=findViewById(R.id.apartment);
      landmark=findViewById(R.id.landmark);
      save_changes=findViewById(R.id.save_changes);
      text_full_address=findViewById(R.id.text_full_address);
      full_address=findViewById(R.id.full_address);
      close1=findViewById(R.id.close1);
      home=findViewById(R.id.home1);
      work=findViewById(R.id.work1);
      others=findViewById(R.id.others1);
      home.setOnClickListener(this);
      work.setOnClickListener(this);
      others.setOnClickListener(this);


        order_id = String.valueOf(getIntent().getIntExtra("order_id",0));
        username=getIntent().getStringExtra("username");
        phone=getIntent().getStringExtra("phone");
        uid=getIntent().getStringExtra("uid");
        email=getIntent().getStringExtra("email");
        address=getIntent().getStringExtra("address");
        deliver_address=getIntent().getStringExtra("deliver_address");
       // Toast.makeText(edit_address.this,address,Toast.LENGTH_LONG).show();
        not_order_class=getIntent().getBooleanExtra("not_order_class",false);
        CLASS=getIntent().getStringExtra("class");
        if(!getIntent().getBooleanExtra("from_addresses_class",false)){
            if(!not_order_class){
            food_image=getIntent().getIntExtra("food_image",0);
            food_name=getIntent().getStringExtra("food_name");
            food_description=getIntent().getStringExtra("food_description").trim();
            food_price_intent=getIntent().getStringExtra("food_price");
            visibility=getIntent().getBooleanExtra("add_ons_visibility",true);
            radio_group_visibility=getIntent().getBooleanExtra("radio",true);
            food_size_regular=getIntent().getStringExtra("regular");
            food_size_large=getIntent().getStringExtra("large");
            activity=getIntent().getStringExtra("activity");
            regular_price=getIntent().getStringExtra("regular_price");
            large_price=getIntent().getStringExtra("large_price");
            layout_no=getIntent().getIntExtra("layout",1);
        }
        }






        close1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               close();
                startActivity(intent1);
                overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                finish();

            }
        });

        save_changes.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             // Toast.makeText(edit_address.this,"typee  "+type,Toast.LENGTH_LONG).show();

              if(getIntent().getBooleanExtra("from_addresses_class",false)){
                 // Toast.makeText(edit_address.this,"from addresses class "+ deliver_address,Toast.LENGTH_LONG).show();
                  try{
                      intent=new Intent(edit_address.this,Addresses.class);
                      intent.putExtra("address",s2);
                      intent.putExtra("order_id",Integer.parseInt(order_id));
                      intent.putExtra("username",username);
                      intent.putExtra("phone",phone);
                      intent.putExtra("uid",uid);
                      intent.putExtra("email",email);
                      intent.putExtra("deliver_address",deliver_address);
                      intent.putExtra("class",CLASS);
                      intent.putExtra("from_current_location_layout",getIntent().getBooleanExtra("from_current_location_layout",false));


                      FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                      if (user != null) {
                          databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                     /* reference=FirebaseDatabase.getInstance().getReference("cart");
                      databaseReference = FirebaseDatabase.getInstance().getReference("orders");
                      place_order_reference=FirebaseDatabase.getInstance().getReference().child("placed_order");
                */  }

                      if(!landmark.getEditText().getText().toString().isEmpty()){
                          landamrk_details=landmark.getEditText().getText().toString();
                          databaseReference.child("Deliver").child(deliver_address).child("landmark").setValue(landamrk_details);
                        //  Toast.makeText(edit_address.this,"from addresses class "+ deliver_address +landamrk_details,Toast.LENGTH_LONG).show();

                      }
                      if(!gone){
                          if(!gone1){
                              if(!street.getEditText().getText().toString().trim().isEmpty() && !apartment.getEditText().getText().toString().trim().isEmpty()){
                                  street_address=street.getEditText().getText().toString().trim();
                                  apartment_details=apartment.getEditText().getText().toString().trim();
                                  databaseReference.child("Deliver").child(deliver_address).child(type).setValue(street_address);
                                  databaseReference.child("Deliver").child(deliver_address).child("apartment").setValue(apartment_details);
                                  databaseReference.child("Deliver").child(deliver_address).child("type").setValue(label);

                                 // Toast.makeText(edit_address.this," from edit address class "+apartment.getEditText().getText().toString().isEmpty(),Toast.LENGTH_LONG).show();
                                  street.setError(null);
                                  street.setErrorEnabled(false);
                                  apartment.setError(null);
                                  apartment.setErrorEnabled(false);
                                  if(!full_address.getEditText().getText().toString().isEmpty()){
                                      full_address_details=full_address.getEditText().getText().toString();
                                      databaseReference.child("Deliver").child(deliver_address).child("full_address").setValue(full_address_details);
                                  }
                                 // Toast.makeText(edit_address.this,"from addresses class "+ street_address,Toast.LENGTH_LONG).show();
                                  startActivity(intent);
                                  overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                                  finish();
                              }
                              else{

                                  if(full_address.getEditText().getText().toString().isEmpty()){
                                      missing.setTextColor(getResources().getColor(R.color.red));

                                      if(street.getEditText().getText().toString().trim().isEmpty()){
                                          street.setError("Field can not be empty");
                                      }
                                      else{
                                          street.setError(null);
                                          street.setErrorEnabled(false);
                                      }
                                      if(apartment.getEditText().getText().toString().trim().isEmpty()){
                                          apartment.setError("Field can not be empty");
                                      }
                                      else {
                                          apartment.setError(null);
                                          apartment.setErrorEnabled(false);
                                      }
                                  }
                                  else{

                                      if(!apartment.getEditText().getText().toString().isEmpty()){
                                          full_address_details=full_address.getEditText().getText().toString();
                                          apartment_details=apartment.getEditText().getText().toString().trim();
                                          databaseReference.child("Deliver").child(deliver_address).child("apartment").setValue(apartment_details);
                                          databaseReference.child("Deliver").child(deliver_address).child("full_address").setValue(full_address_details);
                                          apartment.setError(null);
                                          apartment.setErrorEnabled(false);
                                          startActivity(intent);
                                          overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                                          finish();
                                         // Toast.makeText(edit_address.this," from addreses class full_address_details "+full_address_details,Toast.LENGTH_LONG).show();


                                      }
                                      else {
                                          apartment.setError("Field can not be empty");
                                      }

                                  }
                              }

                          }
                          else {
                              //Toast.makeText(edit_address.this,"gone1  true ",Toast.LENGTH_SHORT).show();

                              if(!full_address.getEditText().getText().toString().isEmpty()&&!apartment.getEditText().getText().toString().isEmpty()){
                                  full_address_details=full_address.getEditText().getText().toString();
                                  apartment_details=apartment.getEditText().getText().toString().trim();
                                  apartment_details=apartment.getEditText().getText().toString().trim();
                                  databaseReference.child("Deliver").child(deliver_address).child("apartment").setValue(apartment_details);
                                  databaseReference.child("Deliver").child(deliver_address).child("full_address").setValue(full_address_details);
                                  apartment.setError(null);
                                  apartment.setErrorEnabled(false);
                                  full_address.setErrorEnabled(false);
                                  full_address.setError(null);
                                  // Toast.makeText(edit_address.this,"full_address_details"+ full_address_details,Toast.LENGTH_LONG).show();

                                  startActivity(intent);
                                  overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                                  finish();

                              }
                              else {
                                  if(full_address.getEditText().getText().toString().isEmpty()){
                                      text_full_address.setTextColor(getResources().getColor(R.color.red));
                                      full_address.setError("Field can not be empty");
                                  }
                                  else {
                                      full_address.setErrorEnabled(false);
                                      full_address.setError(null);
                                  }
                                  if(apartment.getEditText().getText().toString().trim().isEmpty()){
                                      apartment.setError("Field can not be empty");
                                  }
                                  else {
                                      apartment.setError(null);
                                      apartment.setErrorEnabled(false);
                                  }
                              }
                          }
                      }
                      else {
                         // Toast.makeText(edit_address.this," from address class gone true ",Toast.LENGTH_SHORT).show();

                          if(!gone1){
                              if(apartment.getEditText().getText().toString().trim().isEmpty()){
                                  apartment.setError("Field can not be empty");
                              }
                              else {
                                  apartment_details=apartment.getEditText().getText().toString().trim();
                                  databaseReference.child("Deliver").child(deliver_address).child("apartment").setValue(apartment_details);
                                  //Toast.makeText(edit_address.this," from address class gone true "+apartment_details,Toast.LENGTH_SHORT).show();
                                  apartment.setError(null);
                                  apartment.setErrorEnabled(false);
                                  if(!full_address.getEditText().getText().toString().isEmpty()){
                                      full_address_details=full_address.getEditText().getText().toString();
                                      databaseReference.child("Deliver").child(deliver_address).child("full_address").setValue(full_address_details);
                                    //  Toast.makeText(edit_address.this," from address class gone true "+full_address_details,Toast.LENGTH_SHORT).show();
                                  }
                                  startActivity(intent);
                                  overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                                  finish();

                              }


                          }
                          else {
                              if(!full_address.getEditText().getText().toString().isEmpty()&&!apartment.getEditText().getText().toString().isEmpty()){
                                  databaseReference.child("Deliver").child(deliver_address).child("apartment").setValue(apartment_details);
                                  databaseReference.child("Deliver").child(deliver_address).child("full_address").setValue(full_address_details);
                                  apartment.setError(null);
                                  apartment.setErrorEnabled(false);
                                  full_address.setErrorEnabled(false);
                                  full_address.setError(null);
                                  startActivity(intent);
                                  overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                                  finish();
                              }
                              else{
                                  if(apartment.getEditText().getText().toString().trim().isEmpty()){
                                      apartment.setError("Field can not be empty");
                                  }
                                  else {
                                      apartment.setError(null);
                                      apartment.setErrorEnabled(false);
                                  }
                                  if(full_address.getEditText().getText().toString().isEmpty()){
                                      text_full_address.setTextColor(getResources().getColor(R.color.red));
                                      full_address.setError("Field can not be empty");
                                  }
                                  else {
                                      full_address.setErrorEnabled(false);
                                      full_address.setError(null);
                                  }

                              }

                          }
                      }


                  }
                  catch (Exception e){
                      Toast.makeText(edit_address.this,e.getMessage(),Toast.LENGTH_LONG).show();
                  }

              }
              else {
                 // Toast.makeText(edit_address.this," not from addresses class "+full_address_details,Toast.LENGTH_LONG).show();

                  intent=new Intent(edit_address.this,home_delivery.class);
                  intent.putExtra("address",s2);
                  intent.putExtra("order_id",Integer.parseInt(order_id));
                  intent.putExtra("username",username);
                  intent.putExtra("phone",phone);
                  intent.putExtra("uid",uid);
                  intent.putExtra("email",email);
                  intent.putExtra("label",label);
                  intent.putExtra("deliver_address",deliver_address);
                  intent.putExtra("from_edit_address",true);
                  intent.putExtra("not_order_class",not_order_class);
                  intent.putExtra("class",CLASS);
                  intent.putExtra("from_current_location_layout",getIntent().getBooleanExtra("from_current_location_layout",false));
                  if(!not_order_class){

                      intent.putExtra("food_image",food_image);
                      intent.putExtra("food_name",food_name);
                      intent.putExtra("food_description",food_description);
                      intent.putExtra("food_price",food_price_intent);
                      intent.putExtra("regular",food_size_regular);
                      intent.putExtra("large",food_size_large);
                      intent.putExtra("add_ons_visibility",visibility);
                      intent.putExtra("radio",radio_group_visibility);
                      intent.putExtra("regular_price",regular_price);
                      intent.putExtra("large_price",large_price);
                      intent.putExtra("activity",activity);
                      intent.putExtra("layout",layout_no);
                      if(getIntent().getBooleanExtra("from_favourites_class",false)){
                          intent.putExtra("from_favourites_class",true);
                      }
                  }
                  if(!landmark.getEditText().getText().toString().isEmpty()){
                      landamrk_details=landmark.getEditText().getText().toString();
                      intent.putExtra("landmark_details",landamrk_details);
                  }
                  if(!gone){
                      if(!gone1){
                          if(!street.getEditText().getText().toString().trim().isEmpty() && !apartment.getEditText().getText().toString().trim().isEmpty()){
                              street_address=street.getEditText().getText().toString().trim();
                              apartment_details=apartment.getEditText().getText().toString().trim();
                              intent.putExtra("street_details",street_address);
                              intent.putExtra("type",type);
                              intent.putExtra("apartment_details",apartment_details);///////////////////////////
                              //Toast.makeText(edit_address.this," from edit address class "+apartment.getEditText().getText().toString().isEmpty(),Toast.LENGTH_LONG).show();
                              street.setError(null);
                              street.setErrorEnabled(false);
                              apartment.setError(null);
                              apartment.setErrorEnabled(false);
                              if(!full_address.getEditText().getText().toString().isEmpty()){
                                  full_address_details=full_address.getEditText().getText().toString();
                                  intent.putExtra("full_address_details",full_address_details);
                              }
                              startActivity(intent);
                              overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                              finish();
                          }
                          else{

                              if(full_address.getEditText().getText().toString().isEmpty()){
                                  missing.setTextColor(getResources().getColor(R.color.red));

                                  if(street.getEditText().getText().toString().trim().isEmpty()){
                                      street.setError("Field can not be empty");
                                  }
                                  else{
                                      street.setError(null);
                                      street.setErrorEnabled(false);
                                  }
                                  if(apartment.getEditText().getText().toString().trim().isEmpty()){
                                      apartment.setError("Field can not be empty");
                                  }
                                  else {
                                      apartment.setError(null);
                                      apartment.setErrorEnabled(false);
                                  }
                              }
                              else{

                                  if(!apartment.getEditText().getText().toString().isEmpty()){
                                      full_address_details=full_address.getEditText().getText().toString();
                                      apartment_details=apartment.getEditText().getText().toString().trim();
                                      intent.putExtra("apartment_details",apartment_details);
                                      intent.putExtra("full_address_details",full_address_details);///////////////////////////
                                      apartment.setError(null);
                                      apartment.setErrorEnabled(false);
                                      startActivity(intent);
                                      overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                                      finish();
                                      //  Toast.makeText(edit_address.this,"full_address_details "+full_address_details,Toast.LENGTH_LONG).show();


                                  }
                                  else {
                                      apartment.setError("Field can not be empty");
                                  }

                              }
                          }

                      }
                      else {
                          //Toast.makeText(edit_address.this,"gone1  true ",Toast.LENGTH_SHORT).show();

                          if(!full_address.getEditText().getText().toString().isEmpty()&&!apartment.getEditText().getText().toString().isEmpty()){
                              full_address_details=full_address.getEditText().getText().toString();
                              apartment_details=apartment.getEditText().getText().toString().trim();
                              apartment_details=apartment.getEditText().getText().toString().trim();
                              intent.putExtra("apartment_details",apartment_details);
                              intent.putExtra("full_address_details",full_address_details);/////////////////////////////
                              apartment.setError(null);
                              apartment.setErrorEnabled(false);
                              full_address.setErrorEnabled(false);
                              full_address.setError(null);
                              startActivity(intent);
                              overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                              finish();

                          }
                          else {
                              if(full_address.getEditText().getText().toString().isEmpty()){
                                  text_full_address.setTextColor(getResources().getColor(R.color.red));
                                  full_address.setError("Field can not be empty");
                              }
                              else {
                                  full_address.setErrorEnabled(false);
                                  full_address.setError(null);
                              }
                              if(apartment.getEditText().getText().toString().trim().isEmpty()){
                                  apartment.setError("Field can not be empty");
                              }
                              else {
                                  apartment.setError(null);
                                  apartment.setErrorEnabled(false);
                              }
                          }
                      }
                  }
                  else {
                    //  Toast.makeText(edit_address.this,"gone true ",Toast.LENGTH_SHORT).show();

                      if(!gone1){
                          if(apartment.getEditText().getText().toString().trim().isEmpty()){
                              apartment.setError("Field can not be empty");
                          }
                          else {
                              apartment_details=apartment.getEditText().getText().toString().trim();
                              intent.putExtra("apartment_details",apartment_details);/////////////////////////////////////////
                              apartment.setError(null);
                              apartment.setErrorEnabled(false);
                              if(!full_address.getEditText().getText().toString().isEmpty()){
                                  full_address_details=full_address.getEditText().getText().toString();
                                  intent.putExtra("full_address_details",full_address_details);
                                  //     Toast.makeText(edit_address.this,"gone true "+ full_address_details,Toast.LENGTH_SHORT).show();
                              }
                              startActivity(intent);
                              overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                              finish();

                          }


                      }
                      else {
                          if(!full_address.getEditText().getText().toString().isEmpty()&&!apartment.getEditText().getText().toString().isEmpty()){
                              intent.putExtra("full_address_details",full_address.getEditText().getText().toString());
                              intent.putExtra("apartment_details",apartment.getEditText().getText().toString());
                              apartment.setError(null);
                              apartment.setErrorEnabled(false);
                              full_address.setErrorEnabled(false);
                              full_address.setError(null);
                              startActivity(intent);
                              overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                              finish();
                          }
                          else{
                              if(apartment.getEditText().getText().toString().trim().isEmpty()){
                                  apartment.setError("Field can not be empty");
                              }
                              else {
                                  apartment.setError(null);
                                  apartment.setErrorEnabled(false);
                              }
                              if(full_address.getEditText().getText().toString().isEmpty()){
                                  text_full_address.setTextColor(getResources().getColor(R.color.red));
                                  full_address.setError("Field can not be empty");
                              }
                              else {
                                  full_address.setErrorEnabled(false);
                                  full_address.setError(null);
                              }

                          }

                      }
                  }
              }



          }
      });

        ////##############################################################?//////////////////////////////

      autoCompleteTextView=findViewById(R.id.autocomplete_fragment);
      autoCompleteTextView.setAdapter(new PlaceAutoSuggestAdapter(edit_address.this,android.R.layout.simple_list_item_1));


     //   Toast.makeText(edit_address.this,"height "+height+" width "+width,Toast.LENGTH_LONG).show();
        findViewById(R.id.separator).setOnTouchListener(this);



        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder=new Geocoder(edit_address.this);




        //check permission
        if (ActivityCompat.checkSelfPermission(edit_address.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // when permission granted
            setMap();
           // init();
        } else {

            ActivityCompat.requestPermissions(edit_address.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
           // Toast.makeText(edit_address.this,"try again",Toast.LENGTH_LONG).show();
        }

    }


    private void close() {
        if(getIntent().getBooleanExtra("from_addresses_class",false)){
           // Toast.makeText(edit_address.this,"from addresses class",Toast.LENGTH_LONG).show();
            intent1=new Intent(edit_address.this,Addresses.class);
            intent1.putExtra("order_id",Integer.parseInt(order_id));
            intent1.putExtra("username",username);
            intent1.putExtra("phone",phone);
            intent1.putExtra("uid",uid);
            intent1.putExtra("email",email);
            intent1.putExtra("deliver_address",deliver_address);
            intent1.putExtra("class",CLASS);
            intent1.putExtra("from_current_location_layout",getIntent().getBooleanExtra("from_current_location_layout",false));

        }
        else{
            intent1=new Intent(edit_address.this,home_delivery.class);
            intent1.putExtra("address",s2);
            intent1.putExtra("order_id",Integer.parseInt(order_id));
            intent1.putExtra("username",username);
            intent1.putExtra("phone",phone);
            intent1.putExtra("uid",uid);
            intent1.putExtra("email",email);
            intent1.putExtra("deliver_address",deliver_address);
            intent1.putExtra("from_edit_address",false);
            intent1.putExtra("not_order_class",not_order_class);
            intent1.putExtra("class",CLASS);
            intent1.putExtra("from_current_location_layout",getIntent().getBooleanExtra("from_current_location_layout",false));



            if(!not_order_class){
                intent1.putExtra("food_image",food_image);
                intent1.putExtra("food_name",food_name);
                intent1.putExtra("food_description",food_description);
                intent1.putExtra("food_price",food_price_intent);
                intent1.putExtra("regular",food_size_regular);
                intent1.putExtra("large",food_size_large);
                intent1.putExtra("add_ons_visibility",visibility);
                intent1.putExtra("radio",radio_group_visibility);
                intent1.putExtra("regular_price",regular_price);
                intent1.putExtra("large_price",large_price);
                intent1.putExtra("activity",activity);
                intent1.putExtra("layout",layout_no);

                if(getIntent().getBooleanExtra("from_favourites_class",false)){
                    intent1.putExtra("from_favourites_class",true);
                }
            }

        }




    }


    private void setMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                                    .getString(R.string.style_json)));

                            if (!success) {
                               Toast.makeText(edit_address.this,"not success",Toast.LENGTH_LONG).show();
                            }
                            mMap = googleMap;
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            try {
                               // Toast.makeText(edit_address.this,address,Toast.LENGTH_LONG).show();
                                addresses=geocoder.getFromLocationName(address,1);
                                 address1=addresses.get(0);
                                if(addresses.size()>0 ){
                                    latLng=new LatLng(address1.getLatitude(),address1.getLongitude());
                                    markerOptions=new MarkerOptions().position(latLng)
                                            .title(address)
                                            .draggable(true);
                                    marker1=mMap.addMarker(markerOptions);
                                    marker1.showInfoWindow();
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
                                    set_address(address1);


                                   //  init();
                                    autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Log.d("Address : ",autoCompleteTextView.getText().toString());

                                            String searchString =autoCompleteTextView.getText().toString();
                                            address=searchString;

                                            Geocoder geocoder = new Geocoder(edit_address.this);
                                            List<Address> list = new ArrayList<>();
                                            try {
                                                list = geocoder.getFromLocationName(searchString, 1);
                                            } catch (IOException e) {
                                                Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
                                            }

                                            if (list.size() > 0) {
                                                address1 = list.get(0);
                                                latLng = new LatLng(address1.getLatitude(), address1.getLongitude());
                                                markerOptions = new MarkerOptions().position(latLng)
                                                        .title(searchString)
                                                        .draggable(true);
                                                if (marker1 != null) {
                                                    marker1.remove();
                                                }
                                                marker1 = mMap.addMarker(markerOptions);
                                                marker1.showInfoWindow();
                                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                                               set_address(address1);

                                            }
                                        }
                                    });



                                    mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                        @Override
                                        public void onMarkerDragStart(Marker marker) {

                                        }

                                        @Override
                                        public void onMarkerDrag(Marker marker) {

                                        }

                                        @Override
                                        public void onMarkerDragEnd(Marker marker) {
                                            latLng=marker.getPosition();

                                            try {
                                                addresses=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                                                if(addresses.size()>0){
                                                   address1=addresses.get(0);
                                                    String streetAddress=address1.getAddressLine(0);
                                                    address=streetAddress;
                                                    marker.setTitle(streetAddress);
                                                    marker.showInfoWindow();
                                                    autoCompleteTextView.setText("");
                                                    set_address(address1);
                                                   // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));

                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    });



                                }
                                else{
                                    Toast.makeText(edit_address.this,"addresses size less han 0",Toast.LENGTH_LONG).show();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

            }
        });


    }

    private void set_address(Address address1) {
        missing.setTextColor(getResources().getColor(R.color.colorPrimary));
        text_full_address.setTextColor(getResources().getColor(R.color.colorPrimary));
        street.setError(null);
        street.setErrorEnabled(false);
        full_address.setError(null);
        full_address.setErrorEnabled(false);
        full_address.getEditText().setText("");
        apartment.setError(null);
        apartment.setErrorEnabled(false);
        findViewById(R.id.or).setVisibility(View.VISIBLE);
        gone1=false;
        s1="";
        s2=address1.getAddressLine(0);
        if(address1.getSubThoroughfare()!=null){
            s1=s1+address1.getSubThoroughfare()+" ";
        }
        else {
            missing.setVisibility(View.VISIBLE);
            street.setVisibility(View.VISIBLE);
            street.setHint("Street");
            missing.setText("We are missing your street");
            type="street";
            gone=false;
        }
        if(address1.getThoroughfare()!=null){
            s1=s1+address1.getThoroughfare()+" ";
           // missing.setTextColor(getResources().getColor(R.color.colorPrimary));
            // Toast.makeText(edit_address.this," through fare "+s1,Toast.LENGTH_LONG).show();
        }
        else {
            missing.setVisibility(View.VISIBLE);
            street.setVisibility(View.VISIBLE);
            street.setHint("Area");
            type="area";
            missing.setText("We are missing your area");
            gone=false;
        }
        if(address1.getSubLocality()!=null){
            s1=s1+address1.getSubLocality();
            if(address1.getSubThoroughfare()==null&&address1.getThoroughfare()==null){
                missing.setVisibility(View.GONE);
                findViewById(R.id.or).setVisibility(View.GONE);
                street.setVisibility(View.GONE);
                gone=true;
                gone1=true;
            }

        }
        if(address1.getSubThoroughfare()!=null&&address1.getThoroughfare()!=null&&address1.getSubLocality()!=null){
            missing.setVisibility(View.GONE);
            street.setVisibility(View.GONE);
            full_address.setError(null);
            full_address.setErrorEnabled(false);
            gone=true;
        }
        else{
            s1=address1.getAddressLine(0);
        }
        road.setText(s1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==44){
            if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                try {
                    setMap();
                }
                catch (Exception e){}
            }
        }
    }

  @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int Y = (int) event.getRawY();
      Log.d(TAG,"on touch Y "+String.valueOf(Y));

   //  Toast.makeText(edit_address.this,"on touch Y "+String.valueOf(Y),Toast.LENGTH_LONG).show();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _yDelta = Y - lParams.bottomMargin;
                Log.d(TAG,"on touch Y delta down  "+String.valueOf(_yDelta));
              // Toast.makeText(edit_address.this,"on touch Y delta down "+String.valueOf(_yDelta),Toast.LENGTH_LONG).show();
                break;
            case MotionEvent.ACTION_UP:
               // Log.d(TAG,"on touch up  ");
              //  Toast.makeText(edit_address.this,"on touch up ",Toast.LENGTH_LONG).show();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
              if(Y<height-500&& Y>500){
                  RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                  layoutParams.bottomMargin = (Y - _yDelta);
                  layoutParams.topMargin = -layoutParams.bottomMargin;
                  view.setLayoutParams(layoutParams);
                  view.animate().translationY(Y - _yDelta).setDuration(0);
             }
                break;
        }
        findViewById(R.id.root).invalidate();
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        close();
        startActivity(intent1);
        overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
        finish();
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.home1:
              label="Home";
              break;
          case R.id.work1:
              label="Work";
              break;
          case R.id.others1:
              label="Others";
              break;

      }

    }
}