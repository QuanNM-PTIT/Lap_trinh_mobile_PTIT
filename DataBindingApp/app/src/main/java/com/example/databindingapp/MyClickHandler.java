package com.example.databindingapp;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class MyClickHandler
{
    private Context context;

    public MyClickHandler(Context context)
    {
        this.context = context;
    }

    public void onButtonClicked(View view)
    {
        Toast.makeText(context, "Button clicked!", Toast.LENGTH_SHORT).show();
    }
}
