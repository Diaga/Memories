package com.pika.memories;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View fragmentHomeView;
    private RecyclerView memoriesRecyclerView;
    private List<MemoryStorage> memoriesList;
    private MemoryViewModel memoryViewModel;
    private MemoryAdapter memoryAdapter;
    private UserViewModel userViewModel;
    private ImageButton chatButton;
    private DividerItemDecoration decoration;

    // Background
    private final int interval = 5000;
    private Handler handler;
    private Runnable runnable;

    // Variable Holders
    private File fileAsync;
    private Memory memoryAsync;
    private List<Memory> memoriesAsync;

    // Picasso targets below
    private Target imageToLocalTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            new imageToLocalTask(fileAsync, bitmap).execute(memoryAsync);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private Target sendMemoryToServerTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            new saveMemoryTask(memoryViewModel, userViewModel.getSignedInUser().getAccessKey(),
                    bitmap).execute(memoryAsync);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect with database
        memoryViewModel = ViewModelProviders.of(this).get(MemoryViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        // Get SignedIn User
        User signedInUser = userViewModel.getSignedInUser();
        String userId = signedInUser.getId();

        // Initialize MemoryAdapter
        memoriesList = new ArrayList<>();

        if (memoryViewModel.getMemories(userId).getValue() != null) {
            addMemoriesToAdapter(memoryViewModel.getMemories(userId).getValue());
        }
        memoryViewModel.getMemories(userId).observe(this, memories -> {
            removeMemoriesFromAdapter();
            addMemoriesToAdapter(memories);
            sendMemoriesToServer(memories);
        });

        // Run background tasks
        handler = new Handler();
        runnable = () -> {
            Log.i("Runnable", "Running!");
            if (memoriesAsync != null) {
                sendMemoriesToServer(memoriesAsync);
            }
            handler.postAtTime(this.runnable, System.currentTimeMillis()+interval);
            handler.postDelayed(this.runnable, interval);
        };
        handler.postAtTime(runnable, System.currentTimeMillis()+interval);
        handler.postDelayed(runnable, interval);
    }

    private void chatButton() {
        Intent chatIntent = new Intent(getContext(), ChatActivity.class);
        startActivity(chatIntent);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentHomeView = inflater.inflate(R.layout.fragment_home,null);
        memoriesRecyclerView = fragmentHomeView.findViewById(R.id.memory_recycler);
        memoryAdapter = new MemoryAdapter(getContext(), memoriesList);
        memoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        memoriesRecyclerView.setAdapter(memoryAdapter);
        decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.line_divider));
        memoriesRecyclerView.addItemDecoration(decoration);
        chatButton = fragmentHomeView.findViewById(R.id.chatButton);
        chatButton.setOnClickListener(v -> chatButton());
        return fragmentHomeView;
    }

    private void addMemoriesToAdapter(List<Memory> memories) {
        if (memories.size() > 0) {
            Memory memory;

            for (int counter = 0; memories.size()<10 ? counter<memories.size() : counter<10; counter++) {
                memory = memories.get(counter);

                // Image Resolution Below
                memoriesList.add(new MemoryStorage(memory.getMemory(), memory.getImagePath(), memory.getSavedOn()));

                memoryAdapter.updateUI();
            }
        }
    }

    private void removeMemoriesFromAdapter() {
        memoriesList.clear();
        memoryAdapter.updateUI();
    }

    private void sendMemoriesToServer(List<Memory> memories) {
        memoriesAsync = memories;
        for (Memory memory: memories) {
            // Increase scope
            memoryAsync = memory;

            // imageToLocal
            if (!memory.getImagePath().equals("null") && memory.getImageInLocal().equals("0")) {
                File file = new File(getContext().getExternalFilesDir(null), memory.getId()+".jpeg");

                // Update database
                try {
                    memoryViewModel.setImage(String.valueOf(memory.getId()), file.getAbsolutePath());
                } catch (Exception e) {
                    Log.i("Name file", e.toString());
                }
                memoryViewModel.setImageInLocal(String.valueOf(memory.getId()), "1");

                // Populate variables
                fileAsync = file;
                memoryAsync = memory;

                // Load into targetBitmap
                Picasso.with(getContext()).load(memory.getImagePath()).into(imageToLocalTarget);
            }

            // memoryToServer
            if (memory.getSynced().equals("0")) {

                // Populate variables
                memoryAsync = memory;

                // Get bitmap to targetBitmap
                if (memory.getImagePath().equals("null")) {
                    new saveMemoryTask(memoryViewModel, userViewModel.getSignedInUser().getId(),
                            null).execute(memoryAsync);
                } else {
                    Picasso.with(getContext()).load(new File(memory.getImagePath())).into(sendMemoryToServerTarget);
                }
                }
        }
    }
}
