package com.example.blackjack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class Game extends AppCompatActivity {
    protected int[] Deck = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52};
    protected ArrayList<Integer> userArray = new ArrayList<Integer>();
    protected ArrayList<Integer> dealerArray = new ArrayList<Integer>();
    protected int currentIndex = 0; //the card the program retrieves depends on the index. current index ex: Deck[7] will return 6, but when the deck is shuffled, itll return whatever value is there.
    protected int userTotal = 0; //user total is the total value of the pile ex: 5+6=11, loses if userTotal>21

    protected int dealerTotal = 0; //same as user total except this is for the dealer AI
    protected boolean winStatus=false; // true if the player wins, false if player loses.

    protected boolean userBust=false; //true if user bust, player loses
    protected boolean dealerBust=false; //true if dealer bust, player wins, dealer loses.

    // all the text views in the xml,
    // ___Pile is the integer sum of the cards
    // ___Cards is the list of cards
    // gameStatus indicates which player won, if user won or dealer won.
    TextView dealerPile;
    TextView dealerCards;
    TextView userPile;
    TextView userCards;
    TextView gameStatus;

    //buttons self explanatory.
    Button hitMeButton;
    Button stopButton;
    Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        dealerPile = (TextView) findViewById(R.id.dealerPile);
        dealerCards = (TextView) findViewById(R.id.dealerCards);
        userPile = (TextView) findViewById(R.id.userPile);
        userCards = (TextView) findViewById(R.id.userCards);
        hitMeButton = findViewById(R.id.hitMeButton);
        stopButton = findViewById(R.id.stopButton);
        returnButton = findViewById(R.id.returnButton);
        gameStatus = findViewById(R.id.gameStatus);
        returnButton.setEnabled(false);

        Deck = shuffleArray(Deck);

        hitDealer();

        hitMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitUser();
                //hitUser();
                if (userBust==true){
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
                if (dealerBust==true){
                    gameStatus.setText("DEALER BUST, PLAYER VICTORY!");
                    winStatus=true;
                }
                else{
                    gameStatus.setText("PLAYER DEFEAT!");
                }
                stop();
            }
        });






        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //define what happens on return
                reset();
            }
        });




    }
    public void reset(){
        userArray = new ArrayList<Integer>();
        dealerArray = new ArrayList<Integer>();
        currentIndex = 0;
        userTotal = 0;
        dealerTotal = 0;
        winStatus=false;
        userBust=false;
        dealerBust=false;

        Deck = shuffleArray(Deck);
        hitDealer();
        hitUser();

        gameStatus.setText("");

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
        int currentCard = ((Deck[currentIndex]-1) % 12)+1;
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
        int currentCard = ((Deck[currentIndex]-1) % 12)+1;
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

}