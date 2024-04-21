package com.example.viewmodelapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.example.viewmodelapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
{
    private MyViewModel viewModel;
    private int counter = 0;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(MyViewModel.class);

        binding.setViewmodel(viewModel);

        viewModel.getCounter().observe(this,
                new Observer<Integer>()
                {
                    @Override
                    public void onChanged(Integer integer)
                    {
                        binding.textView2.setText(String.valueOf(integer));
                    }
                });
    }
}