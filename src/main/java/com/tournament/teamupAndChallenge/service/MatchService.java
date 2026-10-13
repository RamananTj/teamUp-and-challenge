package com.tournament.teamupAndChallenge.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.tournament.teamupAndChallenge.entity.Match;
import com.tournament.teamupAndChallenge.entity.Sport;
import com.tournament.teamupAndChallenge.entity.User;
import com.tournament.teamupAndChallenge.entity.Venue;
import com.tournament.teamupAndChallenge.repository.MatchRepository;
import com.tournament.teamupAndChallenge.repository.SportRepository;
import com.tournament.teamupAndChallenge.repository.UserRepository;
import com.tournament.teamupAndChallenge.repository.VenueRepository;

public class MatchService {

    private final MatchRepository matchRepository;
    private final SportRepository sportRepository;
    private final VenueRepository venueRepository;
    private final UserRepository userRepository;

    public MatchService(
            MatchRepository matchRepository,
            SportRepository sportRepository,
            VenueRepository venueRepository,
            UserRepository userRepository) {

        this.matchRepository = matchRepository;
        this.sportRepository = sportRepository;
        this.venueRepository = venueRepository;
        this.userRepository = userRepository;
    }

    //create match
    public void scheduleMatch(
            String title,
            Long sportId,
            Long venueId,
            Long organizerId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            String matchType) {

        Sport sport = sportRepository.findById(sportId);
        User organizer = userRepository.findById(organizerId);

        if (sport == null) {
            throw new IllegalArgumentException("Sport not found.");
        }

        if (organizer == null) {
            throw new IllegalArgumentException("Organizer not found.");
        }

        // Venue is optional in the entity.
        Venue venue = null;

        if (venueId != null) {
            venue = venueRepository.findById(venueId);

            if (venue == null) {
                throw new IllegalArgumentException("Venue not found.");
            }

            if (Boolean.FALSE.equals(venue.getAvailable())) {
                throw new IllegalArgumentException("Venue is unavailable.");
            }
        }

        if (date == null || startTime == null || endTime == null) {
            throw new IllegalArgumentException("Match date and times are required.");
        }

        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException("End time must be later than start time.");
        }

        Match match = new Match(title, sport, venue, organizer, date, startTime, endTime, matchType);

         matchRepository.save(match);
    }

    public Match getMatch(Long id) {
        return matchRepository.findById(id);
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public Match updateMatch(Match match) {
        return matchRepository.update(match);
    }

    public void deleteMatch(Long id) {
        matchRepository.delete(id);
    }
}