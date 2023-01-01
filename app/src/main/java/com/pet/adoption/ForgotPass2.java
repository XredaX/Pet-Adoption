package com.pet.adoption;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotPass2 extends AppCompatActivity {

    private boolean passwordShowing = false;
    private boolean conPasswordShowing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass2);

        final EditText password = findViewById(R.id.newPasswordET);
        final EditText conPassword = findViewById(R.id.conNewPasswordET);
        final ImageView passwordIcon = findViewById(R.id.passwordIcon);
        final ImageView conPasswordIcon = findViewById(R.id.conPasswordIcon);

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

        View alertCustomDialog = LayoutInflater.from(ForgotPass2.this).inflate(R.layout.custom_dialog,null);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ForgotPass2.this);
        alertDialog.setView(alertCustomDialog);
        final  AlertDialog dialog = alertDialog.create();

        Button ok_btn = alertCustomDialog.findViewById(R.id.ok_btn_id);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPass2.this, Login.class));
            }
        });



        final TextView resetBtn = findViewById(R.id.newResetBtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String passwordTxt = password.getText().toString();
                final String conPasswordTxt = conPassword.getText().toString();

                if (passwordTxt.isEmpty() || conPasswordTxt.isEmpty()){
                    Toast.makeText(ForgotPass2.this, "We need more data about you", Toast.LENGTH_SHORT).show();
                }
                else if(!passwordTxt.equals(conPasswordTxt)) {
                    Toast.makeText(ForgotPass2.this, "You should type the same password", Toast.LENGTH_SHORT).show();
                }
                else {
                    ImageView imgDialog = alertCustomDialog.findViewById(R.id.imgDialog);
                    imgDialog.setImageResource(R.drawable.pass);

                    TextView titleDialog = alertCustomDialog.findViewById(R.id.titleDialog);
                    titleDialog.setText("Password Changed");

                    TextView descDialog = alertCustomDialog.findViewById(R.id.descDialog);
                    descDialog.setText("Your Password has been successfully changed!");


                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            }
        });
    }
}