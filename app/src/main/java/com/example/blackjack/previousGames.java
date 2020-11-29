package com.example.blackjack;

public class previousGames {

    private String Ended;
    private boolean playerWon;
    private double betValue, dealerHand, playerHand;

    private previousGames(){}

    private previousGames(String Ended, boolean playerWon, double betValue, double dealerHand, double playerHand){
        this.Ended = Ended;
        this.playerHand = playerHand;
        this.playerWon = playerWon;
        this.betValue = betValue;
        this.dealerHand = dealerHand;
    }

    public String getEnded() {
        return Ended;
    }

    public void setEnded(String ended) {
        Ended = ended;
    }

    public boolean isPlayerWon() {
        return playerWon;
    }

    public void setPlayerWon(boolean playerWon) {
        this.playerWon = playerWon;
    }

    public double getBetValue() {
        return betValue;
    }

    public void setBetValue(double betValue) {
        this.betValue = betValue;
    }

    public double getDealerHand() {
        return dealerHand;
    }

    public void setDealerHand(double dealerHand) {
        this.dealerHand = dealerHand;
    }

    public double getPlayerHand() {
        return playerHand;
    }

    public void setPlayerHand(double playerHand) {
        this.playerHand = playerHand;
    }
}
