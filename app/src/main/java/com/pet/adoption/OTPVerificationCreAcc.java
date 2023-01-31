package com.pet.adoption;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class OTPVerificationCreAcc extends AppCompatActivity {

    int readLastButtonPressed() {
        SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
        return sharedPref.getInt("LAST_BUTTON", 0);
    }

    private FirebaseAuth mAuth;
    private EditText otpEt1, otpEt2, otpEt3, otpEt4, otpEt5, otpEt6;
    private TextView resendBtn;
    private boolean resendEnabled = false;
    private int resendTime = 60;
    private int selectedETPosition = 0;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mobile, id;

    Button ok_btn;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pet-adoption-c09f7-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        /*mAuth = FirebaseAuth.getInstance();
        mobile = getIntent().getStringExtra("mobileTxt");
        sendVerificationCode();*/

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

        otpEt1 = findViewById(R.id.otpET1);
        otpEt2 = findViewById(R.id.otpET2);
        otpEt3 = findViewById(R.id.otpET3);
        otpEt4 = findViewById(R.id.otpET4);
        otpEt5 = findViewById(R.id.otpET5);
        otpEt6 = findViewById(R.id.otpET6);
        resendBtn = findViewById(R.id.resendBtn);


        otpEt1.addTextChangedListener(textWatcher);
        otpEt2.addTextChangedListener(textWatcher);
        otpEt3.addTextChangedListener(textWatcher);
        otpEt4.addTextChangedListener(textWatcher);
        otpEt5.addTextChangedListener(textWatcher);
        otpEt6.addTextChangedListener(textWatcher);

        showKeyboard(otpEt1);

        startCountDownTimer();

        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resendEnabled){
                    startCountDownTimer();
//                    sendVerificationCode();
                }
            }
        });

        final Button verifyBtn = findViewById(R.id.verifyBtn);
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String generateOtp = otpEt1.getText().toString()+otpEt2.getText().toString()+otpEt3.getText().toString()+otpEt4.getText().toString()+otpEt5.getText().toString()+otpEt6.getText().toString();

                if (generateOtp.length()==6)
                {
                    if (readLastButtonPressed() == 1) {

                        startActivity(new Intent(OTPVerificationCreAcc.this, ForgotPass2.class));
                    }
                    else if (readLastButtonPressed() == 2) {
                       /* PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, generateOtp.replace(" ", ""));
                        signInWithPhoneAuthCredential(credential);*/

                        final String usernameTxt = getIntent().getStringExtra("usernameTxt");
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

/*
    private void  sendVerificationCode() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mobile)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential credential) {
                                signInWithPhoneAuthCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Toast.makeText(OTPVerificationCreAcc.this, "Failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                OTPVerificationCreAcc.this.id = verificationId;
                                Toast.makeText(OTPVerificationCreAcc.this, "Done", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
  */
/*                          final String usernameTxt = getIntent().getStringExtra("usernameTxt");
                            databaseReference.child("users").child(usernameTxt).child("Fullname").setValue(getIntent().getStringExtra("usernameTxt"));
                            databaseReference.child("users").child(usernameTxt).child("Email").setValue(getIntent().getStringExtra("emailTxt"));
                            databaseReference.child("users").child(usernameTxt).child("Mobile").setValue(getIntent().getStringExtra("mobileTxt"));
                            databaseReference.child("users").child(usernameTxt).child("Password").setValue(getIntent().getStringExtra("passwordTxt"));

                            View alertCustomDialog = LayoutInflater.from(OTPVerificationCreAcc.this).inflate(R.layout.custom_dialog,null);
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(OTPVerificationCreAcc.this);
                            final  AlertDialog dialog = alertDialog.create();

                            alertDialog.setView(alertCustomDialog);

                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();


                            startActivity(new Intent(OTPVerificationCreAcc.this, ActivityHome.class));
                            finish();
                            FirebaseUser user = task.getResult().getUser();
                        } else {
                            Toast.makeText(OTPVerificationCreAcc.this, "Verification Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
*/

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
                resendBtn.setTextColor(getResources().getColor(R.color.bleu));
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
                else if(selectedETPosition == 3){
                    selectedETPosition = 4;
                    showKeyboard(otpEt5);
                }
                else if(selectedETPosition == 4){
                    selectedETPosition = 5;
                    showKeyboard(otpEt6);
                }
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if(selectedETPosition == 6){
                selectedETPosition = 5;
                showKeyboard(otpEt6);
            }
            else if(selectedETPosition == 5){
                selectedETPosition = 4;
                showKeyboard(otpEt5);
            }
            else if(selectedETPosition == 4){
                selectedETPosition = 3;
                showKeyboard(otpEt4);
            }
            else if(selectedETPosition == 3){
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