package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.Player;
import java.util.List;

public interface PlayerRepository
        extends GenericRepository<Player, Long> {

    List<Player> findByCity(String city);
}