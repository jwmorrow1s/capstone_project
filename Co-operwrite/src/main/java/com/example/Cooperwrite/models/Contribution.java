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

    @Size(max = 500)
    private String text;

    private int cardinality;

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

    public Story getStory(){
        return story;
    }

    public void setStory(Story story){
        this.story = story;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public int getCardinality(){
        return cardinality;
    }

    public void setCardinality(int cardinality){
        this.cardinality = cardinality;
    }
}
