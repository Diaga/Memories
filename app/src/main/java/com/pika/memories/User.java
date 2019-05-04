package com.pika.memories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {


    @PrimaryKey
    @ColumnInfo
    @NonNull
    private String id;

    @ColumnInfo
    private String accessKey;

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

    @ColumnInfo
    @NonNull
    private String theme;

    @ColumnInfo
    @NonNull
    private String sync;

    @NonNull
    public String getAccessKey() {
        return accessKey;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getDisplayName() {
        return displayName;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @Nullable
    public String getPhotoURI() {
        return photoURI;
    }

    @Nullable
    public String getRegisteredOn() {
        return registeredOn;
    }

    @NonNull
    public String getSignedIn() {
        return signedIn;
    }

    public void setAccessKey(@NonNull String accessKey) {
        this.accessKey = accessKey;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setDisplayName(@NonNull String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public void setPhotoURI(@Nullable String photoURI) {
        this.photoURI = photoURI;
    }

    public void setRegisteredOn(@Nullable String registeredOn) {
        this.registeredOn = registeredOn;
    }

    public void setSignedIn(@NonNull String signedIn) {
        this.signedIn = signedIn;
    }

    @NonNull
    public String getTheme() {
        return theme;
    }

    public void setTheme(@NonNull String theme) {
        this.theme = theme;
    }

    @NonNull
    public String getSync() {
        return sync;
    }

    public void setSync(@NonNull String sync) {
        this.sync = sync;
    }
}
