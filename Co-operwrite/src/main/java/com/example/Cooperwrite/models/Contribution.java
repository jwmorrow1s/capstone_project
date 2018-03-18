package com.example.Cooperwrite.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//TODO

@Entity
public class Contribution {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    private Story story;

    @ManyToOne
    private User user;

    @Size(max = 160)
    private String text;

    public Contribution(){}

    public Contribution(String text){
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
