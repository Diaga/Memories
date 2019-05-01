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

    public void clearTable() {
        memoryRepository.clearTable();
    }

    public void getMemoryAndDelete(String savedOn) {
        memoryRepository.getMemoryAndDelete(savedOn);
    }
}
