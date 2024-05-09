package com.example.contactsmanagerapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MyViewModel extends AndroidViewModel
{
    private Repository repository;
    private LiveData<List<Contact>> contacts;

    public MyViewModel(@NonNull Application application)
    {
        super(application);
        this.repository = new Repository(application);
    }

    public LiveData<List<Contact>> getContacts()
    {
        contacts = repository.getContacts();
        return contacts;
    }

    public void addContact(Contact contact)
    {
        repository.addContact(contact);
    }

    public void deleteContact(Contact contact)
    {
        repository.deleteContact(contact);
    }
}
