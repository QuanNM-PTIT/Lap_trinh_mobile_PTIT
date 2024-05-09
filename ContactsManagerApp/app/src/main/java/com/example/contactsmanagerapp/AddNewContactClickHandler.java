package com.example.contactsmanagerapp;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

public class AddNewContactClickHandler
{
    Contact contact;
    Context context;
    MyViewModel viewModel;

    public AddNewContactClickHandler(Contact contact, Context context, MyViewModel viewModel)
    {
        this.contact = contact;
        this.context = context;
        this.viewModel = viewModel;
    }

    public void onAddNewContactClicked(View view)
    {
        System.out.println(contact.getName());
        System.out.println(contact.getEmail());
        if (contact.getName() == null || contact.getEmail() == null)
            Toast.makeText(context, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
        else
        {
            Toast.makeText(context, "Contact added successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, MainActivity.class);
//            intent.putExtra("Name", contact.getName());
//            intent.putExtra("Email", contact.getEmail());
            viewModel.addContact(new Contact(contact.getName(), contact.getEmail()));
            context.startActivity(intent);
        }
    }
}
