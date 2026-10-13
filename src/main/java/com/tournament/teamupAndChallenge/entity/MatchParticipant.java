package com.tournament.teamupAndChallenge.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "match_participants")
public class MatchParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;
    
 // For individual sports, e.g., shuttle singles. assign Null value for player_id - for team match
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

 // For team sports, e.g., football. NULL - when individual match
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private String side; // HOME or AWAY

    private String status; // PENDING, ACCEPTED, REJECTED

    public MatchParticipant() {
    }

    // For an individual player
    public MatchParticipant(Match match, Player player, String side) {
        this.match = match;
        this.player = player;
        this.side = side;
        this.status = "ACCEPTED";
    }

    // For a team
    public MatchParticipant(Match match, Team team, String side) {
        this.match = match;
        this.team = team;
        this.side = side;
        this.status = "ACCEPTED";
    }

    public Long getId() {
        return id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
