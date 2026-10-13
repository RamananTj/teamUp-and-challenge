
package com.tournament.teamupAndChallenge.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "referee_assignments")
public class RefereeAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "match_id", nullable = false, unique = true)
    private Match match;

    @ManyToOne
    @JoinColumn(name = "referee_id", nullable = false)
    private Referee referee;

    @Column(nullable = false)
    private String role = "MAIN_REFEREE"; //if you want to add more than 1 referees to a match, change the mapping of match to @ManyToOne and remove its unique constraint

    private LocalDateTime assignedAt;

    public RefereeAssignment() {
    }

    public RefereeAssignment(Match match, Referee referee, String role) {
        this.match = match;
        this.referee = referee;
        this.role = role;
        this.assignedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Referee getReferee() {
        return referee;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
}
