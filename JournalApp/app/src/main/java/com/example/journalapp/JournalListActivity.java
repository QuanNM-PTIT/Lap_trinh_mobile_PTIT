package com.example.journalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.journalapp.models.Journal;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class JournalListActivity extends AppCompatActivity
{
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fAuthListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Journals");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference;
    private List<Journal> journalList;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);

        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();

        recyclerView = findViewById(R.id.journal_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab = findViewById(R.id.fab_add_journal);

        fab.setOnClickListener(v ->
        {
            if (currentUser != null && fAuth != null)
            {
                Intent intent = new Intent(JournalListActivity.this, AddJournalActivity.class);
                startActivity(intent);
            }
        });

        journalList = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.menu_add_journal)
        {
            if (currentUser != null && fAuth != null)
            {
                Intent intent = new Intent(JournalListActivity.this, AddJournalActivity.class);
                startActivity(intent);
            }
        }
        else if (item.getItemId() == R.id.menu_logout)
        {
            if (currentUser != null && fAuth != null)
            {
                fAuth.signOut();
                Intent intent = new Intent(JournalListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        collectionReference.get().addOnSuccessListener(queryDocumentSnapshots ->
        {
            journalList.clear();
            journalList.addAll(queryDocumentSnapshots.toObjects(Journal.class));
            adapter = new MyAdapter(JournalListActivity.this, journalList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e ->
        {
            Toast.makeText(JournalListActivity.this, "Opps! Something went wrong!", Toast.LENGTH_SHORT).show();
        });
    }
}