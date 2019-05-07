package com.pika.memories;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import java.util.Calendar;

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
    //private int counterImage=0;
    private TextView date;

    private DatePickerDialog.OnDateSetListener dateSetListener;



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

        date = (TextView) findViewById(R.id.dateEditorActivity);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSelector();
            }
        });

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
        dateSetListener = new DatePickerDialog.OnDateSetListener(){

            String sMonth;
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;

                switch (month){
                    case 1:
                        sMonth="Jan";
                        break;
                    case 2:
                        sMonth="Feb";
                        break;
                    case 3:
                        sMonth="Mar";
                        break;
                    case 4:
                        sMonth="Apr";
                        break;
                    case 5:
                        sMonth="May";
                        break;
                    case 6:
                        sMonth="Jun";
                        break;
                    case 7:
                        sMonth="Jul";
                        break;
                    case 8:
                        sMonth="Aug";
                        break;
                    case 9:
                        sMonth="Sep";
                        break;
                    case 10:
                        sMonth="Oct";
                        break;
                    case 11:
                        sMonth="Nov";
                        break;
                    case 12:
                        sMonth="Dec";
                        break;
                }
                String dte = dayOfMonth+" "+sMonth+" "+year;
                date.setText(dte);
            }
        };
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

    public void dateSelector(){
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                dateSetListener,
                year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

}
