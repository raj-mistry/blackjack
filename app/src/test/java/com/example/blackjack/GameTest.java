package com.example.blackjack;

import junit.framework.TestCase;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GameTest extends TestCase {
    protected int[] Deck = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52};

    //Methods to test modified to allow for testing

    //1 card
    //2 playerbust
    //3 dealerbust
    public boolean setUserBust(int userTotal){
        if (userTotal>21){
            return true;
        }
        return false;
    }

    public boolean setDealerBust(int dealerTotal){
        if (dealerTotal>21){
            return true;
        }
        return false;
    }

    public ArrayList<Integer> hitMe(int[] Deck, ArrayList<Integer> userArray, int index){
        int currentCard = getCard(Deck,index);
        userArray.add(currentCard);
        return userArray;
    }

    public int getCard(int[] Deck,int  index){
        int currentCard = ((Deck[index]-1) % 13)+1;

        return currentCard;
    }

    public int getSuit(int[] Deck,int  index){
        int findSuit = (Deck[index]-1)/13;
        return findSuit;
    }
    //5 getnew list

    public void testFindCard() {
        assertThat(getCard(Deck, 5),is(equalTo(6)));
        assertThat(getCard(Deck, 26),is(equalTo(1)));
    }
    public void testFindSuit() {
        assertThat(getSuit(Deck, 12),is(equalTo(0)));
        assertThat(getSuit(Deck, 25),is(equalTo(1)));
        assertThat(getSuit(Deck, 38),is(equalTo(2)));
        assertThat(getSuit(Deck, 51),is(equalTo(3)));
    }

    public void testUserBust(){
        assertThat(setUserBust(20),is(false));
        assertThat(setUserBust(21),is(false));
        assertThat(setUserBust(22),is(true));
    }
    public void testDealerBust(){
        assertThat(setDealerBust(20),is(false));
        assertThat(setDealerBust(21),is(false));
        assertThat(setDealerBust(22),is(true));
    }

    public void testHitMe(){
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList = hitMe(Deck, arrayList,0);
        arrayList = hitMe(Deck, arrayList,1);
        arrayList = hitMe(Deck, arrayList,2);

        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        expected.add(3);
        assertThat(arrayList,is(equalTo(expected)));
    }
}