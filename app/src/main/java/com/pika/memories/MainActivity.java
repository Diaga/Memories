package com.pika.memories;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private UserViewModel userViewModel;
    private final int LOGIN_REQUEST = 0;
    private final int LOGOUT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        Intent loginIntent = getIntent();

        // Connect with Database
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        Toolbar toolbar = findViewById(R.id.actionBarTop);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView drawerView = findViewById(R.id.drawerView);
        drawerView.setNavigationItemSelectedListener(this);

        fragmentLoader(new HomeFragment());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (!signedIn()) {
            signIn();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!signedIn()) {
            signIn();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.profile:
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                startActivity(profileIntent);
                break;
            case R.id.settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.help:
                fragment = new HelpFragment();
                break;
            case R.id.logout:
                signOut();
                break;
        }
        fragmentLoader(fragment);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.home:
                    fragment = new HomeFragment();
                    break;
                case R.id.calendar:
                    fragment = new CalendarFragment();
                    break;
                case R.id.statistics:
                    fragment = new StatisticsFragment();
                    break;
                case R.id.bar_test:
                    Intent test_intent = new Intent(getApplicationContext(), EditorActivity.class);
                    startActivity(test_intent);
            }
            return fragmentLoader(fragment);
        }
    };


    public boolean fragmentLoader(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fg_container, fragment).commit();
            return true;
        }
        return false;
    }

    private boolean signedIn() {
        boolean checkSignedIn = userViewModel.getSignedInUser() != null;
        updateUI(checkSignedIn);
        return checkSignedIn;
    }

    private void signIn() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.putExtra("requestCode", LOGIN_REQUEST);
        startActivityForResult(loginIntent, LOGIN_REQUEST);
    }

    private void signOut() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.putExtra("requestCode", LOGOUT_REQUEST);
        startActivityForResult(loginIntent, LOGOUT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                signedIn();
            }
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            NavigationView drawerView = findViewById(R.id.drawerView);
            View drawerHeader = drawerView.getHeaderView(0);
            TextView drawerNameView = drawerHeader.findViewById(R.id.drawerNameView);
            drawerNameView.setText(userViewModel.getSignedInUser().getDisplayName());

            ImageView drawerImageView = drawerHeader.findViewById(R.id.drawerImageView);
            drawerImageView.setImageURI(Uri.parse(userViewModel.getSignedInUser().getPhotoURI()));
        }
    }

    public void startChat(View view) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        startActivity(intent);
    }
}
