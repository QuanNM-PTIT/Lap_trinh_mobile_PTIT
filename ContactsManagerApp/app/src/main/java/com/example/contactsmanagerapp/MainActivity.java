package com.example.contactsmanagerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.contactsmanagerapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private ContactDatabase contactDatabase;
    private ArrayList<Contact> contactArrayList = new ArrayList<>();
    private MyAdapter myAdapter;
    private ActivityMainBinding mainBinding;
    private MainActivityClickHandlers handlers;
    private MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        handlers = new MainActivityClickHandlers(this);
        mainBinding.setClickHandler(handlers);

        RecyclerView recyclerView = mainBinding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        contactDatabase = ContactDatabase.getInstance(this);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        myViewModel.getContacts().observe(this, new Observer<List<Contact>>()
        {
            @Override
            public void onChanged(List<Contact> contacts)
            {
                contactArrayList.clear();
                contactArrayList.addAll(contacts);
                myAdapter.notifyDataSetChanged();
            }
        });

        myAdapter = new MyAdapter(contactArrayList);

        recyclerView.setAdapter(myAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
        {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target)
            {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)
            {
                Contact contact = contactArrayList.get(viewHolder.getAdapterPosition());
                myViewModel.deleteContact(contact);
            }
        }).attachToRecyclerView(recyclerView);
    }
}