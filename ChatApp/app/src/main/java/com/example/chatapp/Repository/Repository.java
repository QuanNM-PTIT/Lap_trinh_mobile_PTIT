package com.example.chatapp.Repository;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapp.model.ChatGroup;
import com.example.chatapp.model.ChatMessage;
import com.example.chatapp.views.GroupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Repository
{
    private MutableLiveData<List<ChatGroup>> chatGroupList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    MutableLiveData<List<ChatMessage>> chatMessageList;
    DatabaseReference groupRef;

    public Repository()
    {
        this.chatGroupList = new MutableLiveData<>();
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = firebaseDatabase.getReference();
        this.chatMessageList = new MutableLiveData<>();
    }

    public void firebaseAnynomousAuth(Context context)
    {
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    Intent intent = new Intent(context, GroupActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    Toast.makeText(context, "Login successfully!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getCurrentUserId()
    {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void signOut()
    {
        FirebaseAuth.getInstance().signOut();
    }

    public MutableLiveData<List<ChatGroup>> getChatGroupList()
    {
        List<ChatGroup> chatGroups = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                chatGroups.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    ChatGroup chatGroup = new ChatGroup(dataSnapshot.getKey());
                    chatGroups.add(chatGroup);
                }
                chatGroupList.postValue(chatGroups);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
        return chatGroupList;
    }

    public void addChatGroup(String groupName)
    {
        databaseReference.child(groupName).setValue(groupName);
    }

    public MutableLiveData<List<ChatMessage>> getChatMessageList(String groupName)
    {
        groupRef = firebaseDatabase.getReference().child(groupName);
        List<ChatMessage> chatMessages = new ArrayList<>();
        groupRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                chatMessages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    chatMessages.add(chatMessage);
                }
                chatMessageList.postValue(chatMessages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
        return chatMessageList;
    }

    public void sendMessage(String groupName, String chatMessage)
    {
        DatabaseReference messageRef = firebaseDatabase.getReference(groupName);

        if (!chatMessage.trim().isEmpty())
        {
            ChatMessage message = new ChatMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(), chatMessage, System.currentTimeMillis());
            String randomKey = messageRef.push().getKey();
            messageRef.child(randomKey).setValue(message);
        }
    }
}
