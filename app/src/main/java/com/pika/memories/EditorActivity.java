package com.pika.memories;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import androidx.annotation.Nullable;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class EditorActivity extends BaseActivity {

    public static final int PICK_IMAGE = 1;
    private MemoryViewModel memoryViewModel;
    private UserViewModel userViewModel;
    private Uri imageURI;
    private GpsTracker gpsTracker;
    private ImageButton locationButton;
    private ImageButton galleyButton;
    int counter=0;


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

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    public void selectLocation() {
        ActivityCompat.requestPermissions(EditorActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
        Location location = gpsTracker.getLocation();
        locationButton.setOnClickListener(v -> {
                if (location!=null){
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    locationButton.setBackgroundResource(R.drawable.ic_location_on_select_24dp);
                    counter+=1;
                    Toast.makeText(EditorActivity.this, "Latitude: "+latitude+" Longitude: "+longitude, Toast.LENGTH_SHORT).show();
                    if (counter%2==0){
                        locationButton.setBackgroundResource(R.drawable.ic_location_on_white_24dp);
                    }
                    //Log.i("Info", "Longitude: "+latitude+" Longitude: "+longitude);
                }
            });

            //Log.i("Info", "Longitude: "+latitude+" Longitude: "+longitude);
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
        Memory memory = new Memory();
        memory.setMemory(memoryText);
        memory.setSavedOn(String.valueOf(System.currentTimeMillis()));
        if (imageURI != null) {
            memory.setImagePath(imageURI.toString());
        }
        memory.setUserId(userViewModel.getSignedInUser().getId());
        memoryViewModel.insert(memory);
        onBackPressed();
    }

    static void saveMemory(String memory) {
        saveMemory(memory, "");
    }

    static void saveMemory(String memory, String geoTag) {
        saveMemory(memory, geoTag, null);
    }

    static void saveMemory(String memory, String geoTag, Uri imageUri) {
        ArrayList<Serializable> array = new ArrayList<>();

        String[] args = {"accessKey", "savedOn", "memory", "geoTag"};
        String[] params = {"s", String.valueOf(new Date(System.currentTimeMillis())), memory, geoTag};
        String query = Server.queryBuilder(args, params);
        String url = Server.urlBuilder("save", query);
        array.add(url);

        new saveMemoryTask().execute(array);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        imageURI = data.getData();
                        galleyButton.setBackgroundResource(R.drawable.ic_menu_galley_blue);
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
