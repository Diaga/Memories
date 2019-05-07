package com.pika.memories;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MemoryViewModel extends AndroidViewModel {
    private MemoryRepository memoryRepository;

    public MemoryViewModel(Application application) {
        super(application);
        memoryRepository = new MemoryRepository(application);
    }

    public LiveData<List<Memory>> getMemories(String userId) {
        return memoryRepository.getMemories(userId);
    }

    public void insert(Memory memory) {
        memoryRepository.insert(memory);
    }

    public void setMemory(String id, String memory) { memoryRepository.setMemory(id, memory); }

    public void setMood(String id, String mood) { memoryRepository.setMood(id, mood); }

    public void setImage(String id, String image) { memoryRepository.setImage(id, image); }

    public void setLongitude(String id, String longitude) { memoryRepository.setLongitude(id, longitude); }

    public void setLatitude(String id, String latitude) { memoryRepository.setLatitude(id, latitude); }

    public void setSynced(String id, String synced) { memoryRepository.setSynced(id, synced); }

    public void setImageInLocal(String id, String imageInLocal) { memoryRepository.setImageInLocal(id, imageInLocal); }

    public void getMemoryAndDelete(String savedOn) {
        memoryRepository.getMemoryAndDelete(savedOn);
    }

    public List<Memory> getMemoriesFromId(String userId) {return memoryRepository.getMemoriesFromId(userId);}

    public Memory getMemoryFromId(String id) {return memoryRepository.getMemoryFromId(id);}

    public void clearTable() {
        memoryRepository.clearTable();
    }
}
