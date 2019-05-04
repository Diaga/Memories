package com.pika.memories;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
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
        private TextView savedOn;
        private TextView savedOnTime;
        private TextView memory;
        private ImageView imageView;
        private RelativeLayout memory_container;


        ViewHolder(View itemView) {
            super(itemView);
            memory_container=itemView.findViewById(R.id.memory_container);
            memory = itemView.findViewById(R.id.memoryText);
            savedOn = itemView.findViewById(R.id.savedOnMemory);
            savedOnTime = itemView.findViewById(R.id.savedOnMemoryTime);
            imageView = itemView.findViewById(R.id.imageMemoryView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_home_recycle_view, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.memory_container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.memories_in));
        holder.memory.setText(memories.get(position).getMemory());
        holder.savedOn.setText(memories.get(position).getSavedOn());
        holder.savedOnTime.setText(memories.get(position).getSavedOnTime());
        Picasso.with(context).load(new File(memories.get(position).getImagePath())).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return memories.size();
    }

    void updateUI() {
        notifyDataSetChanged();
    }
}

class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    ItemOffsetDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
    }
}


class MemoryStorage {

    private String memory;
    private String imagePath;
    private String savedOn;
    private String savedOnTime;

    MemoryStorage(String memory, String imagePath, String savedOn) {
        this.memory = memory;
        this.imagePath = imagePath;
        this.savedOn = savedOn;
        this.savedOnTime = savedOn;
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
}