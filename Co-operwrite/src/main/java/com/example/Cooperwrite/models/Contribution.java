package com.example.Cooperwrite.models;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

//TODO

@Entity
public class Contribution {
    @Id
    @GeneratedValue
    private int id;

    private int storyId;

    @Min(0)
    @Max(5)
    private int cardinality;

    @ManyToOne
    private User user;

    @Size(max = 600)
    private String text;

    public Contribution(){}

    public Contribution(String text, int storyId, int cardinality){
        this.text = text;
        this.storyId = storyId;
        this.cardinality = cardinality;
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

    public void setStoryId(int storyId){
        this.storyId = storyId;
    }

    public int getStoryId(){
        return storyId;
    }

    public int getCardinality(){
        return cardinality;
    }

    public void setCardinality(int cardinality){
        this.cardinality = cardinality;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }
}
