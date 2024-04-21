package com.example.databindingapp;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Person extends BaseObservable
{
    private String name;
    private String email;

    public Person()
    {
    }

    public Person(String name, String email)
    {
        this.name = name;
        this.email = email;
    }

    @Bindable
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
