package com.example.Cooperwrite.models;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
public class User {
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min=3, max=15)
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min=6, max=15)
    private String password;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Contribution> contributions;

    private Boolean active;

    public User(){}

    public User(String name, String email, String password, Boolean active) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addContribution(Contribution contribution){
        this.contributions.add(contribution);
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }


}
