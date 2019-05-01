package com.pika.memories;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

public class SplashActivity extends BaseActivity {

    private UserViewModel userViewModel;
    private MemoryViewModel memoryViewModel;
    private MessageViewModel messageViewModel;
    private SettingsViewModel settingsViewModel;

    private final int LOGIN_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Connect with Database
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        memoryViewModel = ViewModelProviders.of(this).get(MemoryViewModel.class);
        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);

        // Clear Tables
        memoryViewModel.clearTable();
        messageViewModel.clearTable();

        // SignIn user
        if (!signedIn()) {
            signIn();
        } else {
            // Get current settings and update app
            Settings settings = settingsViewModel.getCurrentSettings(userViewModel.getSignedInUser().getId());
            Utils.updateThemeOnSettings(settings);

            // Start main activity
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.putExtra("fromCode", "SplashActivity");
            startActivity(mainIntent);
            finish();
        }
    }


    private boolean signedIn() {
        return (userViewModel.getSignedInUser() != null);
    }

    private void signIn() {
        Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
        loginIntent.putExtra("requestCode", LOGIN_REQUEST);
        startActivity(loginIntent);
        finish();
    }
}
