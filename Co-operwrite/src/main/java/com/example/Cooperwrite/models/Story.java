package com.example.Cooperwrite.models;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
//TODO

@Entity
public class Story {
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 3, max = 50)
    private String title;

    @OneToMany
    @JoinColumn(name = "story_id")
    private List<Contribution> contributions;

    @Min(3)
    @Max(20)
    private int turns;

    public Story(){}

    public Story(String title, int turns){
        this.title = title;
        this.turns = (turns < 3) ? 3 : turns;
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

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    public List<Contribution> getContributions(){
        return contributions;
    }

}
