package com.example.chatapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapp.Repository.Repository;
import com.example.chatapp.model.ChatGroup;
import com.example.chatapp.model.ChatMessage;

import java.util.List;

public class MyViewModel extends AndroidViewModel
{
    private Repository repository;

    public MyViewModel(@NonNull Application application)
    {
        super(application);
        repository = new Repository();
    }

    public void signUpAnonymously()
    {
        repository.firebaseAnynomousAuth(this.getApplication());
    }

    public String getCurrentUserId()
    {
        return repository.getCurrentUserId();
    }

    public void signOut()
    {
        repository.signOut();
    }

    public MutableLiveData<List<ChatGroup>> getChatGroupList()
    {
        return repository.getChatGroupList();
    }

    public void addChatGroup(String chatGroup)
    {
        repository.addChatGroup(chatGroup);
    }

    public MutableLiveData<List<ChatMessage>> getChatMessageList(String groupName)
    {
        return repository.getChatMessageList(groupName);
    }

    public void sendMessage(String groupName, String message)
    {
        repository.sendMessage(groupName, message);
    }
}
