
package com.tournament.teamupAndChallenge.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.tournament.teamupAndChallenge.entity.Sport;
import com.tournament.teamupAndChallenge.entity.Tournament;
import com.tournament.teamupAndChallenge.entity.User;
import com.tournament.teamupAndChallenge.entity.Venue;
import com.tournament.teamupAndChallenge.repository.SportRepository;
import com.tournament.teamupAndChallenge.repository.TournamentRepository;
import com.tournament.teamupAndChallenge.repository.UserRepository;
import com.tournament.teamupAndChallenge.repository.VenueRepository;

public class TournamentService {

    private static final Set<String> FORMATS = Set.of("KNOCKOUT", "LEAGUE", "ROUND_ROBIN");

    private static final Set<String> STATUSES = Set.of("UPCOMING", "REGISTRATION_OPEN", "ONGOING",
                   										"COMPLETED", "CANCELLED");

    private final TournamentRepository tournamentRepository;
    private final SportRepository sportRepository;
    private final VenueRepository venueRepository;
    private final UserRepository userRepository;

    public TournamentService(
            TournamentRepository tournamentRepository,
            SportRepository sportRepository,
            VenueRepository venueRepository,
            UserRepository userRepository) {
        this.tournamentRepository = tournamentRepository;
        this.sportRepository = sportRepository;
        this.venueRepository = venueRepository;
        this.userRepository = userRepository;
    }

    public void createTournament(
            String name, Long sportId, Long organizerId, Long venueId,
            LocalDate registrationStartDate,
            LocalDate registrationEndDate,
            LocalDate startDate, LocalDate endDate,
            Integer maximumParticipants, String format,
            String description) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tournament name is required.");
        }

        Sport sport = sportRepository.findById(sportId);
        User organizer = userRepository.findById(organizerId);

        if (sport == null) {
            throw new IllegalArgumentException("Sport not found.");
        }

        if (organizer == null) {
            throw new IllegalArgumentException("Organizer not found.");
        }

        Venue venue = null;
        if (venueId != null) {
            venue = venueRepository.findById(venueId);
            if (venue == null) {
                throw new IllegalArgumentException("Venue not found.");
            }
        }

        validateDates(registrationStartDate, registrationEndDate,
                      startDate, endDate);

        if (maximumParticipants == null || maximumParticipants < 2) {
            throw new IllegalArgumentException(
                    "Maximum participants must be at least 2.");
        }

        String normalizedFormat = normalizeFormat(format);

        Tournament tournament = new Tournament(
                name.trim(), sport, organizer, venue,
                registrationStartDate, registrationEndDate,
                startDate, endDate, maximumParticipants,
                normalizedFormat, description);

        tournamentRepository.save(tournament);
    }

    public Tournament getTournament(Long id) {
        return requireTournament(id);
    }

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament updateTournament(Tournament tournament) {
        if (tournament == null || tournament.getId() == null) {
            throw new IllegalArgumentException(
                    "Tournament and tournament ID are required.");
        }

        requireTournament(tournament.getId());

        if (tournament.getName() == null
                || tournament.getName().isBlank()) {
            throw new IllegalArgumentException(
                    "Tournament name is required.");
        }

        if (tournament.getSport() == null
                || sportRepository.findById(
                        tournament.getSport().getId()) == null) {
            throw new IllegalArgumentException("Valid sport is required.");
        }

        if (tournament.getOrganizer() == null
                || userRepository.findById(tournament.getOrganizer().getId()) == null) {
            throw new IllegalArgumentException("Valid organizer is required.");
        }

        if (tournament.getVenue() != null
                && venueRepository.findById(
                        tournament.getVenue().getId()) == null) {
            throw new IllegalArgumentException("Venue not found.");
        }

        validateDates(
                tournament.getRegistrationStartDate(),
                tournament.getRegistrationEndDate(),
                tournament.getStartDate(),
                tournament.getEndDate());

        if (tournament.getMaximumParticipants() == null
                || tournament.getMaximumParticipants() < 2) {
            throw new IllegalArgumentException("Maximum participants must be at least 2.");
        }

        tournament.setName(tournament.getName().trim());
        tournament.setFormat(normalizeFormat(tournament.getFormat()));

        if (tournament.getStatus() == null
                || !STATUSES.contains(
                        tournament.getStatus().trim().toUpperCase())) {
            throw new IllegalArgumentException(
                    "Invalid tournament status.");
        }

        tournament.setStatus(
                tournament.getStatus().trim().toUpperCase());

        return tournamentRepository.update(tournament);
    }

    public void deleteTournament(Long id) {
        requireTournament(id);
        tournamentRepository.delete(id);
    }

    private void validateDates(
            LocalDate registrationStart,
            LocalDate registrationEnd,
            LocalDate startDate,
            LocalDate endDate) {

        if (registrationStart != null && registrationEnd != null
                && registrationStart.isAfter(registrationEnd)) {
            throw new IllegalArgumentException("Registration start must not be after registration end.");
        }

        if (startDate != null && endDate != null
                && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Tournament start must not be after tournament end.");
        }

        if (registrationEnd != null && startDate != null
                && registrationEnd.isAfter(startDate)) {
            throw new IllegalArgumentException("Registration must end on or before the tournament starts.");
        }
    }

    private String normalizeFormat(String format) {
        if (format == null
                || !FORMATS.contains(format.trim().toUpperCase())) {
            throw new IllegalArgumentException("Format must be KNOCKOUT, LEAGUE, or ROUND_ROBIN.");
        }

        return format.trim().toUpperCase();
    }

    private Tournament requireTournament(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Tournament ID is required.");
        }

        Tournament tournament = tournamentRepository.findById(id);

        if (tournament == null) {
            throw new IllegalArgumentException("Tournament not found.");
        }

        return tournament;
    }
}
