package com.pika.memories;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.List;

public class MemoryDataObject {
    private String timestamp;
    private String score;

    private String dateTime;
    private String mood;

    private double X;
    private double Y;

    private static int[] moodsCounter = new int[5];

    public MemoryDataObject(String timestamp, String score) {
        this.timestamp = timestamp;
        this.score = score;

        dateTime = Utils.timestampToDateTime(timestamp, "dd/MM");
        mood = Utils.getMoodFromScore(score);

        counterManage(mood);
    }

    public static void assignXValues(List<MemoryDataObject> memoryDataObjects,
                                                         String[] weekDays) {
        for (MemoryDataObject memoryDataObject: memoryDataObjects) {
            for (int counter=0; counter<weekDays.length; counter++) {
                if (memoryDataObject.dateTime.equals(weekDays[counter])) {
                    memoryDataObject.setX(counter);
                }
            }
        }
    }

    public static void assignYValues(List<MemoryDataObject> memoryDataObjects) {
        for (MemoryDataObject memoryDataObject: memoryDataObjects) {
            memoryDataObject.setY(Double.parseDouble(memoryDataObject.score));
        }
    }

    private void counterManage(String mood) {
        if (mood.equals("excited")) {
            moodsCounter[0]++;
        } else if (mood.equals("happy")) {
            moodsCounter[1]++;
        } else if (mood.equals("neutral")) {
            moodsCounter[2]++;
        } else if (mood.equals("depressed")) {
            moodsCounter[3]++;
        } else if (mood.equals("angry")) {
            moodsCounter[4]++;
        }
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public static int[] getMoodsCounter() {
        return moodsCounter;
    }

    public static void setMoodsCounter(int[] moodsCounter) {
        MemoryDataObject.moodsCounter = moodsCounter;
    }
}

class PercentFormatter extends ValueFormatter
{

    public DecimalFormat mFormat;
    private PieChart pieChart;
    private boolean percentSignSeparated;

    public PercentFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0");
        percentSignSeparated = true;
    }

    // Can be used to remove percent signs if the chart isn't in percent mode
    public PercentFormatter(PieChart pieChart) {
        this();
        this.pieChart = pieChart;
    }

    // Can be used to remove percent signs if the chart isn't in percent mode
    public PercentFormatter(PieChart pieChart, boolean percentSignSeparated) {
        this(pieChart);
        this.percentSignSeparated = percentSignSeparated;
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value) + (percentSignSeparated ? " %" : "%");
    }

    @Override
    public String getPieLabel(float value, PieEntry pieEntry) {
        if (pieChart != null && pieChart.isUsePercentValuesEnabled()) {
            // Converted to percent
            return getFormattedValue(value);
        } else {
            // raw value, skip percent sign
            return mFormat.format(value);
        }
    }

}