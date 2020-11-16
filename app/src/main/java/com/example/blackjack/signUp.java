package com.example.blackjack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signUp extends AppCompatActivity {

    public DocumentReference mDocRef = FirebaseFirestore.getInstance().document("game/playerInfo");
    public static final String TAG = "Player Info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void signedUp(View view){
        TextView fname, lname, email, username, password, confirmedPass, result;
        fname = findViewById(R.id.textFname);
        lname = findViewById(R.id.textLname);
        email = findViewById(R.id.textEmail);
        username = findViewById(R.id.textUser);
        password = findViewById(R.id.textPassword);
        confirmedPass = findViewById(R.id.textConfirm);
        result = findViewById(R.id.textResult);

        if (!password.getText().toString().equals(confirmedPass.getText().toString()) || password.getText().toString().isEmpty()){
            result.setText("Invalid password or passwords do not match");
            return;
        }

        String fname1, lname1, email1, username1;
        fname1 = fname.getText().toString();
        lname1 = lname.getText().toString();
        email1 = email.getText().toString();
        username1 = username.getText().toString();

        if (fname1.isEmpty() || lname1.isEmpty() || email1.isEmpty() || username1.isEmpty()) {return;}
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("name", fname1+" "+lname1);
        dataToSave.put("email", email1);
        dataToSave.put("password", password.getText().toString());
        dataToSave.put("username", username1);
        dataToSave.put("playerBalance", "0");

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