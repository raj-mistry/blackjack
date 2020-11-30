package com.example.blackjack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class settingsPage extends AppCompatActivity {

    protected String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        Intent intent = getIntent();
        UID = intent.getStringExtra("USER");
    }

    public void viewLeaderboard(View view){

    }

    public void viewHistory(View view){
        Intent intent = new Intent(getBaseContext(), history.class);
        intent.putExtra("USER", UID);
        startActivity(intent);
    }

    public void viewProfile(View view){
        Intent intent = new Intent(getBaseContext(), profile.class);
        intent.putExtra("USER", UID);
        startActivity(intent);
    }

    public void addFunds(View view){
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("USER", UID);
        startActivity(intent);
    }

    public void signOut(View view){
        Intent intent = new Intent(getBaseContext(), login.class);
        startActivity(intent);
    }
}