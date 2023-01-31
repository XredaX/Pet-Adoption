package com.pet.adoption;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class Register extends AppCompatActivity {

    private boolean passwordShowing = false;
    private boolean conPasswordShowing = false;
    private CountryCodePicker countryCodePicker;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pet-adoption-c09f7-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText username = findViewById(R.id.usernameEt);
        final EditText fullName = findViewById(R.id.fullNameEt);
        final EditText email = findViewById(R.id.emailEt);
        final EditText mobile = findViewById(R.id.mobileEt);
        final EditText password = findViewById(R.id.passwordEt);
        final EditText conPassword = findViewById(R.id.conPasswordEt);
        final ImageView passwordIcon = findViewById(R.id.passwordIcon);
        final ImageView conPasswordIcon = findViewById(R.id.conPasswordIcon);
        final TextView signUpBtn = findViewById(R.id.signUpBtn);
        final TextView signInBtn = findViewById(R.id.signIpBtn);

        countryCodePicker = findViewById(R.id.ccp);
        countryCodePicker.registerCarrierNumberEditText(mobile);

        passwordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordShowing)
                {
                    passwordShowing = false;
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.show);
                }
                else{
                    passwordShowing = true;
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordIcon.setImageResource(R.drawable.hide);
                }

                password.setSelection(password.length());
            }
        });

        conPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(conPasswordShowing)
                {
                    conPasswordShowing = false;
                    conPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.show);
                }
                else{
                    conPasswordShowing = true;
                    conPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    conPasswordIcon.setImageResource(R.drawable.hide);
                }

                conPassword.setSelection(conPassword.length());
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usernameTxt = username.getText().toString();
                final String fullnameTxt = fullName.getText().toString();
                final String emailTxt = email.getText().toString();
                final String mobileTxt = mobile.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String conPasswordTxt = conPassword.getText().toString();

                if (usernameTxt.isEmpty() || fullnameTxt.isEmpty() || emailTxt.isEmpty() || mobileTxt.isEmpty() || passwordTxt.isEmpty() || conPasswordTxt.isEmpty()){
                    Toast.makeText(Register.this, "We need more data about you", Toast.LENGTH_SHORT).show();
                }
                else if(!passwordTxt.equals(conPasswordTxt)) {
                    Toast.makeText(Register.this, "You should type the same password", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(usernameTxt)){
                                Toast.makeText(Register.this, "Already exist", Toast.LENGTH_SHORT).show();
                            }
                            else if (mobileTxt.replace(" ", "").length()<10){
                                Toast.makeText(Register.this, "Please enter a correct phone number", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                saveLastButtonPressed(2);
                                Intent intent = new Intent(Register.this, OTPVerificationCreAcc.class);
                                intent.putExtra("usernameTxt", usernameTxt);
                                intent.putExtra("fullnameTxt", fullnameTxt);
                                intent.putExtra("emailTxt", emailTxt);
                                intent.putExtra("mobileTxt", mobileTxt);
                                intent.putExtra("passwordTxt", passwordTxt);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                        void saveLastButtonPressed(int buttonNumber) {
                            SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("LAST_BUTTON", buttonNumber);
                            editor.apply();
                        }
                    });
                }

            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }
}