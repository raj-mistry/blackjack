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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String BALANCE="balance";
    public static final String TAG = "Player Balance";

    public DocumentReference mDocRef;
    public CollectionReference pDocRef = FirebaseFirestore.getInstance().collection("addFunds");

    TextView balanceTextView;
    double currentBalance;
    String UID, name, email, username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        UID = intent.getStringExtra("USER");
        balanceTextView = (TextView) findViewById(R.id.playerBalance);
        mDocRef = FirebaseFirestore.getInstance().document("playerInfo/" +UID);
    }

    //display current player balance
    protected void onStart(){
        super.onStart();
        mDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
                    currentBalance = documentSnapshot.getDouble(BALANCE);
                    Log.d(TAG, "Textview updated");
                    balanceTextView.setText("$"+Double.toString(currentBalance));
                } else if (error != null) {
                    Log.w(TAG, "Got an error", error);
                }
            }
        });
    }

    //update player balance into firebase
    public void updateBalance(View view) {
        EditText updateBalance = (EditText) findViewById(R.id.updateBalance);
        String balance = updateBalance.getText().toString();
        if (balance.isEmpty()){
            Toast.makeText(MainActivity.this, "Please Enter a Value", Toast.LENGTH_LONG).show();
            return;
        }
        double newBalance = currentBalance + Double.parseDouble(balance);
        double finalBalance = Math.round(newBalance*100.0)/100.0;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put(BALANCE, finalBalance);
        mDocRef.update(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
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

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("Amount Added", Double.parseDouble(balance));
        data.put("Previous Balance", currentBalance);
        data.put("New Balance", finalBalance);
        data.put("User", UID);
        data.put("Date", formattedDate);
        pDocRef.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "Document has been saved");
            }
        });
    }

    //reset player balance (meant for debugging purposes only)
    public void resetBalance(View view) {
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put(BALANCE, 0.0);
        mDocRef.update(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    }
}