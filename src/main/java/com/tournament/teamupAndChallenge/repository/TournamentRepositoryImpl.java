package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.Tournament;

public class TournamentRepositoryImpl
        extends GenericRepositoryImpl<Tournament, Long>
        implements TournamentRepository {

    public TournamentRepositoryImpl() {
        super(Tournament.class);
    }
}
