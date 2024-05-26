package com.example.itisconnect.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.itisconnect.R;
import com.example.itisconnect.adapters.UserAdapter;
import com.example.itisconnect.models.Event;
import com.example.itisconnect.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class EventDetailViewActivity extends AppCompatActivity
{
    private EditText title, description, participantET;
    private Spinner status;
    private Button saveButton, cancelButton;
    private FirebaseAuth fAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private CollectionReference collectionReference = db.collection("Events");
    private CollectionReference userCollectionReference = db.collection("Users");
    private String currentEventId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail_view);

        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();

        title = findViewById(R.id.et_event_title);
        description = findViewById(R.id.et_event_description);
        status = findViewById(R.id.spinner_event_status_detail_view);

        saveButton = findViewById(R.id.event_save_button_detail_view);
        cancelButton = findViewById(R.id.event_cancel_button_detail_view);
        participantET = findViewById(R.id.et_event_participants);

        participantET.setEnabled(false);
        participantET.setText("DANH SÁCH NGƯỜI THAM GIA\n\n");


        currentEventId = getIntent().getStringExtra("eventId");

        List<String> statuses = new ArrayList<>();
        statuses.add("Chưa diễn ra");
        statuses.add("Đang diễn ra");
        statuses.add("Đã diễn ra");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(adapter);

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
                                saveButton.setVisibility(View.GONE);
                                status.setEnabled(false);
                                title.setEnabled(false);
                                description.setEnabled(false);
                            }
                        }
                    });
        }


        if (currentEventId != null && !currentEventId.isEmpty())
        {
            db.collection("Events").whereEqualTo("eventId", currentEventId).get()
                    .addOnSuccessListener(queryDocumentSnapshots ->
                    {
                        if (!queryDocumentSnapshots.isEmpty())
                        {
                            Event event = queryDocumentSnapshots.getDocuments().get(0).toObject(Event.class);
                            title.setText(event.getTitle());
                            description.setText(event.getDescription());
                            status.setSelection(statuses.indexOf(event.getStatus()));
                        }
                    });
            //loadUsers();

            database.getReference("Events").child(currentEventId).child("participants").addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    List<String> participants = (List<String>) snapshot.getValue();
                    participantET.setText("DANH SÁCH NGƯỜI THAM GIA\n\n");
                    if (participants != null)
                    {
                        for (String email : participants)
                        {
                            db.collection("Users").whereEqualTo("email", email).get()
                                    .addOnSuccessListener(queryDocumentSnapshots ->
                                    {
                                        if (!queryDocumentSnapshots.isEmpty())
                                        {
                                            User user = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                                            String participantsString = participantET.getText().toString();
                                            String newUser = "* " + user.getFullName() + " - " + user.getEmail() + " - " + user.getPosition() + "\n\n";
                                            participantET.setText(participantsString + newUser);
                                        }
                                    });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });
        }

        cancelButton.setOnClickListener(v -> finish());

        saveButton.setOnClickListener(v ->
        {
            String titleText = title.getText().toString();
            String descriptionText = description.getText().toString();
            String statusText = status.getSelectedItem().toString();

            if (titleText.isEmpty() || descriptionText.isEmpty() || statusText.isEmpty())
            {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (currentEventId.isEmpty())
            {
                Event newEvent = new Event(titleText, descriptionText, statusText);

                collectionReference.add(newEvent)
                        .addOnSuccessListener(documentReference ->
                        {
                            Toast.makeText(this, "Thêm sự kiện thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e ->
                        {
                            Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                        });

                String eventId = newEvent.getEventId();
                List<String> participants = new ArrayList<>();
                participants.add(currentUser.getEmail());
                database.getReference("Events").child(eventId).child("participants").setValue(participants);
                database.getReference("Events").child(eventId).child("currentCode").setValue("hihi");
            }
            else
            {
                collectionReference.whereEqualTo("eventId", currentEventId).get()
                        .addOnSuccessListener(queryDocumentSnapshots ->
                        {
                            if (!queryDocumentSnapshots.isEmpty())
                            {
                                String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                                Event updatedEvent = new Event(currentEventId, titleText, descriptionText, statusText);
                                collectionReference.document(documentId).set(updatedEvent)
                                        .addOnSuccessListener(aVoid ->
                                        {
                                            Toast.makeText(this, "Cập nhật sự kiện thành công!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        })
                                        .addOnFailureListener(e ->
                                        {
                                            Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        });
            }
        });


    }

    private void loadUsers()
    {
        database.getReference("Events").child(currentEventId).child("participants").get()
                .addOnSuccessListener(dataSnapshot ->
                {
                    List<String> participants = (List<String>) dataSnapshot.getValue();
                    if (participants != null)
                    {
                        for (String email : participants)
                        {
                            db.collection("Users").whereEqualTo("email", email).get()
                                    .addOnSuccessListener(queryDocumentSnapshots ->
                                    {
                                        if (!queryDocumentSnapshots.isEmpty())
                                        {
                                            User user = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
                                            String participantsString = participantET.getText().toString();
                                            String newUser = "* " + user.getFullName() + " - " + user.getEmail() + " - " + user.getPosition() + "\n\n";
                                            participantET.setText(participantsString + newUser);
                                        }
                                    });
                        }
                    }
                });
    }
}