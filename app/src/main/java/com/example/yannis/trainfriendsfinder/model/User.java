package com.example.yannis.trainfriendsfinder.model;

/**
 * Created by yannis on 17/06/2018.
 */

public class User {
    public String naam, email, groep;

    public User(){

    }

    public User(String naam, String email, String groep) {
        this.naam = naam;
        this.email = email;
        this.groep = groep;
    }
}
