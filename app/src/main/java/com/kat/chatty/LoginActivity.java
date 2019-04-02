package com.kat.chatty;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import static com.kat.chatty.MainActivity.gsAccount;
import static com.kat.chatty.MainActivity.gsClient;

public class LoginActivity extends AppCompatActivity {
    private final int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent loginIntent = getIntent();
        updateLoginUI(gsAccount);
    }

    private void updateLoginUI(GoogleSignInAccount gsAccount) {
        if (gsAccount != null) {
            Log.i("GoogleSignInSuccessful", gsAccount.getEmail());
            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
            setResult(RESULT_OK, mainIntent);
            startActivity(mainIntent);
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
}
