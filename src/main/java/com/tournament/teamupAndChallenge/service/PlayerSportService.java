package com.tournament.teamupAndChallenge.service;

import com.tournament.teamupAndChallenge.entity.Player;
import com.tournament.teamupAndChallenge.entity.Sport;
import com.tournament.teamupAndChallenge.entity.PlayerSport;
import com.tournament.teamupAndChallenge.repository.PlayerRepository;
import com.tournament.teamupAndChallenge.repository.SportRepository;
import com.tournament.teamupAndChallenge.repository.PlayerSportRepository;

import java.util.List;

public class PlayerSportService {

    private final PlayerRepository playerRepository;
    private final SportRepository sportRepository;
    private final PlayerSportRepository playerSportRepository;

    public PlayerSportService(
            PlayerRepository playerRepository,
            SportRepository sportRepository,
            PlayerSportRepository playerSportRepository) {

        this.playerRepository = playerRepository;
        this.sportRepository = sportRepository;
        this.playerSportRepository = playerSportRepository;
    }

    public void registerSport(
            Long playerId, Long sportId, String skillLevel) {

        Player player = playerRepository.findById(playerId);
        Sport sport = sportRepository.findById(sportId);

        if (player == null) {
            throw new IllegalArgumentException("Player not found");
        }

        if (sport == null) {
            throw new IllegalArgumentException("Sport not found");
        }

        if (playerSportRepository
                .existsByPlayerIdAndSportId(playerId, sportId)) {
            throw new IllegalArgumentException(
                "Player is already registered for this sport"
            );
        }

        PlayerSport playerSport =
            new PlayerSport(player, sport, skillLevel);

        playerSportRepository.save(playerSport);
    }

    public List<PlayerSport> getSportsByPlayer(Long playerId) {
        return playerSportRepository.findByPlayerId(playerId);
    }

    public List<PlayerSport> getPlayersBySport(Long sportId) {
        return playerSportRepository.findBySportId(sportId);
    }

    public List<PlayerSport> getAllRegistrations() {
        return playerSportRepository.findAll();
    }

    public PlayerSport getRegistration(Long id) {
        return playerSportRepository.findById(id);
    }

    public PlayerSport updateRegistration(PlayerSport registration) {
        return playerSportRepository.update(registration);
    }

    public void deleteRegistration(Long id) {
        playerSportRepository.delete(id);
    }
}