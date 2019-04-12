package com.pika.memories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Memory {

    @ColumnInfo
    @PrimaryKey
    @NonNull
    private String savedOn;

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
    private String geoTag;

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

    @Nullable
    public String getGeoTag() {
        return geoTag;
    }

    public void setGeoTag(@Nullable String geoTag) {
        this.geoTag = geoTag;
    }
}
