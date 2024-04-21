package com.example.viewmodelapp;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel
{
    private MutableLiveData<Integer> counter = new MutableLiveData<>();

    public void incrementCounter(View view)
    {
        int currentCounter = counter.getValue() == null ? 0 : counter.getValue();
        counter.setValue(currentCounter + 1);
    }

    public LiveData<Integer> getCounter()
    {
        return counter;
    }
}
