package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.Sport;

public interface SportRepository
        extends GenericRepository<Sport, Long> {

    Sport findByName(String name);
}