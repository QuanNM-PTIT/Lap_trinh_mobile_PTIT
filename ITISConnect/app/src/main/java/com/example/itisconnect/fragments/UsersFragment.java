package com.example.itisconnect.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.itisconnect.R;
import com.example.itisconnect.adapters.UserAdapter;
import com.example.itisconnect.models.User;
import com.example.itisconnect.views.AddNewUserActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment
{
    private FirebaseAuth fAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference;
    private List<User> users;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;
    private UserAdapter adapter;

    public UsersFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        users = new ArrayList<>();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        loadUsers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.users_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new UserAdapter(getContext(), users);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_user_view);

        swipeRefreshLayout.setOnRefreshListener(() ->
        {
            loadUsers();
            swipeRefreshLayout.setRefreshing(false);
        });

        fab = view.findViewById(R.id.fab_add_user);
        setupFabVisibility();

        fab.setOnClickListener(v ->
        {
            Intent intent = new Intent(getContext(), AddNewUserActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadUsers()
    {
        collectionReference.orderBy("priority", Query.Direction.ASCENDING).get()
                .addOnSuccessListener(queryDocumentSnapshots ->
                {
                    if (!queryDocumentSnapshots.isEmpty())
                    {
                        users.clear();
                        users.addAll(queryDocumentSnapshots.toObjects(User.class));
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e ->
                {
                    Toast.makeText(getContext(), "Có lỗi xảy ra, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                });
    }

    private void setupFabVisibility()
    {
        if (currentUser != null)
        {
            db.collection("Users").whereEqualTo("email", currentUser.getEmail()).get()
                    .addOnSuccessListener(queryDocumentSnapshots ->
                    {
                        if (!queryDocumentSnapshots.isEmpty())
                        {
                            boolean isNotAdmin = Boolean.FALSE.equals(queryDocumentSnapshots.getDocuments().get(0).getBoolean("roleAdmin"));
                            fab.setVisibility(isNotAdmin ? View.GONE : View.VISIBLE);
                        }
                    });
        }
    }
}
