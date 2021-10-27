package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Verification extends AppCompatActivity {
    String PHONE, ID, EMAIL, username, UID, branch, order_id, delivery_type, token,CLASS;
    boolean from_other_activity;
    Intent intent,service_intent;

    ProgressBar progressBar;
    TextInputLayout code;

    ImageView back;
    String phoneNo,verificationCode,name,mail,id,pass,uid;
    TextInputEditText editText;
    TextView time;

    LinearLayout gone, gone1;
    FirebaseAuth mAuth;
    LinearLayout verify,resend;
    FirebaseDatabase rootNode;
    DatabaseReference databaseReference;
    CountDownTimer countDownTimer;
    ValueEventListener listener;
    PhoneAuthCredential credential;
    Dialog internet_connection_dialog;
    int c=0;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.transparent));
        }

        getSupportActionBar().hide();
        setContentView(R.layout.activity_verification);

        //////
        progressBar=findViewById(R.id.progressbar);
        code=findViewById(R.id.code);
        verify=findViewById(R.id.verify);
        resend=findViewById(R.id.resendOTP);
        back=findViewById(R.id.toolbar_back).findViewById(R.id.toolbarback);
        editText=findViewById(R.id.edittext);
        gone=findViewById(R.id.gone);
        gone1=findViewById(R.id.gone2);
        time=findViewById(R.id.time);
        mAuth=FirebaseAuth.getInstance();


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
        }

        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(s, forceResendingToken);
                // inside this method we will store the verification id that is stored
                time.setText("00:00");

                resend.setVisibility(View.GONE);
                resend_OTP();
                //Toast.makeText(Verification.this,"on code sent "+s+"  "+token,Toast.LENGTH_LONG).show();

                verificationCode=s;
                forceResendingToken=token;
            }

            @Override
            // this method is called when the verification is completed
            // we can get the code that is sent by sms automatically
            // if this succeeds, user do not have to enter the code
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // This method is called when the verification is completed.
                // Here we have the PhoneAuthCredential Object which will give us the code if it is automatically detected.
                //getting the code sent by sms
                String SMScode = phoneAuthCredential.getSmsCode();
                //sometime sms code can not be detected automatically
                //in this case code will be null
                //the user have to manually enter the code
                if(SMScode!=null){
                    editText.setText(SMScode);
                    progressBar.setVisibility(View.VISIBLE);
                    // verifyVerificationCode(SMScode);
                    verifyAuthentication(phoneAuthCredential);
                    resend.setVisibility(View.GONE);

                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(Verification.this,"verification failed",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                resend.setVisibility(View.VISIBLE);
            }
        };
        if(getIntent().getBooleanExtra("reset",false)){
            phoneNo=getIntent().getStringExtra("phone");

        }
        else {
            Intent intent1=getIntent();
            phoneNo=intent1.getStringExtra("phone");
            name=intent1.getStringExtra("name");
            id=intent1.getStringExtra("id");
            pass=intent1.getStringExtra("password");
            mail=intent1.getStringExtra("email");
            uid=intent1.getStringExtra("uid");
        }

        sendVerificationCodeToUser(phoneNo);
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
                    gone1.setVisibility(View.GONE);
                } else {
                    gone.setVisibility(View.VISIBLE);
                    gone1.setVisibility(View.VISIBLE);
                }
            }
        });
////////###################################?//////////////////////////////////
        internet_connection_dialog = new Dialog(Verification.this);
        internet_connection_dialog.setContentView(R.layout.dialog_no_internet);
        internet_connection_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        internet_connection_dialog.getWindow().setGravity(Gravity.CENTER);
        internet_connection_dialog.setCancelable(true);
        internet_connection_dialog.getWindow().getAttributes().windowAnimations = R.style.CustomActivityAnimation2;
        internet_connection_dialog.findViewById(R.id.try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected(Verification.this)){
                    internet_connection_dialog.dismiss();
                    if(c==0) {
                        verifyAuthentication(credential);
                    }                  //  tryAgain();
                }
            }
        });
