package com.example.contactsmanagerapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsmanagerapp.databinding.ContactListViewBinding;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ContactViewHolder>
{
    private ArrayList<Contact> contacts;

    public MyAdapter(ArrayList<Contact> contacts)
    {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        ContactListViewBinding contactListViewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.contact_list_view,
                parent,
                false);
        return new ContactViewHolder(contactListViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position)
    {
        Contact contact = contacts.get(position);
        holder.contactListViewBinding.setContact(contact);
    }

    @Override
    public int getItemCount()
    {
        if (contacts != null)
            return contacts.size();
        return 0;
    }

    public void setContacts(ArrayList<Contact> contacts)
    {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder
    {
        private ContactListViewBinding contactListViewBinding;
        public ContactViewHolder(@NonNull ContactListViewBinding contactListViewBinding)
        {
            super(contactListViewBinding.getRoot());
            this.contactListViewBinding = contactListViewBinding;
        }
    }
}
