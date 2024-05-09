package com.example.contactsmanagerapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class}, version = 1)
public abstract class ContactDatabase extends RoomDatabase
{
    public abstract ContactDAO getContactDAO();

    private static ContactDatabase instance;

    public static synchronized ContactDatabase getInstance(Context context)
    {
        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), ContactDatabase.class,
                    "contact_database").fallbackToDestructiveMigration().build();
        return instance;
    }
}
