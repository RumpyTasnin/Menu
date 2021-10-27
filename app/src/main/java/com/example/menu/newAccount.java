package com.example.menu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.Keyboard;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import java.util.EnumMap;
import java.util.regex.Pattern;

public class newAccount extends AppCompatActivity {

    ImageView back,image;
    LinearLayout go,linearLayout,gone;
    TextInputLayout address, username, email, phone, password;
    TextView signIn;
    String name, adr, mail, phn, pass,uid;
    ConstraintLayout root;
    boolean r=false;
    int c=0;
    boolean button_click=true;
    DatabaseReference admin_ref,databaseReference;
    ValueEventListener listener,listener1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        setContentView(R.layout.activity_new_account);


        back = findViewById(R.id.toolbar_back).findViewById(R.id.toolbarback);
        go = findViewById(R.id.go);
        address = findViewById(R.id.address);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.signIn);
        root=findViewById(R.id.root);
        gone=findViewById(R.id.gone);
        linearLayout=findViewById(R.id.layout1);

        image=findViewById(R.id.image);


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





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(newAccount.this, Login.class);
                i.putExtra("open_login_activity",true);
                startActivity(i);
                overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
               // Toast.makeText(newAccount.this,"back",Toast.LENGTH_LONG).show();
                finish();
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(newAccount.this, signIn.class);
                startActivity(intent1);

                overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  rootNode = FirebaseDatabase.getInstance();
                databaseReference = rootNode.getReference("users");
*/

                // get values
                if(button_click) {
                    name = username.getEditText().getText().toString();
                    mail = email.getEditText().getText().toString();
                    adr = address.getEditText().getText().toString();
                    pass = password.getEditText().getText().toString();
                    phn = phone.getEditText().getText().toString();


                    try {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        PhoneValidation(phn, name, mail, adr, pass);

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                 else {
                  Toast.makeText(newAccount.this, "wait a moment", Toast.LENGTH_LONG).show();
              }

            }

        });
    }


    public Boolean nameValidation(String fullname) {

        if (fullname.trim().isEmpty()) {
            username.setError("Field can not be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
           // Toast.makeText(getApplicationContext(), "name validation", Toast.LENGTH_LONG).show();

            return true;
        }
    }
    public Boolean addressValidation(String adr) {

        if (adr.trim().isEmpty()) {
            address.setError("Field can not be empty");
            return false;
        } else {
            address.setError(null);
            address.setErrorEnabled(false);
           // Toast.makeText(getApplicationContext(), "address validation", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    public Boolean EmailValidation(String mail) {

        if (mail.trim().isEmpty()) {
            email.setError("Field can not be empty");
            return false;
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(mail.trim()).matches()) {
                email.setError("Invalid email address");

                return false;
            }
            //Toast.makeText(getApplicationContext(), "email validation", Toast.LENGTH_LONG).show();
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    public Boolean PasswordValidation(String pass) {
        Pattern pattern = Pattern.compile("^" +

                "(?=.*[a-zA-Z0-9])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{8,}"+               //at least 4 characters
                "$");
        //  Pattern pattern = Pattern.compile("^" +
        //                "(?=.*[0-9])" +         //at least 1 digit
        //                 "(?=.*[a-z])" +         //at least 1 lower case letter
        //                "(?=.*[A-Z])" +       //at least 1 upper case letter
        //                "(?=.*[a-zA-Z])" +      //any letter
        //                "(?=.*[@#$%^&+=])" +    //at least 1 special character
        //                "(?=\\S+$)" +           //no white spaces
        //                ".{8,}"+               //at least 4 characters
        //                "$");
        if (pass.trim().isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else if (!pattern.matcher(pass).matches()) {
            password.setError("Password too weak");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
           // Toast.makeText(getApplicationContext(), "password validation", Toast.LENGTH_LONG).show();
            return true;
        }
    }
    public Boolean PhoneValidation(String phn, String name, String mail, String adr, String pass) {


        if (phn.trim().isEmpty()) {
            phone.setError("Field can not be empty");
            return false;
        } else {
            if (phn.trim().length()!=11) {
                phone.setError("Invalid phone number");
                return false;
            }
            else {
               databaseReference = FirebaseDatabase.getInstance().getReference("users");
               admin_ref=FirebaseDatabase.getInstance().getReference("Admin");

                    listener=databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.child("Registered_Phone").hasChild(phn)) {
                                phone.setError("Already used this phone number");
                               //Toast.makeText(newAccount.this, "Account has already been created with this phone number", Toast.LENGTH_LONG).show();
                            } else {
                                  listener1=admin_ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.child("Registered_Phone").hasChild(phn)){
                                            if (nameValidation(name) && EmailValidation(mail) && addressValidation(adr) && PasswordValidation(pass)) {
                                                Intent intent = new Intent(newAccount.this, Verification.class);
                                                intent.putExtra("phone", phn.trim());
                                                intent.putExtra("name", name.trim());
                                                intent.putExtra("email", mail.trim());
                                                intent.putExtra("address", adr.trim());
                                                intent.putExtra("password", pass.trim());
                                                FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if(task.isSuccessful()){
                                                            button_click=false;
                                                            phone.setError(null);
                                                            phone.setErrorEnabled(false);
                                                            r = true;
                                                            intent.putExtra("from_new_account",true);
                                                            if (databaseReference != null && listener != null) {
                                                                databaseReference.removeEventListener(listener);
                                                            }
                                                            if (admin_ref != null && listener1 != null) {
                                                                admin_ref.removeEventListener(listener1);
                                                            }
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                            overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                                                        }
                                                        else {
                                                            Toast.makeText(newAccount.this,"not successful",Toast.LENGTH_LONG).show();

                                                        }
                                                    }
                                                });


                                                // finish();
                                            } else {
                                                r = false;
                                            }
                                        }
                                        else{
                                            phone.setError("Already used this phone number");
                                         //   Toast.makeText(newAccount.this,"if you have already used this phone number to register an account in elCafe Admin app then you can not use that phone number again",Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
               return  r;
                }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(newAccount.this, Login.class);
        intent.putExtra("open_login_activity",true);
        startActivity(intent);
        overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
        finish();
    }
    @Override
    protected void onStop() {
        super.onStop();

        if (databaseReference != null && listener != null) {
            databaseReference.removeEventListener(listener);
        }
        if (admin_ref != null && listener1 != null) {
            admin_ref.removeEventListener(listener1);
        }

    }
}