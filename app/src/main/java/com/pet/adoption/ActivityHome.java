package com.pet.adoption;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ActivityHome extends AppCompatActivity {

    private BottomNavigationView bo;
    private FloatingActionButton addpet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bo = findViewById(R.id.bottomNavigationview);

        addpet = findViewById(R.id.addpet);
        addpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new fragment_add());
            }
        });

        bo.setBackground(null);
        replaceFragment(new fragment_home());
        bo.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.pet:
                    replaceFragment(new fragment_pet());
                    break;
                case R.id.favor:
                    replaceFragment(new fragment_fav());
                    break;
                case R.id.profil:
                    replaceFragment(new fragment_profil());
                    break;
                case R.id.home:
                    replaceFragment(new fragment_home());
                    break;
            }
            return true;
        });

    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}


