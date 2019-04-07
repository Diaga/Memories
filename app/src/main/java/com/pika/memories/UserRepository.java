package com.pika.memories;

import android.app.Application;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import androidx.lifecycle.LiveData;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> userLiveData;
    private User signedInUser;

    UserRepository(Application application) {
        Database db = Database.getDatabase(application);
        userDao = db.userDao();
        userLiveData = userDao.getUsers();
        signedInUser = userDao.getSignedInUser();
    }

    public void insert(User user) {
        new insertUserTask(userDao).execute(user);
    }

    LiveData<List<User>> getUsers() {
        return userLiveData;
    }

    User getSignedInUser() { return signedInUser;}

    void signOutAllUsers() {new signOutTask(userDao).execute();}

    void setAccessKey(String accessKey) {new accessKeyTask(userDao).execute(accessKey);}

}

class insertUserTask extends AsyncTask<User, Void, Void> {
    private WeakReference<UserDao> userDaoWeakReference;

    insertUserTask(UserDao userDao) {
        this.userDaoWeakReference = new WeakReference<>(userDao);
    }

    @Override
    protected Void doInBackground(User... users) {
        userDaoWeakReference.get().insert(users[0]);
        return null;
    }
}

class accessKeyTask extends AsyncTask<String, Void, Void> {
    private WeakReference<UserDao> userDaoWeakReference;

    accessKeyTask(UserDao userDao) {this.userDaoWeakReference = new WeakReference<>(userDao);}

    @Override
    protected Void doInBackground(String... strings) {
        userDaoWeakReference.get().setAccessKey(strings[0]);
        return null;
    }
}

class signOutTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<UserDao> userDaoWeakReference;

    signOutTask(UserDao userDao) {this.userDaoWeakReference = new WeakReference<>(userDao);}

    @Override
    protected Void doInBackground(Void... voids) {
        userDaoWeakReference.get().signOutAllUsers();
        return null;
    }
}