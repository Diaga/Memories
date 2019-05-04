package com.pika.memories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ProfileFragment extends Fragment {
    private UserViewModel userViewModel;
    private ImageButton settings;
    View view;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_profile, container, false);
            settings = view.findViewById(R.id.settingsButton);
            settings.setOnClickListener(v -> startSettings());
            return view;
    }

    public void startSettings(){
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }



    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect with database
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        //updateUI();
    }
    private void updateUI() {
        TextView nameTextView = getView().findViewById(R.id.displayName);
        nameTextView.setText(userViewModel.getSignedInUser().getDisplayName());

        ImageView avatar = getView().findViewById(R.id.avatar);
        String imageURI = userViewModel.getSignedInUser().getPhotoURI();
        if (imageURI == null) {
            Picasso.with(getContext()).load(R.drawable.default_avatar).into(avatar);
        } else {
            File[] fileCheck = getActivity().getCacheDir().listFiles((dir, name) -> name.equals("avatar.jpg"));
            if (fileCheck.length > 0) {
                byte[] bytes = Utils.readBytes(fileCheck[0]);
                Bitmap bitmap = Utils.bytesToImage(bytes);
                avatar.setImageBitmap(bitmap);
            } else {
                Picasso.with(getContext()).load(imageURI).into(avatar);
            }
        }
    }
}
