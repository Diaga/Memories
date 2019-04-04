package com.kat.chatty;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

class Server {
    private static final String server = "https://diaga.pythonanywhere.com/";

    static String queryBuilder(String[] query, String[] data) {
        if (query.length == data.length) {
            StringBuilder url = new StringBuilder();
            url.append("?");
            for (int counter=0;counter < query.length;counter++) {
                try {
                    data[counter] = URLEncoder.encode(data[counter], "utf-8");
                } catch (Exception e) {
                    Log.d("URL_ENCODE_ERROR", e.toString());
                }
                url.append(query[counter]).append("=").append(data[counter]);
                if (counter < query.length-1) url.append("&");
            }
            return url.toString();
        }
        return "Query!=ResponseERROR";
    }

    static String urlBuilder(String action, String query) {
        return server+action+query;
    }
    
    static String getServer() {
        return server;
    }
    
    static String getResponse(String urlString) {
        HttpsURLConnection connection;
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof HttpsURLConnection) {
                connection = (HttpsURLConnection) urlConnection;
                return bufferRead(connection);
            }
        } catch (Exception e) {
            Log.d("getResponse_ERROR", e.toString());
        }
        return "";
    }

    static String saveMemory(String urlString, Image image) {
        HttpsURLConnection connection;
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof HttpsURLConnection) {
                connection = (HttpsURLConnection) urlConnection;
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****");

                DataOutputStream request = new DataOutputStream(connection.getOutputStream());
                request.writeBytes("--*****\r\n");
                request.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" +
                        "image\"\r\n");
                request.writeBytes("\r\n");

                return bufferRead(connection);
            }
        } catch (Exception e) {
            Log.d("sendPost_ERROR", e.toString());
        }
        return "NotImplementedFullyYet";
    }

    void sendMessage(String message) {

    }

    private static String bufferRead(HttpsURLConnection conn) {
        try {
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            conn.disconnect();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}

class registerTask extends AsyncTask<String, Void, String> {

    private WeakReference<User> user;

    registerTask(User user) {
        this.user = new WeakReference<>(user);
    }
    @Override
    protected String doInBackground(String... urls) {
        return Server.getResponse(urls[0]);
    }

    @Override
    protected void onPostExecute(String accessKey) {
        if (!accessKey.equals("FAIL")) {
            user.get().setAccessKey(accessKey);
        } else {
            // Do something here if already registered!
        }
    }
}

class saveMemoryTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
        // Fix this
        return Server.getResponse(urls[0]);
    }

    @Override
    protected void onPostExecute(String status) {
        // Do something with status
        // OK if success, FAIL if failed
    }
}