package com.example.itisconnect.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.itisconnect.R;
import com.example.itisconnect.adapters.TaskAdapter;
import com.example.itisconnect.models.Task;
import com.example.itisconnect.views.TaskDetailViewActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment
{
    private FirebaseAuth fAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Tasks");
    private CollectionReference userCollectionReference = db.collection("Users");
    private CheckBox isCompleted, isMine;
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> tasks;
    private Spinner spinner;
    private FloatingActionButton addTaskButton;

    public TaskFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        tasks = new ArrayList<>();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        db.collection("Tasks").orderBy("dueDate").get().addOnSuccessListener(queryDocumentSnapshots ->
        {
            tasks.clear();
            for (int i = 0; i < queryDocumentSnapshots.size(); i++)
            {
                Task task = queryDocumentSnapshots.getDocuments().get(i).toObject(Task.class);
                tasks.add(task);
            }
            adapter.notifyDataSetChanged();
        });

        isCompleted.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (isChecked)
            {
                db.collection("Tasks").whereNotEqualTo("status", "Hoàn thành").get().addOnSuccessListener(queryDocumentSnapshots ->
                {
                    tasks.clear();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++)
                    {
                        Task task = queryDocumentSnapshots.getDocuments().get(i).toObject(Task.class);
                        tasks.add(task);
                    }
                    adapter.notifyDataSetChanged();
                });
            }
            else
            {
                db.collection("Tasks").orderBy("dueDate").get().addOnSuccessListener(queryDocumentSnapshots ->
                {
                    tasks.clear();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++)
                    {
                        Task task = queryDocumentSnapshots.getDocuments().get(i).toObject(Task.class);
                        tasks.add(task);
                    }
                    adapter.notifyDataSetChanged();
                });
            }
        });

        isMine.setOnCheckedChangeListener((buttonView, isChecked) ->
        {
            if (isChecked)
            {
                db.collection("Tasks").whereEqualTo("assignedToEmail", currentUser.getEmail()).get().addOnSuccessListener(queryDocumentSnapshots ->
                {
                    tasks.clear();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++)
                    {
                        Task task = queryDocumentSnapshots.getDocuments().get(i).toObject(Task.class);
                        tasks.add(task);
                    }
                    adapter.notifyDataSetChanged();
                });
            }
            else
            {
                db.collection("Tasks").orderBy("dueDate").get().addOnSuccessListener(queryDocumentSnapshots ->
                {
                    tasks.clear();
                    for (int i = 0; i < queryDocumentSnapshots.size(); i++)
                    {
                        Task task = queryDocumentSnapshots.getDocuments().get(i).toObject(Task.class);
                        tasks.add(task);
                    }
                    adapter.notifyDataSetChanged();
                });
            }
        });
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        isCompleted = view.findViewById(R.id.checkbox_completed_tasks);
        isMine = view.findViewById(R.id.checkbox_my_tasks);

        currentUser = fAuth.getCurrentUser();
        spinner = view.findViewById(R.id.email_spinner_task);

        recyclerView = view.findViewById(R.id.recycler_view_tasks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        addTaskButton = view.findViewById(R.id.fab_add_task);

        adapter = new TaskAdapter(getContext(), tasks);
        recyclerView.setAdapter(adapter);

        List<String> emails = new ArrayList<>();
        emails.add("Tất cả");

        addTaskButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(getContext(), TaskDetailViewActivity.class);
            intent.putExtra("taskId", "");
            startActivity(intent);
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
                                addTaskButton.setVisibility(View.GONE);
                            }
                        }
                    });
        }

        userCollectionReference.get().addOnSuccessListener(queryDocumentSnapshots ->
        {
            for (int i = 0; i < queryDocumentSnapshots.size(); i++)
            {
                String email = queryDocumentSnapshots.getDocuments().get(i).getString("email");
                emails.add(email);
            }

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, emails);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    if (position == 0)
                    {
                        db.collection("Tasks").orderBy("dueDate").get().addOnSuccessListener(queryDocumentSnapshots ->
                        {
                            tasks.clear();
                            for (int i = 0; i < queryDocumentSnapshots.size(); i++)
                            {
                                Task task = queryDocumentSnapshots.getDocuments().get(i).toObject(Task.class);
                                tasks.add(task);
                            }
                            adapter.notifyDataSetChanged();
                        });
                    }
                    else
                    {
                        db.collection("Tasks").whereEqualTo("assignedToEmail", emails.get(position)).get().addOnSuccessListener(queryDocumentSnapshots ->
                        {
                            tasks.clear();
                            for (int i = 0; i < queryDocumentSnapshots.size(); i++)
                            {
                                Task task = queryDocumentSnapshots.getDocuments().get(i).toObject(Task.class);
                                tasks.add(task);
                            }
                            adapter.notifyDataSetChanged();
                        });
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {
                }
            });
        });

        return view;
    }
}