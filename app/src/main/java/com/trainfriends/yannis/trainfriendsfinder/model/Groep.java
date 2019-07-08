package com.trainfriends.yannis.trainfriendsfinder.model;

/**
 * Created by yannis on 17/07/2018.
 */

public class Groep {
    public String id;
    public String naam;
    public String code;

    public Groep() {
    }

    public Groep(String id) {
        this.id = id;
    }

    public Groep(String id, String naam, String code) {
        this.id = id;
        this.naam = naam;
        this.code = code;
    }
}
