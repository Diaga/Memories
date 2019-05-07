package com.pika.memories;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {

    private UserViewModel userViewModel;
    private MemoryViewModel memoryViewModel;
    private MessageViewModel messageViewModel;

    // Chart
    private PieChart pieChart;
    private List<MemoryDataObject> memoryDataObjects = new ArrayList<>();
    private List<PieEntry> memoryEntries = new ArrayList<>();
    private PieDataSet pieDataSet;
    private Description memoryDescription = new Description();

    // Background
    private final int interval = 5000;
    private Handler handler;
    private Runnable runnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Connect with database
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        memoryViewModel = ViewModelProviders.of(this).get(MemoryViewModel.class);
        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);

        // Compute dataObjects and populate Entries
        computeMemoryDataObjects();

        // Chart below
        pieDataSet = new PieDataSet(memoryEntries, "");

        // Color the dataSet
        pieDataSet.setColors(new int[] {R.color.colorExcited, R.color.colorHappy, R.color.colorNeutral,
                R.color.colorDepressed, R.color.colorAngry}, getContext());

        pieDataSet.setDrawValues(false);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());

        pieChart = view.findViewById(R.id.memory_chart);
        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(false);

        pieChart.setDescription(memoryDescription);
        pieChart.animateXY(500, 500);
        pieChart.invalidate();



        // Run background tasks
        handler = new Handler();
        runnable = () -> {
            Log.i("Runnable", "Running Statistics!");
            pieDataSet.clear();
            computeMemoryDataObjects();
            pieDataSet.setValues(memoryEntries);
            pieChart.notifyDataSetChanged();
            pieChart.invalidate();
            handler.postAtTime(this.runnable, System.currentTimeMillis()+interval);
            handler.postDelayed(this.runnable, interval);
        };

        handler.postAtTime(runnable, System.currentTimeMillis()+interval);
        handler.postDelayed(runnable, interval);
    }

    private void computeMemoryDataObjects() {
        // Get all memories
        List<Memory> allMemories = memoryViewModel.getMemoriesFromId(userViewModel.getSignedInUser().getId());

        if (allMemories != null) {
            for (Memory memory : allMemories) {
                memoryDataObjects.add(new MemoryDataObject(memory.getSavedOn(), memory.getMood()));
                Log.i("ami", memory.getMood());
            }
        }

        String[] labels = {"Excited", "Happy", "Neutral", "Depressed", "Angry"};

        memoryEntries.clear();
        int max = 0;
        for (int counter = 0; counter < 5; counter++){
            memoryEntries.add(new PieEntry(MemoryDataObject.getMoodsCounter()[counter],
                    labels[counter]));
            if (MemoryDataObject.getMoodsCounter()[counter] > MemoryDataObject.getMoodsCounter()[max]) {
                max = counter;
            }
        }

        maxToDesc(max);
    }

    private void colorDataSet() {
        for (PieEntry memoryEntry: memoryEntries) {
            if (memoryEntry.getLabel().equals("Excited")) {
                pieDataSet.addColor(getResources().getColor(R.color.colorExcited));
            } else if (memoryEntry.getLabel().equals("Happy")) {
                pieDataSet.addColor(getResources().getColor(R.color.colorHappy));
            } else if (memoryEntry.getLabel().equals("Neutral")) {
                pieDataSet.addColor(getResources().getColor(R.color.colorNeutral));
            } else if (memoryEntry.getLabel().equals("Sad")) {
                pieDataSet.addColor(getResources().getColor(R.color.colorDepressed));
            } else if (memoryEntry.getLabel().equals("Depressed")) {
                pieDataSet.addColor(getResources().getColor(R.color.colorAngry));
            }
        }
    }

    private void maxToDesc(int max) {
        if (max == 0) {
            memoryDescription.setText("emo feels excitement in the air! :D");
        } else if (max == 1) {
            memoryDescription.setText("emo feels happiness in the air! :)");
        } else if (max == 2) {
            memoryDescription.setText("emo feels nothing in the air! :|");
        } else if (max == 3) {
            memoryDescription.setText("emo feels sadness in the air! :(");
        } else if (max == 4) {
            memoryDescription.setText("emo feels depression in the air! :'(");
        }
        memoryDescription.setTextSize(8);
    }

}
