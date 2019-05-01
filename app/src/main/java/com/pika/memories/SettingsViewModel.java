package com.pika.memories;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;


public class SettingsViewModel extends AndroidViewModel {
    private SettingsRepository settingsRepository;

    public SettingsViewModel(Application application) {
        super(application);
        settingsRepository = new SettingsRepository(application);
    }

    public void insert(Settings settings) { settingsRepository.insert(settings); }

    public void delete(Settings settings) { settingsRepository.delete(settings); }

    public void changeTheme(String theme, String userId) { settingsRepository.changeTheme(theme, userId); }

    public Settings getCurrentSettings(String userId) { return settingsRepository.getCurrentSettings(userId); }
}
