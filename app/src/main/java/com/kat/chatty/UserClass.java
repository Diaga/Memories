package com.kat.chatty;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

class UserClass {
    private String displayName;
    private String id;
    private String email;
    private String photoURI;
    private String accessKey;

    void register(GoogleSignInAccount gsAccount) {
        setDisplayName(gsAccount.getDisplayName());
        setId(gsAccount.getId());
        setEmail(gsAccount.getEmail());
        setPhotoURI(gsAccount.getPhotoUrl());
        String[] args = {"id", "displayName", "email", "photoURI"};
        String[] params = {getId(), getDisplayName(), getEmail(), getPhotoURI()};
        String query = ServerClass.queryBuilder(args, params);
        String url = ServerClass.urlBuilder("register", query);
        new registerTask(this).execute(url);
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
