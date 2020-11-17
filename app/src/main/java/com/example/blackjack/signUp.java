package com.example.blackjack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signUp extends AppCompatActivity {

    public DocumentReference mDocRef = FirebaseFirestore.getInstance().document("playerInfo/7O0zBXDRU8kYUBNyetvB");
    public static final String TAG = "Player Info";
    TextView fname, lname, email, username, password, confirmedPass, result;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        fname = findViewById(R.id.textFname);
        lname = findViewById(R.id.textLname);
        email = findViewById(R.id.textEmail);
        username = findViewById(R.id.textUser);
        password = findViewById(R.id.textPassword);
        confirmedPass = findViewById(R.id.textConfirm);
        result = findViewById(R.id.textResult);
    }

    public void signedUp(View view){
        String fname1, lname1, email1, username1, password1, confirmedPass1;
        fname1 = fname.getText().toString();
        lname1 = lname.getText().toString();
        email1 = email.getText().toString();
        username1 = username.getText().toString();
        password1 = password.getText().toString();
        confirmedPass1 = confirmedPass.getText().toString();

        if (fname1.isEmpty()){
            fname.setError("Field is empty or invalid");
            return;
        }
        if (lname1.isEmpty()){
            lname.setError("Field is empty or invalid");
            return;
        }
        if (email1.isEmpty()){
            email.setError("Field is empty or invalid");
            return;
        }
        if (username1.isEmpty()){
            username.setError("Field is empty or invalid");
            return;
        }
        if (!password1.equals(confirmedPass1) || password.getText().toString().isEmpty()){
            password.setError("Invalid password or passwords do not match");
            return;
        }

        /*
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

         */
        mAuth.createUserWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Users user = new Users(fname1, lname1, username1, email1);
                            FirebaseDatabase.getInstance().getReference("playerInfo")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(signUp.this,"Successful", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(signUp.this,"Failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else{
                            Toast.makeText(signUp.this,"Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}