package com.example.thecounterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    TextView title, counter;
    Button btn;
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        counter = findViewById(R.id.counter_txt);
        title = findViewById(R.id.title);

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                counter.setText("" + increaseCounter());
            }
        });
    }

    public int increaseCounter()
    {
        return ++cnt;
    }
}