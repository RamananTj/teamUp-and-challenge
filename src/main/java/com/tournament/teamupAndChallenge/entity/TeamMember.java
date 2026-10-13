package com.tournament.teamupAndChallenge.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
    name = "team_members",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"team_id", "player_id"})
    }
)
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    private String role; // PLAYER, CAPTAIN, VICE_CAPTAIN

    private LocalDate joinedAt;

    public TeamMember() {
    }

    public TeamMember(Team team, Player player, String role) {
        this.team = team;
        this.player = player;
        this.role = role;
        this.joinedAt = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDate getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDate joinedAt) {
        this.joinedAt = joinedAt;
    }
}
