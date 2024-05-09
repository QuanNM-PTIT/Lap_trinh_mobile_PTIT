package com.example.contactsmanagerapp;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class MainActivityClickHandlers
{
    private Context context;

    public MainActivityClickHandlers(Context context)
    {
        this.context = context;
    }

    public void onFabClicked(View view)
    {
        Intent intent = new Intent(view.getContext(), AddNewContactActivity.class);
        context.startActivity(intent);
    }
}
