package com.example.luckynumber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class SecondActivity extends AppCompatActivity
{
    TextView luckyNumber;
    Button buttonShare;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        luckyNumber = findViewById(R.id.luckyNumber);
        buttonShare = findViewById(R.id.buttonShare);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        luckyNumber.setText(String.valueOf(getRandNumber()));

        buttonShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                shareData(name, Integer.parseInt(luckyNumber.getText().toString()));
            }
        });
    }

    public int getRandNumber()
    {
        Random random = new Random();
        return random.nextInt(1000);
    }

    public void shareData(String name, int luckyNumber)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_SUBJECT, "Hi, my name is " + name);
        intent.putExtra(Intent.EXTRA_TEXT, "My lucky number is " + luckyNumber);
        startActivity(Intent.createChooser(intent, "Share with"));
    }
}