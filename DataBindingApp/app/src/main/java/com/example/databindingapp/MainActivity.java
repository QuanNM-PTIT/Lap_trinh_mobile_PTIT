package com.example.databindingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.databindingapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
{
    private ActivityMainBinding binding;
    private MyClickHandler clickHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Person person1 = new Person("John Doe", "johndoe@gmail.com");

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setPerson(person1);

        clickHandler = new MyClickHandler(this);
        binding.setClickHandler(clickHandler);


    }
}