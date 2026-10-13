package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.MatchParticipant;

public class MatchParticipantRepositoryImpl
        extends GenericRepositoryImpl<MatchParticipant, Long>
        implements MatchParticipantRepository {

    public MatchParticipantRepositoryImpl() {
        super(MatchParticipant.class);
    }
}