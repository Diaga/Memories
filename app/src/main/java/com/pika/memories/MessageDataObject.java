package com.pika.memories;

public class MessageDataObject {

    private String score;
    private String mood;

    private static int[] moodsCounter = new int[5];

    MessageDataObject(String score) {
        this.score = score;
        mood = Utils.getMoodFromScore(score);

        counterManage(mood);
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public static int[] getMoodsCounter() {
        return moodsCounter;
    }

    public static void setMoodsCounter(int[] moodsCounter) {
        MessageDataObject.moodsCounter = moodsCounter;
    }

    public static void moodsCounterClear() {
        for (int counter = 0; counter < moodsCounter.length; counter++) {
            moodsCounter[counter] = 0;
        }
    }
}
