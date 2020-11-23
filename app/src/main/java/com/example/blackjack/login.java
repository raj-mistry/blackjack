package com.example.blackjack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText txtEmail, txtPass;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPassword);
        mAuth = FirebaseAuth.getInstance();
        pb = findViewById(R.id.progressBar);
    }

    public void signIn(View view){
        String email = txtEmail.getText().toString();
        String pass = txtPass.getText().toString().trim();

        if (email.isEmpty()){
            txtEmail.setError("Email is required");
            txtEmail.requestFocus();
            return;
        }

        if (pass.isEmpty()){
            txtPass.setError("Password is required");
            txtPass.requestFocus();
            return;
        }

        pb.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String UID = FirebaseAuth.getInstance().getUid();
                    pb.setVisibility(View.GONE);
                    Toast.makeText(login.this, "Logged In Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getBaseContext(), homePage.class);
                    intent.putExtra("USER", UID);
                    startActivity(intent);
                }
                else{
                    pb.setVisibility(View.GONE);
                    Toast.makeText(login.this, "Failed to login", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void register(View view){
        Intent intent = new Intent(getBaseContext(), signUp.class);
        startActivity(intent);
    }
}