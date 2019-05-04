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

import android.os.Handler;
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

public class MainActivity extends BaseActivity {
    UserViewModel userViewModel;
    private final int LOGOUT_REQUEST = 1;

    Fragment[] fragments = {new HomeFragment(), new CalendarFragment(), new StatisticsFragment(),new ProfileFragment()};
    String[] fragmentTags = {"Home", "Calendar", "Statistics","Profile"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        Intent mainIntent = getIntent();

        // Connect with Database
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        // Check from where MainActivity was invoked and perform corresponding actions
        String fromCode = mainIntent.getExtras().getString("fromCode");
        if (fromCode != null && fromCode.equals("LoginActivity")) {
            if (userViewModel.getSignedInUser() != null) {
                Utils.changeTheme(userViewModel.getSignedInUser().getTheme());
            }
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Load And Hide All Fragments
        loadAllFragments();
        hideAllFragments();

        fragmentLoader(getSupportFragmentManager().findFragmentByTag(fragmentTags[0]));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = menuItem -> {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.home:
                fragment = getSupportFragmentManager().findFragmentByTag(fragmentTags[0]);
                if (fragment != null && fragment.isVisible()) {
                    return false;
                }
                break;
            case R.id.calendar:
                fragment = getSupportFragmentManager().findFragmentByTag(fragmentTags[1]);
                if (fragment != null && fragment.isVisible()) {
                    return false;
                }
                break;
            case R.id.statistics:
                fragment = getSupportFragmentManager().findFragmentByTag(fragmentTags[2]);
                if (fragment != null && fragment.isVisible()) {
                    return false;
                }
                break;
            case R.id.add:
                Intent intent = new Intent(getApplicationContext(),EditorActivity.class);
                startActivity(intent);
                break;
            case R.id.bottom_profile:
                fragment = getSupportFragmentManager().findFragmentByTag(fragmentTags[3]);
                if (fragment != null && fragment.isVisible()) {
                    return false;
                }
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


    private void hideAllFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int counter = 0; counter < 4; counter++) {
            fragmentTransaction.hide(fragmentManager.findFragmentByTag(fragmentTags[counter]));
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }


    private void loadAllFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int counter = 0; counter <4; counter++) {
            fragmentTransaction.add(R.id.fg_container, fragments[counter], fragmentTags[counter]);
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }


    public void signOut(View view) {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.putExtra("requestCode", LOGOUT_REQUEST);
        startActivityForResult(loginIntent, LOGOUT_REQUEST);
        finish();
    }
}
