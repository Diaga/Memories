package com.pika.memories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.lang.ref.WeakReference;
import java.util.List;

public class MessageRepository {

    private MessageDao messageDao;

    MessageRepository(Application application) {
        Database db = Database.getDatabase(application);
        messageDao = db.messageDao();
    }

    public void insert(Message message) { new insertMessageTask(messageDao).execute(message); }

    public void delete(Message message) { new deleteMessageTask(messageDao).execute(message); }

    public LiveData<List<Message>> getMessages(String userId) { return messageDao.getMessages(userId); }

    public void getMessage(String savedOn) { new getMessageTask(messageDao).execute(savedOn); }

    public void clearTable() { new clearMessageTableTask(messageDao).execute(); }
}

class insertMessageTask extends AsyncTask<Message, Void, Void> {

    private WeakReference<MessageDao> messageDaoWeakReference;

    insertMessageTask(MessageDao messageDao) {
        messageDaoWeakReference = new WeakReference<>(messageDao);
    }

    @Override
    protected Void doInBackground(Message... messages) {
        messageDaoWeakReference.get().insert(messages[0]);
        return null;
    }
}

class deleteMessageTask extends AsyncTask<Message, Void, Void> {

    private WeakReference<MessageDao> messageDaoWeakReference;

    deleteMessageTask(MessageDao messageDao) {
        messageDaoWeakReference = new WeakReference<>(messageDao);
    }

    @Override
    protected Void doInBackground(Message... messages) {
        messageDaoWeakReference.get().delete(messages[0]);
        return null;
    }
}

class getMessageTask extends AsyncTask<String, Void, Message> {

    private WeakReference<MessageDao> messageDaoWeakReference;

    getMessageTask(MessageDao messageDao) {
        messageDaoWeakReference = new WeakReference<>(messageDao);
    }

    @Override
    protected Message doInBackground(String... strings) {
        messageDaoWeakReference.get().getMessage(strings[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Message message) {
        // Do something with the message.
    }
}

class clearMessageTableTask extends AsyncTask<Void, Void, Void> {
    private WeakReference<MessageDao> messageDaoWeakReference;

    clearMessageTableTask(MessageDao messageDao) {
        messageDaoWeakReference = new WeakReference<>(messageDao);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        messageDaoWeakReference.get().clearTable();
        return null;
    }
}