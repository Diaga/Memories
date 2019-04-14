package com.pika.memories;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


class Utils {
    final static int THEME_BLACK = 1;
    final static int THEME_WHITE = 2;
    private static int sTheme = THEME_WHITE;

    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    static void changeTheme() {
        if (sTheme == THEME_BLACK) {
            sTheme = THEME_WHITE;
        } else {
            sTheme = THEME_BLACK;
        }
    }

    /**
     * Set the theme of the activity, according to the configuration.
     */
    static void onActivityCreateSetTheme(Activity activity) {
        Log.i("Theme", String.valueOf(sTheme));
        switch (sTheme) {
            default:
            case THEME_BLACK:
                activity.setTheme(R.style.AppThemeDark);
                break;
            case THEME_WHITE:
                activity.setTheme(R.style.AppThemeLight);
                break;
        }
    }

    static byte[] imageToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    static FileOutputStream getFileOutStream(Context context, String filename) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(filename, 0);
        } catch (Exception e) {
            Log.e("FileCreateERROR: ", filename);
        }
        return fileOutputStream;
    }

    static File getCacheFile(Context context, String filename) {
        File file = null;
        try {
            file = File.createTempFile(filename, null, context.getCacheDir());
        } catch (IOException e) {
            Log.e("FileCacheCreateERROR: ", filename);
        }
        return file;
    }

    static void writeCacheBytes(byte[] bytes, File file) {
        FileOutputStream fileOutPutStream;
        try {
            fileOutPutStream = new FileOutputStream(file);
            fileOutPutStream.write(bytes);
            fileOutPutStream.close();
        } catch (Exception e) {
            Log.e("FileCacheWriteERROR: ", file.getName());
        }
    }

    static byte[] readCacheToBytes(File file) {
        BufferedInputStream bufferedInputStream;
        byte[] bytes = new byte[(int)file.length()];
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            int bytesRead = bufferedInputStream.read(bytes);
        } catch (Exception e) {
            Log.e("FileCacheReadError: ", file.getName());
        }
        return bytes;
    }

    static Bitmap bytesToImage(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    static String timestampToDateTime(String timestamp, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.valueOf(timestamp));
        return DateFormat.format(format, cal).toString();
    }
}