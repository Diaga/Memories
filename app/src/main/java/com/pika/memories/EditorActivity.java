package com.pika.memories;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class EditorActivity extends BaseActivity {

    public static final int PICK_IMAGE = 1;
    private MemoryViewModel memoryViewModel;
    private Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_editor);

        // Connect with time
        setTime(findViewById(R.id.dateEditorActivity));

        // Connect with database
        memoryViewModel = ViewModelProviders.of(this).get(MemoryViewModel.class);

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
}
