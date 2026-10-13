package com.tournament.teamupAndChallenge.service;

import java.util.List;

import com.tournament.teamupAndChallenge.entity.Venue;
import com.tournament.teamupAndChallenge.repository.VenueRepository;

public class VenueService {

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public void createVenue(String name, String address, String city, int capacity) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Venue name is required.");
        }

//        if (capacity <= 0) {  //already capacity validated in Main
//            throw new IllegalArgumentException("Venue capacity must be greater than zero.");
//        }

        Venue venue = new Venue(name, address, city, capacity);

         venueRepository.save(venue);
    }

    public Venue getVenue(Long id) {
        return venueRepository.findById(id);
    }

    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    public Venue updateVenue(Venue venue) {
        return venueRepository.update(venue);
    }

    public void deleteVenue(Long id) {
        venueRepository.delete(id);
    }
}