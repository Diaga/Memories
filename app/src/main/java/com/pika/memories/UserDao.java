package com.pika.memories;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("SELECT * FROM User")
    LiveData<List<User>> getUsers();

    @Query("SELECT * FROM User WHERE id = :id LIMIT 1")
    User getUserFromId(String id);

    @Query("SELECT * FROM User WHERE signedIn = \"1\" LIMIT 1")
    User getSignedInUser();

    @Query("UPDATE User SET signedIn = \"0\" WHERE signedIn = \"1\"")
    void signOutAllUsers();

    @Query("UPDATE User SET accessKey = :accessKey WHERE signedIn = \"1\"")
    void setAccessKey(String accessKey);

    @Query("UPDATE User SET theme = :theme WHERE signedIn = \"1\"")
    void setTheme(String theme);

    @Query("UPDATE User SET sync = :sync WHERE signedIn = \"1\"")
    void setSync(String sync);

    @Query("UPDATE User SET signedIn = :signedIn WHERE id = :id")
    void signIn(String id, String signedIn);

    @Query("DELETE FROM User")
    void clearTable();
}
