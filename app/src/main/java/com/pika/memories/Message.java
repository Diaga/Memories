package com.pika.memories;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Message {

    @ColumnInfo
    @PrimaryKey
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
    private boolean isUser;

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

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }
}
