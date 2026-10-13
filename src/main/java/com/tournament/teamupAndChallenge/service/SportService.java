package com.tournament.teamupAndChallenge.service;

import com.tournament.teamupAndChallenge.entity.Sport;
import com.tournament.teamupAndChallenge.repository.SportRepository;

import java.util.List;

public class SportService {

    private final SportRepository repository;

    public SportService(SportRepository repository) {
        this.repository = repository;
    }

    public void createSport(Sport sport) {
        if (sport == null || sport.getName() == null
                || sport.getName().isBlank()) {
            throw new IllegalArgumentException(
                "Sport name is required"
            );
        }

        if (repository.findByName(sport.getName()) != null) {
            throw new IllegalArgumentException(
                "Sport already exists"
            );
        }

        repository.save(sport);
    }

    public Sport getSport(Long id) {
        return repository.findById(id);
    }

    public Sport getSportByName(String name) {
        return repository.findByName(name);
    }

    public List<Sport> getAllSports() {
        return repository.findAll();
    }

    public Sport updateSport(Sport sport) {
        return repository.update(sport);
    }

    public void deleteSport(Long id) {
        repository.delete(id);
    }
}