package com.zerostic.androiddevelopment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zerostic.androiddevelopment.Home.HomeFragment;
import com.zerostic.androiddevelopment.Profile.ProfileFragment;
import com.zerostic.androiddevelopment.Reels.ReelsFragment;
import com.zerostic.androiddevelopment.Search.SearchFragment;

public class MainActivity extends AppCompatActivity {
/*    MaterialCheckBox home, search, reels, profile;*/
    BottomNavigationView bottom_navigation;
    Fragment selectedFragment = null;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home){
                selectedFragment = new HomeFragment();
                Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
            }
            else if (item.getItemId() == R.id.search)
                selectedFragment = new SearchFragment();
            else if (item.getItemId() == R.id.reels)
                selectedFragment = new ReelsFragment();
            else if (item.getItemId() == R.id.profile){
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
                    sharedPreferences.edit().putString("profileId", mAuth.getCurrentUser().getUid()).apply();
                }
                selectedFragment = new ProfileFragment();
            }


            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }else{
            SharedPreferences sharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE);
            sharedPreferences.edit().putString("profileId", currentUser.getUid()).apply();
        }
    }
}