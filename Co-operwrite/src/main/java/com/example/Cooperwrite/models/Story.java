package com.example.Cooperwrite.models;

import com.example.Cooperwrite.models.data.ContributionDao;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.util.List;

@Entity
public class Story {
    @Id
    @GeneratedValue
    private int id;

    private String title;

    @Transient
    private ContributionDao contributionDao;

    @Transient
    private List<Contribution> contributions;

    @Max(20)
    private int turns;

    @Min(0)
    @Max(5)
    private int turn;

    private boolean active;

    public Story(){}

    public Story(String title, int turns, int currentTurn, boolean active){
        this.title = (title.length() < 1) ? "Untitled" : title;
        this.turns = (turns < 3) ? 3 : turns;
        this.active = active;
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

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    public int getTurn(){
        return turn;
    }

    public void setTurn(int turn){
        this.turn = turn;
    }

    public List<Contribution> getContributions(){
        for(Contribution c : contributionDao.findByStoryIdOrderByCardinalityAsc(this.id)){
            contributions.add(c);
        }
        return contributions;
    }

    public boolean getActive(){
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

}
