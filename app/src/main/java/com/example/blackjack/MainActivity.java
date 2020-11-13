package com.example.blackjack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

//hi
public class MainActivity extends AppCompatActivity {
    public static final String BALANCE="playerBalance";
    public static final String TAG = "Player Balance";

    TextView balanceTextView;

    public DocumentReference mDocRef = FirebaseFirestore.getInstance().document("game/playerBalance");

    /*
    AppDatabase db = Room.databaseBuilder(getApplicationContext(), //this is our on-device storage
            AppDatabase.class, "database-name").build();
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        balanceTextView = (TextView) findViewById(R.id.playerBalance);
    }

    //display current player balance
    protected void onStart(){
        super.onStart();
        mDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()){
                    String balanceText= documentSnapshot.getString(BALANCE);
                    Log.d(TAG, "Textview updated yoooo");
                    balanceTextView.setText(balanceText);
                } else if (error!=null){
                    Log.w(TAG, "Got an error",error);
                }
            }
        });
    }

    //update player balance into firebase
    public void updateBalance(View view) {
        EditText updateBalance = (EditText) findViewById(R.id.updateBalance);
        String balance = updateBalance.getText().toString();

        if (balance.isEmpty()){return;}
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put(BALANCE, balance);

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
    }
}