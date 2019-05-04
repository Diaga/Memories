package com.pika.memories;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class LoginActivity extends BaseActivity {

    private UserViewModel userViewModel;

    private final int RC_SIGN_IN = 0;
    private final int RC_SIGN_OUT = 1;
    private GoogleSignInOptions gsOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .requestProfile()
            .requestEmail()
            .build();

    private GoogleSignInAccount gsAccount;
    private GoogleSignInClient gsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_login);
        Intent loginIntent = getIntent();
        gsClient = GoogleSignIn.getClient(this, gsOptions);

        // Connect with database
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        if (loginIntent.getIntExtra("requestCode", 0) == RC_SIGN_OUT) {
            signOut();
        }
        updateLoginUI(gsAccount);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void updateLoginUI(GoogleSignInAccount gsAccount) {
        if (gsAccount != null) {
            Log.i("GoogleSignInSuccessful", gsAccount.getEmail());

            userViewModel.signIn(gsAccount.getId(), "1");
            if (userViewModel.getUserFromId(gsAccount.getId()) == null) {
                // Local Database
                updateDatabase();

                // Server
                register();
            }

            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            mainIntent.putExtra("fromCode", "LoginActivity");
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            finish();
        } else {
            findViewById(R.id.sign_in_button).setOnClickListener(v -> {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            });
        }
    }

    private void signIn() {
        Intent googleSignInIntent = gsClient.getSignInIntent();
        startActivityForResult(googleSignInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                Task<GoogleSignInAccount> gsAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignIn(gsAccountTask);
        }
    }

    private void handleSignIn(Task<GoogleSignInAccount> gsAccountTask) {
        try {
            gsAccount = gsAccountTask.getResult(ApiException.class);
            updateLoginUI(gsAccount);
        } catch (ApiException e) {
            Log.w("GoogleSignInFailed", e);
        }
    }

    private void signOut() {
        gsClient.signOut().addOnCompleteListener(this, task -> {
            userViewModel.signOutAllUsers();
            Log.i("GoogleSignOutSuccessful", "OK!");
        });
    }

    void updateDatabase () {
        User user = userViewModel.getSignedInUser();
        if (user != null) {
                userViewModel.signOutAllUsers();
            }
        insertDatabase();
        }

    void insertDatabase () {

        // Insert User
        User user = new User();
        user.setDisplayName(gsAccount.getDisplayName());
        user.setId(gsAccount.getId());
        user.setEmail(gsAccount.getEmail());
        user.setPhotoURI("null");
        if (gsAccount.getPhotoUrl() != null) {
            user.setPhotoURI(gsAccount.getPhotoUrl().toString());
        }
        if (userViewModel.getSignedInUser() != null) {
            user.setAccessKey(userViewModel.getSignedInUser().getAccessKey());
        } else {
            user.setAccessKey("201");
        }
        user.setSignedIn("1");

        // Update User Settings
        user.setTheme(Utils.THEME_WHITE);
        user.setSync("1");

        userViewModel.insertUser(user);
    }

    void register() {
        String name = gsAccount.getDisplayName();
        String id = gsAccount.getId();
        String email = gsAccount.getEmail();
        String photoURI = "null";
        if (gsAccount.getPhotoUrl() != null) {
            photoURI = gsAccount.getPhotoUrl().toString();
        }
        String timestamp = String.valueOf(System.currentTimeMillis());
        String accessKey = "201";
        String theme = "white";
        String sync = "1";

        String[] args = {"id", "name", "email", "imageURI", "timestamp", "accessKey",
                "theme", "sync"};
        String[] params = {id, name, email, photoURI, timestamp, accessKey, theme, sync};
        String query = Server.queryBuilder(args, params);
        String url = Server.urlBuilder("register", query);

        // Update on server
        new registerTask(userViewModel).execute(url);
    }

}
