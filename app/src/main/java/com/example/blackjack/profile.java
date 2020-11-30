package com.example.blackjack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class profile extends AppCompatActivity {

    private DocumentReference mPlayerRef, mRecordsRef;
    private CollectionReference mGameRef;
    private TextView played, won, earnings;
    private TextView name, email, username, balance, created;
    private TextView result, date, bet, playerHand, dealerHand;
    private Double sPlayed, sWon, sEarnings;
    private String sName, sEmail, sUsername, sCreated;
    private double sBalance;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        UID = intent.getStringExtra("USER");
        mPlayerRef = FirebaseFirestore.getInstance().document("/playerInfo/"+UID);
        mRecordsRef = FirebaseFirestore.getInstance().document("/records/"+UID);
        mGameRef = FirebaseFirestore.getInstance().collection("game");
        initializer();
        putPlayerInfo();
        putRecordInfo();
        putGameInfo();
    }

    protected void putGameInfo(){
        mGameRef.whereEqualTo("User", UID)
                .orderBy("Ended", Query.Direction.DESCENDING).limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("hi", document.getId() + " => " + document.getData());
                        previousGames pg = document.toObject(previousGames.class);
                        date.setText("Date: "+pg.getEnded());
                        bet.setText("Bet: $"+pg.getBetValue()+"0");
                        playerHand.setText("Player Hand: "+(int)pg.getPlayerHand());
                        dealerHand.setText("Dealer Hand: "+(int)pg.getDealerHand());
                        if (pg.isPlayerWon()) { result.setText("Won"); }
                        else { result.setText("Lost"); }
                        System.out.println(pg.getPlayerHand());
                    }
                } else {
                    Log.d("bye", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    protected void putPlayerInfo() {
        mPlayerRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
                    sEmail = documentSnapshot.getString("email");
                    sUsername = documentSnapshot.getString("username");
                    sName = documentSnapshot.getString("name");
                    sCreated = documentSnapshot.getString("createdOn");
                    sBalance = documentSnapshot.getDouble("balance");

                    name.setText(sName);
                    username.setText(sUsername);
                    email.setText(sEmail);
                    created.setText("Created On: "+sCreated);
                    balance.setText("Current Balance: $"+sBalance+"0");
                    Log.d("PLAYER INFO",sName+" "+sUsername+" "+sEmail+" "+sBalance);
                } else if (error != null) {
                    Log.w("hi", "Got an error", error);
                }
            }
        });
    }

    protected void putRecordInfo(){
        mRecordsRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
                    sEarnings = documentSnapshot.getDouble("totalWinnings");
                    sPlayed = documentSnapshot.getDouble("gamesPlayed");
                    sWon = documentSnapshot.getDouble("victories");

                    earnings.setText("Total Earnings: $"+sEarnings+"0");
                    won.setText("Games Won: "+Math.round(sWon));
                    played.setText("Games Played: "+Math.round(sPlayed));
                    Log.d("RECORDS",sEarnings+" "+sWon+" "+sPlayed);
                } else if (error != null) {
                    Log.w("RECORDS", "Got an error", error);
                }
            }
        });
    }

    protected void initializer(){
        played = findViewById(R.id.profileGames);
        won = findViewById(R.id.profileWon);
        earnings = findViewById(R.id.profileEarnings);
        name = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        username = findViewById(R.id.profileUser);
        result = findViewById(R.id.profileResult);
        date = findViewById(R.id.profileDate);
        bet = findViewById(R.id.profileBet);
        playerHand = findViewById(R.id.profilePlayerHand);
        dealerHand = findViewById(R.id.profileDealerHand);
        balance = findViewById(R.id.personalBalance);
        created = findViewById(R.id.profileCreated);
    }
}