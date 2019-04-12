package com.pika.memories;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EditorActivity extends BaseActivity {

    public static final int PICK_IMAGE = 1;
    private MemoryViewModel memoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_editor);

        // Connect with database
        memoryViewModel = ViewModelProviders.of(this).get(MemoryViewModel.class);

        memoryViewModel.getMemories().observe(this, new Observer<List<Memory>>() {
            @Override
            public void onChanged(List<Memory> memories) {
                if (memories.size() > 0)
                Toast.makeText(getApplicationContext(), memories.get(memories.size()-1).getMemory(),
                        Toast.LENGTH_SHORT).show();
            }
        });
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

    static void saveMemory(String memory) {
        saveMemory(memory, "");
    }

    static void saveMemory(String memory, String geoTag) {
        saveMemory(memory, geoTag, null);
    }

    static void saveMemory(String memory, String geoTag, Uri imageUri) {
        ArrayList<Serializable> array = new ArrayList<>();

        String[] args = {"accessKey", "savedOn", "memory", "geoTag"};
        String[] params = {"s", String.valueOf(System.currentTimeMillis()), memory, geoTag};
        String query = Server.queryBuilder(args, params);
        String url = Server.urlBuilder("save", query);
        array.add(url);
        if (imageUri != null) {
            byte[] bytes;
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                InputStream inputStream = new FileInputStream(imageUri.toString());
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (Exception e) {
                Log.d("IMG_TO_BYTE_ERROR: ", e.toString());
            }
            bytes = outputStream.toByteArray();
            array.add(bytes);
        }
        new saveMemoryTask().execute(array);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        Uri image = data.getData();
                        try {
                            saveMemory("s", "a", image);
                        } catch (Exception e) {
                            Log.d("IMAGE_SELECTION_ERROR: ", e.toString());
                        }
                    }
                }
        }
    }

    public void backToMain(View view) {
        onBackPressed();
    }

    public void saveMemory(View view) {
        EditText editorText = findViewById(R.id.editorText);
        String memoryText = editorText.getText().toString();
        Memory memory = new Memory();
        memory.setMemory(memoryText);
        memory.setSavedOn(String.valueOf(System.currentTimeMillis()));
        memoryViewModel.insert(memory);
    }
}
