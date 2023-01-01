package com.pet.adoption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    void login(String username, String password){
        SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.putString("username", username);
        Ed.putString("password", password);
        Ed.commit();
    }

    private boolean passwordShowing = false;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pet-adoption-c09f7-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


//        login("usernameTxt", "passwordTxt", "False");
        SharedPreferences sp1=this.getSharedPreferences("CHECKLOGIN", MODE_PRIVATE);
        String checkL=sp1.getString("checkL", null);

//        check = new checkLogin(this);
        if (checkL.equals("False")) {
            startActivity(new Intent(Login.this, ActivityHome.class));
            finish();
        }

        final EditText usernameET = findViewById(R.id.usernameEt);
        final EditText passwordEt = findViewById(R.id.passwordEt);
        final ImageView passwordIcon = findViewById(R.id.passwordIcon);
        final TextView signUpBtn = findViewById(R.id.signUpBtn);

        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordShowing)
                {
                    passwordShowing = false;
                    passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.show);
                }
                else{
                    passwordShowing = true;
                    passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.hide);
                }

                passwordEt.setSelection(passwordEt.length());
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        final TextView forgotPasswordBtn = findViewById(R.id.forgotPasswordBtn);
        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, ForgotPass.class));
            }
        });

        final TextView signInBtn = findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String usernameTxt = usernameET.getText().toString();
                final String passwordTxt = passwordEt.getText().toString();

                if (usernameTxt.isEmpty() || passwordTxt.isEmpty() ) Toast.makeText(Login.this, "Check your information, then try again!", Toast.LENGTH_SHORT).show();
                else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(usernameTxt)){
                                final String getPassword = snapshot.child(usernameTxt).child("Password").getValue(String.class);
                                if (passwordTxt.equals(getPassword)){
                                    login(usernameTxt, passwordTxt);
                                    SharedPreferences sp=getSharedPreferences("CHECKLOGIN", MODE_PRIVATE);
                                    SharedPreferences.Editor Ed=sp.edit();
                                    Ed.putString("checkL", "False");
                                    Ed.commit();
                                    startActivity(new Intent(Login.this, ActivityHome.class));
                                    finish();
                                }
                                else Toast.makeText(Login.this, "incorrect password", Toast.LENGTH_SHORT).show();
                            }
                            else Toast.makeText(Login.this, "Not exist", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

    private void launchHomeScreen() {
//        check.setIsFirstTimeLaunch(false);
        startActivity(new Intent(Login.this, ActivityHome.class));
        finish();
    }
}