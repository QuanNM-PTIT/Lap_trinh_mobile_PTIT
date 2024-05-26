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
import com.example.itisconnect.adapters.PostAdapter;
import com.example.itisconnect.models.Post;
import com.example.itisconnect.views.AddNewPostActivity;
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

public class PostFragment extends Fragment
{
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fAuthListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Posts");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference;
    private List<Post> posts;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;
    private PostAdapter adapter;

    public PostFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();

        posts = new ArrayList<>();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        collectionReference.orderBy("timeAdded", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots ->
        {
            if (!queryDocumentSnapshots.isEmpty())
            {
                posts.clear();
                posts.addAll(queryDocumentSnapshots.toObjects(Post.class));
                adapter = new PostAdapter(getContext(), posts);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(e ->
        {
            Toast.makeText(getContext(), "Có lỗi xảy ra, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        recyclerView = view.findViewById(R.id.post_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_post);

        swipeRefreshLayout.setOnRefreshListener(() ->
        {
            collectionReference.orderBy("timeAdded", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots ->
            {
                if (!queryDocumentSnapshots.isEmpty())
                {
                    posts.clear();
                    posts.addAll(queryDocumentSnapshots.toObjects(Post.class));
                    adapter = new PostAdapter(getContext(), posts);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }).addOnFailureListener(e ->
            {
                Toast.makeText(getContext(), "Có lỗi xảy ra, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
            });

            swipeRefreshLayout.setRefreshing(false);
        });

        fab = view.findViewById(R.id.fab_add_post);
        fab.setOnClickListener(v ->
        {
            Intent intent = new Intent(getContext(), AddNewPostActivity.class);
            startActivity(intent);
        });
        return view;
    }
}