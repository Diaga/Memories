package com.pika.memories;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.GregorianCalendar;

class Utils {
    final static String THEME_BLACK = "black";
    final static String THEME_WHITE = "white";
    static String currentTheme = THEME_WHITE;

    // Setter function for currentTheme
    static void changeTheme(String theme) {
        currentTheme = theme;
    }

    // Set the theme of the activity according to the configurations
    static void onActivityCreateSetTheme(Activity activity) {
        Log.i("Theme", String.valueOf(currentTheme));
        switch (currentTheme) {
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

    static void writeBytes(File file, byte[] bytes) {
        FileOutputStream fileOutPutStream;
        try {
            fileOutPutStream = new FileOutputStream(file);
            fileOutPutStream.write(bytes);
            fileOutPutStream.close();
        } catch (Exception e) {
            Log.e("FileWriteERROR: ", file.getName());
        }
    }

    static byte[] readBytes(File file) {
        BufferedInputStream bufferedInputStream;
        byte[] bytes = new byte[(int)file.length()];
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            int bytesRead = bufferedInputStream.read(bytes);
        } catch (Exception e) {
            Log.e("FileReadError: ", file.getName());
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

    static String bitmapToBase64(Bitmap bitmap) {
        return Base64.encodeToString(Utils.imageToByteArray(bitmap), Base64.URL_SAFE);
    }

    static String getMoodFromScore (String scoreString) {
        double score = Double.parseDouble(scoreString);
        String mood;
        if (score >= 0.6 && score <= 1.0) {
            mood = "excited";
        } else if (score >= 0.2) {
            mood = "happy";
        } else if (score >= -0.2) {
            mood = "neutral";
        } else if (score >= -0.6) {
            mood = "depressed";
        } else if (score >= -1.0) {
            mood  = "angry";
        } else {
            mood = "invalid";
        }
        return mood;
    }

    static String getDayMonthRelativeToNow(int offset) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, offset);
        return Utils.timestampToDateTime(String.valueOf(cal.getTimeInMillis()), "dd/MM");
    }

    static String[] getSevenDayMonthRelativeToNow() {
        return new String[] {getDayMonthRelativeToNow(0), getDayMonthRelativeToNow(-1),
        getDayMonthRelativeToNow(-2), getDayMonthRelativeToNow(-3), getDayMonthRelativeToNow(-4),
        getDayMonthRelativeToNow(-5), getDayMonthRelativeToNow(-6), getDayMonthRelativeToNow(-7)};
    }

    static String customDateToTimestamp(String customDate) {
        String[] customDateArray = customDate.split(" ");
        String month = customDateArray[0];
        String day = customDateArray[1].split(",")[0];
        String year = customDateArray[2];

        Calendar calendar = new GregorianCalendar();
        String hour = timestampToDateTime(String.valueOf(System.currentTimeMillis()), "HH");
        String min = timestampToDateTime(String.valueOf(System.currentTimeMillis()), "mm");
        String sec = timestampToDateTime(String.valueOf(System.currentTimeMillis()), "ss");

        calendar.set(Integer.parseInt(year), monthToInt(month), Integer.parseInt(day), Integer.parseInt(hour),
                Integer.parseInt(min), Integer.parseInt(sec));
        return String.valueOf(calendar.getTimeInMillis());
    }

    static int monthToInt(String month) {
        if (month.equals("January")) {
            return 0;
        } else if (month.equals("February")) {
            return 1;
        } else if (month.equals("March")) {
            return 2;
        } else if (month.equals("April")) {
            return 3;
        } else if (month.equals("May")) {
            return 4;
        } else if (month.equals("June")) {
            return 5;
        } else if (month.equals("July")) {
            return 6;
        } else if (month.equals("August")) {
            return 7;
        } else if (month.equals("September")) {
            return 8;
        } else if (month.equals("October")) {
            return 9;
        } else if (month.equals("November")) {
            return 10;
        } else if (month.equals("December")) {
            return 11;
        }
        return 4;
    }
}

class imageToLocalTask extends AsyncTask<Memory, Void, Void> {
    private WeakReference<File> fileWeakReference;
    private WeakReference<Bitmap> bitmapWeakReference;

    imageToLocalTask(File file, Bitmap bitmap) {
        fileWeakReference = new WeakReference<>(file);
        bitmapWeakReference = new WeakReference<>(bitmap);
    }

    @Override
    protected Void doInBackground(Memory... memories) {
        Utils.writeBytes(fileWeakReference.get(), Utils.imageToByteArray(bitmapWeakReference.get()));
        return null;
    }
}