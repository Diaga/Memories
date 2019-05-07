package com.pika.memories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    Context context;
    List<CalendarMemoryStorage> calendarMemory;

    public CalendarAdapter(Context context, List<CalendarMemoryStorage> calendarMemory) {
        this.context = context;
        this.calendarMemory = calendarMemory;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.calendar_card,parent,false);

        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        holder.textView.setText(calendarMemory.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return  calendarMemory.size();
    }

    static class CalendarViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;
        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.memoryText);
        }
    }
}
