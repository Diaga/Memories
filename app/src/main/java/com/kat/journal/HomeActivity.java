package com.kat.journal;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    BottomAppBar navBottomBar;
    FloatingActionButton chatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent homeIntent = getIntent();
        chatButton = findViewById(R.id.chatButton);

        navBottomBar = findViewById(R.id.navBottomBar);
    }
}
