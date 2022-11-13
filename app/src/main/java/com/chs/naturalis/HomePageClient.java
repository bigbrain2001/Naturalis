package com.chs.naturalis;

import static java.util.logging.Logger.getLogger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.logging.Logger;

public class HomePageClient extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private static final int SPLASH_SCREEN = 1000;

    private static final Logger LOGGER = getLogger(HomePageClient.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_client);

        actionOnNavBarItemSelected();
    }

    @SuppressLint("NonConstantResourceId")
    private void actionOnNavBarItemSelected() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuHome:
                    return true;
                case R.id.menuCart:
                    // TODO
                    return true;
                case R.id.menuAccount:
                    LOGGER.info("Transition to Profile activity was successful.");
                    transitionToProfileActivity();
                    return true;
                case R.id.menuLogout:
                    // TODO
                    return true;
            }
            return false;
        });
    }

    private void transitionToProfileActivity() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(HomePageClient.this, Profile.class);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN);
    }
}