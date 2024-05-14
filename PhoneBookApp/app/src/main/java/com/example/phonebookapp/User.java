package com.example.phonebookapp;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class User extends BaseObservable
{
    private String username;
    private String phoneNumber;
    private String group;

    public User()
    {
    }

    public User(String username, String phoneNumber, String group)
    {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.group = group;
    }

    @Bindable
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
        notifyPropertyChanged(BR.phoneNumber);
    }

    @Bindable
    public String getGroup()
    {
        return group;
    }

    public void setGroup(String group)
    {
        this.group = group;
        notifyPropertyChanged(BR.group);
    }
}
