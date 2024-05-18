package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity
{
    private EditText email, password, username, confirmPassword;
    private Button registerButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        username = findViewById(R.id.register_username);
        confirmPassword = findViewById(R.id.register_confirm_password);
        registerButton = findViewById(R.id.create_account_button);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null)
                {
                    // user is already logged in
                }
                else
                {
                    // no user yet
                }
            }
        };

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()) && !TextUtils.isEmpty(username.getText().toString()) && !TextUtils.isEmpty(confirmPassword.getText().toString()))
                    createUserEmailAccount(email.getText().toString(), password.getText().toString(), confirmPassword.getText().toString(), username.getText().toString());
                else
                    Toast.makeText(RegisterActivity.this, "Empty fields not allowed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUserEmailAccount(String email, String password, String confirmPassword, String username)
    {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword) && !TextUtils.isEmpty(username))
        {
            if (password.equals(confirmPassword))
            {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                            Toast.makeText(RegisterActivity.this, "Account is created successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
                Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        }
    }
}