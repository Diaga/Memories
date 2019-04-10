package com.pika.memories;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
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

            // Local Database
            updateDatabase();

            // Server
            register(gsAccount);

            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            setResult(RESULT_OK, mainIntent);
            startActivity(mainIntent);
            finish();
        } else {
            findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.sign_in_button:
                            signIn();
                            break;
                    }
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
        gsClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                userViewModel.signOutAllUsers();
                Log.i("GoogleSignOutSuccessful", "OK!");
            }
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
        User user = new User();
        user.setDisplayName(gsAccount.getDisplayName());
        user.setId(gsAccount.getId());
        user.setEmail(gsAccount.getEmail());
        if (gsAccount.getPhotoUrl() != null) {
            user.setPhotoURI(gsAccount.getPhotoUrl().toString());
        } else {
            user.setPhotoURI("null");
        }
        user.setSignedIn("1");
        userViewModel.insertUser(user);
    }


    void register(GoogleSignInAccount gsAccount) {
        String displayName = gsAccount.getDisplayName();
        String id = gsAccount.getId();
        String email = gsAccount.getEmail();
        String photoURL = "null";
        if (gsAccount.getPhotoUrl() != null) {
            photoURL = gsAccount.getPhotoUrl().toString();
        }
        String[] args = {"id", "displayName", "email", "photoURI"};
        String[] params = {id, displayName, email, photoURL};
        String query = Server.queryBuilder(args, params);
        String url = Server.urlBuilder("register", query);
        new registerTask(userViewModel).execute(url);
    }

}
