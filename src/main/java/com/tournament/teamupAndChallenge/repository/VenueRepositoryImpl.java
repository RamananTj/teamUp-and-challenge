package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.Venue;

public class VenueRepositoryImpl
        extends GenericRepositoryImpl<Venue, Long>
        implements VenueRepository {

    public VenueRepositoryImpl() {
        super(Venue.class);
    }
}