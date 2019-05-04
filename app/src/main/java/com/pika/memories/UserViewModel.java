package com.pika.memories;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private LiveData<List<User>> userLiveData;
    private User signedInUser;

    public UserViewModel(Application application) {
        super(application);
        userRepository = new UserRepository(application);
        userLiveData = userRepository.getUsers();
        signedInUser = userRepository.getSignedInUser();
    }

    public LiveData<List<User>> getUsers() {
        return userLiveData;
    }

    public User getSignedInUser() {
        return signedInUser;
    }

    public User getUserFromId(String id) { return userRepository.getUserFromId(id); }

    public void insertUser(User user) {
        userRepository.insert(user);
    }

    public void signOutAllUsers() {userRepository.signOutAllUsers();}

    public void setAccessKey(String accessKey) {userRepository.setAccessKey(accessKey);}

    public void setTheme(String theme) {userRepository.setTheme(theme);}

    public void setSync(String sync) {userRepository.setSync(sync);}

    public void signIn(String id, String signedIn) {userRepository.signIn(id, signedIn);}

    public void clearTable() { userRepository.clearTable(); }
}
