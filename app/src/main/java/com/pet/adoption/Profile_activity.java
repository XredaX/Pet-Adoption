package com.pet.adoption;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Profile_activity extends AppCompatActivity {

    AppCompatButton AddAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



/*        AddAddress = (AppCompatButton) findViewById(R.id.AddAddress);
        AddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Adresse_Activity.this, AddAddress.class);
                startActivity(intent);
            }
        });*/
    }
}