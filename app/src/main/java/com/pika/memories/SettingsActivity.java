package com.pika.memories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private Switch themeSwitch;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_settings);

        // Connect with database
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        themeSwitch = findViewById(R.id.mode_switch);

        // Change themeSwitch state based on theme
        if (Utils.currentTheme.equals(Utils.THEME_BLACK)) {
            themeSwitch.setChecked(true);
        } else if (Utils.currentTheme.equals(Utils.THEME_WHITE)) {
            themeSwitch.setChecked(false);
        }

        // Change theme here!
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String theme;
            if (isChecked) {
                theme = Utils.THEME_BLACK;
                Utils.changeTheme(theme);
            } else {
                theme = Utils.THEME_WHITE;
                Utils.changeTheme(theme);
            }
            userViewModel.setTheme(theme);
            restartApp();
        });
    }

    public void restartApp() {
        Log.i("CHANGETHEME_INFO", "Done!");
        Intent changeThemeIntent = new Intent(getApplicationContext(), MainActivity.class);
        changeThemeIntent.putExtra("fromCode", "SettingsActivity");
        changeThemeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(changeThemeIntent);
    }

    public void delete(View view){
        Toast.makeText(this, "Feature under Testing", Toast.LENGTH_SHORT).show();
    }
}
