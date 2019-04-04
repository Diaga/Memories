package com.kat.chatty;

import android.media.Image;
import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.sql.Timestamp;
import java.util.Date;

class User {
    private String displayName;
    private String id;
    private String email;
    private String photoURI;
    private String accessKey;
    private Date date = new Date();

    void register(GoogleSignInAccount gsAccount) {
        setDisplayName(gsAccount.getDisplayName());
        setId(gsAccount.getId());
        setEmail(gsAccount.getEmail());
        setPhotoURI(gsAccount.getPhotoUrl());
        String[] args = {"id", "displayName", "email", "photoURI"};
        String[] params = {getId(), getDisplayName(), getEmail(), getPhotoURI()};
        String query = Server.queryBuilder(args, params);
        String url = Server.urlBuilder("register", query);
        new registerTask(this).execute(url);
    }

    void saveMemory(String memory) {
        saveMemory(memory, "", null);
    }

    void saveMemory(String memory, String geoTag) {
        saveMemory(memory, geoTag, null);
    }

    void saveMemory(String memory, String geoTag, Image image) {
        String[] args = {"accessKey", "savedOn", "type", "memory", "geoTag"};
        String[] params = {accessKey, new Timestamp(date.getTime()).toString(), memory, geoTag};
        String query = Server.queryBuilder(args, params);
        String url = Server.urlBuilder("save", query);
        new saveMemoryTask().execute(url);
        if (image != null) {
            // Not yet implemented
        }
    }

    void sendMessage(String message) {
        // To be implemented
    }

    private void setId(String id) {
        this.id = id;
    }

    private void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private void setPhotoURI(Uri photoURI) {
        if (photoURI != null) {
            this.photoURI = photoURI.toString();
        }
    }

    void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    String getDisplayName() {
        return displayName;
    }

    String getId() {
        return id;
    }

    String getEmail() {
        return email;
    }

    String getPhotoURI() {
        return photoURI;
    }

    String getAccessKey() {
        return accessKey;
    }
}
