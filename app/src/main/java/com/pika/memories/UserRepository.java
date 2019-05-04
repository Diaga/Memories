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

    public LiveData<List<User>> getUsers() {
        return userLiveData;
    }

    // Does not run asynchronously!
    public User getSignedInUser() { return signedInUser;}

    // Does not run asynchronously!
    public User getUserFromId(String id) { return userDao.getUserFromId(id); }

    public void signOutAllUsers() {new signOutTask(userDao).execute();}

    public void setAccessKey(String accessKey) {new accessKeyTask(userDao).execute(accessKey);}

    public void setTheme(String theme) {new themeTask(userDao).execute(theme);}

    public void setSync(String sync) {new syncTask(userDao).execute(sync);}

    public void clearTable() { new clearUserTableTask(userDao).execute(); }
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

class themeTask extends AsyncTask<String, Void, Void> {
    private WeakReference<UserDao> userDaoWeakReference;

    themeTask(UserDao userDao) {
        this.userDaoWeakReference = new WeakReference<>(userDao);
    }

    @Override
    protected Void doInBackground(String... strings) {
        userDaoWeakReference.get().setTheme(strings[0]);
        return null;
    }
}

class syncTask extends AsyncTask<String, Void, Void> {
    private WeakReference<UserDao> userDaoWeakReference;

    syncTask(UserDao userDao) {
        this.userDaoWeakReference = new WeakReference<>(userDao);
    }

    @Override
    protected Void doInBackground(String... strings) {
        userDaoWeakReference.get().setSync(strings[0]);
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

class clearUserTableTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<UserDao> userDaoWeakReference;

    clearUserTableTask(UserDao userDao) {
        this.userDaoWeakReference = new WeakReference<>(userDao);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        userDaoWeakReference.get().clearTable();
        return null;
    }
}