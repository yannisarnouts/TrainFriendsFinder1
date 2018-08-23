package com.example.yannis.trainfriendsfinder.model;

/**
 * Created by yannis on 23/08/2018.
 */

public class Melding {
    String id;
    String from;
    String message;
    String to;

    public Melding(String id, String from, String message, String to) {
        this.id = id;
        this.from = from;
        this.message = message;
        this.to = to;
    }

    public String getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public String getTo() {
        return to;
    }
}
