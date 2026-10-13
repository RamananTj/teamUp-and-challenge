package com.tournament.teamupAndChallenge.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "referees")
public class Referee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "player_id", nullable = false, unique = true)
    private Player player;

    @Column(nullable = false)
    private String certificationLevel;

    private Integer experienceYears;

    @Column(nullable = false)
    private String status = "ACTIVE";

    public Referee() {
    }

    public Referee(Player player, String certificationLevel,
                   Integer experienceYears) {
        this.player = player;
        this.certificationLevel = certificationLevel;
        this.experienceYears = experienceYears;
        this.status = "ACTIVE";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getCertificationLevel() {
        return certificationLevel;
    }

    public void setCertificationLevel(String certificationLevel) {
        this.certificationLevel = certificationLevel;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
