package com.example.blackjack;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class homePage extends AppCompatActivity {

    private DocumentReference mDocRef;
    private double balance;
    private TextView txtbalance;
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Intent intent = getIntent();
        UID = intent.getStringExtra("USER");
        txtbalance = findViewById(R.id.homeBalance);
        mDocRef = FirebaseFirestore.getInstance().document("playerInfo/"+UID);
    }

    protected void onStart() {
        super.onStart();
        mDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
                    balance = documentSnapshot.getDouble("balance");
                    txtbalance.setText("Your balance: $" + balance + "0");
                    Log.d("hi", "Textview updated");
                } else if (error != null) {
                    Log.w("bye", "Got an error", error);
                }
            }
        });
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