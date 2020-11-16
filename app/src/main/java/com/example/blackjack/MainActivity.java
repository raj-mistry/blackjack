package com.example.blackjack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String BALANCE="playerBalance";
    public static final String TAG = "Player Balance";

    public DatabaseReference mRef;

    TextView balanceTextView;
    double currentBalance;
    String UID;


    /*
    AppDatabase db = Room.databaseBuilder(getApplicationContext(), //this is our on-device storage
            AppDatabase.class, "database-name").build();
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        UID = intent.getStringExtra("USER");
        balanceTextView = (TextView) findViewById(R.id.playerBalance);
        mRef = FirebaseDatabase.getInstance().getReference().child("playerInfo").child(UID).child("balance");
    }

    //display current player balance
    protected void onStart(){
        super.onStart();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue().toString();
                balanceTextView.setText("$"+value);
                currentBalance = Double.parseDouble(value);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    //update player balance into firebase
    public void updateBalance(View view) {
        EditText updateBalance = (EditText) findViewById(R.id.updateBalance);
        String balance = updateBalance.getText().toString();
        if (balance.isEmpty()){
            Toast.makeText(this, "Please Enter a Value", Toast.LENGTH_LONG).show();
            return;
        }
        double newBalance = currentBalance + Double.parseDouble(balance);
        mRef.setValue(Math.round(newBalance * 100.0) / 100.0);
    }

    //reset player balance (meant for debugging purposes only)
    public void resetBalance(View view) {
        mRef.setValue(0);
    }
}