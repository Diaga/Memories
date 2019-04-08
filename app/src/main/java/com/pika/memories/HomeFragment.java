package com.pika.memories;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment {

    private int currentTheme = 1;

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

    void changeTheme() {
        Log.i("CHANGETHEME_INFO", String.valueOf(currentTheme));
        if (currentTheme == 1) {
            currentTheme = 2;
            Utils.changeToTheme(getActivity(), currentTheme);
        } else {
            currentTheme = 1;
            Utils.changeToTheme(getActivity(), currentTheme);
        }
    }
}
