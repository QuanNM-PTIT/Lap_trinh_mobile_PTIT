package com.example.firestoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity
{
    private Button addBTN, readBTN, updateBTN, deleteBTN;
    private EditText nameET, emailET;
    private TextView dataTV;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private DocumentReference docRef = database.collection("Users").document("Students");
    private DocumentReference docRef2 = database.collection("Users").document("2TRNNIUnxIbNXCnwBdY9");
    private CollectionReference colRef = database.collection("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBTN = findViewById(R.id.addBT);
        readBTN = findViewById(R.id.readBT);
        updateBTN = findViewById(R.id.updateBT);
        deleteBTN = findViewById(R.id.deleteBT);

        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);

        dataTV = findViewById(R.id.dataTV);

        addBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addData();
            }
        });

        readBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                readData();
            }
        });

        updateBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                updateData();
            }
        });

        deleteBTN.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                deleteData();
            }
        });
    }

    private void addData()
    {
        String name = nameET.getText().toString();
        String email = emailET.getText().toString();

        Student student = new Student(name, email);

        colRef.add(student).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
        {
            @Override
            public void onSuccess(DocumentReference documentReference)
            {
                nameET.setText("");
                emailET.setText("");
                String id = documentReference.getId();
                Toast.makeText(MainActivity.this, "Add student with id: " + id + " successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void readData()
    {
        colRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
        {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots)
            {
                String data = "STUDENTS:\n";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    Student student = documentSnapshot.toObject(Student.class);
                    data += "ID: " + documentSnapshot.getId() + "\nName:" + student.getName() + "\nEmail: " + student.getEmail() + "\n\n";
                }
                dataTV.setText(data);
            }
        });
    }

    private void updateData()
    {
        String name = nameET.getText().toString();
        String email = emailET.getText().toString();

        Student student = new Student(name, email);

        docRef2.set(student).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                nameET.setText("");
                emailET.setText("");
                Toast.makeText(MainActivity.this, "Update student successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteData()
    {
        docRef2.delete().addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                Toast.makeText(MainActivity.this, "Delete student successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}