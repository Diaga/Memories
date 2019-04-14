package com.pika.memories;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View fragmentHomeView;
    private RecyclerView memoriesRecyclerView;
    private List<MemoryStorage> memoriesList;
    private MemoryViewModel memoryViewModel;
    private MemoryAdapter memoryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect with database
        memoryViewModel = ViewModelProviders.of(this).get(MemoryViewModel.class);

        // Initialize MemoryAdapter
        memoriesList = new ArrayList<>();

        if (memoryViewModel.getMemories().getValue() != null) {
            addMemoriesToAdapter(memoryViewModel.getMemories().getValue());
        }
        memoryViewModel.getMemories().observe(this, memories -> {
            removeMemoriesFromAdapter();
            addMemoriesToAdapter(memories);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentHomeView = inflater.inflate(R.layout.fragment_home,null);
        memoriesRecyclerView = fragmentHomeView.findViewById(R.id.memory_recycler);
        memoryAdapter = new MemoryAdapter(getContext(), memoriesList);
        memoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        memoriesRecyclerView.setAdapter(memoryAdapter);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        memoriesRecyclerView.addItemDecoration(itemDecoration);
        return fragmentHomeView;
    }

    private void addMemoriesToAdapter(List<Memory> memories) {
        if (memories.size() > 0) {
            Memory memory;
            Bitmap bitmap = null;

            for (int counter = 0; memories.size()<10 ? counter<memories.size() : counter<10; counter++) {
                memory = memories.get(counter);

                // Image Resolution Below
                if (memory.getImagePath() != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),
                                Uri.parse(memory.getImagePath()));
                    } catch (Exception e) {
                        Log.d("UriToImagePathERROR: ", memory.getImagePath());
                    }
                }
                memoriesList.add(new MemoryStorage(memory.getMemory(), bitmap, memory.getSavedOn()));
                bitmap = null;
            }
            memoryAdapter.updateUI();
        }
    }

    private void removeMemoriesFromAdapter() {
        memoriesList.clear();
        memoryAdapter.updateUI();
    }
}
