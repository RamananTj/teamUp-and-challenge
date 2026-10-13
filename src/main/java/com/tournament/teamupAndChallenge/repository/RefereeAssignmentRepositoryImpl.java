package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.RefereeAssignment;

public class RefereeAssignmentRepositoryImpl
        extends GenericRepositoryImpl<RefereeAssignment, Long>
        implements RefereeAssignmentRepository {

    public RefereeAssignmentRepositoryImpl() {
        super(RefereeAssignment.class);
    }
}
