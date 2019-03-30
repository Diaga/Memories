package com.kat.journal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GoogleSignInClient gsClient;
    private final int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gsOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestProfile()
                .requestEmail()
                .build();

        gsClient = GoogleSignIn.getClient(this, gsOptions);
        GoogleSignInAccount gsAccount = GoogleSignIn.getLastSignedInAccount(this);
        updateLoginUI(gsAccount);
    }

    private void updateLoginUI(GoogleSignInAccount gsAccount) {
        if (gsAccount != null) {
            Log.i("GoogleSignInSuccessful", gsAccount.getEmail());
            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(homeIntent);
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
            GoogleSignInAccount gsAccount = gsAccountTask.getResult(ApiException.class);
            updateLoginUI(gsAccount);
        } catch (ApiException e) {
            Log.w("GoogleSignInFailed", e);
        }
    }
}

