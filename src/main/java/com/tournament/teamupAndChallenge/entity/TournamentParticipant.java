package com.tournament.teamupAndChallenge.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "tournament_participants",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_tournament_player",
            columnNames = {"tournament_id", "player_id"}
        ),
        @UniqueConstraint(
            name = "uk_tournament_team",
            columnNames = {"tournament_id", "team_id"}
        )
    }
)
public class TournamentParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private LocalDateTime registeredAt;

    @Column(nullable = false)
    private String status = "REGISTERED";

    private Integer seedNumber;	//Seed 1 is the top-ranked participant. Seed 2 is the second-ranked participant. null means the participant has not been seeded. Higher seeds are typically placed in the tournament bracket to reduce the chance of top-ranked participants meeting in early rounds.

    public TournamentParticipant() {
    }

    public TournamentParticipant(Tournament tournament, Player player) {
        this.tournament = tournament;
        this.player = player;
        this.team = null;
        this.registeredAt = LocalDateTime.now();
        this.status = "REGISTERED";
    }

    public TournamentParticipant(Tournament tournament, Team team) {
        this.tournament = tournament;
        this.team = team;
        this.player = null;
        this.registeredAt = LocalDateTime.now();
        this.status = "REGISTERED";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
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

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSeedNumber() {
        return seedNumber;
    }

    public void setSeedNumber(Integer seedNumber) {
        this.seedNumber = seedNumber;
    }
}
