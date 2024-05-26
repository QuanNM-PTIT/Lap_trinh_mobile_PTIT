package com.example.itisconnect.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.itisconnect.models.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends AndroidViewModel
{
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    MutableLiveData<List<ChatMessage>> chatMessages;

    public ChatViewModel(@NonNull Application application)
    {
        super(application);
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference("Chats");
        this.chatMessages = new MutableLiveData<>();
    }

    public MutableLiveData<List<ChatMessage>> getChatMessageList()
    {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                chatMessageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    chatMessageList.add(chatMessage);
                }
                chatMessages.postValue(chatMessageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        return chatMessages;
    }

    public void sendMessage(String message)
    {
        if (message != null)
        {
            ChatMessage chatMessage = new ChatMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(), message, System.currentTimeMillis(), FirebaseAuth.getInstance().getCurrentUser().getEmail());
            String randomKey = databaseReference.push().getKey();
            databaseReference.child(randomKey).setValue(chatMessage);
        }
    }
}
