package com.tournament.teamupAndChallenge.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "sports")
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String type; // INDIVIDUAL / TEAM

    private Integer minimumPlayers;		//Why Integer instead of int? Integer allows null.

    private Integer maximumPlayers;
    
    @OneToMany(mappedBy = "sport")
    private Set<PlayerSport> playerSports  = new HashSet<>();
    
    
 // Constructors

    public Sport() {
    }

    public Sport(String name, String type,
                 Integer minimumPlayers,
                 Integer maximumPlayers) {
        this.name = name;
        this.type = type;
        this.minimumPlayers = minimumPlayers;
        this.maximumPlayers = maximumPlayers;
    }

    
    // getters and setters
	public Long getId() {
		return id;
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getMinimumPlayers() {
		return minimumPlayers;
	}

	public void setMinimumPlayers(Integer minimumPlayers) {
		this.minimumPlayers = minimumPlayers;
	}

	public Integer getMaximumPlayers() {
		return maximumPlayers;
	}

	public void setMaximumPlayers(Integer maximumPlayers) {
		this.maximumPlayers = maximumPlayers;
	}

	public Set<PlayerSport> getPlayerSports() {
        return playerSports;
    }

    public void setPlayerSports(Set<PlayerSport> playerSports) {
        this.playerSports = playerSports;
    }
    
}