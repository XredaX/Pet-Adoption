package com.pet.adoption;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class fragment_profil extends Fragment {

    TextView TxtProfile, TxtAddress, TxtLogout;

    Activity context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        context = getActivity();
        View getview = inflater.inflate(R.layout.fragment_profil, container, false);

        return inflater.inflate(R.layout.fragment_profil, container, false);

    }

    public void onStart() {
        super.onStart();

        //Go to Profile_Activity
        TxtProfile = (TextView) context.findViewById(R.id.imgProfile);
        TxtProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Profile_activity.class);
                startActivity(intent);
            }
        });

        //Go to Address_Activity
        TxtAddress = (TextView) context.findViewById(R.id.imgAddress);
        TxtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Adresse_Activity.class);
                startActivity(intent);
            }
        });

        /*
        TxtLogout = (TextView) context.findViewById(R.id.imgLogout);
        TxtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Login.class);
                startActivity(intent);
            }
        });
         */
    }
}