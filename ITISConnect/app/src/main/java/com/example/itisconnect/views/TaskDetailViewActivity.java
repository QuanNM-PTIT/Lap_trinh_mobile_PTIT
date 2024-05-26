package com.example.itisconnect.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.itisconnect.R;
import com.example.itisconnect.models.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TaskDetailViewActivity extends AppCompatActivity
{
    private EditText title, description, startDate, dueDate, comment;
    private Spinner status, users;
    private Button saveButton, cancelButton;
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private CollectionReference collectionReference;
    private CollectionReference userCollectionReference;
    private ScrollView taskDetailView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail_view);

        taskDetailView = findViewById(R.id.task_detail_view_layout);
        taskDetailView.setVisibility(View.GONE);

        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Tasks");
        userCollectionReference = db.collection("Users");

        title = findViewById(R.id.task_title_detail_view);
        description = findViewById(R.id.description_task_view);
        startDate = findViewById(R.id.start_date_edit_task_view);
        dueDate = findViewById(R.id.due_date_edit_task_view);
        users = findViewById(R.id.assignee_spinner_task);
        status = findViewById(R.id.status_task_view);
        comment = findViewById(R.id.comment_edit_task_view);

        saveButton = findViewById(R.id.task_view_save_button);
        cancelButton = findViewById(R.id.task_view_cancel_button);

        cancelButton.setOnClickListener(v -> finish());

        String taskId = getIntent().getStringExtra("taskId");

        if (taskId != null && !taskId.isEmpty())
        {
            collectionReference.whereEqualTo("taskId", taskId).get().addOnSuccessListener(queryDocumentSnapshots ->
            {
                title.setText(queryDocumentSnapshots.getDocuments().get(0).getString("title"));
                description.setText(queryDocumentSnapshots.getDocuments().get(0).getString("description"));
                startDate.setText(queryDocumentSnapshots.getDocuments().get(0).getString("startDate"));
                dueDate.setText(queryDocumentSnapshots.getDocuments().get(0).getString("dueDate"));
                comment.setText(queryDocumentSnapshots.getDocuments().get(0).getString("comment"));

                String assignedToEmail = queryDocumentSnapshots.getDocuments().get(0).getString("assignedToEmail");

                userCollectionReference.get().addOnSuccessListener(queryDocumentSnapshots1 ->
                {
                    List<String> emails = new ArrayList<>();
                    for (int i = 0; i < queryDocumentSnapshots1.size(); i++)
                    {
                        String email = queryDocumentSnapshots1.getDocuments().get(i).getString("email");
                        emails.add(email);
                    }

                    List<String> statuses = new ArrayList<>();
                    statuses.add("Chưa bắt đầu");
                    statuses.add("Đang thực hiện");
                    statuses.add("Chờ xác nhận");
                    statuses.add("Hoàn thành");

                    db.collection("Users").whereEqualTo("email", currentUser.getEmail()).get()
                            .addOnSuccessListener(queryDocumentSnapshots2 ->
                            {
                                if (!queryDocumentSnapshots2.isEmpty())
                                {
                                    boolean isAdmin = Boolean.TRUE.equals(queryDocumentSnapshots2.getDocuments().get(0).getBoolean("roleAdmin"));
                                    if (!isAdmin && !assignedToEmail.equals(currentUser.getEmail()))
                                    {
                                        users.setEnabled(false);
                                        title.setEnabled(false);
                                        description.setEnabled(false);
                                        startDate.setEnabled(false);
                                        dueDate.setEnabled(false);
                                        status.setEnabled(false);
                                        comment.setEnabled(false);
                                        saveButton.setVisibility(View.GONE);
                                    }
                                    else if (assignedToEmail.equals(currentUser.getEmail()) && !isAdmin)
                                    {
                                        users.setEnabled(false);
                                        startDate.setEnabled(false);
                                        dueDate.setEnabled(false);
                                        if (queryDocumentSnapshots.getDocuments().get(0).getString("status").equals("Hoàn thành"))
                                        {
                                            users.setEnabled(false);
                                            title.setEnabled(false);
                                            description.setEnabled(false);
                                            status.setEnabled(false);
                                            comment.setEnabled(false);
                                            saveButton.setVisibility(View.GONE);
                                        }
                                        else
                                        {
                                            status.setEnabled(true);
                                            statuses.remove("Hoàn thành");
                                        }
                                    }
                                    else if (isAdmin)
                                    {
                                        users.setEnabled(true);
                                    }
                                }
                            });

                    ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(TaskDetailViewActivity.this, android.R.layout.simple_spinner_item, statuses);
                    statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    status.setAdapter(statusAdapter);

                    int statusPosition = statuses.indexOf(queryDocumentSnapshots.getDocuments().get(0).getString("status"));
                    status.setSelection(statusPosition);

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(TaskDetailViewActivity.this, android.R.layout.simple_spinner_item, emails);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    users.setAdapter(spinnerAdapter);

                    int position = emails.indexOf(assignedToEmail);
                    users.setSelection(position);
                    taskDetailView.setVisibility(View.VISIBLE);
                });
            });
        }
        else
        {
            userCollectionReference.get().addOnSuccessListener(queryDocumentSnapshots1 ->
            {
                List<String> emails = new ArrayList<>();
                for (int i = 0; i < queryDocumentSnapshots1.size(); i++)
                {
                    String email = queryDocumentSnapshots1.getDocuments().get(i).getString("email");
                    emails.add(email);
                }

                List<String> statuses = new ArrayList<>();
                statuses.add("Chưa bắt đầu");
                statuses.add("Đang thực hiện");
                statuses.add("Chờ xác nhận");
                statuses.add("Hoàn thành");

                ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(TaskDetailViewActivity.this, android.R.layout.simple_spinner_item, statuses);
                statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                status.setAdapter(statusAdapter);

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(TaskDetailViewActivity.this, android.R.layout.simple_spinner_item, emails);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                users.setAdapter(spinnerAdapter);
                taskDetailView.setVisibility(View.VISIBLE);
            });
        }

        saveButton.setOnClickListener(v ->
        {
            String titleText = title.getText().toString();
            String descriptionText = description.getText().toString();
            String startDateText = startDate.getText().toString();
            String dueDateText = dueDate.getText().toString();
            String statusText = status.getSelectedItem().toString();
            String commentText = comment.getText().toString();
            String assignedToEmail = users.getSelectedItem().toString();

            if (taskId == null || taskId.isEmpty())
            {
                collectionReference.add(new Task(titleText, descriptionText, assignedToEmail, startDateText, dueDateText, statusText, commentText)).addOnSuccessListener(documentReference ->
                {
                    Toast.makeText(TaskDetailViewActivity.this, "Thêm task thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
            else
            {
                db.collection("Tasks").whereEqualTo("taskId", taskId).get().addOnSuccessListener(queryDocumentSnapshots ->
                {
                    String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                    collectionReference.document(documentId).set(new Task(titleText, descriptionText, assignedToEmail, startDateText, dueDateText, statusText, commentText)).addOnSuccessListener(aVoid ->
                    {
                        Toast.makeText(TaskDetailViewActivity.this, "Cập nhật thông tin task thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                });
            }

            finish();
        });

    }
}