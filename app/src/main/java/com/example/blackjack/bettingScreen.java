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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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

    public CollectionReference mDocRef = FirebaseFirestore.getInstance().collection("bet");


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting_screen);
        Intent intent = getIntent();
        UID = intent.getStringExtra("USER");
        bet = (TextView)findViewById(R.id.textValue);
        betValue = (Slider)findViewById(R.id.betSlider);
    }

    public void placeBet(View view){

        if (betValue.getValue() == 0) {
            bet.setText("You must bet a value grater than 0");
            return;
        }
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("User", UID);
        dataToSave.put(AMOUNT, (double)betValue.getValue());

        mDocRef.add(dataToSave).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                bet.setText("You have placed a bet of: $"+betValue.getValue());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                bet.setText("Error setting bet. Please try again");
            }
        });
        /*
        if (betValue.getValue() == 0) {
            bet.setText("You must bet a value grater than 0");
            return;
        }



         */
    }

    /*
    TO-DO: Prevent user from going above maximum value on their bet
     */
}