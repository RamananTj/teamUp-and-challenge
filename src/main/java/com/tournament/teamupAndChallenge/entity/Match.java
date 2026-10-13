package com.tournament.teamupAndChallenge.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    private LocalDate matchDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private String matchType; // CASUAL, COMPETITIVE, TOURNAMENT

    private String status; // OPEN, FULL, SCHEDULED, COMPLETED, CANCELLED

    public Match() {
    }

    public Match(String title, Sport sport, Venue venue,
                 User organizer, LocalDate matchDate,
                 LocalTime startTime, LocalTime endTime,
                 String matchType) {
        this.title = title;
        this.sport = sport;
        this.venue = venue;
        this.organizer = organizer;
        this.matchDate = matchDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.matchType = matchType;
        this.status = "OPEN";
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public LocalDate getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDate matchDate) {
        this.matchDate = matchDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
