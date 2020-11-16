package com.example.blackjack;

public class Users {
    public String name, username, email;

    public Users(){}

    public Users(String fname, String lname, String username, String email){
        this.name = fname + " " + lname;
        this.username = username;
        this.email = email;
    }

}
