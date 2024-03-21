package com.example.sportsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    RecyclerView recyclerView;
    List<Sport> sports;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        sports = new ArrayList<>();

        sports.add(new Sport("Football", R.drawable.football));
        sports.add(new Sport("Basketball", R.drawable.basketball));
        sports.add(new Sport("Volleyball", R.drawable.volley));
        sports.add(new Sport("Tennis", R.drawable.tennis));
        sports.add(new Sport("Cricket", R.drawable.ping));

        customAdapter = new CustomAdapter(sports);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customAdapter);
    }
}