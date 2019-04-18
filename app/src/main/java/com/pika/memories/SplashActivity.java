package com.pika.memories;

import android.content.Intent;
import android.os.Bundle;

import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

public class SplashActivity extends BaseActivity {

    private MemoryViewModel memoryViewModel;
    private MessageViewModel messageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Connect with Database
        memoryViewModel = ViewModelProviders.of(this).get(MemoryViewModel.class);
        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        memoryViewModel.clearTable();
        messageViewModel.clearTable();

        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
