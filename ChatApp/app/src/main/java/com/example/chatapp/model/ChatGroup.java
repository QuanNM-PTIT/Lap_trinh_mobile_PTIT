package com.example.chatapp.model;

import androidx.databinding.Bindable;

public class ChatGroup
{
    private String groupName;

    public ChatGroup()
    {
    }

    public ChatGroup(String groupName)
    {
        this.groupName = groupName;
    }

    public String getGroupName()
    {
        return groupName;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }
}
