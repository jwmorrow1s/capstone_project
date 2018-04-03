package com.example.Cooperwrite.models;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Story {
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 3, max = 50)
    private String title;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)//???
    @JoinColumn(name = "story_id")
    private List<Contribution> contributions = new ArrayList<>();

    @Min(3)
    @Max(20)
    private int turns;

    private int turn;

    private boolean active;

    public Story(){}

    public Story(String title, int turns){
        this.title = title;
        this.turns = (turn < 3) ? 3 : turns;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addContribution(Contribution contribution){
        this.contributions.add(contribution);
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public List<Contribution> getContributions(){
        return contributions;
    }

    public void setContributions(List<Contribution> contributions)
    {
        this.contributions = contributions;
    }

    public boolean isActive(){
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public int getTurns(){
        return turns;
    }

    public void setTurns(int turns){
        this.turns = turns;
    }
}
