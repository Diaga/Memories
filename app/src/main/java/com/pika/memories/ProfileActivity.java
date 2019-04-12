package com.pika.memories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

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
        TextView nameTextView = findViewById(R.id.displayName);
        nameTextView.setText(userViewModel.getSignedInUser().getDisplayName());

        ImageView avatar = findViewById(R.id.avatar);
        String imageURI = userViewModel.getSignedInUser().getPhotoURI();
        if (imageURI == null) {
            Picasso.with(getApplicationContext()).load(R.drawable.default_avatar).into(avatar);
        } else {
            File[] fileCheck = getCacheDir().listFiles((dir, name) -> name.equals("avatar.jpg"));
            if (fileCheck.length > 0) {
                byte[] bytes = Utils.readCacheToBytes(fileCheck[0]);
                Bitmap bitmap = Utils.bytesToImage(bytes);
                avatar.setImageBitmap(bitmap);
            } else {
                Picasso.with(getApplicationContext()).load(imageURI).into(avatar);
            }
        }
    }
}
