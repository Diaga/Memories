package com.pika.memories;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Message {

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
    @NonNull
    private String message;

    @ColumnInfo
    @NonNull
    private String mood;

    @ColumnInfo
    @NonNull
    private String reply;

    @ColumnInfo
    @NonNull
    private String synced;

    @NonNull
    public String getSavedOn() {
        return savedOn;
    }

    public void setSavedOn(@NonNull String savedOn) {
        this.savedOn = savedOn;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getMood() {
        return mood;
    }

    public void setMood(@NonNull String mood) {
        this.mood = mood;
    }

    @NonNull
    public String getReply() {
        return reply;
    }

    public void setReply(@NonNull String reply) {
        this.reply = reply;
    }

    @NonNull
    public String getSynced() {
        return synced;
    }

    public void setSynced(@NonNull String synced) {
        this.synced = synced;
    }
}
