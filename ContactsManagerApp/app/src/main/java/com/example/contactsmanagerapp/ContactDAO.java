package com.example.contactsmanagerapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDAO
{
    @Insert
    public void insert(Contact contact);

    @Delete
    public void delete(Contact contact);

    @Query("SELECT * FROM contacts ORDER BY contact_name ASC")
    LiveData<List<Contact>> getContacts();
}
