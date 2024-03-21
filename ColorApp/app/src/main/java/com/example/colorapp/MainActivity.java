package com.example.colorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    Button blackBtn, purpleBtn, greenBtn, redBtn, yellowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        blackBtn = findViewById(R.id.blackBtn);
        purpleBtn = findViewById(R.id.purpleBtn);
        greenBtn = findViewById(R.id.greenBtn);
        redBtn = findViewById(R.id.redBtn);
        yellowBtn = findViewById(R.id.yellowBtn);

        blackBtn.setOnClickListener(this);
        purpleBtn.setOnClickListener(this);
        greenBtn.setOnClickListener(this);
        redBtn.setOnClickListener(this);
        yellowBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();

        if (id == R.id.blackBtn)
            playSound(R.raw.black);
        else if (id == R.id.purpleBtn)
            playSound(R.raw.purple);
        else if (id == R.id.greenBtn)
            playSound(R.raw.green);
        else if (id == R.id.redBtn)
            playSound(R.raw.red);
        else if (id == R.id.yellowBtn)
            playSound(R.raw.yellow);
    }

    public void playSound(int id)
    {
        MediaPlayer mp = MediaPlayer.create(this, id);
        mp.start();
    }
}