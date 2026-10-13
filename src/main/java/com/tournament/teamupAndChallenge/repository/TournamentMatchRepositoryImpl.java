package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.TournamentMatch;

public class TournamentMatchRepositoryImpl
        extends GenericRepositoryImpl<TournamentMatch, Long>
        implements TournamentMatchRepository {

    public TournamentMatchRepositoryImpl() {
        super(TournamentMatch.class);
    }
}
