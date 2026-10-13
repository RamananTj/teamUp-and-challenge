package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.User;

public interface UserRepository
        extends GenericRepository<User, Long> {

    User findByEmail(String email);
}