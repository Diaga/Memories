package com.pika.memories;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;

class Server {
    private static final String server = "http://memories.pika.cf/";

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
        HttpURLConnection connection;
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof HttpURLConnection) {
                connection = (HttpURLConnection) urlConnection;
                return bufferRead(connection);
            }
        } catch (Exception e) {
            Log.d("getResponse_ERROR", e.toString());
        }
        return "";
    }

    static String saveMemory(String urlString, byte[] bytes) {
        HttpURLConnection connection;
        try {
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof HttpURLConnection) {
                connection = (HttpURLConnection) urlConnection;
                if (bytes != null) {
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****");

                    DataOutputStream request = new DataOutputStream(connection.getOutputStream());
                    request.writeBytes("--*****\r\n");
                    request.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" +
                            "image\"\r\n");
                    request.writeBytes("\r\n");
                    request.write(bytes, 0, bytes.length);
                }
                return bufferRead(connection);
            }
        } catch (Exception e) {
            Log.d("sendPost_ERROR", e.toString());
        }
        return "NotTestedYet";
    }

    void sendMessage(String message) {

    }

    private static String bufferRead(HttpURLConnection conn) {
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
    private WeakReference<UserViewModel> userViewModelWeakReference;
    registerTask(UserViewModel userViewModel) {
        this.userViewModelWeakReference= new WeakReference<>(userViewModel);
    }
    @Override
    protected String doInBackground(String... urls) {
        return Server.getResponse(urls[0]);
    }

    @Override
    protected void onPostExecute(String accessKey) {
        if (!accessKey.equals("FAIL")) {
            userViewModelWeakReference.get().setAccessKey(accessKey);
        } else {
            // Do something here if already registered!
        }
    }
}

class saveMemoryTask extends AsyncTask<ArrayList, Void, String> {

    @Override
    protected String doInBackground(ArrayList... array) {
        if (array.length == 2) {
            return Server.saveMemory(array[0].toString(), (byte[]) Objects.requireNonNull(array[1].toArray())[0]);
        } else {
            return Server.saveMemory(array[0].toString(), null);
        }
    }

    @Override
    protected void onPostExecute(String status) {
        // Do something with status
        // OK if success, FAIL if failed
    }
}