package com.tournament.teamupAndChallenge.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    @ManyToOne
    @JoinColumn(name = "captain_id", nullable = false)
    private Player captain;

    private String city;

    public Team() {
    }

    public Team(String name, Sport sport, Player captain, String city) {
        this.name = name;
        this.sport = sport;
        this.captain = captain;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Player getCaptain() {
        return captain;
    }

    public void setCaptain(Player captain) {
        this.captain = captain;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

