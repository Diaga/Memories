package com.pika.memories;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MemoryDao {

    @Insert
    void insert(Memory memory);

    @Delete
    void delete(Memory memory);

    @Query("SELECT * FROM Memory WHERE savedOn = :savedOn LIMIT 1")
    Memory getMemory(String savedOn);

    @Query("SELECT * FROM Memory ORDER BY savedOn DESC")
    LiveData<List<Memory>> getMemories();

    @Query("DELETE FROM Memory")
    void clearTable();
}
