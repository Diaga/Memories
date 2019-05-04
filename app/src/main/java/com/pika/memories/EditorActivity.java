package com.pika.memories;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import androidx.annotation.Nullable;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class EditorActivity extends BaseActivity {

    public static final int PICK_IMAGE = 1;
    private MemoryViewModel memoryViewModel;
    private UserViewModel userViewModel;
    private Uri imageURI;
    private double longitude;
    private double latitude;
    private GpsTracker gpsTracker;
    private ImageButton locationButton;
    private ImageButton galleyButton;
    private int counterLocation=0;
    private int counterImage=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_editor);

        // Connect with time
        setTime(findViewById(R.id.dateEditorActivity));

        // Connect with database
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        memoryViewModel = ViewModelProviders.of(this).get(MemoryViewModel.class);

        // GeoTag
        gpsTracker = new GpsTracker(getApplicationContext());
        locationButton = findViewById(R.id.geo_tag_location);
        selectLocation();

        galleyButton = findViewById(R.id.menuGalleryButton);
        /* Debug Toast
        memoryViewModel.getMemories().observe(this, memories -> {
            if (memories.size() > 0)
            Toast.makeText(getApplicationContext(), memories.get(memories.size()-1).getSavedOn(),
                    Toast.LENGTH_SHORT).show();
        });
        */
        Intent getIntent = getIntent();
    }

    public void selectImage(View view) {
        counterImage += 1;

        if (counterImage%2!=0) {
            counterImage = 0;

            // Change background resource
            galleyButton.setBackgroundResource(R.drawable.ic_menu_galley_blue);

            // Handle image selector
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");
            Intent pickIntent = new Intent(Intent.ACTION_PICK);
            pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
            startActivityForResult(chooserIntent, PICK_IMAGE);
        } else {
            // Change background resource
            galleyButton.setBackgroundResource(R.drawable.ic_menu_gallery);
            imageURI = null;

        }
    }

    public void selectLocation() {
        ActivityCompat.requestPermissions(EditorActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
        Location location = gpsTracker.getLocation();
        locationButton.setOnClickListener(v -> {
                if (location!=null){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    locationButton.setBackgroundResource(R.drawable.ic_location_on_select_24dp);
                    counterLocation+=1;
                    if (counterLocation%2==0){
                        counterLocation = 0;

                        locationButton.setBackgroundResource(R.drawable.ic_location_on_white_24dp);
                        longitude = 0;
                        latitude = 0;
                    }
                    Log.i("Info", "Longitude: "+latitude+" Longitude: "+longitude);
                }
            });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void saveMemory(View view) {
        EditText editorText = findViewById(R.id.editorText);
        String memoryText = editorText.getText().toString();
        if (memoryText.equals("")) {
            return;
        }

        // Create memory
        Memory memory = new Memory();
        memory.setUserId(userViewModel.getSignedInUser().getId());
        memory.setMemory(memoryText);
        memory.setSavedOn(String.valueOf(System.currentTimeMillis()));
        memory.setSynced("0");
        memory.setImagePath("null");
        memory.setLongitude("null");
        memory.setLatitude("null");
        memory.setImageInLocal("1");
        memory.setMood("201");

        if (imageURI != null) {
            memory.setImagePath(imageURI.toString());
            memory.setImageInLocal("0");
        }

        if (latitude != 0 && longitude != 0) {
            memory.setLongitude(String.valueOf(longitude));
            memory.setLatitude(String.valueOf(latitude));
        }

        memoryViewModel.insert(memory);

        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        imageURI = data.getData();
                    } else {
                        imageURI = null;
                    }
                }
        }
    }

    public void backToMain(View view) {
        onBackPressed();
    }

    private void setTime(TextView textView) {
        textView.setText(Utils.timestampToDateTime(String.valueOf(System.currentTimeMillis()),
                "EEEE dd, yyyy"));
    }

    public void dateSelector(View view){
        Toast.makeText(this, "Date Pressed", Toast.LENGTH_SHORT).show();
    }
}
