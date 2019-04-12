package com.pika.memories;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MemoryViewModel extends AndroidViewModel {
    private MemoryRepository memoryRepository;
    private LiveData<List<Memory>> memoryLiveData;

    public MemoryViewModel(Application application) {
        super(application);
        memoryRepository = new MemoryRepository(application);
        memoryLiveData = memoryRepository.getMemories();
    }

    public LiveData<List<Memory>> getMemories() {
        return memoryLiveData;
    }

    public void insert(Memory memory) {
        memoryRepository.insert(memory);
    }

    public void getMemoryAndDelete(String savedOn) {
        memoryRepository.getMemoryAndDelete(savedOn);
    }
}
