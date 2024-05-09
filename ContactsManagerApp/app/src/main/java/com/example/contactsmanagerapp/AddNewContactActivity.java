package com.example.contactsmanagerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.contactsmanagerapp.databinding.ActivityAddNewContactBinding;

public class AddNewContactActivity extends AppCompatActivity
{
    private ActivityAddNewContactBinding addNewContactBinding;
    private AddNewContactClickHandler clickHandler;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);

        contact = new Contact();
        addNewContactBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_contact);

        MyViewModel myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        clickHandler = new AddNewContactClickHandler(contact, this, myViewModel);

        addNewContactBinding.setContact(contact);
        addNewContactBinding.setClickHandler(clickHandler);
    }
}