package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.PlayerSport;
import java.util.List;

public interface PlayerSportRepository
        extends GenericRepository<PlayerSport, Long> {

    List<PlayerSport> findByPlayerId(Long playerId);

    List<PlayerSport> findBySportId(Long sportId);

    boolean existsByPlayerIdAndSportId(
        Long playerId, Long sportId
    );
}