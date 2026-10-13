package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.Team;

public class TeamRepositoryImpl
        extends GenericRepositoryImpl<Team, Long>
        implements TeamRepository {

    public TeamRepositoryImpl() {
        super(Team.class);
    }
}