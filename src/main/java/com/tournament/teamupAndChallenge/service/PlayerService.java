package com.tournament.teamupAndChallenge.service;

import com.tournament.teamupAndChallenge.entity.Player;
import com.tournament.teamupAndChallenge.repository.PlayerRepository;

import java.util.List;

public class PlayerService {

    private final PlayerRepository repository;

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    public void createPlayer(Player player) {
        if (player == null || player.getName() == null
                || player.getName().isBlank()) {
            throw new IllegalArgumentException(
                "Player name is required"
            );
        }

        repository.save(player);
    }

    public Player getPlayer(Long id) {
        return repository.findById(id);
    }

    public List<Player> getAllPlayers() {
        return repository.findAll();
    }

    public List<Player> getPlayersByCity(String city) {
        return repository.findByCity(city);
    }

    public Player updatePlayer(Player player) {
        return repository.update(player);
    }

    public void deletePlayer(Long id) {
        repository.delete(id);
    }
}