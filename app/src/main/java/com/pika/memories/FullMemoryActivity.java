package com.pika.memories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

public class FullMemoryActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private MemoryViewModel memoryViewModel;
    String id;
    Memory memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_full_memory);

        // Get memory id
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        // Connect with database
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        memoryViewModel = ViewModelProviders.of(this).get(MemoryViewModel.class);

        // Get memory data
        memory = memoryViewModel.getMemoryFromId(id);

        // Get views
        TextView fullMemory = findViewById(R.id.fullMemory);
        ImageView imageView = findViewById(R.id.imageMemoryView);
        ImageView moodImageView = findViewById(R.id.moodImageInFull);

        // Set values
        fullMemory.setText(memory.getMemory());
        Picasso.with(getApplicationContext()).load(new File(memory.getImagePath())).into(imageView);
        
        // Set mood
        if (memory.getMood() != null) {
            String mood = Utils.getMoodFromScore(memory.getMood());
            if (mood.equals("excited")) {
                Picasso.with(getApplicationContext()).load(R.drawable.emo_excited).into(moodImageView);
            } else if (mood.equals("happy")) {
                Picasso.with(getApplicationContext()).load(R.drawable.emo_happy).into(moodImageView);
            } else if (mood.equals("neutral")) {
                Picasso.with(getApplicationContext()).load(R.drawable.emo_neutral).into(moodImageView);
            } else if (mood.equals("depressed")) {
                Picasso.with(getApplicationContext()).load(R.drawable.emo_depressed).into(moodImageView);
            } else if (mood.equals("angry")) {
                Picasso.with(getApplicationContext()).load(R.drawable.emo_angry).into(moodImageView);
            }
        }
        
    }

    public void delete_memory(View view) {
        onBackPressed();
        memoryViewModel.getMemoryAndDelete(id);
    }

    public void toastLocation(View view) {
        // Get place
        if (!memory.getLongitude().equals("null") && memory.getPlace().equals("null")) {
            String locationURL = Server.buildLocationURL(memory.getLatitude(), memory.getLongitude());
            Log.i("locationURL", locationURL);
            new getLocationTask(memoryViewModel, String.valueOf(memory.getId())).execute(locationURL);
        }

        if (!memory.getPlace().equals("null")) {
            Toast.makeText(getApplicationContext(), memory.getPlace(), Toast.LENGTH_SHORT).show();
        } else {
            memory = memoryViewModel.getMemoryFromId(id);
        }
    }
}
