package com.pet.adoption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotPass extends AppCompatActivity {
    void saveLastButtonPressed(int buttonNumber) {
        SharedPreferences sharedPref = getSharedPreferences("application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("LAST_BUTTON", buttonNumber);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        final TextView emailEt = findViewById(R.id.emailEt);


/*        ok_btn = (Button) alertCustomDialog.findViewById(R.id.ok_btn_id);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });*/

        final TextView resetBtn = findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String emailTxt = emailEt.getText().toString();

                if (emailTxt.isEmpty()) Toast.makeText(ForgotPass.this, "Check your information, then try again!", Toast.LENGTH_SHORT).show();
                else {
                    saveLastButtonPressed(1);
                    startActivity(new Intent(ForgotPass.this, OTPVerificationCreAcc.class));
                }
            }


        });
    }
}