package com.example.blackjack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.ktx.Firebase;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class bettingScreen extends AppCompatActivity {

    public static final String AMOUNT = "amount";
    public static final String TAG = "betValue";
    String UID;
    TextView bet;
    Slider betValue;
    double currentBalance;

    public CollectionReference mDocRef = FirebaseFirestore.getInstance().collection("bet");
    public DocumentReference mRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting_screen);
        Intent intent = getIntent();
        UID = intent.getStringExtra("USER");
        bet = (TextView)findViewById(R.id.textValue);
        betValue = (Slider)findViewById(R.id.betSlider);
        mRef = FirebaseFirestore.getInstance().document("playerInfo/" +UID);
    }

    protected void onStart(){
        super.onStart();
        mRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
                    currentBalance = documentSnapshot.getDouble("balance");
                    if (currentBalance <= 0){
                        Toast.makeText(bettingScreen.this, "Out of funds. Please add more funds to your balance before playing", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getBaseContext(), homePage.class);
                        intent.putExtra("USER", UID);
                        startActivity(intent);
                    }
                    else {
                        betValue.setValueTo((float) currentBalance);
                    }
                    Log.d(TAG, "Textview updated");
                } else if (error != null) {
                    Log.w(TAG, "Got an error", error);
                }
            }
        });

    }

    public void placeBet(View view){

        if (betValue.getValue() == 0) {
            bet.setText("You must bet a value grater than 0");
            return;
        }
        double betAmount = Math.round(betValue.getValue() * 100.0)/100.0;
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("User", UID);
        dataToSave.put(AMOUNT, betAmount);

        mDocRef.add(dataToSave).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Intent intent = new Intent(getBaseContext(), Game.class);
                intent.putExtra("USER", UID);
                intent.putExtra("BET", betAmount);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                bet.setText("Error setting bet. Please try again");
            }
        });

    }

}