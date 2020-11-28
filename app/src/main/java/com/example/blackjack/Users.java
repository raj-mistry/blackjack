package com.example.blackjack;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Users {
    private String name, username, email, createdOn;
    private double balance;

    public Users(){}

    public Users(String fname, String lname, String username, String email, String createdOn){
        this.name = fname + " " + lname;
        this.username = username;
        this.email = email;
        this.balance = 0;
        this.createdOn = createdOn;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public double getBalance() {
        return balance;
    }
}