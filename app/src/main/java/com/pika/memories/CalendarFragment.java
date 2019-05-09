package com.pika.memories;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class CalendarFragment extends Fragment {

    private boolean hasInflated = false;
    private View viewLoad;
    private View viewCalendar;
    private RecyclerView recyclerView;
    private List<CalendarMemoryStorage> calendarMemoryStorages;
    private CalendarAdapter calendarAdapter;
    private DividerItemDecoration decoration;

    // Buffer
    private TextView noMemories;

    private UserViewModel userViewModel;
    private MemoryViewModel memoryViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendarMemoryStorages = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewLoad = inflater.inflate(R.layout.fragment_loading, container, false);
        if (!hasInflated) {
            AsyncLayoutInflater.OnInflateFinishedListener callback = (view, resid, parent) -> {
                view.setVisibility(View.INVISIBLE);
                container.addView(view);
                onCreateViewAfterViewStubInflated(view);
                afterViewStubInflated(viewLoad);
                viewCalendar = view;

                // Adapter
                recyclerView = view.findViewById(R.id.calendarRecyler);
                calendarAdapter = new CalendarAdapter(getContext(), calendarMemoryStorages);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                decoration.setDrawable(getResources().getDrawable(R.drawable.line_divider));
                recyclerView.addItemDecoration(decoration);
                recyclerView.setAdapter(calendarAdapter);
            };

            AsyncLayoutInflater asyncLayoutInflater = new AsyncLayoutInflater(getContext());
            asyncLayoutInflater.inflate(R.layout.fragment_calendar, container, callback);

            // Connect to database
            userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
            memoryViewModel = ViewModelProviders.of(this).get(MemoryViewModel.class);
        }
        return viewLoad;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (viewCalendar != null) {
            if (!hidden) {
                viewCalendar.setAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left));
                viewCalendar.setVisibility(View.VISIBLE);
            } else {
                viewCalendar.setAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right));
                viewCalendar.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    void onCreateViewAfterViewStubInflated(View view) {
        // Buffer views
        noMemories = view.findViewById(R.id.noMemoriesCalendarText);

        MaterialCalendarView materialCalendarView = view.findViewById(R.id.calendarView);
        materialCalendarView.addDecorator(new CurrentDateDecorator(getContext()));
        materialCalendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (selected) {
                String dateString = getDateString(date.getDay(), date.getMonth(), date.getYear());
                List<Memory> memories = memoryViewModel.getMemoriesFromId(userViewModel.
                        getSignedInUser().getId());
                if (memories.size() > 0) {
                    noMemories.setVisibility(View.INVISIBLE);

                    calendarMemoryStorages.clear();
                    calendarAdapter.notifyDataSetChanged();
                    Log.i("DateCal", dateString);
                    for (Memory memory : memories) {
                        Log.i("DateMem", Utils.timestampToDateTime(memory.getSavedOn(), "dd/MM/yyyy"));
                        if (Utils.timestampToDateTime(memory.getSavedOn(), "dd/MM/yyyy").equals(dateString)) {
                            calendarMemoryStorages.add(new CalendarMemoryStorage(memory.getMemory()));
                            calendarAdapter.notifyDataSetChanged();
                        }
                    }
                }
            } else {
                noMemories.setVisibility(View.VISIBLE);
            }
        });
    }

    void afterViewStubInflated(View view) {
        hasInflated = true;
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    private String getDateString(int day, int month, int year) {
        String dayString = String.valueOf(day);
        String monthString = String.valueOf(month);
        String yearString = String.valueOf(year);
        if (dayString.length() == 1) {
            dayString = "0" + dayString;
        }
        if (monthString.length() == 1) {
            monthString = "0" + monthString;
        }
        return dayString + "/" + monthString + "/" + yearString;
    }
}

class CurrentDateDecorator implements DayViewDecorator {

    private Drawable highlightDrawable;
    private Context context;

    public CurrentDateDecorator(Context context) {
        this.context = context;
        highlightDrawable = this.context.getResources().getDrawable(R.drawable.calendar_today_background);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(CalendarDay.today());
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(highlightDrawable);
        view.addSpan(new ForegroundColorSpan(Color.WHITE));
        view.addSpan(new StyleSpan(Typeface.NORMAL));
        view.addSpan(new RelativeSizeSpan(1.5f));
    }
}
