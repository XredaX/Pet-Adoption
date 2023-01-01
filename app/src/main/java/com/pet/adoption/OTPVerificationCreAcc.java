package com.pet.adoption;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class OTPVerificationCreAcc extends AppCompatActivity {

    int readLastButtonPressed() {
        SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
        return sharedPref.getInt("LAST_BUTTON", 0);
    }

    private EditText otpEt1, otpEt2, otpEt3, otpEt4;
    private TextView resendBtn;
    private boolean resendEnabled = false;
    private int resendTime = 60;
    private int selectedETPosition = 0;

    ImageButton cancelButton;
    Button ok_btn;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pet-adoption-c09f7-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        View alertCustomDialog = LayoutInflater.from(OTPVerificationCreAcc.this).inflate(R.layout.custom_dialog,null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OTPVerificationCreAcc.this);

        alertDialog.setView(alertCustomDialog);

        final  AlertDialog dialog = alertDialog.create();
        ok_btn = alertCustomDialog.findViewById(R.id.ok_btn_id);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                if (readLastButtonPressed() == 2) {
                    startActivity(new Intent(OTPVerificationCreAcc.this, Login.class));
                }

            }
        });

        final Button verifyBtn = findViewById(R.id.verifyBtn);
        otpEt1 = findViewById(R.id.otpET1);
        otpEt2 = findViewById(R.id.otpET2);
        otpEt3 = findViewById(R.id.otpET3);
        otpEt4 = findViewById(R.id.otpET4);
        resendBtn = findViewById(R.id.resendBtn);

        otpEt1.addTextChangedListener(textWatcher);
        otpEt2.addTextChangedListener(textWatcher);
        otpEt3.addTextChangedListener(textWatcher);
        otpEt4.addTextChangedListener(textWatcher);

        showKeyboard(otpEt1);

        startCountDownTimer();

        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resendEnabled){
                    startCountDownTimer();
                }
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                final String generateOtp = otpEt1.getText().toString()+otpEt2.getText().toString()+otpEt3.getText().toString()+otpEt4.getText().toString();

                if (generateOtp.length()==4)
                {
                    if (readLastButtonPressed() == 1) {

                        startActivity(new Intent(OTPVerificationCreAcc.this, ForgotPass2.class));
                    }
                    else if (readLastButtonPressed() == 2) {
                        final String usernameTxt = getIntent().getStringExtra("usernameTxt");
/*                        final String fullnameTxt = getIntent().getStringExtra("fullnameTxt");
                        final String emailTxt = getIntent().getStringExtra("emailTxt");
                        final String mobileTxt = getIntent().getStringExtra("mobileTxt");
                        final String passwordTxt = getIntent().getStringExtra("passwordTxt");*/

                        databaseReference.child("users").child(usernameTxt).child("Fullname").setValue(getIntent().getStringExtra("usernameTxt"));
                        databaseReference.child("users").child(usernameTxt).child("Email").setValue(getIntent().getStringExtra("emailTxt"));
                        databaseReference.child("users").child(usernameTxt).child("Mobile").setValue(getIntent().getStringExtra("mobileTxt"));
                        databaseReference.child("users").child(usernameTxt).child("Password").setValue(getIntent().getStringExtra("passwordTxt"));

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                }
            }


        });
    }

    private void showKeyboard(EditText otpEt){
        otpEt.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(otpEt, InputMethodManager.SHOW_IMPLICIT);
    }

    private void startCountDownTimer(){

        resendEnabled = false;
        resendBtn.setTextColor(Color.parseColor("#4297D7"));

        new CountDownTimer(resendTime * 1000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                resendBtn.setText("Resend ("+(millisUntilFinished / 1000+")"));
            }

            @Override
            public void onFinish() {
                resendEnabled = true;
                resendBtn.setText("Resend");
                resendBtn.setTextColor(getResources().getColor(R.color.orange_two));
            }
        }.start();
    }

    private final TextWatcher textWatcher =  new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length() > 0){
                if(selectedETPosition == 0){
                    selectedETPosition = 1;
                    showKeyboard(otpEt2);
                }
                else if(selectedETPosition == 1){
                    selectedETPosition = 2;
                    showKeyboard(otpEt3);
                }
                else if(selectedETPosition == 2){
                    selectedETPosition = 3;
                    showKeyboard(otpEt4);
                }
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if(selectedETPosition == 3){
                selectedETPosition = 2;
                showKeyboard(otpEt3);
            }
            else if(selectedETPosition == 2){
                selectedETPosition = 1;
                showKeyboard(otpEt2);
            }
            else if(selectedETPosition == 1){
                selectedETPosition = 0;
                showKeyboard(otpEt1);
            }
            return true;
        }
        else return super.onKeyUp(keyCode, event);
    }
}