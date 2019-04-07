package com.pika.memories;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class EditorActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent getIntent = getIntent();
        Toolbar toolbar = findViewById(R.id.editorOptions);
        toolbar.inflateMenu(R.menu.activity_editor_toolbar);
        toolbar.setOnMenuItemClickListener(toolbarListener);
    }

    private Toolbar.OnMenuItemClickListener toolbarListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.attach_media:
                    selectImage();
                    break;
            }
            return true;
        }
    };

    private void selectImage() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
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
}
