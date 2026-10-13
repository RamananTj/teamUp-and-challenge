package com.tournament.teamupAndChallenge.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "player_sports",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"player_id", "sport_id"})  //combination of player id and sport id should be unique
    }
)
public class PlayerSport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne  //Many player_id can present within this table eg: player 7 can be present in multiple rows - but 1 player 7 will be in player_entity
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    private String skillLevel;

    private Integer matchesPlayed;

    private Integer matchesWon;

    private Integer matchesLost;

    private Integer rating;


    // Constructors

    public PlayerSport() {
    }

    public PlayerSport(Player player, Sport sport, String skillLevel) {
        this.player = player;
        this.sport = sport;
        this.skillLevel = skillLevel;
        this.matchesPlayed = 0;
        this.matchesWon = 0;
        this.matchesLost = 0;
        this.rating = 1000;
    }


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public Integer getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(Integer matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public Integer getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(Integer matchesWon) {
        this.matchesWon = matchesWon;
    }

    public Integer getMatchesLost() {
        return matchesLost;
    }

    public void setMatchesLost(Integer matchesLost) {
        this.matchesLost = matchesLost;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}