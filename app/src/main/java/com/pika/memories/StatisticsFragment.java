package com.pika.memories;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Update;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {
    private Dialog statistics_dialog;
    private ImageButton helpButton;
    private UserViewModel userViewModel;
    private MemoryViewModel memoryViewModel;
    private MessageViewModel messageViewModel;

    // Buffer Views
    TextView noMemoryGraph;
    TextView noChatGraph;

    // Info
    private int totalAnalysis;
    private int excitedAnalysis;
    private int happyAnalysis;
    private int neutralAnalysis;
    private int depressedAnalysis;
    private int angryAnalysis;

    private int totalMemoryAnalysis;
    private int excitedMemoryAnalysis;
    private int happyMemoryAnalysis;
    private int neutralMemoryAnalysis;
    private int depressedMemoryAnalysis;
    private int angryMemoryAnalysis;

    private int totalMessageAnalysis;
    private int excitedMessageAnalysis;
    private int happyMessageAnalysis;
    private int neutralMessageAnalysis;
    private int depressedMessageAnalysis;
    private int angryMessageAnalysis;


    // Memories Chart
    private PieChart pieChart;
    private List<MemoryDataObject> memoryDataObjects = new ArrayList<>();
    private List<PieEntry> memoryEntries = new ArrayList<>();
    private PieDataSet pieDataSet;
    private Description memoryDescription = new Description();

    // Messages Chart
    private PieChart messagesPieChart;
    private List<MessageDataObject> messageDataObjects = new ArrayList<>();
    private List<PieEntry> messageEntries = new ArrayList<>();
    private PieDataSet messageDataSet;
    private Description messageDescription = new Description();

    // Background
    private final int interval = 5000;
    private Handler handler;
    private Runnable runnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        statistics_dialog = new Dialog(getContext());
        helpButton = view.findViewById(R.id.helpButton);
        helpButton.setOnClickListener(v -> showpop());
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Buffers
        noMemoryGraph = view.findViewById(R.id.memorygraph);
        noChatGraph = view.findViewById(R.id.chatgraph);

        // Connect with database
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        memoryViewModel = ViewModelProviders.of(this).get(MemoryViewModel.class);
        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);

        // Compute dataObjects and populate Entries
        computeMemoryDataObjects();
        computeMessageDataObjects();

        // Chart below
        pieDataSet = new PieDataSet(memoryEntries, "");
        messageDataSet = new PieDataSet(messageEntries, "");

        // Color the dataSet
        pieDataSet.setColors(new int[] {R.color.colorExcited, R.color.colorHappy, R.color.colorNeutral,
                R.color.colorDepressed, R.color.colorAngry}, getContext());
        messageDataSet.setColors(new int[] {R.color.colorExcited, R.color.colorHappy, R.color.colorNeutral,
        R.color.colorDepressed, R.color.colorAngry}, getContext());

        pieDataSet.setDrawValues(false);
        messageDataSet.setDrawValues(false);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());

        PieData messageData = new PieData(messageDataSet);
        messageData.setValueFormatter(new PercentFormatter());

        pieChart = view.findViewById(R.id.memory_chart);
        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(false);

        pieChart.setDescription(memoryDescription);
        pieChart.animateXY(500, 500);
        pieChart.invalidate();

        messagesPieChart = view.findViewById(R.id.messages_chart);
        messagesPieChart.setData(messageData);
        messagesPieChart.setDrawEntryLabels(false);

        messagesPieChart.setDescription(messageDescription);
        messagesPieChart.animateXY(500, 500);
        messagesPieChart.invalidate();


        // Run background tasks
        handler = new Handler();
        runnable = () -> {
            Log.i("Runnable", "Running Statistics!");
            pieDataSet.clear();
            computeMemoryDataObjects();
            pieDataSet.setValues(memoryEntries);
            pieChart.notifyDataSetChanged();
            pieChart.invalidate();

            messageDataSet.clear();
            computeMessageDataObjects();
            messageDataSet.setValues(messageEntries);
            messagesPieChart.notifyDataSetChanged();
            messagesPieChart.invalidate();

            computeAnalysis();

            handler.postAtTime(this.runnable, System.currentTimeMillis()+interval);
            handler.postDelayed(this.runnable, interval);
        };

        handler.postAtTime(runnable, System.currentTimeMillis()+interval);
        handler.postDelayed(runnable, interval);
    }

    @SuppressLint("DefaultLocale")
    public void showpop(){
        statistics_dialog.setContentView(R.layout.statistics_popup);
        TextView totalNumbers = statistics_dialog.findViewById(R.id.entryNumbers);
        TextView excitedNumbers = statistics_dialog.findViewById(R.id.excitedNumbers);
        TextView happyNumbers = statistics_dialog.findViewById(R.id.happyNumbers);
        TextView neutralNumbers = statistics_dialog.findViewById(R.id.neutralNumbers);
        TextView depressedNumbers = statistics_dialog.findViewById(R.id.depressedNumbers);
        TextView angryNumbers = statistics_dialog.findViewById(R.id.angryNumbers);

        if (totalAnalysis != 0) {
            totalNumbers.setText(String.valueOf(totalAnalysis));
            excitedNumbers.setText(String.format("%.1f %s", ((double) excitedAnalysis / totalAnalysis) * 100, "%"));
            happyNumbers.setText(String.format("%.1f %s", ((double) happyAnalysis / totalAnalysis) * 100, "%"));
            neutralNumbers.setText(String.format("%.1f %s", ((double) neutralAnalysis / totalAnalysis) * 100, "%"));
            depressedNumbers.setText(String.format("%.1f %s", ((double) depressedAnalysis / totalAnalysis) * 100, "%"));
            angryNumbers.setText(String.format("%.1f %s", ((double) angryAnalysis / totalAnalysis) * 100, "%"));
        }

        statistics_dialog.show();
    }

    private void computeMemoryDataObjects() {
        // Get all memories
        List<Memory> allMemories = memoryViewModel.getMemoriesFromId(userViewModel.getSignedInUser().getId());

        if (allMemories.size() == 0 && pieChart != null) {
            noMemoryGraph.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.INVISIBLE);
        } else if (pieChart != null) {
            noMemoryGraph.setVisibility(View.INVISIBLE);
            pieChart.setVisibility(View.VISIBLE);
        }

        memoryDataObjects.clear();
        MemoryDataObject.moodsCounterClear();
        if (allMemories != null) {
            for (Memory memory : allMemories) {
                if (memory.getMood() != null) {
                    Log.i("MemoryAnalysis", memory.getMood());
                    memoryDataObjects.add(new MemoryDataObject(memory.getSavedOn(), memory.getMood()));
                }
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

        // Update Info
        totalMemoryAnalysis = allMemories.size();
        excitedMemoryAnalysis = MemoryDataObject.getMoodsCounter()[0];
        happyMemoryAnalysis = MemoryDataObject.getMoodsCounter()[1];
        neutralMemoryAnalysis = MemoryDataObject.getMoodsCounter()[2];
        depressedMemoryAnalysis = MemoryDataObject.getMoodsCounter()[3];
        angryMemoryAnalysis = MemoryDataObject.getMoodsCounter()[4];


        maxToDesc(max);
    }

    private void computeMessageDataObjects() {
        // Get all messages
        List<Message> allMessages = messageViewModel.getMessagesFromUserId(userViewModel.getSignedInUser().getId());

        if (allMessages.size() == 0 && messagesPieChart != null) {
            noChatGraph.setVisibility(View.VISIBLE);
            messagesPieChart.setVisibility(View.INVISIBLE);
        } else if (messagesPieChart != null) {
            noChatGraph.setVisibility(View.INVISIBLE);
            messagesPieChart.setVisibility(View.VISIBLE);
        }

        messageDataObjects.clear();
        MessageDataObject.moodsCounterClear();
        if (allMessages != null) {
            for (Message message : allMessages) {
                messageDataObjects.add(new MessageDataObject(message.getMood()));
                Log.i("MessageAnalysis", message.getMood());
            }
        }

        String[] labels = {"Excited", "Happy", "Neutral", "Depressed", "Angry"};

        messageEntries.clear();
        int max = 0;
        for (int counter = 0; counter < 5; counter++){
            messageEntries.add(new PieEntry(MessageDataObject.getMoodsCounter()[counter],
                    labels[counter]));
            if (MessageDataObject.getMoodsCounter()[counter] > MessageDataObject.getMoodsCounter()[max]) {
                max = counter;
            }
        }

        // Update Info
        totalMessageAnalysis = allMessages.size();
        excitedMessageAnalysis = MessageDataObject.getMoodsCounter()[0];
        happyMessageAnalysis = MessageDataObject.getMoodsCounter()[1];
        neutralMessageAnalysis = MessageDataObject.getMoodsCounter()[2];
        depressedMessageAnalysis = MessageDataObject.getMoodsCounter()[3];
        angryMessageAnalysis = MessageDataObject.getMoodsCounter()[4];

        maxToDescMessage(max);
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
            memoryDescription.setText(":D");
        } else if (max == 1) {
            memoryDescription.setText(":)");
        } else if (max == 2) {
            memoryDescription.setText(":|");
        } else if (max == 3) {
            memoryDescription.setText(":(");
        } else if (max == 4) {
            memoryDescription.setText(":'(");
        }
        memoryDescription.setTextSize(8);
    }

    private void maxToDescMessage(int max) {
        if (max == 0) {
            messageDescription.setText(":D");
        } else if (max == 1) {
            messageDescription.setText(":)");
        } else if (max == 2) {
            messageDescription.setText(":|");
        } else if (max == 3) {
            messageDescription.setText(":(");
        } else if (max == 4) {
            messageDescription.setText(":'(");
        }
        messageDescription.setTextSize(8);
    }

    private void computeAnalysis() {
        Log.i("Data", neutralAnalysis+" "+neutralMemoryAnalysis+" "+neutralMessageAnalysis);
        totalAnalysis = totalMemoryAnalysis + totalMessageAnalysis;
        excitedAnalysis = excitedMemoryAnalysis + excitedMessageAnalysis;
        happyAnalysis = happyMemoryAnalysis + happyMessageAnalysis;
        neutralAnalysis = neutralMemoryAnalysis + neutralMessageAnalysis;
        depressedAnalysis = depressedMemoryAnalysis + depressedMessageAnalysis;
        angryAnalysis = angryMemoryAnalysis + angryMessageAnalysis;
    }
}
