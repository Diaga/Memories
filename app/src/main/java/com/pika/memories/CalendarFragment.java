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

import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class CalendarFragment extends Fragment {

    private boolean hasInflated = false;
    private View viewLoad;
    private View viewCalendar;

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
            };
            AsyncLayoutInflater asyncLayoutInflater = new AsyncLayoutInflater(getContext());
            asyncLayoutInflater.inflate(R.layout.fragment_calendar, container, callback);
        }
        return viewLoad;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (viewCalendar != null) {
            if (!hidden) {
                viewCalendar.setVisibility(View.VISIBLE);
            } else {
                viewCalendar.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    void onCreateViewAfterViewStubInflated(View view) {
        MaterialCalendarView materialCalendarView = view.findViewById(R.id.calendarView);
        materialCalendarView.addDecorator(new CurrentDateDecorator(getContext()));
    }

    void afterViewStubInflated(View view) {
        hasInflated = true;
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
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
