package com.example.chatapp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityLoginBinding;
import com.example.chatapp.viewmodel.MyViewModel;

public class LoginActivity extends AppCompatActivity
{
    private MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        binding.setViewModel(myViewModel);


    }
}