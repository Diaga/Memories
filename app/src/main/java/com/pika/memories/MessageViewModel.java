package com.pika.memories;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MessageViewModel extends AndroidViewModel {
    private MessageRepository messageRepository;

    public MessageViewModel(Application application) {
        super(application);
        messageRepository = new MessageRepository(application);
    }

    public void insert(Message message) { messageRepository.insert(message); }

    public void delete(Message message) { messageRepository.delete(message); }

    public void getMessage(String savedOn) { messageRepository.getMessage(savedOn); }

    public List<Message> getMessagesFromUserId(String userId) { return messageRepository.getMessagesFromUserId(userId);}

    public LiveData<List<Message>> getMessages(String userId) { return messageRepository.getMessages(userId); }

    public void clearTable() { messageRepository.clearTable(); }
}
