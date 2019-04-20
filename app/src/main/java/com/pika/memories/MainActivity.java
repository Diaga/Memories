package com.pika.memories;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    UserViewModel userViewModel;
    SettingsViewModel settingsViewModel;
    private final int LOGOUT_REQUEST = 1;

    Fragment[] fragments = {new HomeFragment(), new CalendarFragment(), new StatisticsFragment()};
    String[] fragmentTags = {"Home", "Calendar", "Statistics"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        Intent mainIntent = getIntent();

        // Connect with Database
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);

        // Check from where MainActivity was invoked and perform corresponding actions
        String fromCode = mainIntent.getExtras().getString("fromCode");
        if (fromCode != null && fromCode.equals("LoginActivity")) {
            Utils.updateThemeOnSettings(settingsViewModel.getCurrentSettings(userViewModel.getSignedInUser().getId()));
        }

        Toolbar toolbar = findViewById(R.id.actionBarTop);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView drawerView = findViewById(R.id.drawerView);
        drawerView.setNavigationItemSelectedListener(this);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Update UI
        updateUI();

        // Load And Hide All Fragments
        loadAllFragments();
        hideAllFragments();

        fragmentLoader(getSupportFragmentManager().findFragmentByTag(fragmentTags[0]));
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
            = menuItem -> {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.home:
                fragment = getSupportFragmentManager().findFragmentByTag(fragmentTags[0]);
                break;
            case R.id.calendar:
                fragment = getSupportFragmentManager().findFragmentByTag(fragmentTags[1]);
                break;
            case R.id.statistics:
                fragment = getSupportFragmentManager().findFragmentByTag(fragmentTags[2]);
                break;
            case R.id.chat:
                Intent chatIntent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(chatIntent);
                break;
        }
        return fragmentLoader(fragment);
    };


    public boolean fragmentLoader(Fragment fragment) {
        if (fragment != null) {
            hideAllFragments();
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
            return true;
        }
        return false;
    }

    private void signOut() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.putExtra("requestCode", LOGOUT_REQUEST);
        startActivityForResult(loginIntent, LOGOUT_REQUEST);
    }

    private void updateUI() {
        NavigationView drawerView = findViewById(R.id.drawerView);
        View drawerHeader = drawerView.getHeaderView(0);
        TextView drawerNameView = drawerHeader.findViewById(R.id.drawerNameView);
        drawerNameView.setText(userViewModel.getSignedInUser().getDisplayName());

        ImageView drawerImageView = drawerHeader.findViewById(R.id.drawerImageView);
        String imageURI = userViewModel.getSignedInUser().getPhotoURI();
        if (imageURI == null) {
            Picasso.with(getApplicationContext()).load(R.drawable.default_avatar).into(drawerImageView);
        } else {
            File[] fileCheck = getCacheDir().listFiles((dir, name) -> name.equals("avatar.jpg"));
            if (fileCheck.length > 0) {
                byte[] bytes = Utils.readCacheToBytes(fileCheck[0]);
                Bitmap bitmap = Utils.bytesToImage(bytes);
                drawerImageView.setImageBitmap(bitmap);
            } else {
                Picasso.with(getApplicationContext()).load(imageURI).into(drawerImageView);
            }
        }
    }

    private void hideAllFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int counter = 0; counter < 3; counter++) {
            fragmentTransaction.hide(fragmentManager.findFragmentByTag(fragmentTags[counter]));
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }


    private void loadAllFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int counter = 0; counter < 3; counter++) {
            fragmentTransaction.add(R.id.fg_container, fragments[counter], fragmentTags[counter]);
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }


    public void startEditor(View view) {
        Intent editorIntent = new Intent(getApplicationContext(), EditorActivity.class);
        startActivity(editorIntent);
    }

}
