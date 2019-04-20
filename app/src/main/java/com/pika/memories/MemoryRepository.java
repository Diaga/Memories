package com.pika.memories;

import android.app.Application;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.lifecycle.LiveData;

public class MemoryRepository {
    private MemoryDao memoryDao;

    MemoryRepository(Application application) {
        Database db = Database.getDatabase(application);
        memoryDao = db.memoryDao();
    }

    public void insert(Memory memory) {
        new insertMemoryTask(memoryDao).execute(memory);
    }

    public LiveData<List<Memory>> getMemories(String userId) {
        return memoryDao.getMemories(userId);
    }

    public void getMemoryAndDelete(String savedOn) {
        new getMemoryAndDeleteTask(memoryDao).execute(savedOn);
    }

    public void deleteMemory(Memory memory) {
        new deleteMemoryTask(memoryDao).execute(memory);
    }

    public void clearTable() {new clearMemoryTableTask(memoryDao).execute();}
}

class insertMemoryTask extends AsyncTask<Memory, Void, Void> {
    private WeakReference<MemoryDao> memoryDaoWeakReference;

    insertMemoryTask(MemoryDao memoryDao) {
        memoryDaoWeakReference = new WeakReference<>(memoryDao);
    }

    @Override
    protected Void doInBackground(Memory... memories) {
        memoryDaoWeakReference.get().insert(memories[0]);
        return null;
    }
}

class deleteMemoryTask extends AsyncTask<Memory, Void, Void> {
    private WeakReference<MemoryDao> memoryDaoWeakReference;

    deleteMemoryTask(MemoryDao memoryDao) {
        memoryDaoWeakReference = new WeakReference<>(memoryDao);
    }

    @Override
    protected Void doInBackground(Memory... memories) {
        memoryDaoWeakReference.get().delete(memories[0]);
        return null;
    }
}

class getMemoryAndDeleteTask extends AsyncTask<String, Void, Memory> {
    private WeakReference<MemoryDao> memoryDaoWeakReference;

    getMemoryAndDeleteTask(MemoryDao memoryDao) {
        memoryDaoWeakReference = new WeakReference<>(memoryDao);
    }

    @Override
    protected Memory doInBackground(String... strings) {
        return memoryDaoWeakReference.get().getMemory(strings[0]);
    }

    @Override
    protected void onPostExecute(Memory memory) {
        new deleteMemoryTask(memoryDaoWeakReference.get()).execute(memory);
    }
}

class clearMemoryTableTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<MemoryDao> memoryDaoWeakReference;

    clearMemoryTableTask(MemoryDao memoryDao) {
        this.memoryDaoWeakReference = new WeakReference<>(memoryDao);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        memoryDaoWeakReference.get().clearTable();
        return null;
    }
}
