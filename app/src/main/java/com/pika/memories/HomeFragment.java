package com.pika.memories;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        Button button = view.findViewById(R.id.change_theme_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTheme();
            }
        });
        return view;
    }

    private void changeTheme() {
        Log.i("CHANGETHEME_INFO", String.valueOf("changetheme"));
        Utils.changeTheme();
        Intent changeThemeIntent = new Intent(getContext(), MainActivity.class);
        startActivity(changeThemeIntent);
    }
}
