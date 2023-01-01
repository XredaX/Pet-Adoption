package com.pet.adoption;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class ActivityHome extends AppCompatActivity {

    private MeowBottomNavigation BottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigation = findViewById(R.id.navigationBar);

        BottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        BottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_pet));
        BottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.ic_add));
        BottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_favo));
        BottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_profil));

        BottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                if (item.getId() ==5) {
                    fragment = new fragment_add();
                }
                else if (item.getId() ==4){
                    fragment = new fragment_profil();
                }else  if (item.getId() ==3){
                    fragment = new fragment_fav();
                }
                else  if (item.getId() ==2){
                    fragment = new fragment_pet();
                }
                else {
                    fragment = new fragment_home();
                }
                loadFragment(fragment);
            }
        });

        BottomNavigation.setCount(1, "10");
        BottomNavigation.show(1, false);

        BottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //display a toast
//                Toast.makeText(getApplicationContext()," You clicked "+ item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                //display a toast
//                Toast.makeText(getApplicationContext()," You reselected "+ item.getId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        //replace the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,fragment, null)
                .commit();
    }
}


