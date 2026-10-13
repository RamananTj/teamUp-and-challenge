package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.Match;

public class MatchRepositoryImpl
        extends GenericRepositoryImpl<Match, Long>
        implements MatchRepository {

    public MatchRepositoryImpl() {
        super(Match.class);
    }
}