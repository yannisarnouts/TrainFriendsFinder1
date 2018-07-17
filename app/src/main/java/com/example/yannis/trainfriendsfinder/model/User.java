package com.example.yannis.trainfriendsfinder.model;

/**
 * Created by yannis on 17/06/2018.
 */

public class User {
    public String naam, email;
    Groep groep;

    public User(){

    }

    public User(String naam, String email) {
        this.naam = naam;
        this.email = email;
    }

    public User(String naam, String email, Groep groep) {
        this.naam = naam;
        this.email = email;
        this.groep = groep;
    }

    public User(String naam, Groep groep) {
        this.naam = naam;
        this.groep = groep;
    }

    public Groep getGroep() {
        return groep;
    }
}
