package com.pika.memories;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

class Server {
    private static final String server = "http://10.7.13.17:5000/";

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

class saveMemoryTask extends AsyncTask<Memory, Void, String[]> {
    private WeakReference<MemoryViewModel> memoryViewModelWeakReference;
    private WeakReference<String> stringWeakReference;
    private WeakReference<Bitmap> bitmapWeakReference;

    saveMemoryTask(MemoryViewModel memoryViewModel, String accessKey, Bitmap bitmap) {
        memoryViewModelWeakReference = new WeakReference<>(memoryViewModel);
        stringWeakReference = new WeakReference<>(accessKey);
        if (bitmap != null) {
            bitmapWeakReference = new WeakReference<>(bitmap);
        }
    }

    @Override
    protected String[] doInBackground(Memory... memories) {
        String[] args = {"id", "accessKey", "memory", "image", "longitude", "latitude", "score",
                "timestamp"};
        String[] params = {String.valueOf(memories[0].getId()), stringWeakReference.get(),
                memories[0].getMemory(), memories[0].getImagePath(),
                memories[0].getLongitude(), memories[0].getLatitude(), memories[0].getMood(),
                memories[0].getSavedOn() };
        String query = Server.queryBuilder(args, params);
        String urlString = Server.urlBuilder("save-memory", query);
        Log.i("Server", urlString);
        return new String[] {String.valueOf(memories[0].getId()), Server.getResponse(urlString)};
    }

    @Override
    protected void onPostExecute(String[] s) {
        // Update database
        memoryViewModelWeakReference.get().setMood(s[0], s[1]);

        Log.i("Id", s[0]+s[1]);
        memoryViewModelWeakReference.get().setSynced(s[0], "1");
    }
}

class sendMessageTask extends AsyncTask<String, Void, String> {

    private WeakReference<MessageViewModel> messageViewModelWeakReference;
    private WeakReference<Message> messageWeakReference;

    sendMessageTask(MessageViewModel messageViewModel, Message message) {
        this.messageViewModelWeakReference = new WeakReference<>(messageViewModel);
        this.messageWeakReference = new WeakReference<>(message);
    }

    @Override
    protected String doInBackground(String... strings) {
        return Server.getResponse(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        // Get useful data
        String[] data = s.split("#");

        // Update and send to Recycler View
        messageWeakReference.get().setReply(data[0]);
        messageWeakReference.get().setMood(data[1]);
        messageWeakReference.get().setSynced("1");
        messageViewModelWeakReference.get().insert(messageWeakReference.get());
    }
}