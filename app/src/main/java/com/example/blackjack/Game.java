package com.example.blackjack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Game extends AppCompatActivity {
    protected int[] Deck = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52};
    protected ArrayList<Integer> userArray = new ArrayList<Integer>();
    protected ArrayList<Integer> dealerArray = new ArrayList<Integer>();
    protected int currentIndex = 0; //the card the program retrieves depends on the index. current index ex: Deck[7] will return 6, but when the deck is shuffled, itll return whatever value is there.
    protected int userTotal = 0; //user total is the total value of the pile ex: 5+6=11, loses if userTotal>21
    protected int playerRound = 0; //keep track of which card the user is playing (1st card, 2nd card etc.)
    protected int dealerRound = 0; //same as playerRound but for the dealer

    protected int dealerTotal = 0; //same as user total except this is for the dealer AI
    protected boolean winStatus=false; // true if the player wins, false if player loses.

    protected boolean userBust=false; //true if user bust, player loses
    protected boolean dealerBust=false; //true if dealer bust, player wins, dealer loses.

    //Get UID and bet value, and their respective DocReferences to keep track of user and game session
    protected String UID;
    protected double betVal;
    protected double balance;
    protected double victories, gamesPlayed, totalEarnings;
    protected DocumentReference mDocRef;
    protected CollectionReference mGameRef = FirebaseFirestore.getInstance().collection("game");
    protected DocumentReference mRecordRef;
    public static final String TAG = "Player Balance";
    public static final String BALANCE="balance";


    // all the text views in the xml,
    // ___Pile is the integer sum of the cards
    // ___Cards is the list of cards
    // gameStatus indicates which player won, if user won or dealer won.
    TextView dealerPile;
    TextView dealerCards;
    TextView userPile;
    TextView userCards;
    TextView gameStatus;

    //Set ImageViews to represent player cards
    ImageView playerCard1;
    ImageView playerCard2;
    ImageView playerCard3;
    ImageView playerCard4;
    ImageView playerCard5;
    ImageView playerCard6;
    ImageView playerCard7;
    ImageView playerCard8;
    ImageView playerCard9;
    ImageView dealerCard1;
    ImageView dealerCard2;
    ImageView dealerCard3;
    ImageView dealerCard4;
    ImageView dealerCard5;
    ImageView dealerCard6;
    ImageView dealerCard7;
    ImageView dealerCard8;
    ImageView dealerCard9;
    ImageView[]playerCards=new ImageView[9];
    ImageView[]dealCards=new ImageView[9];


    //buttons self explanatory.
    Button hitMeButton;
    Button stopButton;
    Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        betVal = intent.getDoubleExtra("BET", 0);
        UID = intent.getStringExtra("USER");
        mDocRef = FirebaseFirestore.getInstance().document("playerInfo/" +UID);
        mRecordRef = FirebaseFirestore.getInstance().document("records/"+UID);

        dealerPile = (TextView) findViewById(R.id.dealerPile);
        dealerCards = (TextView) findViewById(R.id.dealerCards);
        userPile = (TextView) findViewById(R.id.userPile);
        userCards = (TextView) findViewById(R.id.userCards);
        hitMeButton = findViewById(R.id.hitMeButton);
        stopButton = findViewById(R.id.stopButton);
        returnButton = findViewById(R.id.returnButton);
        gameStatus = findViewById(R.id.gameStatus);
        gameStatus.setText("Current bet: $"+betVal+"0");
        returnButton.setEnabled(false);

        setCardImages();

        Deck = shuffleArray(Deck);

        hitDealer();

        hitMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitUser();
                //hitUser();
                if (userBust){
                    //END THE GAME
                    gameStatus.setText("PLAYER BUST, PLAYER DEFEAT!");
                    stop();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playDealer();
                if (dealerBust){
                    gameStatus.setText("DEALER BUST, PLAYER VICTORY!");
                    winStatus=true;
                }
                else{
                    gameStatus.setText("PLAYER DEFEAT!");
                }
                stop();
            }
        });

        //define what happens on return
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGameData();
                setBalance();
                setRecord();
                Intent intent = new Intent(getBaseContext(), homePage.class);
                intent.putExtra("USER", UID);
                startActivity(intent);
            }
        });

    }

    public void onStart(){
        super.onStart();
        mDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
                    balance = documentSnapshot.getDouble(BALANCE);
                    Log.d(TAG, "Balance updated");
                } else if (error != null) {
                    Log.w(TAG, "Got an error", error);
                }
            }
        });

        mRecordRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@com.google.firebase.database.annotations.Nullable DocumentSnapshot documentSnapshot, @com.google.firebase.database.annotations.Nullable FirebaseFirestoreException error) {
                if (documentSnapshot.exists()) {
                    totalEarnings = documentSnapshot.getDouble("totalWinnings");
                    victories = documentSnapshot.getDouble("victories");
                    gamesPlayed = documentSnapshot.getDouble("gamesPlayed");
                    Log.d(TAG, "Records updated");
                } else if (error != null) {
                    Log.w(TAG, "Got an error", error);
                }
            }
        });
    }

    public void reset(){
        userArray = new ArrayList<Integer>();
        dealerArray = new ArrayList<Integer>();
        playerRound = 0;
        currentIndex = 0;
        userTotal = 0;
        dealerTotal = 0;
        winStatus=false;
        userBust=false;
        dealerBust=false;

        Deck = shuffleArray(Deck);
        hitDealer();
        hitUser();

        gameStatus.setText("Current bet: $"+betVal+"0");

        hitMeButton.setEnabled(true);
        stopButton.setEnabled(true);
        returnButton.setEnabled(false);
    }


    public int[] shuffleArray(int[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        return ar;
    }

    public void updateUserPile(){

    }

    public void hitUser(){
        currentIndex+=1;
        playerRound+=1;
        int currentCard = ((Deck[currentIndex]-1) % 13)+1;
        int findSuit = (Deck[currentIndex]-1)/13;
        String suit;
        if (findSuit==1){ suit="s"; }
        else if (findSuit==2){ suit="c"; }
        else if (findSuit==3) { suit="d"; }
        else { suit="h"; }

        if (playerRound < 10){
            String cardVal;
            if (currentCard<11 && currentCard>1){ cardVal=Integer.toString(currentCard); }
            else if (currentCard==11){ cardVal="j"; }
            else if (currentCard==12){ cardVal="q"; }
            else if (currentCard==1){ cardVal="a"; }
            else { cardVal="k"; }
            String image = suit+"_"+cardVal;
            int resID = getResources().getIdentifier(image, "drawable", getPackageName());
            playerCards[playerRound-1].setImageResource(resID);
        }
        userArray.add(currentCard);
        userTotal+=currentCard;

        setUserBust();
        userPile.setText("User Pile: "+ Integer.toString(userTotal));
        userCards.setText("User Cards: "+ userArray.toString());
    }

    public void setUserBust(){
        if (userTotal>21){
            userBust=true;
        }
    }

    public void setDealerBust(){
        if (dealerTotal>21){
            dealerBust=true;
        }
    }


    public void hitDealer(){
        currentIndex+=1;
        dealerRound+=1;
        int currentCard = ((Deck[currentIndex]-1) % 13)+1;
        int findSuit = (Deck[currentIndex]-1)/13;
        String suit;
        if (findSuit==1){ suit="s"; }
        else if (findSuit==2){ suit="c"; }
        else if (findSuit==3) { suit="d"; }
        else { suit="h"; }

        if (dealerRound < 10){
            String cardVal;
            if (currentCard<11 && currentCard>1){ cardVal=Integer.toString(currentCard); }
            else if (currentCard==11){ cardVal="j"; }
            else if (currentCard==12){ cardVal="q"; }
            else if (currentCard==1){ cardVal="a"; }
            else { cardVal="k"; }
            String image = suit+"_"+cardVal;
            int resID = getResources().getIdentifier(image, "drawable", getPackageName());
            dealCards[dealerRound-1].setImageResource(resID);
        }
        dealerArray.add(currentCard);
        dealerTotal+=currentCard;
        setDealerBust();
        dealerPile.setText("Dealer Pile: "+ Integer.toString(dealerTotal));
        dealerCards.setText("Dealer Cards: "+ dealerArray.toString());
    }

    public void playDealer(){
        while (dealerTotal<userTotal){
            hitDealer();
        }
    }

    public void stop(){
        hitMeButton.setEnabled(false);
        stopButton.setEnabled(false);
        returnButton.setEnabled(true);
    }

    public double calcBalance(){
        if (winStatus)
            balance = balance + (betVal*2);
        else
            balance = balance - betVal;
        return balance;
    }

    public void setGameData(){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("User", UID);
        dataToSave.put("playerHand", userTotal);
        dataToSave.put("dealerHand", dealerTotal);
        dataToSave.put("playerWon", winStatus);
        dataToSave.put("betValue", betVal);
        dataToSave.put("Ended", formattedDate);
        mGameRef.add(dataToSave).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(Game.this, "Game Over. Data Recorded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setBalance(){
        Map<String, Object> playerData = new HashMap<String, Object>();
        playerData.put(BALANCE, Math.round(calcBalance()*100)/100);
        mDocRef.update(playerData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Balance updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Got an error");
            }
        });
    }

    public void setRecord(){
        gamesPlayed+=1;
        if (winStatus){
            totalEarnings=totalEarnings+(betVal*2);
            victories+=1;
        }
        else{
            totalEarnings=totalEarnings-betVal;
        }
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("totalWinnings", Math.round(totalEarnings*100)/100);
        dataToSave.put("victories", victories);
        dataToSave.put("gamesPlayed",gamesPlayed);
        mRecordRef.update(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Balance updated");
            }
        });
    }

    public void setCardImages(){
        playerCard1 = findViewById(R.id.imgCard1);
        playerCard2 = findViewById(R.id.imgCard2);
        playerCard3 = findViewById(R.id.imgCard3);
        playerCard4 = findViewById(R.id.imgCard4);
        playerCard5 = findViewById(R.id.imgCard5);
        playerCard6 = findViewById(R.id.imgCard6);
        playerCard7 = findViewById(R.id.imgCard7);
        playerCard8 = findViewById(R.id.imgCard8);
        playerCard9 = findViewById(R.id.imgCard9);
        playerCards[0]=playerCard1;
        playerCards[1]=playerCard2;
        playerCards[2]=playerCard3;
        playerCards[3]=playerCard4;
        playerCards[4]=playerCard5;
        playerCards[5]=playerCard6;
        playerCards[6]=playerCard7;
        playerCards[7]=playerCard8;
        playerCards[8]=playerCard9;

        dealerCard1 = findViewById(R.id.dealerCard1);
        dealerCard2 = findViewById(R.id.dealerCard2);
        dealerCard3 = findViewById(R.id.dealerCard3);
        dealerCard4 = findViewById(R.id.dealerCard4);
        dealerCard5 = findViewById(R.id.dealerCard5);
        dealerCard6 = findViewById(R.id.dealerCard6);
        dealerCard7 = findViewById(R.id.dealerCard7);
        dealerCard8 = findViewById(R.id.dealerCard8);
        dealerCard9 = findViewById(R.id.dealerCard9);
        dealCards[0]=dealerCard1;
        dealCards[1]=dealerCard2;
        dealCards[2]=dealerCard3;
        dealCards[3]=dealerCard4;
        dealCards[4]=dealerCard5;
        dealCards[5]=dealerCard6;
        dealCards[6]=dealerCard7;
        dealCards[7]=dealerCard8;
        dealCards[8]=dealerCard9;
    }

}