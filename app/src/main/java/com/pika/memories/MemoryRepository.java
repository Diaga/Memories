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

    public void setMemory(String id, String memory) {new memoryTask(memoryDao).execute(id, memory); }

    public void setMood(String id, String mood) {new moodTask(memoryDao).execute(id, mood); }

    public void setImage(String id, String image) {new imageTask(memoryDao).execute(id, image); }

    public void setLongitude(String id, String longitude) {new longitudeTask(memoryDao).execute(id, longitude); }

    public void setLatitude(String id, String latitude) {new latitudeTask(memoryDao).execute(id, latitude); }

    public void setSynced(String id, String synced) {new syncedMemoryTask(memoryDao).execute(id, synced); }

    public void setImageInLocal(String id, String imageInLocal) {new imageInLocalTask(memoryDao).execute(id, imageInLocal); }

    public void getMemoryAndDelete(String id) {
        new getMemoryAndDeleteTask(memoryDao).execute(id);
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

class memoryTask extends AsyncTask<String, Void, Void> {
    private WeakReference<MemoryDao> memoryDaoWeakReference;

    memoryTask(MemoryDao memoryDao) {
        memoryDaoWeakReference = new WeakReference<>(memoryDao);
    }

    @Override
    protected Void doInBackground(String... strings) {
        memoryDaoWeakReference.get().setMemory(Integer.parseInt(strings[0]), strings[1]);
        return null;
    }
}

class moodTask extends AsyncTask<String, Void, Void> {
    private WeakReference<MemoryDao> memoryDaoWeakReference;

    moodTask(MemoryDao memoryDao) {
        memoryDaoWeakReference = new WeakReference<>(memoryDao);
    }

    @Override
    protected Void doInBackground(String... strings) {
        memoryDaoWeakReference.get().setMood(Integer.parseInt(strings[0]), strings[1]);
        return null;
    }
}

class imageTask extends AsyncTask<String, Void, Void> {
    private WeakReference<MemoryDao> memoryDaoWeakReference;

    imageTask(MemoryDao memoryDao) {
        memoryDaoWeakReference = new WeakReference<>(memoryDao);
    }

    @Override
    protected Void doInBackground(String... strings) {
        memoryDaoWeakReference.get().setImage(Integer.parseInt(strings[0]), strings[1]);
        return null;
    }
}

class longitudeTask extends AsyncTask<String, Void, Void> {
    private WeakReference<MemoryDao> memoryDaoWeakReference;

    longitudeTask(MemoryDao memoryDao) {
        memoryDaoWeakReference = new WeakReference<>(memoryDao);
    }

    @Override
    protected Void doInBackground(String... strings) {
        memoryDaoWeakReference.get().setLongitude(Integer.parseInt(strings[0]), strings[1]);
        return null;
    }
}

class latitudeTask extends AsyncTask<String, Void, Void> {
    private WeakReference<MemoryDao> memoryDaoWeakReference;

    latitudeTask(MemoryDao memoryDao) {
        memoryDaoWeakReference = new WeakReference<>(memoryDao);
    }

    @Override
    protected Void doInBackground(String... strings) {
        memoryDaoWeakReference.get().setLatitude(Integer.parseInt(strings[0]), strings[1]);
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

class syncedMemoryTask extends AsyncTask<String, Void, Void> {
    private WeakReference<MemoryDao> memoryDaoWeakReference;

    syncedMemoryTask(MemoryDao memoryDao) {
        memoryDaoWeakReference = new WeakReference<>(memoryDao);
    }

    @Override
    protected Void doInBackground(String... strings) {
        memoryDaoWeakReference.get().setSynced(Integer.parseInt(strings[0]), strings[1]);
        return null;
    }
}

class imageInLocalTask extends AsyncTask<String, Void, Void> {
    private WeakReference<MemoryDao> memoryDaoWeakReference;

    imageInLocalTask(MemoryDao memoryDao) {
        memoryDaoWeakReference = new WeakReference<>(memoryDao);
    }

    @Override
    protected Void doInBackground(String... strings) {
        memoryDaoWeakReference.get().setImageInLocal(Integer.parseInt(strings[0]), strings[1]);
        return null;
    }
}