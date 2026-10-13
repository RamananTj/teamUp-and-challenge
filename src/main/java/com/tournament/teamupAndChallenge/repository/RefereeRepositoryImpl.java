package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.Referee;

public class RefereeRepositoryImpl
        extends GenericRepositoryImpl<Referee, Long>
        implements RefereeRepository {

    public RefereeRepositoryImpl() {
        super(Referee.class);
    }
}
