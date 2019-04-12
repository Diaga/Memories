package com.pika.memories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Connect with database
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        updateUI();
    }

    private void updateUI() {
        String defaultImageURI = "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg";
        TextView nameTextView = findViewById(R.id.displayName);
        nameTextView.setText(userViewModel.getSignedInUser().getDisplayName());

        ImageView avatar = findViewById(R.id.avatar);
        if (userViewModel.getSignedInUser().getPhotoURI().equals("")) {
            avatar.setImageURI(Uri.parse(defaultImageURI));
        } else {
            avatar.setImageURI(Uri.parse(userViewModel.getSignedInUser().getPhotoURI()));
        }
    }
}
