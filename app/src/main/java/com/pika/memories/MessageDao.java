package com.pika.memories;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    void insert (Message message);

    @Delete
    void delete (Message message);

    @Query("SELECT * FROM Message WHERE savedOn = :savedOn LIMIT 1")
    Message getMessage(String savedOn);

    @Query("SELECT * FROM Message WHERE userId = :userId ORDER BY savedOn ASC")
    LiveData<List<Message>> getMessages(String userId);

    @Query("DELETE FROM Message")
    void clearTable();
}
