package com.tournament.teamupAndChallenge.service;

import java.util.List;

import com.tournament.teamupAndChallenge.entity.Player;
import com.tournament.teamupAndChallenge.entity.Sport;
import com.tournament.teamupAndChallenge.entity.Team;
import com.tournament.teamupAndChallenge.repository.TeamRepository;
import com.tournament.teamupAndChallenge.repository.PlayerRepository;
import com.tournament.teamupAndChallenge.repository.SportRepository;

public class TeamService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final SportRepository sportRepository;

    public TeamService(
            TeamRepository teamRepository,
            PlayerRepository playerRepository,
            SportRepository sportRepository) {

        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.sportRepository = sportRepository;
    }

    public void createTeam(
            String name, Long sportId, Long captainId, String city) {

        Sport sport = sportRepository.findById(sportId);
        Player captain = playerRepository.findById(captainId);

        if (sport == null) {
            throw new IllegalArgumentException("Sport not found.");
        }

        if (captain == null) {
            throw new IllegalArgumentException("Captain/player not found.");
        }

        Team team = new Team(name, sport, captain, city);

         teamRepository.save(team);
    }

    public Team getTeam(Long id) {
        return teamRepository.findById(id);
    }    
    
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team updateTeam(Team team) {
        return teamRepository.update(team);
    }

    public void deleteTeam(Long id) {
        teamRepository.delete(id);
    }
}