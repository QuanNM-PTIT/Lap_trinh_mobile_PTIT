package com.example.journalapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.journalapp.models.Journal;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddJournalActivity extends AppCompatActivity
{
    private Button saveJournalButton;
    private ImageView addPhotoButton, imageView;
    private ProgressBar progressBar;
    private EditText titleEditText, thoughtsEditText;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference collectionReference = db.collection("Journals");
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fAuthListener;
    private FirebaseUser currentUser;

    private String currentUserId;
    private String currentUserName;

    private ActivityResultLauncher<String> mTakePhoto;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        progressBar = findViewById(R.id.post_progressBar);
        titleEditText = findViewById(R.id.post_title_ET);
        thoughtsEditText = findViewById(R.id.post_description_ET);
        imageView = findViewById(R.id.post_image_view);
        saveJournalButton = findViewById(R.id.post_save_button);
        addPhotoButton = findViewById(R.id.post_camera_button);
        progressBar.setVisibility(View.INVISIBLE);

        storageReference = FirebaseStorage.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        if (currentUser != null)
        {
            currentUserId = currentUser.getUid();
            currentUserName = currentUser.getDisplayName();
        }

        saveJournalButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveJournal();
            }
        });

        mTakePhoto = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>()
        {
            @Override
            public void onActivityResult(Uri o)
            {
                imageView.setImageURI(o);
                imageUri = o;
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mTakePhoto.launch("image/*");
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        currentUser = fAuth.getCurrentUser();
        Toast.makeText(this, "Current user: " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
    }

    private void saveJournal()
    {
        String title = titleEditText.getText().toString().trim();
        String thoughts = thoughtsEditText.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(thoughts) && imageUri != null)
        {
            final StorageReference filepath = storageReference.child("journal_images").child("my_image_" + Timestamp.now().getSeconds());

            filepath.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
            {
                filepath.getDownloadUrl().addOnSuccessListener(uri ->
                {
                    String imageUrl = uri.toString();
                    Journal journal = new Journal(title, thoughts, imageUrl, currentUserId, Timestamp.now(), currentUserName);

                    collectionReference.add(journal).addOnSuccessListener(documentReference ->
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(AddJournalActivity.this, JournalListActivity.class));
                        finish();
                    }).addOnFailureListener(e ->
                    {
                        Toast.makeText(AddJournalActivity.this, "Error saving journal!", Toast.LENGTH_SHORT).show();
                    });
                });
            }).addOnFailureListener(e ->
            {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(AddJournalActivity.this, "Error saving journal!", Toast.LENGTH_SHORT).show();
            });
        }
        else
        {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Empty fields not allowed!", Toast.LENGTH_SHORT).show();
        }
    }
}