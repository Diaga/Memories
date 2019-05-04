package com.pika.memories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Memory {

    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo
    @NonNull
    private String savedOn;

    @ColumnInfo
    @NonNull
    private String userId;

    @ColumnInfo
    private String memory;

    @ColumnInfo
    @Nullable
    private String mood;

    @ColumnInfo
    @Nullable
    private String imagePath;

    @ColumnInfo
    @Nullable
    private String longitude;

    @ColumnInfo
    @Nullable
    private String latitude;

    @ColumnInfo
    @NonNull
    private String synced;

    @ColumnInfo
    @NonNull
    private String imageInLocal;

    @NonNull
    public String getSavedOn() {
        return savedOn;
    }

    public void setSavedOn(@NonNull String savedOn) {
        this.savedOn = savedOn;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    @Nullable
    public String getMood() {
        return mood;
    }

    public void setMood(@Nullable String mood) {
        this.mood = mood;
    }

    @Nullable
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(@Nullable String imagePath) {
        this.imagePath = imagePath;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @Nullable
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(@Nullable String longitude) {
        this.longitude = longitude;
    }

    @Nullable
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(@Nullable String latitude) {
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getSynced() {
        return synced;
    }

    public void setSynced(@NonNull String synced) {
        this.synced = synced;
    }

    @NonNull
    public String getImageInLocal() {
        return imageInLocal;
    }

    public void setImageInLocal(@NonNull String imageInLocal) {
        this.imageInLocal = imageInLocal;
    }
}
