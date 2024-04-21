package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        User user1 = new User("Jack", "jack@gmail.com");
        myRef.setValue(user1);

        //myRef.setValue("Hello, World!");

        TextView textView = findViewById(R.id.textView);

        myRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                User user = snapshot.getValue(User.class);
                textView.setText(user.getName() + " - " + user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                // Handle error here
            }
        });
    }
}