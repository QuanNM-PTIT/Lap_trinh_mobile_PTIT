package com.example.itisconnect.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.itisconnect.MainActivity;
import com.example.itisconnect.R;
import com.example.itisconnect.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    private EditText email, password;
    private Button loginButton;
    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        email = binding.emailLogin;
        password = binding.passwordLogin;
        loginButton = binding.buttonLogin;

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = firebaseAuth ->
        {
            currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null)
            {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };

        loginButton.setOnClickListener(v ->
        {
            String emailString = email.getText().toString().trim();
            String passwordString = password.getText().toString().trim();

            if (!emailString.isEmpty() && !passwordString.isEmpty())
            {
                loginUser(emailString, passwordString);
            }
            else
            {
//                loginUser("quannm.ptit@gmail.com", "ManhQuan@123");
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin đăng nhập!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loginUser(String email, String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
            {
                Toast.makeText(this, "Chào mừng đến với ITIS Connect!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                authStateListener.onAuthStateChanged(firebaseAuth);
            }
        }).addOnFailureListener(e ->
        {
            Toast.makeText(this, "Thông tin đăng nhập không đúng!", Toast.LENGTH_SHORT).show();
        });
    }
}