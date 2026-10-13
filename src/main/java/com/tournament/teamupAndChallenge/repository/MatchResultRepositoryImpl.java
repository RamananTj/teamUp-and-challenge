package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.MatchResult;

public class MatchResultRepositoryImpl
        extends GenericRepositoryImpl<MatchResult, Long>
        implements MatchResultRepository {

    public MatchResultRepositoryImpl() {
        super(MatchResult.class);
    }
}
