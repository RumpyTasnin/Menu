package com.example.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import java.util.regex.Pattern;

public class newAccount extends AppCompatActivity {

    ImageView back, image;
    LinearLayout go, linearLayout, gone;
    TextInputLayout ID, username, email, phone, password;
    TextView signIn;
    String name, id, mail, phn, pass, uid;
    ConstraintLayout root;
    boolean r = false;
    int c = 0;
    boolean ret = false;
    boolean button_click=true;
    Intent intent;

    DatabaseReference admin_ref,user_ref;
    ValueEventListener listener,listener1;

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
        ID = findViewById(R.id.id);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.signIn);
        root = findViewById(R.id.root);
        gone = findViewById(R.id.gone);
        linearLayout = findViewById(R.id.layout1);

        image = findViewById(R.id.image);

        admin_ref = FirebaseDatabase.getInstance().getReference("Admin");
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
              //  Toast.makeText(newAccount.this, "screen height " + screenHeight + "  visible display " + r.bottom, Toast.LENGTH_LONG).show();
                if (keypadHeight > screenHeight * 0.15) {
                    //  Toast.makeText(newAccount.this,"Keyboard is showing",Toast.LENGTH_LONG).show();
                    gone.setVisibility(View.GONE);
                } else {
                    gone.setVisibility(View.VISIBLE);
                    // Toast.makeText(newAccount.this,"keyboard closed",Toast.LENGTH_LONG).show();
                }
            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(newAccount.this, Login.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(newAccount.this, signIn.class);
                startActivityForResult(intent, 2);
                overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  rootNode = FirebaseDatabase.getInstance();
                databaseReference = rootNode.getReference("users");
*/

              if(button_click) {
                  // get values
                  name = username.getEditText().getText().toString();
                  mail = email.getEditText().getText().toString();
                  id = ID.getEditText().getText().toString();
                  pass = password.getEditText().getText().toString();
                  phn = phone.getEditText().getText().toString();


                  try {

                      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                      inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                      PhoneValidation(phn, name, mail, id, pass);

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
            ID.setError("Field can not be empty");
            ret = false;
        } else {

           listener= admin_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild("registered_admins")) {
                        if (snapshot.child("registered_admins").hasChild(id)) {
                            ID.setError(null);
                            ID.setErrorEnabled(false);
                           // Toast.makeText(getApplicationContext(), "id validation", Toast.LENGTH_LONG).show();
                            ret = true;
                        } else {
                            ID.setError("Invalid ID");
                            ret = false;
                        }
                    } else {
                        ID.setError("ID is not on the server");
                        ret = false;
                    }
                    if (admin_ref != null && listener != null) {
                        admin_ref.removeEventListener(listener);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
        return ret;
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
    //    Toast.makeText(getApplicationContext(), "password validation called", Toast.LENGTH_LONG).show();

        Pattern pattern = Pattern.compile("^" +

                "(?=.*[a-zA-Z0-9])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 4 characters
                "$");

        if (pass.trim().isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else if (!pattern.matcher(pass).matches()) {
            password.setError("Password too weak");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            //Toast.makeText(getApplicationContext(), "password validation", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    public void PhoneValidation(String phn, String name, String mail, String id, String pass) {

        if (phn.trim().isEmpty()) {
            phone.setError("Field can not be empty");
        } else {
            if (phn.trim().length() != 11) {
                phone.setError("Invalid phone number");
            } else {
               admin_ref = FirebaseDatabase.getInstance().getReference("Admin");
               user_ref=FirebaseDatabase.getInstance().getReference("users");
                Toast.makeText(newAccount.this, "wait a moment", Toast.LENGTH_LONG).show();
                    listener=admin_ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.child("Registered_Phone").hasChild(phn)) {
                                //Toast.makeText(newAccount.this, "Account has already been created with this phone number", Toast.LENGTH_LONG).show();
                                phone.setError("Already used this phone number");
                               // Toast.makeText(newAccount.this, "wait a moment", Toast.LENGTH_LONG).show();
                            } else {
                                listener1=user_ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.child("Registered_Phone").hasChild(phn)) {
                                            if (nameValidation(name) && EmailValidation(mail) && addressValidation(id) && PasswordValidation(pass)) {
                                                //Toast.makeText(getApplicationContext(), "phone validation", Toast.LENGTH_LONG).show();
                                                intent = new Intent(newAccount.this, Verification.class);
                                                intent.putExtra("phone", phn.trim());
                                                intent.putExtra("name", name.trim());
                                                intent.putExtra("email", mail.trim());
                                                intent.putExtra("id", id.trim());
                                                intent.putExtra("password", pass.trim());


                                                FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(newAccount.this, "successful", Toast.LENGTH_LONG).show();
                                                            button_click=false;
                                                            phone.setError(null);
                                                            phone.setErrorEnabled(false);
                                                            r = true;
                                                            intent.putExtra("from_new_account",true);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            if (user_ref != null && listener1 != null) {
                                                                user_ref.removeEventListener(listener1);
                                                            }
                                                            if (admin_ref != null && listener != null) {
                                                                admin_ref.removeEventListener(listener);
                                                            }
                                                            startActivity(intent);
                                                            overridePendingTransition(R.anim.animate_slide_left_enter, R.anim.animate_slide_left_exit);
                                                        } else {
                                                            Toast.makeText(newAccount.this, "Unknown error occurred, check your connection", Toast.LENGTH_LONG).show();

                                                        }
                                                    }
                                                });

                                            } else {
                                                r = false;

                                            }
                                        }
                                        else {
                                           // Toast.makeText(newAccount.this,"if you have already used this phone number to register an account in elCafe Admin app then you can not use that phone number again",Toast.LENGTH_LONG).show();
                                            phone.setError("Already used this phone number");

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


    }
    @Override
    protected void onStop() {
        super.onStop();

        if (user_ref != null && listener1 != null) {
            user_ref.removeEventListener(listener1);
        }
        if (admin_ref != null && listener != null) {
            admin_ref.removeEventListener(listener);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(newAccount.this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}