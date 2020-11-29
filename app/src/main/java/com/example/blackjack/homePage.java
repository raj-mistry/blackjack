package com.example.blackjack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class homePage extends AppCompatActivity {

    TextView test;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Intent intent = getIntent();
        UID = intent.getStringExtra("USER");
    }

    public void launchSettings(View view){
        Intent intent = new Intent(getBaseContext(), settingsPage.class);
        intent.putExtra("USER", UID);
        startActivity(intent);
    }

    public void play(View view){
        Intent intent = new Intent(getBaseContext(), bettingScreen.class);
        intent.putExtra("USER", UID);
        startActivity(intent);
    }

    public void howtoplay(View view){
        Intent intent = new Intent(getBaseContext(), howtoplay.class);
        startActivity(intent);
    }
}