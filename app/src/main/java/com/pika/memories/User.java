package com.pika.memories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @ColumnInfo
    private String accessKey;

    @PrimaryKey
    @ColumnInfo
    @NonNull
    private String id;

    @ColumnInfo
    @NonNull
    private String displayName;

    @ColumnInfo
    @NonNull
    private String email;

    @ColumnInfo
    @Nullable
    private String photoURI;

    @ColumnInfo
    @Nullable
    private String registeredOn;

    @ColumnInfo
    @NonNull
    private String signedIn;

    @NonNull
    public String getAccessKey() {
        return accessKey;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }

    public String getSignedIn() {
        return signedIn;
    }

    public void setAccessKey(@NonNull String accessKey) {
        this.accessKey = accessKey;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhotoURI(String photoURI) {
        this.photoURI = photoURI;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }

    public void setSignedIn(String signedIn) {
        this.signedIn = signedIn;
    }
}
