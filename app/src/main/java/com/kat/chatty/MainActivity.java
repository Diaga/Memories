package com.kat.chatty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final int LOGIN_REQUEST = 0;
    static GoogleSignInAccount gsAccount;
    static GoogleSignInClient gsClient;

    public void startchat(View view) {
        Intent intent = new Intent(getApplicationContext(), Chat.class);
        startActivity(intent);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent loginIntent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.profile:
                fragment = new ProfileFragment();
                break;
            case R.id.settings:
                fragment = new SettingsFragment();
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
        gsAccount = GoogleSignIn.getLastSignedInAccount(this);
        GoogleSignInOptions gsOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestProfile()
                .requestEmail()
                .build();

        gsClient = GoogleSignIn.getClient(this, gsOptions);
        boolean checkSignedIn = gsAccount != null;
        if (checkSignedIn) {
            updateUI(true);
        }
        updateUI(false);
        return checkSignedIn;
    }

    private void signIn() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(loginIntent, LOGIN_REQUEST);
    }

    private void signOut() {
        gsClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                signIn();
            }
        });
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
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View navHeader = navigationView.getHeaderView(0);
            TextView navBarNameView = navHeader.findViewById(R.id.navBarNameView);
            navBarNameView.setText(gsAccount.getDisplayName());

            ImageView navBarImageView = navHeader.findViewById(R.id.navBarImageView);
            navBarImageView.setImageURI(gsAccount.getPhotoUrl());
        }
    }
}
