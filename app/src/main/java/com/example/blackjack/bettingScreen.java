package com.example.blackjack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class bettingScreen extends AppCompatActivity {

    public static final String AMOUNT = "amount";
    public static final String TAG = "betValue";
    DecimalFormat currency = new DecimalFormat("#.##");


    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("bet/player");


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting_screen);
    }

    public void placeBet(View view){
        TextView bet = (TextView)findViewById(R.id.textValue);
        Slider betValue = (Slider)findViewById(R.id.betSlider);
        if (betValue.getValue() == 0) {
            bet.setText("You must bet a value grater than 0");
            return;
        }

        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put(AMOUNT, currency.format(betValue.getValue()));

        mDocRef.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Document has been saved");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Document was not saved", e);
            }
        });
        bet.setText("$"+currency.format(betValue.getValue()));
    }
}