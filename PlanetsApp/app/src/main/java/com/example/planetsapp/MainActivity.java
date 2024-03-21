package com.example.planetsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    ListView listView;
    ArrayList<Planet> planets;
    private static MyCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.planetList);
        planets = new ArrayList<Planet>();

        planets.add(new Planet("Mercury", "0", R.drawable.mercury));
        planets.add(new Planet("Venus", "0", R.drawable.venus));
        planets.add(new Planet("Earth", "1", R.drawable.earth));
        planets.add(new Planet("Mars", "2", R.drawable.mars));
        planets.add(new Planet("Jupiter", "79", R.drawable.jupiter));
        planets.add(new Planet("Saturn", "82", R.drawable.saturn));
        planets.add(new Planet("Uranus", "27", R.drawable.uranus));
        planets.add(new Planet("Neptune", "14", R.drawable.neptune));

        adapter = new MyCustomAdapter(planets, getApplicationContext());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id)
            {
                Toast.makeText(getApplicationContext(), planets.get(i).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}