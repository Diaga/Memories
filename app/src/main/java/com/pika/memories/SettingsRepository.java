package com.pika.memories;

import android.app.Application;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class SettingsRepository {
    private SettingsDao settingsDao;

    SettingsRepository(Application application) {
        Database db = Database.getDatabase(application);
        settingsDao = db.settingsDao();
    }

    public void insert(Settings settings) { new insertSettingsTask(settingsDao).execute(settings); }

    public void delete(Settings settings) { new deleteSettingsTask(settingsDao).execute(settings); }

    public void changeTheme(String theme, String userId) { new changeThemeTask(settingsDao).execute(theme, userId); }

    // Does not run asynchronously!
    public Settings getCurrentSettings(String userId) { return settingsDao.getCurrentSettings(userId); }
}

class insertSettingsTask extends AsyncTask<Settings, Void, Void> {
    private WeakReference<SettingsDao> settingsDaoWeakReference;

    insertSettingsTask(SettingsDao settingsDao) {
        this.settingsDaoWeakReference = new WeakReference<>(settingsDao);
    }

    @Override
    protected Void doInBackground(Settings... settings) {
        settingsDaoWeakReference.get().insert(settings[0]);
        return null;
    }
}

class deleteSettingsTask extends AsyncTask<Settings, Void, Void> {
    private WeakReference<SettingsDao> settingsDaoWeakReference;

    deleteSettingsTask(SettingsDao settingsDao) {
        this.settingsDaoWeakReference = new WeakReference<>(settingsDao);
    }

    @Override
    protected Void doInBackground(Settings... settings) {
        settingsDaoWeakReference.get().delete(settings[0]);
        return null;
    }
}

class changeThemeTask extends AsyncTask<String, Void, Void> {
    private WeakReference<SettingsDao> settingsDaoWeakReference;

    changeThemeTask(SettingsDao settingsDao) {
        this.settingsDaoWeakReference = new WeakReference<>(settingsDao);
    }

    @Override
    protected Void doInBackground(String... strings) {
        settingsDaoWeakReference.get().changeTheme(strings[0], strings[1]);
        return null;
    }
}