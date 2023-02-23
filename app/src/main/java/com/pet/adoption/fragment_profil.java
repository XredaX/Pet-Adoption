package com.pet.adoption;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

import es.dmoral.toasty.Toasty;


public class fragment_profil extends Fragment implements recycleViewInterface{

    TextView TxtProfile, TxtAddress, TxtLogout, TxtNoti;

    Activity context;
    private BottomSheetDialog bottomSheetDialog;
    RecyclerView rec, recNotifications;
    noti_adapter adapter1;
    ArrayList<notifications> li1 = new ArrayList<>();

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

        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetTheme);


        li1.add(new notifications("Notification 1", "2022-10-07", "we’ll notify you when something arrives..."));
        li1.add(new notifications("Notification 2", "2022-05-20", "we’ll notify you when something arrives..."));
        li1.add(new notifications("Notification 3", "2022-12-17", "we’ll notify you when something arrives..."));
        li1.add(new notifications("Notification 4", "2022-11-27", "we’ll notify you when something arrives..."));
        li1.add(new notifications("Notification 4", "2022-11-27", "we’ll notify you when something arrives..."));
        li1.add(new notifications("Notification 4", "2022-11-27", "we’ll notify you when something arrives..."));
        li1.add(new notifications("Notification 4", "2022-11-27", "we’ll notify you when something arrives..."));
        li1.add(new notifications("Notification 4", "2022-11-27", "we’ll notify you when something arrives..."));

        adapter1 = new noti_adapter(li1, this);

        getview.findViewById(R.id.TxtNoti).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.notification, null);

                recNotifications = view1.findViewById(R.id.recNotifications);
                GridLayoutManager gg = new GridLayoutManager(getContext() ,1);
                recNotifications.setLayoutManager(gg);
                recNotifications.setAdapter(adapter1);

                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();
            }

        });

        return getview;


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

        TxtLogout = (TextView) context.findViewById(R.id.imgLogout);
        TxtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, Login.class));
                context.finish();

                SharedPreferences sp = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
                SharedPreferences.Editor Ed = sp.edit();
                Ed.putString("username", "");
                Ed.putString("password", "");
                Ed.commit();

                SharedPreferences sp1=getContext().getSharedPreferences("CHECKLOGIN", Context.MODE_PRIVATE);
                SharedPreferences.Editor Ed1 = sp1.edit();
                Ed1.putString("checkL", "True");
                Ed1.commit();
            }
        });
    }

    public void onclick1(int pos) {

    }
}