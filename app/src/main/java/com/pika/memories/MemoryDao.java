package com.pika.memories;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface MemoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Memory memory);

    @Delete
    void delete(Memory memory);

    @Query("SELECT * FROM Memory WHERE id = :id LIMIT 1")
    Memory getMemory(String id);

    @Query("UPDATE Memory SET memory = :memory WHERE id = :id")
    void setMemory(int id, String memory);

    @Query("UPDATE Memory SET mood = :mood WHERE id = :id")
    void setMood(int id, String mood);

    @Query("UPDATE Memory SET imagePath = :imagePath WHERE id = :id")
    void setImage(int id, String imagePath);

    @Query("UPDATE Memory SET longitude = :longitude WHERE id = :id")
    void setLongitude(int id, String longitude);

    @Query("UPDATE Memory SET latitude = :latitude WHERE id = :id")
    void setLatitude(int id, String latitude);

    @Query("UPDATE Memory SET synced = :synced WHERE id = :id")
    void setSynced(int id, String synced);

    @Query("UPDATE Memory SET imageInLocal = :imageInLocal WHERE id = :id")
    void setImageInLocal(int id, String imageInLocal);

    @Query("SELECT * FROM Memory WHERE userId = :userId")
    List<Memory> getMemoriesFromId(String userId);

    @Query("SELECT * FROM Memory WHERE id = :id LIMIT 1")
    Memory getMemoryFromId(int id);

    @Query("SELECT * FROM Memory WHERE userId = :userId ORDER BY savedOn DESC")
    LiveData<List<Memory>> getMemories(String userId);

    @Query("DELETE FROM Memory")
    void clearTable();
}
