package com.example.admin;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class notificationService extends Service {
   // private Looper serviceLooper;

    final String PREFS_NAME = "MyPrefsFile1";
    int c = 0;
    boolean first_time = false;
    Intent i;
    DatabaseReference place_order_reference;
    String phone, id, email, username, uid, branch, order_id, delivery_type, token, order_status;
    ChildEventListener listener1,listener2,listener3,listener4,listener5,listener6,listener7,listener8,listener9,listener10,listener11,listener12,listener13
            ,listener14,listener15,listener16,listener17,listener18,listener19,listener20,listener21,listener22,listener23,listener24;
    SharedPreferences settings;
    boolean task_removed=false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        Toast.makeText(notificationService.this, intent.toString(), Toast.LENGTH_LONG).show();
        phone = intent.getStringExtra("phone");
        username =intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        id = intent.getStringExtra("id");
        uid = intent.getStringExtra("uid");
        //Toast.makeText(notificationService.this,"service started",Toast.LENGTH_LONG).show();
         settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("my_first_time_service", true)) {
            notification1( "Welcome");
            settings.edit().putBoolean("my_first_time_service", false).apply();
            first_time = true;
        } else {
            first_time = false;
////////////////////////
        }

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  startService(i);

        if (settings.getBoolean("my_first_time_service", true)) {


            first_time = true;
        } else {
            settings.edit().putBoolean("my_first_time_service", true).apply();
            first_time = false;
////////////////////////
        }
        notification1("Destroyed Service");
        Toast.makeText(notificationService.this, "service canceled", Toast.LENGTH_LONG).show();
        remove_listener();
        stopSelf();


    }

    private void remove_listener() {
        if (place_order_reference.child("Mohammadpur").child("home_delivery") != null && listener1 != null) {
            place_order_reference.child("Mohammadpur").child("home_delivery").removeEventListener(listener1);
        }
        if (place_order_reference.child("Mohammadpur").child("take_away") != null && listener2 != null) {
            place_order_reference.child("Mohammadpur").child("take_away").removeEventListener(listener2);
        }
        if (place_order_reference.child("Mohammadpur").child("dine_in") != null && listener3 != null) {
            place_order_reference.child("Mohammadpur").child("dine_in").removeEventListener(listener3);
        }

        if (place_order_reference.child("Shyamoli").child("home_delivery") != null && listener4 != null) {
            place_order_reference.child("Shyamoli").child("home_delivery").removeEventListener(listener4);
        }
        if (place_order_reference.child("Shyamoli").child("take_away") != null && listener5 != null) {
            place_order_reference.child("Shyamoli").child("take_away").removeEventListener(listener5);
        }
        if (place_order_reference.child("Shyamoli").child("dine_in") != null && listener6 != null) {
            place_order_reference.child("Shyamoli").child("dine_in").removeEventListener(listener6);
        }

        if (place_order_reference.child("Dhanmondi").child("home_delivery") != null && listener7 != null) {
            place_order_reference.child("Dhanmondi").child("home_delivery").removeEventListener(listener7);
        }
        if (place_order_reference.child("Dhanmondi").child("take_away") != null && listener8 != null) {
            place_order_reference.child("Dhanmondi").child("take_away").removeEventListener(listener8);
        }
        if (place_order_reference.child("Dhanmondi").child("dine_in") != null && listener9 != null) {
            place_order_reference.child("Dhanmondi").child("dine_in").removeEventListener(listener9);
        }

        if (place_order_reference.child("Mirpur").child("home_delivery") != null && listener10 != null) {
            place_order_reference.child("Mirpur").child("home_delivery").removeEventListener(listener10);
        }
        if (place_order_reference.child("Mirpur").child("take_away") != null && listener11 != null) {
            place_order_reference.child("Mirpur").child("take_away").removeEventListener(listener11);
        }
        if (place_order_reference.child("Mirpur").child("dine_in") != null && listener12 != null) {
            place_order_reference.child("Mirpur").child("dine_in").removeEventListener(listener12);
        }

        if (place_order_reference.child("Mohakhali").child("home_delivery") != null && listener13 != null) {
            place_order_reference.child("Mohakhali").child("home_delivery").removeEventListener(listener13);
        }
        if (place_order_reference.child("Mohakhali").child("take_away") != null && listener14 != null) {
            place_order_reference.child("Mohakhali").child("take_away").removeEventListener(listener14);
        }
        if (place_order_reference.child("Mohakhali").child("dine_in") != null && listener15 != null) {
            place_order_reference.child("Mohakhali").child("dine_in").removeEventListener(listener15);
        }

        if (place_order_reference.child("Gulshan").child("home_delivery") != null && listener16 != null) {
            place_order_reference.child("Gulshan").child("home_delivery").removeEventListener(listener16);
        }
        if (place_order_reference.child("Gulshan").child("take_away") != null && listener17!= null) {
            place_order_reference.child("Gulshan").child("take_away").removeEventListener(listener17);
        }
        if (place_order_reference.child("Gulshan").child("dine_in") != null && listener18 != null) {
            place_order_reference.child("Gulshan").child("dine_in").removeEventListener(listener18);
        }

        if (place_order_reference.child("Bashundhara").child("home_delivery") != null && listener19 != null) {
            place_order_reference.child("Bashundhara").child("home_delivery").removeEventListener(listener19);
        }
        if (place_order_reference.child("Bashundhara").child("take_away") != null && listener20!= null) {
            place_order_reference.child("Bashundhara").child("take_away").removeEventListener(listener20);
        }
        if (place_order_reference.child("Bashundhara").child("dine_in") != null && listener21 != null) {
            place_order_reference.child("Bashundhara").child("dine_in").removeEventListener(listener21);
        }

        if (place_order_reference.child("Uttara").child("home_delivery") != null && listener22 != null) {
            place_order_reference.child("Uttara").child("home_delivery").removeEventListener(listener22);
        }
        if (place_order_reference.child("Uttara").child("take_away") != null && listener23!= null) {
            place_order_reference.child("Uttara").child("take_away").removeEventListener(listener23);
        }
        if (place_order_reference.child("Uttara").child("dine_in") != null && listener24 != null) {
            place_order_reference.child("Uttara").child("dine_in").removeEventListener(listener24);
        }
    }


    @Override
    public void onCreate() {

        valueListener();



    }

    private void valueListener() {
        place_order_reference = FirebaseDatabase.getInstance().getReference("placed_order").child("confirmed_orders");
        if (first_time) {
            Toast.makeText(notificationService.this, "First time service running", Toast.LENGTH_LONG).show();
        } else {
           // Toast.makeText(notificationService.this, "NOT First time service running", Toast.LENGTH_LONG).show();

            listener1=place_order_reference.child("Mohammadpur").child("home_delivery").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="home_delivery";
                    branch="Mohammadpur";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                      //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                /*Toast.makeText(notificationService.this,token+" orderid  "+order_id+" orderstatus "+order_status+" name "+
                                        username+" phone "+phone+" uid "+uid,Toast.LENGTH_LONG).show();*/
                                notification(token,order_id,order_status,delivery_type,branch);

                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            listener2=place_order_reference.child("Mohammadpur").child("take_away").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="take_away";
                    branch="Mohammadpur";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            listener3=place_order_reference.child("Mohammadpur").child("dine_in").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="dine_in";
                    branch="Mohammadpur";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            listener4=place_order_reference.child("Shyamoli").child("home_delivery").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="home_delivery";
                    branch="Shyamoli";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            listener5=place_order_reference.child("Shyamoli").child("take_away").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="take_away";
                    branch="Shyamoli";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            listener6=place_order_reference.child("Shyamoli").child("dine_in").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="dine_in";
                    branch="Shyamoli";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            listener7=place_order_reference.child("Dhanmondi").child("home_delivery").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="home_delivery";
                    branch="Dhanmondi";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            listener8=place_order_reference.child("Dhanmondi").child("take_away").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="take_away";
                    branch="Dhanmondi";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            listener9=place_order_reference.child("Dhanmondi").child("dine_in").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="dine_in";
                    branch="Dhanmondi";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();

                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            listener10=place_order_reference.child("Mirpur").child("home_delivery").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="home_delivery";
                    branch="Mirpur";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            listener11=place_order_reference.child("Mirpur").child("take_away").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="take_away";
                    branch="Mirpur";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            listener12=place_order_reference.child("Mirpur").child("dine_in").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="dine_in";
                    branch="Mirpur";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            listener13=place_order_reference.child("Mohakhali").child("home_delivery").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="home_delivery";
                    branch="Mohakhali";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            listener14=place_order_reference.child("Mohakhali").child("take_away").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="take_away";
                    branch="Mohakhali";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            listener15=place_order_reference.child("Mohakhali").child("dine_in").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="dine_in";
                    branch="Mohakhali";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            listener16=place_order_reference.child("Gulshan").child("home_delivery").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="home_delivery";
                    branch="Gulshan";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            listener17=place_order_reference.child("Gulshan").child("take_away").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="take_away";
                    branch="Gulshan";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            listener18=place_order_reference.child("Gulshan").child("dine_in").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="dine_in";
                    branch="Gulshan";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            listener19=place_order_reference.child("Bashundhara").child("home_delivery").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="home_delivery";
                    branch="Bashundhara";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            listener20=place_order_reference.child("Bashundhara").child("take_away").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="take_away";
                    branch="Bashundhara";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            listener21=place_order_reference.child("Bashundhara").child("dine_in").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="dine_in";
                    branch="Bashundhara";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            listener22=place_order_reference.child("Uttara").child("home_delivery").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Toast.makeText(notificationService.this, snapshot.getKey(),Toast.LENGTH_LONG).show();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="home_delivery";
                    branch="Uttara";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            listener23=place_order_reference.child("Uttara").child("take_away").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="take_away";
                    branch="Uttara";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            listener24=place_order_reference.child("Uttara").child("dine_in").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    delivery_type="dine_in";
                    branch="Uttara";
                    if(snapshot.hasChild("Token")){
                        token= Objects.requireNonNull(snapshot.child("Token").getValue()).toString();
                        //  Toast.makeText(notificationService.this, "token : " + token, Toast.LENGTH_LONG).show();
                        if(snapshot.hasChild("order_id")){
                            order_id= Objects.requireNonNull(snapshot.child("order_id").getValue()).toString();
                            if(snapshot.hasChild("status")){
                                order_status= Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                                notification(token,order_id,order_status,delivery_type,branch);
                            }
                        }

                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    private void notification(String token, String order_id, String order_status, String delivery_type, String branch) {


        Intent resultingIntent=new Intent(getApplicationContext(),Orders.class);
        resultingIntent.putExtra("phone", phone);
        resultingIntent.putExtra("username", username);
        resultingIntent.putExtra("email", email);
        resultingIntent.putExtra("id", id);
        resultingIntent.putExtra("uid", uid);
        resultingIntent.putExtra("branch", branch);
        resultingIntent.putExtra("deliver_type", delivery_type);
        resultingIntent.putExtra("order_id", order_id);
        resultingIntent.putExtra("token", token);
        resultingIntent.putExtra("order_status",order_status);
        resultingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);



        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),1,resultingIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("elCafe", "New Order", NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
       Notification notification = new NotificationCompat.Builder(notificationService.this, "elCafe")
       .setContentTitle("ElCafe")
        .setContentText("New Order #"+token+" has been placed.")
       .setSmallIcon(R.drawable.coffee)
        .setContentIntent(pendingIntent)
       .setPriority(NotificationCompat.PRIORITY_HIGH).build();
        if (order_status.equals("confirmed")) {
            startForeground(1,notification);
        }


    }
    private void notification1(String s) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("message", "New Order", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat.Builder(notificationService.this, "message")
                .setContentTitle("ElCafe")
                .setContentText(s)
                .setSmallIcon(R.drawable.coffee)
                .setAutoCancel(true)
                .build();

        startForeground(9999,notification);

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
       // Toast.makeText(notificationService.this, "task removed", Toast.LENGTH_LONG).show();


        Intent intent = new Intent(getApplicationContext(), this.getClass());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            intent.putExtra("phone",phone);
            intent.putExtra("username",username);
            intent.putExtra("email",email);
            intent.putExtra("id",id);
            intent.putExtra("uid",uid);
            intent.setPackage(getPackageName());
            ContextCompat.startForegroundService(getApplicationContext(),intent);

        } else {
            intent.putExtra("phone",phone);
            intent.putExtra("username",username);
            intent.putExtra("email",email);
            intent.putExtra("id",id);
            intent.putExtra("uid",uid);
            intent.setPackage(getPackageName());
            startService(intent);

        }

        super.onTaskRemoved(rootIntent);
    }

}