//////////////////////###########################################?///////////////////
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getIntent().getBooleanExtra("reset",false)) {
                    Intent intent = new Intent(Verification.this, newAccount.class);
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }
                else{
                    Intent intent = new Intent(Verification.this, forgotPassword.class);
                    if(from_other_activity){
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
                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(time.getText().toString().equals("00:00")){
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+88"+ phoneNo,        // Phone number to verify
                            120,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            Verification.this,               // Activity (for callback binding)
                            mCallbacks,
                            forceResendingToken);}

            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editCode= editText.getText().toString();
                if((editCode.trim().isEmpty())||editCode.length()<6){
                    code.setError("Wrong OTP......");
                    code.requestFocus();
                    //  Toast.makeText(Verification.this,editCode.length(),Toast.LENGTH_LONG).show();
                    return;
                }

                else {
                    rootNode = FirebaseDatabase.getInstance();
                    databaseReference = rootNode.getReference("users");
                    code.setError(null);
                    code.setErrorEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);

                    verifyVerificationCode(editCode);
                    Intent intent=new Intent(Verification.this,Dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);// will create new activity by closing all other
                    // activity in the application
//
                    //startActivity(intent);
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    //finish();
                }

            }
        });



    }
    private void resend_OTP() {
        ////time resend handling
        long duration=TimeUnit.MINUTES.toMillis(2);
        countDownTimer= new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setVisibility(View.VISIBLE);
                String stime=String.format(Locale.ENGLISH,"%02d:%02d",TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                                -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                // Toast.makeText(Verification.this,"time "+stime,Toast.LENGTH_SHORT).show();
                time.setText(stime);
            }

            @Override
            public void onFinish() {
                time.setText("00:00");
                progressBar.setVisibility(View.GONE);


            }
        }.start();

    }
    /// sends code to the user
    private void sendVerificationCodeToUser(String phoneNo) {
     /*  PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);*/
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+88"+ phoneNo,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                Verification.this,               // Activity (for callback binding)
                mCallbacks);




    }
    private void verifyVerificationCode(String smScode) {

         credential=PhoneAuthProvider.getCredential(verificationCode,smScode);
        //  Toast.makeText(Verification.this,"verifyVerificationCode  credential  "+ credential,Toast.LENGTH_LONG).show();
        // signInWithPhoneAuthCredential(credential);
       if(isConnected(Verification.this)){
           verifyAuthentication(credential);
           if(internet_connection_dialog.isShowing()){
               internet_connection_dialog.dismiss();
           }
        }
       else {
           internet_connection_dialog.show();
       }

    }
    private void verifyAuthentication(PhoneAuthCredential credential) {
        if(!getIntent().getBooleanExtra("reset",false)){
            Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        rootNode = FirebaseDatabase.getInstance();
                        databaseReference = rootNode.getReference("Admin");

                        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();

                        intent=new Intent(Verification.this,Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);// will create new activity by closing all other
                        // databaseReference.child(phoneNo).setValue(userHelperClass);
                        databaseReference.child("Registered_Phone").child(phoneNo).child("UID").setValue(uid);
                        databaseReference.child("Registered_Phone").child(phoneNo).child("email").setValue(mail);
                        databaseReference.child("Registered_Phone").child(phoneNo).child("password").setValue(pass);
                        databaseReference.child("Registered_Phone").child(phoneNo).child("id").setValue(id);

                        databaseReference.child(uid).child("name").setValue(name);
                        databaseReference.child(uid).child("email").setValue(mail);
                        databaseReference.child(uid).child("password").setValue(pass);
                        databaseReference.child(uid).child("id").setValue(id);
                        databaseReference.child(uid).child("phone").setValue(phoneNo);
                        databaseReference.child(uid).child("UID").setValue(uid);

                            intent=new Intent(Verification.this,Dashboard.class);
                            intent.putExtra("username",name);
                            intent.putExtra("phone",phoneNo);
                            intent.putExtra("email",mail);
                            intent.putExtra("uid",uid);
                            intent.putExtra("id",id);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            service_intent=new Intent(Verification.this,notificationService.class);
                            service_intent.putExtra("phone",  phoneNo);
                            service_intent.putExtra("uid", uid);
                            service_intent.putExtra("username", name);
                            service_intent.putExtra("email", mail);
                            service_intent.putExtra("id", id);
                            ContextCompat.startForegroundService(Verification.this,service_intent);

                        } else {
                            service_intent=new Intent(Verification.this,notificationService.class);
                            service_intent.putExtra("phone",  phoneNo);
                            service_intent.putExtra("uid", uid);
                            service_intent.putExtra("username", name);
                            service_intent.putExtra("email", mail);
                            service_intent.putExtra("id", id);
                            startService(service_intent);
                        }
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);


                    }
                    else{

                        Toast.makeText(Verification.this,"not successfully linked",Toast.LENGTH_LONG).show();
                        //or if you have already used this phone number to register an account in elCafe Menu app then you can not use that phone number again
                     //   Toast.makeText(Verification.this,"if you have already used this phone number to register an account in elCafe Menu app then you can not use that phone number again",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else{
            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        c=1;
                        Toast.makeText(Verification.this,"code verified",Toast.LENGTH_LONG).show();
                        Intent i=new Intent(Verification.this,reset.class);
                        if(from_other_activity){
                            //Toast.makeText(Verification.this,"Verification " +ID,Toast.LENGTH_LONG).show();

                            i.putExtra("class", CLASS);
                            i.putExtra("order_id", order_id);
                            i.putExtra("username", username);
                            i.putExtra("email", EMAIL);
                            i.putExtra("phone",PHONE);
                            i.putExtra("uid", UID);
                            i.putExtra("id", ID);
                            i.putExtra("branch", branch);
                            i.putExtra("deliver_type", delivery_type);
                            i.putExtra("token", token);
                            i.putExtra("from_other_activity",true);
                            i.putExtra("order_status", getIntent().getStringExtra("order_status"));



                        }
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                        finish();

                    }
                    else{
                        c=0;
                        Toast.makeText(Verification.this,"code not verified.",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });


        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!getIntent().getBooleanExtra("reset",false)) {
            Intent intent = new Intent(Verification.this, newAccount.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        }
        else{
            if(getIntent().getBooleanExtra("from_new_account",false)){
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    FirebaseAuth.getInstance().getCurrentUser().delete();
                }
            }
            Intent intent = new Intent(Verification.this, forgotPassword.class);
            if(from_other_activity){
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
/*
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
*/
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
       // Toast.makeText(Verification.this,"started",Toast.LENGTH_LONG).show();

    }
    @Override
    protected void onStop() {
        super.onStop();
      //  Toast.makeText(Verification.this,"stopped",Toast.LENGTH_LONG).show();

    }
    @Override
    protected void onPause() {
        super.onPause();
      //  Toast.makeText(Verification.this,"paused",Toast.LENGTH_LONG).show();

    }

    // 2) :
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Toast.makeText(Verification.this,"destroyed",Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
     //   Toast.makeText(Verification.this,"resumed",Toast.LENGTH_LONG).show();

    }
    private boolean isConnected(Context context) {
        boolean RETURN = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifi_connection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile_connection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifi_connection != null && wifi_connection.isConnected()) || (mobile_connection != null && mobile_connection.isConnected())) {
          /*  if (internet_connection_dialog.isShowing()) {
                internet_connection_dialog.dismiss();
                // layout_progress.setVisibility(View.GONE);
            }
           */ //Toast.makeText(Verification.this, "secure connection", Toast.LENGTH_LONG).show();

            RETURN = true;
        } else {
            Toast.makeText(Verification.this, "no connection", Toast.LENGTH_LONG).show();
          /*  if (!internet_connection_dialog.isShowing()) {
                internet_connection_dialog.show();
            }*/

            RETURN = false;
        }
        return RETURN;
    }

}