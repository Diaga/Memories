package com.kat.chatty;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

class ServerClass {
    private static final String server = "http://localhost:5000/";

    static String queryBuilder(String[] query, String[] data) {
        if (query.length == data.length) {
            StringBuilder url = new StringBuilder();
            url.append("?");
            for (int counter=0;counter < query.length;counter++) {
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

    static String urlBuilder(String accessKey, String action, String query) {
        return server+accessKey+"/"+action+query;
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
            e.printStackTrace();
        }
        return "";
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

    private WeakReference<UserClass> user;

    registerTask(UserClass user) {
        this.user = new WeakReference<>(user);
    }
    @Override
    protected String doInBackground(String... urls) {
        return ServerClass.getResponse(urls[0]);
    }

    @Override
    protected void onPostExecute(String accessKey) {
        user.get().setAccessKey(accessKey);
    }
}
