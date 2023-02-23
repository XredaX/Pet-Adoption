package com.pet.adoption;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class fragment_fav extends Fragment implements recycleViewInterface{
    rec_adapter1 adapter  ;
    ArrayList<animal_profil> li = new ArrayList<>();
    RecyclerView rec;
    LinearLayout linearLayout;
    private BottomSheetDialog bottomSheetDialog;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pet-adoption-c09f7-default-rtdb.firebaseio.com/");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);

        SharedPreferences sp1=getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        String username =sp1.getString("username", "");

        databaseReference.child("favs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists()) {
                        for(DataSnapshot item: snapshot.getChildren()){
                            String dt1 = item.child("Owner").getValue(String.class);
                            String dt2 = item.child("ID").getValue(String.class);
                            if (dt1.equals(username)){
                                getFavPets(dt2);
                            }
                        }
                    }
                }
                catch (Exception e){}
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        adapter = new rec_adapter1(li, this);
        rec = view.findViewById(R.id.rec2);
        rec.setAdapter(adapter);
        GridLayoutManager g = new GridLayoutManager(getContext() , 2);
        rec.setLayoutManager(g);

        return view;
    }

    private void getFavPets(String dt2) {
        databaseReference.child("pets").child(dt2).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists()) {
                            String uniqueID = snapshot.child("ID").getValue(String.class);
                            String[] date1 = snapshot.child("BirthDay").getValue(String.class).split("-", 3);
                            String url = snapshot.child("Image").getValue(String.class);
                            String race = snapshot.child("PetBreed").getValue(String.class);
                            String weight = snapshot.child("Weight").getValue(String.class);
                            String description = snapshot.child("Description").getValue(String.class);
                            String loc = snapshot.child("City").getValue(String.class);
                            String owner = snapshot.child("Owner").getValue(String.class);
                            int year = Integer.parseInt(date1[0]), month=Integer.parseInt(date1[1]), dayOfMonth=Integer.parseInt(date1[2]);
                            String age;
                            int age1 = Period.between(
                                    LocalDate.of(year, month, dayOfMonth),
                                    LocalDate.now()
                            ).getYears();
                            if (age1 == 0){
                                age1 = Period.between(
                                        LocalDate.of(year, month, dayOfMonth),
                                        LocalDate.now()
                                ).getMonths();
                                age = String.valueOf(age1) + " Months";
                            }
                            else age = String.valueOf(age1) + " Years";
                            String desc = snapshot.child("Gender").getValue(String.class) + ", " + age;
                            li.add(new animal_profil(url , snapshot.child("PetName").getValue(String.class),
                                    desc,loc, race, weight, description, owner, uniqueID));
                        }
                    adapter.notifyDataSetChanged();
                }
                catch (Exception e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onclick1(int pos) {
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.petselected, null);
        ImageView imageSelected, favBtn;
        TextView namePetDialog, raceAnimal, locAnimal, ageAnimal, genderAnimal, weightAnimal, descriptionAnimal;
        String id = li.get(pos).uniqueID;

        imageSelected = view1.findViewById(R.id.imageSelected);
        namePetDialog = view1.findViewById(R.id.namePetDialog);
        raceAnimal = view1.findViewById(R.id.raceAnimal);
        locAnimal = view1.findViewById(R.id.locAnimal);
        ageAnimal = view1.findViewById(R.id.ageAnimal);
        genderAnimal = view1.findViewById(R.id.genderAnimal);
        weightAnimal = view1.findViewById(R.id.weightAnimal);
        descriptionAnimal = view1.findViewById(R.id.descriptionAnimal);
        favBtn = view1.findViewById(R.id.favBtn);

        Picasso.get().load(li.get(pos).image).into(imageSelected);
        namePetDialog.setText(li.get(pos).nom);
        raceAnimal.setText(li.get(pos).race);
        locAnimal.setText(li.get(pos).loc);
        weightAnimal.setText(li.get(pos).weight);
        descriptionAnimal.setText(li.get(pos).description);

        String[] genderAge = li.get(pos).desc.split(",", 2);
        genderAnimal.setText(genderAge[0]);
        ageAnimal.setText(genderAge[1]);

        SharedPreferences sp1=getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        String username =sp1.getString("username", "");

        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetTheme);
        view1.findViewById(R.id.adoptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AdoptNow.class));
            }
        });

        check(username, id, favBtn);

        view1.findViewById(R.id.favBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("favs").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final String id1 = UUID.randomUUID().toString();
                        int a = 0;
                        try {
                            if (snapshot.exists()) {
                                for(DataSnapshot item: snapshot.getChildren()){
                                    String dt1 = item.child("Owner").getValue(String.class);
                                    String dt2 = item.child("ID").getValue(String.class);
                                    if (dt1.equals(username) && dt2.equals(id)){
                                        item.getRef().removeValue();
                                        favBtn.setColorFilter(ContextCompat.getColor(getContext(), R.color.gray));
                                        Toasty.info(getContext(), "Removed successfully", Toast.LENGTH_SHORT, true).show();
                                        a = 1;
                                    }
                                }
                                if (a != 1){
                                    databaseReference.child("favs").child(id1).child("ID").setValue(id);
                                    databaseReference.child("favs").child(id1).child("Owner").setValue(username);
                                    favBtn.setColorFilter(ContextCompat.getColor(getContext(), R.color.bleu));
                                    Toasty.success(getContext(), "Added successfully", Toast.LENGTH_SHORT, true).show();
                                }
                            }

                            else {
                                databaseReference.child("favs").child(id1).child("ID").setValue(id);
                                databaseReference.child("favs").child(id1).child("Owner").setValue(username);
                                favBtn.setColorFilter(ContextCompat.getColor(getContext(), R.color.bleu));
                                Toasty.success(getContext(), "Added successfully", Toast.LENGTH_SHORT, true).show();
                            }
                        }
                        catch (Exception e){}
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
        });

        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.show();
    }

    void check(String username, String id, ImageView favBtn){
        databaseReference.child("favs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists()) {
                        for(DataSnapshot item: snapshot.getChildren()){
                            String dt1 = item.child("Owner").getValue(String.class);
                            String dt2 = item.child("ID").getValue(String.class);
                            if (dt1.equals(username) && dt2.equals(id)) favBtn.setColorFilter(ContextCompat.getColor(getContext(), R.color.bleu));
                        }
                    }
                }
                catch (Exception e){}
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}