package com.tournament.teamupAndChallenge.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String city;

    private String phone;

    @OneToMany(mappedBy = "player")
    private Set<PlayerSport> playerSports = new HashSet<>();


    // Constructors

    public Player() {
    }

    public Player(String name, String city, String phone) {
        this.name = name;
        this.city = city;
        this.phone = phone;
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<PlayerSport> getPlayerSports() {
        return playerSports;
    }

    public void setPlayerSports(Set<PlayerSport> playerSports) {
        this.playerSports = playerSports;
    }
}