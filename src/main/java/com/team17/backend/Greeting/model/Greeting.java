package com.team17.backend.Greeting.model;

import jakarta.persistence.*;

@Entity
@Table(name = "greeting")
public class Greeting {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public long id;

    private String message;

    public Greeting(String message) {
        this.message = message;
    }

    public Greeting() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
