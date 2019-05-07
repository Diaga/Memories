package com.pika.memories;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity {

    EditText queryEditText;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    List<MessageStorage> messagesList;
    private MessageViewModel messageViewModel;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.chat_layout);

        // Connect to database
        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        // Get signedIn user
        User user = userViewModel.getSignedInUser();

        // Initialize MessageAdapter and set the adapter
        messagesList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messagesList, this);

        // Set Recycler View
        queryEditText = findViewById(R.id.input_message);
        recyclerView = findViewById(R.id.conversation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(messageAdapter);

        // Send message To Server
        queryEditText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEND) {

                Message message = new Message();
                message.setMessage(queryEditText.getText().toString());
                message.setSavedOn(String.valueOf(System.currentTimeMillis()));
                message.setUserId(userViewModel.getSignedInUser().getId());
                message.setSynced("0");
                message.setReply("null");
                message.setMood("-0");

                messageViewModel.insert(message);
                // Clear edit text
                queryEditText.getText().clear();
            }
            return false;
        });

        // Listen for LiveData Message
        messageViewModel.getMessages(user.getId()).observe(this, messages -> {
                removeMessagesFromAdapter();
                sendMessagesToServer(messages);
                addMessagesToAdapter(messages);
            if (messages.size() > 0) {
                scrollToLastMessage();
            }
        });
    }

    boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems = recyclerView.getAdapter().getItemCount();
        return (pos >= numItems);
    }

    void scrollToLastMessage() {
        if (!isLastVisible())
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
    }

    private void removeMessagesFromAdapter() {
        messagesList.clear();
        messageAdapter.updateUI();
    }

    private void addMessagesToAdapter(List<Message> messages) {
        if (messages.size() > 0) {
            Message message;

            for (int counter = 0; counter < messages.size(); counter++) {
                message = messages.get(counter);
                messagesList.add(new MessageStorage(message.getMessage(), true));
                if (!message.getReply().equals("null")) {
                    messagesList.add(new MessageStorage(message.getReply(), false));
                }
            }
            messageAdapter.updateUI();
        }
    }

    private void sendMessagesToServer(List<Message> messages) {
        for (Message message: messages) {
            if (message.getSynced().equals("0")) {

                String[] args = {"id", "accessKey", "message", "timestamp"};
                String[] params = {String.valueOf(message.getId()),
                        userViewModel.getSignedInUser().getId(), message.getMessage(),
                message.getSavedOn()};
                String query = Server.queryBuilder(args, params);
                String urlString = Server.urlBuilder("reply", query);
                new sendMessageTask(messageViewModel, message).execute(urlString);
            }
        }
    }
}
