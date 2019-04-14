package com.pika.memories;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_settings);
    }

    public void changeTheme(View view) {
        Log.i("CHANGETHEME_INFO", "Done!");
        Utils.changeTheme();
        Intent changeThemeIntent = new Intent(getApplicationContext(), MainActivity.class);
        changeThemeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(changeThemeIntent);
    }
}
