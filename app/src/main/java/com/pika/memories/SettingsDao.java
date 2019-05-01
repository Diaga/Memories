package com.pika.memories;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert (Settings settings);

    @Delete
    void delete (Settings settings);

    @Query("UPDATE Settings SET theme = :theme WHERE userId = :userId")
    void changeTheme(String theme, String userId);

    @Query("SELECT * FROM Settings WHERE userId = :userId")
    Settings getCurrentSettings(String userId);
}
