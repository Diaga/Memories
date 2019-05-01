package com.pika.memories;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Settings {

    @ColumnInfo
    @PrimaryKey
    @NonNull
    private String userId;

    @ColumnInfo
    @NonNull
    private String theme;

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getTheme() {
        return theme;
    }

    public void setTheme(@NonNull String theme) {
        this.theme = theme;
    }
}
