package com.pika.memories;

import android.app.Activity;
import android.util.Log;

public class Utils
{
    public final static int THEME_BLACK = 1;
    public final static int THEME_WHITE = 2;
    private static int sTheme = THEME_WHITE;
    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeTheme()
    {
        if (sTheme == THEME_BLACK) {sTheme = THEME_WHITE;} else {sTheme = THEME_BLACK;}
    }
    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {
        Log.i("Theme", String.valueOf(sTheme));
        switch (sTheme)
        {
            default:
            case THEME_BLACK:
                activity.setTheme(R.style.AppThemeDark);
                break;
            case THEME_WHITE:
                activity.setTheme(R.style.AppThemeLight);
                break;
        }
    }
}