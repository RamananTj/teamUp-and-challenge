package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.TournamentParticipant;

public class TournamentParticipantRepositoryImpl
        extends GenericRepositoryImpl<TournamentParticipant, Long>
        implements TournamentParticipantRepository {

    public TournamentParticipantRepositoryImpl() {
        super(TournamentParticipant.class);
    }
}
