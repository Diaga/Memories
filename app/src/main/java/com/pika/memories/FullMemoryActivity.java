package com.pika.memories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class FullMemoryActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private MemoryViewModel memoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_full_memory);

        // Get memory id
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        // Connect with database
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        memoryViewModel = ViewModelProviders.of(this).get(MemoryViewModel.class);

        // Get memory data
        Memory memory = memoryViewModel.getMemoryFromId(id);

        // Get views
        TextView fullMemory = findViewById(R.id.fullMemory);
        ImageView imageView = findViewById(R.id.imageView);

        // Set values
        fullMemory.setText(memory.getMemory());
        if (!memory.getImagePath().equals("null")) {
            Picasso.with(getApplicationContext()).load(memory.getImagePath()).into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }
    }
}
