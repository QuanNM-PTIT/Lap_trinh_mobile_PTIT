package com.example.itisconnect.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.itisconnect.R;
import com.example.itisconnect.adapters.EventAdapter;
import com.example.itisconnect.models.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity
{
    private FirebaseAuth fAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Events");
    private CollectionReference userCollectionReference = db.collection("Users");
    private RecyclerView recyclerView;
    private List<Event> events;
    private EventAdapter eventAdapter;
    private Spinner spinner;
    private FloatingActionButton addEventButton;

    public EventActivity()
    {
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        db.collection("Events").orderBy("status").get().addOnSuccessListener(queryDocumentSnapshots ->
        {
            events.clear();
            events.addAll(queryDocumentSnapshots.toObjects(Event.class));
            eventAdapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        events = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view_events);
        addEventButton = findViewById(R.id.fab_add_event);
        eventAdapter = new EventAdapter(this, events);
        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db.collection("Events").orderBy("status").get().addOnSuccessListener(queryDocumentSnapshots ->
        {
            events.clear();
            events.addAll(queryDocumentSnapshots.toObjects(Event.class));
            eventAdapter.notifyDataSetChanged();
        });

        spinner = findViewById(R.id.status_spinner_event);

        List<String> statuses = new ArrayList<>();
        statuses.add("Tất cả");
        statuses.add("Chưa diễn ra");
        statuses.add("Đang diễn ra");
        statuses.add("Đã diễn ra");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0)
                {
                    db.collection("Events").orderBy("status").get().addOnSuccessListener(queryDocumentSnapshots ->
                    {
                        events.clear();
                        events.addAll(queryDocumentSnapshots.toObjects(Event.class));
                        eventAdapter.notifyDataSetChanged();
                    });
                }
                else
                {
                    db.collection("Events").whereEqualTo("status", statuses.get(position)).get().addOnSuccessListener(queryDocumentSnapshots ->
                    {
                        events.clear();
                        events.addAll(queryDocumentSnapshots.toObjects(Event.class));
                        eventAdapter.notifyDataSetChanged();
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        if (currentUser != null)
        {
            db.collection("Users").whereEqualTo("email", currentUser.getEmail()).get()
                    .addOnSuccessListener(queryDocumentSnapshots ->
                    {
                        if (!queryDocumentSnapshots.isEmpty())
                        {
                            boolean isAdmin = Boolean.TRUE.equals(queryDocumentSnapshots.getDocuments().get(0).getBoolean("roleAdmin"));
                            if (!isAdmin)
                            {
                                addEventButton.setVisibility(View.GONE);
                            }
                        }
                    });
        }

        addEventButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(this, EventDetailViewActivity.class);
            intent.putExtra("eventId", "");
            startActivity(intent);
        });


    }
}