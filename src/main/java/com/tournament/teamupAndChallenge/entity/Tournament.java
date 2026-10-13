package com.tournament.teamupAndChallenge.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    private LocalDate registrationStartDate;

    private LocalDate registrationEndDate;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer maximumParticipants;

    @Column(nullable = false)
    private String format = "KNOCKOUT";

    @Column(nullable = false)
    private String status = "UPCOMING";

    @Column(length = 1000)
    private String description;

    public Tournament() {
    }

    public Tournament(String name, Sport sport, User organizer,
                      Venue venue, LocalDate registrationStartDate,
                      LocalDate registrationEndDate,
                      LocalDate startDate, LocalDate endDate,
                      Integer maximumParticipants, String format,
                      String description) {
        this.name = name;
        this.sport = sport;
        this.organizer = organizer;
        this.venue = venue;
        this.registrationStartDate = registrationStartDate;
        this.registrationEndDate = registrationEndDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maximumParticipants = maximumParticipants;
        this.format = format;
        this.description = description;
        this.status = "UPCOMING";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public LocalDate getRegistrationStartDate() {
        return registrationStartDate;
    }

    public void setRegistrationStartDate(LocalDate registrationStartDate) {
        this.registrationStartDate = registrationStartDate;
    }

    public LocalDate getRegistrationEndDate() {
        return registrationEndDate;
    }

    public void setRegistrationEndDate(LocalDate registrationEndDate) {
        this.registrationEndDate = registrationEndDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getMaximumParticipants() {
        return maximumParticipants;
    }

    public void setMaximumParticipants(Integer maximumParticipants) {
        this.maximumParticipants = maximumParticipants;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
