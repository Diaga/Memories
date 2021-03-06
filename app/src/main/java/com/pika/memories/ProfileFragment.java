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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Connect with database
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        updateUI(view);
    }

    private void updateUI(View view) {
        TextView nameTextView = view.findViewById(R.id.displayName);
        nameTextView.setText(userViewModel.getSignedInUser().getDisplayName());

        TextView emailTextView = view.findViewById(R.id.emailAddress);
        emailTextView.setText(userViewModel.getSignedInUser().getEmail());

        TextView registeredOnView = view.findViewById(R.id.date);
        registeredOnView.setText(Utils.timestampToDateTime(userViewModel.getSignedInUser().
                getRegisteredOn(), " hh:mm:ss a on dd/MM/yy"));

        ImageView avatar = view.findViewById(R.id.avatar);
        String imageURI = userViewModel.getSignedInUser().getPhotoURI();

        if (imageURI.equals("null")) {
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
