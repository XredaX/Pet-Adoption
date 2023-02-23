package com.pet.adoption;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import es.dmoral.toasty.Toasty;


public class fragment_pet extends Fragment implements recycleViewInterface{
    rec_adapter1 adapter  ;
    ArrayList<animal_profil> li= new ArrayList<>();
    RecyclerView rec;
    LinearLayout linearLayout;
    private BottomSheetDialog bottomSheetDialog;
    private TextInputLayout textInputLayout1, textInputLayout2;
    private AutoCompleteTextView autoCompleteTextView1, autoCompleteTextView2;
    private SearchView searchView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pet-adoption-c09f7-default-rtdb.firebaseio.com/");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pet, container, false);
        linearLayout = view.findViewById(R.id.linearLayout3);
        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });

        searchPets();

        adapter = new rec_adapter1(li, this);
        rec = view.findViewById(R.id.rec1);
        rec.setAdapter(adapter);
        GridLayoutManager g = new GridLayoutManager(getContext() , 2);
        rec.setLayoutManager(g);


        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetTheme);
        view.findViewById(R.id.filterPetsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.filter, null);

                autoCompleteTextView1 = view1.findViewById(R.id.autoCompleteTextView1);
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getContext(),
                        R.array.categoriesS, android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                autoCompleteTextView1.setAdapter(adapter1);

                autoCompleteTextView2 = view1.findViewById(R.id.autoCompleteTextView2);
                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                        R.array.categoriesS, android.R.layout.simple_spinner_item);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                autoCompleteTextView2.setAdapter(adapter2);

                final RadioGroup radioGrp1 = view1.findViewById(R.id.radioBtnGrpFilter);
                textInputLayout1 = view1.findViewById(R.id.textInputLayoutFilter1);
                textInputLayout2 = view1.findViewById(R.id.textInputLayoutFilter2);

                filterData(textInputLayout1, textInputLayout2, radioGrp1);

                view1.findViewById(R.id.applyBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int radioId1 = radioGrp1.getCheckedRadioButtonId();
                        RadioButton radioButton1 = view1.findViewById(radioId1);
                        String spinner1 = autoCompleteTextView1.getText().toString();
                        String spinner2 = autoCompleteTextView2.getText().toString();

                        SharedPreferences sp=getContext().getSharedPreferences("filter", Context.MODE_PRIVATE);
                        SharedPreferences.Editor Ed=sp.edit();
                        Ed.putString("PET_TYPE", spinner1);
                        Ed.putString("PET_BREED", spinner2);
                        Ed.putString("PET_GENDER", String.valueOf(radioId1));
                        Ed.commit();
                        Toasty.normal(getContext(), "Modifications saved", Toast.LENGTH_SHORT, ContextCompat.getDrawable(getContext(), R.drawable.ic_pet)).show();

                    }
                });

                view1.findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.cancel();
                    }
                });

                view1.findViewById(R.id.resetBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            clearFilterData(textInputLayout1, textInputLayout2, radioGrp1);
                        }
                        catch (Exception e) {}
                    }
                });

                bottomSheetDialog.setContentView(view1);
                bottomSheetDialog.show();
            }

        });

        return view;
    }

    private void filterList(String s) {
        ArrayList<animal_profil> filterList = new ArrayList<>();

        for (animal_profil item : li) {
            String[] string = item.getDesc().toLowerCase().split("", 2);
            String genre = string[0];
            if (item.getNom().toLowerCase().contains(s.toLowerCase()) ||
                    item.getLoc().toLowerCase().contains(s.toLowerCase())
                || genre.contains(s.toLowerCase())) filterList.add(item);
        }
        if (filterList.isEmpty()) Toast.makeText(getContext(), "No data found!", Toast.LENGTH_SHORT).show();
        else adapter.setFilterList(filterList);
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

    public void check(String username, String id, ImageView favBtn){
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

    void filterData(TextInputLayout textInputLayout1, TextInputLayout textInputLayout2, RadioGroup radioGrp1){
        try {
            SharedPreferences sp1=getContext().getSharedPreferences("filter", Context.MODE_PRIVATE);
            String PET_TYPE =sp1.getString("PET_TYPE", null);
            String PET_BREED =sp1.getString("PET_BREED", null);
            String PET_GENDER =sp1.getString("PET_GENDER", null);

            if (!PET_TYPE.isEmpty()) textInputLayout1.setHint(PET_TYPE);
            if (!PET_BREED.isEmpty()) textInputLayout2.setHint(PET_BREED);
            if (!PET_GENDER.isEmpty()) radioGrp1.check(Integer.parseInt(PET_GENDER));
        }
        catch (Exception e) {}
    }

    void clearFilterData(TextInputLayout textInputLayout1, TextInputLayout textInputLayout2, RadioGroup radioGrp1) {
        SharedPreferences sp1 = getContext().getSharedPreferences("filter", Context.MODE_PRIVATE);
        SharedPreferences.Editor Ed = sp1.edit();
        Ed.putString("PET_TYPE", "All");
        Ed.putString("PET_BREED", "All");
        Ed.putString("PET_GENDER", "");
        Ed.commit();

        textInputLayout1.setHint("All");
        textInputLayout2.setHint("All");
        radioGrp1.clearCheck();
        Toasty.normal(getContext(), "Reset done", Toast.LENGTH_SHORT, ContextCompat.getDrawable(getContext(), R.drawable.ic_reset)).show();

    }

    void searchPets() {
        SharedPreferences sp1=getContext().getSharedPreferences("filter", Context.MODE_PRIVATE);
        String PET_TYPE =sp1.getString("PET_TYPE", null);
        String PET_BREED =sp1.getString("PET_BREED", null);
        String PET_GENDER =sp1.getString("PET_GENDER", null);

        databaseReference.child("pets").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists()) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            String uniqueID = item.child("ID").getValue(String.class);
                            String[] date1 = item.child("BirthDay").getValue(String.class).split("-", 3);
                            String url = item.child("Image").getValue(String.class);
                            String race = item.child("PetBreed").getValue(String.class);
                            String weight = item.child("Weight").getValue(String.class);
                            String description = item.child("Description").getValue(String.class);
                            String loc = item.child("City").getValue(String.class);
                            String owner = item.child("Owner").getValue(String.class);
                            int year = Integer.parseInt(date1[0]), month = Integer.parseInt(date1[1]), dayOfMonth = Integer.parseInt(date1[2]);
                            String age;
                            int age1 = Period.between(
                                    LocalDate.of(year, month, dayOfMonth),
                                    LocalDate.now()
                            ).getYears();
                            if (age1 == 0) {
                                age1 = Period.between(
                                        LocalDate.of(year, month, dayOfMonth),
                                        LocalDate.now()
                                ).getMonths();
                                age = String.valueOf(age1) + " Months";
                            } else age = String.valueOf(age1) + " Years";
                            String desc = item.child("Gender").getValue(String.class) + ", " + age;

//                            if (PET_TYPE.equals("All") && PET_BREED.equals("All") && PET_LOC.equals("") && PET_GENDER.equals(""))
                            li.add(new animal_profil(url, item.child("PetName").getValue(String.class),
                                    desc, loc, race, weight, description, owner, uniqueID));
                            /*else if (PET_TYPE.equals("All") && PET_BREED.equals("All")*/


                        }
                        adapter.notifyDataSetChanged();
                    } else linearLayout.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}