package com.pika.memories;

public class CalendarMemoryStorage {
    String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text.length() > 20) {
            this.text = text.substring(0, 20) + "...";
        } else {
            this.text = text;
        }
    }

    public CalendarMemoryStorage(String text) {
        setText(text);
    }
}
