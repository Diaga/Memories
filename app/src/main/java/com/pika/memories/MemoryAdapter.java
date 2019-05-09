package com.pika.memories;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {

    private Context context;
    private List<MemoryStorage> memories;


    public MemoryAdapter(Context context, List<MemoryStorage> memories) {
        this.context = context;
        this.memories = memories;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private TextView savedOn;
        private TextView savedOnTime;
        private TextView memory;
        private ImageView imageView;
        private ImageView moodImageView;
        private ConstraintLayout memory_container;


        ViewHolder(View itemView) {
            super(itemView);
            memory_container=itemView.findViewById(R.id.memory_container);
            id = itemView.findViewById(R.id.idMemory);
            memory = itemView.findViewById(R.id.memoryText);
            savedOn = itemView.findViewById(R.id.savedOnMemory);
            savedOnTime = itemView.findViewById(R.id.savedOnMemoryTime);
            imageView = itemView.findViewById(R.id.imageMemoryView);
            moodImageView = itemView.findViewById(R.id.moodImage);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.memory_card, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.memory_container.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), FullMemoryActivity.class);
            intent.putExtra("id", viewHolder.id.getText().toString());
            v.getContext().startActivity(intent);
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.memory_container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.memories_in));
        holder.memory.setText(memories.get(position).getMemory());
        holder.savedOn.setText(memories.get(position).getSavedOn());
        holder.savedOnTime.setText(memories.get(position).getSavedOnTime());
        holder.id.setText(memories.get(position).getId());
        Picasso.with(context).load(new File(memories.get(position).getImagePath())).into(holder.imageView);

        if (memories.get(position).getMood() != null) {
            if (memories.get(position).getMood().equals("excited")) {
                Picasso.with(context).load(R.drawable.emo_excited).into(holder.moodImageView);
            } else if (memories.get(position).getMood().equals("happy")) {
                Picasso.with(context).load(R.drawable.emo_happy).into(holder.moodImageView);
            } else if (memories.get(position).getMood().equals("neutral")) {
                Picasso.with(context).load(R.drawable.emo_neutral).into(holder.moodImageView);
            } else if (memories.get(position).getMood().equals("depressed")) {
                Picasso.with(context).load(R.drawable.emo_depressed).into(holder.moodImageView);
            } else if (memories.get(position).getMood().equals("angry")) {
                Picasso.with(context).load(R.drawable.emo_angry).into(holder.moodImageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return memories.size();
    }

    void updateUI() {
        notifyDataSetChanged();
    }
}


class MemoryStorage {

    private String mood;
    private String memory;
    private String imagePath;
    private String savedOn;
    private String savedOnTime;
    private String id;

    MemoryStorage(String memory, String imagePath, String savedOn, int id, String score) {
        this.memory = memory;
        this.imagePath = imagePath;
        this.savedOn = savedOn;
        this.savedOnTime = savedOn;
        this.id = String.valueOf(id);
        Log.i("Score", score);
        this.mood = Utils.getMoodFromScore(score);
        Log.i("Score", mood+score);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String getMemory() {
        return memory;
    }

    void setMemory(String memory) {
        this.memory = memory;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSavedOn() {
        return Utils.timestampToDateTime(this.savedOn, "MMMM dd, yyyy - E");
    }

    public void setSavedOn(String savedOn) {
        this.savedOn = savedOn;
    }

    public String getSavedOnTime() {
        return Utils.timestampToDateTime(savedOnTime, "hh:mm a");
    }

    public void setSavedOnTime(String savedOnTime) {
        this.savedOnTime = savedOnTime;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }
}