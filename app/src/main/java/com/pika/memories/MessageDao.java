package com.pika.memories;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insert (Message message);

    @Delete
    void delete (Message message);

    @Query("SELECT * FROM Message WHERE savedOn = :savedOn LIMIT 1")
    Message getMessage(String savedOn);

    @Query("UPDATE Message SET mood = :mood WHERE id = :id")
    void setMood(int id, String mood);

    @Query("UPDATE Message SET reply = :reply WHERE id =:id")
    void setReply(int id, String reply);

    @Query("SELECT * FROM Message WHERE userId = :userId")
    List<Message> getMessagesFromUserId(String userId);

    @Query("SELECT * FROM Message WHERE userId = :userId ORDER BY savedOn ASC")
    LiveData<List<Message>> getMessages(String userId);

    @Query("DELETE FROM Message")
    void clearTable();
}
