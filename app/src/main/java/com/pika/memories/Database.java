package com.pika.memories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@androidx.room.Database(entities = {User.class, Memory.class}, version = 5)
public abstract class Database extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract MemoryDao memoryDao();

    private static volatile Database INSTANCE;

    static Database getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class,
                            "database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }

    private static Database.Callback databaseCallback = new Database.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // Add Async Task To Update!
        }
    };
}
