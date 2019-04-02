package com.kat.chatty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {

    protected void OnCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
//initializes and runs textview I guess
        TextView title = (TextView)findViewById(R.id.textViewChat);

    }
}
