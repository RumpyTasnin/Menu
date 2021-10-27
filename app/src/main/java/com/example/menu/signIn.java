package com.example.menu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signIn extends AppCompatActivity {


    TextView signUp,forgot_password;
    ImageView back;
    LinearLayout linearLayout,gone,sign_in;
    TextInputLayout email_id,password;
    Boolean can_sign_in;
    Intent intent;

    ValueEventListener listener;
    DatabaseReference ref;
    String EMAIL,PASSWORD,uid;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        //status bar transparent
//        requestWindowFeature(1);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.transparent));
        }
      /*  if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getWindow().setNavigationBarColor(getResources().getColor(R.color.red));

        }
*/
        //Toast.makeText(newAccount.this,"navigation ba color "+getWindow().getNavigationBarColor(),Toast.LENGTH_LONG).show();
        getSupportActionBar().hide();

        setContentView(R.layout.activity_sign_in);

        gone=findViewById(R.id.gone);
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
                  ////  Toast.makeText(signIn.this,"Keyboard is showing",Toast.LENGTH_LONG).show();
                    gone.setVisibility(View.GONE);
                } else {
                    gone.setVisibility(View.VISIBLE);
                   // Toast.makeText(signIn.this,"keyboard closed",Toast.LENGTH_LONG).show();
                }
            }
        });


        signUp=findViewById(R.id.signUp);
        back= findViewById(R.id.toolbar_back).findViewById(R.id.toolbarback);
        email_id=findViewById(R.id.email_id);
        password=findViewById(R.id.password);
        sign_in=findViewById(R.id.sign_in);
        forgot_password=findViewById(R.id.forgot_password);

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(signIn.this,forgotPassword.class);
                startActivity(intent);
                overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                finish();
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!email_id.getEditText().getText().toString().isEmpty() && !password.getEditText().getText().toString().isEmpty() ){
                    EMAIL=email_id.getEditText().getText().toString();
                    PASSWORD=password.getEditText().getText().toString();

                    if (!Patterns.EMAIL_ADDRESS.matcher(email_id.getEditText().getText().toString().trim()).matches()) {
                        email_id.setError("Invalid email address");
                        can_sign_in=false;
                    }

                    else {
                       can_sign_in=true;
                        email_id.setError(null);
                        email_id.setErrorEnabled(false);
                    }
                    if(can_sign_in){
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(EMAIL,PASSWORD)
                                .addOnCompleteListener(signIn.this,new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(signIn.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                                            uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                          ref=FirebaseDatabase.getInstance().getReference().child("users").child(uid);

                                            listener=ref.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            email_id.setError(null);
                                                            email_id.setErrorEnabled(false);
                                                            password.setError(null);
                                                            password.setErrorEnabled(false);
                                                            intent = new Intent(signIn.this, MainMenu.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            intent.putExtra("phone",  snapshot.child("phone").getValue().toString());
                                                            intent.putExtra("uid", uid);
                                                            intent.putExtra("username", snapshot.child("name").getValue().toString());
                                                            intent.putExtra("email", snapshot.child("email").getValue().toString());
                                                            intent.putExtra("deliver_address", snapshot.child("deliver_address").getValue().toString());
                                                            if (snapshot.hasChild("order_history")) {
                                                                if (snapshot.child("order_history").hasChild("recent_order_id")) {
                                                                    String order_id = snapshot.child("order_history").child("recent_order_id").getValue().toString();
                                                                    intent.putExtra("order_id", Integer.parseInt(order_id));
                                                                }
                                                            }
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                                                    finish();
     }


                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        }
                                        else {
                                            Toast.makeText(signIn.this,"Logging error",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                    }
                }
                else{
                    if(email_id.getEditText().getText().toString().isEmpty()){
                        email_id.setError("Field can not be empty");
                    }
                    if(password.getEditText().getText().toString().isEmpty()){
                        password.setError("Field can not be empty");
                    }

                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(signIn.this,Login.class);
                intent.putExtra("open_login_activity",true);
                startActivity(intent);
                overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
                finish();



            }
        });

      signUp.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(signIn.this,newAccount.class);

              startActivity(intent);
              overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
              finish();

          }
      });

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent intent=new Intent(signIn.this,MainMenu.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);// will create new activity by closing all other
            // activity in the application

            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }

    }
*/


   @Override
    public void onBackPressed() {
       Intent intent=new Intent(signIn.this,Login.class);
       intent.putExtra("open_login_activity",true);
       startActivity(intent);
       overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
       finish();

   }
    @Override
    protected void onStop() {
        super.onStop();

        if (ref != null && listener != null) {
            ref.removeEventListener(listener);
        }

    }
}