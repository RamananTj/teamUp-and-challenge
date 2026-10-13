package com.tournament.teamupAndChallenge.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "tournament_matches",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_tournament_match_number",
            columnNames = {"tournament_id", "match_number"}
        ),
        @UniqueConstraint(
            name = "uk_tournament_match",
            columnNames = {"match_id"}
        )
    }
)
public class TournamentMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @OneToOne
    @JoinColumn(name = "match_id", nullable = false, unique = true)
    private Match match;

    @Column(name = "round_number", nullable = false)
    private Integer roundNumber;

    @Column(name = "match_number", nullable = false)
    private Integer matchNumber;

    private String roundName;

    private String bracketPosition;

    @Column(nullable = false)
    private String status = "SCHEDULED";

    public TournamentMatch() {
    }

    public TournamentMatch(Tournament tournament, Match match,
                           Integer roundNumber, Integer matchNumber,
                           String roundName, String bracketPosition) {
        this.tournament = tournament;
        this.match = match;
        this.roundNumber = roundNumber;
        this.matchNumber = matchNumber;
        this.roundName = roundName;
        this.bracketPosition = bracketPosition;
        this.status = "SCHEDULED";
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

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    public Integer getMatchNumber() {
        return matchNumber;
    }

    public void setMatchNumber(Integer matchNumber) {
        this.matchNumber = matchNumber;
    }

    public String getRoundName() {
        return roundName;
    }

    public void setRoundName(String roundName) {
        this.roundName = roundName;
    }

    public String getBracketPosition() {
        return bracketPosition;
    }

    public void setBracketPosition(String bracketPosition) {
        this.bracketPosition = bracketPosition;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
