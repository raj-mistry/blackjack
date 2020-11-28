package com.example.blackjack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class signUp extends AppCompatActivity {

    public static final String TAG = "Player Info";
    private TextView fname, lname, email, username, password, confirmedPass;
    private ProgressBar pb;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        pb = findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();
        fname = findViewById(R.id.textFname);
        lname = findViewById(R.id.textLname);
        email = findViewById(R.id.textEmail);
        username = findViewById(R.id.textUser);
        password = findViewById(R.id.textPassword);
        confirmedPass = findViewById(R.id.textConfirm);
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
        if (!password1.equals(confirmedPass1) || password1.isEmpty() || password1.length()<6){
            password.setError("Invalid password or passwords do not match");
            return;
        }
        //pb.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String formattedDate = df.format(c.getTime());
                            String UID = FirebaseAuth.getInstance().getUid();
                            DocumentReference mDocRef = FirebaseFirestore.getInstance().document("playerInfo/"+UID);
                            Users user = new Users(fname1, lname1, username1, email1, formattedDate);
                            mDocRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //pb.setVisibility(View.GONE);
                                    Toast.makeText(signUp.this, "Success", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getBaseContext(), homePage.class);
                                    intent.putExtra("USER", UID);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //pb.setVisibility(View.GONE);
                                    Toast.makeText(signUp.this, "Failed", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else{
                            //pb.setVisibility(View.GONE);
                            Toast.makeText(signUp.this,"Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}