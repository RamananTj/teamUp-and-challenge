package com.tournament.teamupAndChallenge.service;

import java.util.List;
import java.util.Set;

import com.tournament.teamupAndChallenge.entity.Match;
import com.tournament.teamupAndChallenge.entity.Tournament;
import com.tournament.teamupAndChallenge.entity.TournamentMatch;
import com.tournament.teamupAndChallenge.repository.MatchRepository;
import com.tournament.teamupAndChallenge.repository.TournamentMatchRepository;
import com.tournament.teamupAndChallenge.repository.TournamentRepository;

public class TournamentMatchService {

    private static final Set<String> STATUSES = Set.of("SCHEDULED", "IN_PROGRESS", "COMPLETED", "CANCELLED");

    private final TournamentMatchRepository tournamentMatchRepository;
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;

    public TournamentMatchService(
            TournamentMatchRepository tournamentMatchRepository,
            TournamentRepository tournamentRepository,
            MatchRepository matchRepository) {
        this.tournamentMatchRepository = tournamentMatchRepository;
        this.tournamentRepository = tournamentRepository;
        this.matchRepository = matchRepository;
    }

    public void addMatch(
            Long tournamentId, Long matchId,
            Integer roundNumber, Integer matchNumber,
            String roundName, String bracketPosition) {

        Tournament tournament = requireTournament(tournamentId);
        Match match = matchRepository.findById(matchId);

        if (match == null) {
            throw new IllegalArgumentException("Match not found.");
        }

        validateMatch(tournament, match, roundNumber, matchNumber);

        boolean alreadyLinked = tournamentMatchRepository.findAll().stream()
                .anyMatch(tm -> tm.getMatch() != null
                        && matchId.equals(tm.getMatch().getId()));

        if (alreadyLinked) {
            throw new IllegalStateException("This match already belongs to a tournament.");
        }

        tournamentMatchRepository.save(new TournamentMatch(
					                        tournament, match, roundNumber, matchNumber,
					                        roundName, bracketPosition));
    }

    public TournamentMatch getTournamentMatch(Long id) {
        return requireTournamentMatch(id);
    }

    public List<TournamentMatch> getAllTournamentMatches() {
        return tournamentMatchRepository.findAll();
    }

    public TournamentMatch updateTournamentMatch(
            TournamentMatch tournamentMatch) {

        if (tournamentMatch == null || tournamentMatch.getId() == null) {
            throw new IllegalArgumentException("Tournament match and ID are required.");
        }

        requireTournamentMatch(tournamentMatch.getId());

        if (tournamentMatch.getTournament() == null
                || tournamentMatch.getMatch() == null) {
            throw new IllegalArgumentException("Tournament and match are required.");
        }

        Tournament tournament = requireTournament(tournamentMatch.getTournament().getId());

        Match match = matchRepository.findById(tournamentMatch.getMatch().getId());

        if (match == null) {
            throw new IllegalArgumentException("Match not found.");
        }

        validateMatch(
                tournament, match,
                tournamentMatch.getRoundNumber(),
                tournamentMatch.getMatchNumber());

        boolean duplicate = tournamentMatchRepository.findAll().stream()
                .anyMatch(tm -> !tournamentMatch.getId().equals(tm.getId())
                        && tm.getMatch() != null
                        && match.getId().equals(tm.getMatch().getId()));

        if (duplicate) {
            throw new IllegalStateException("This match already belongs to a tournament.");
        }

        boolean duplicateNumber = tournamentMatchRepository.findAll().stream()
                .anyMatch(tm -> !tournamentMatch.getId().equals(tm.getId())
                        && tm.getTournament() != null
                        && tournament.getId().equals(
                                tm.getTournament().getId())
                        && tournamentMatch.getMatchNumber().equals(
                                tm.getMatchNumber()));

        if (duplicateNumber) {
            throw new IllegalStateException("This match number is already used in this tournament.");
        }

        String status = tournamentMatch.getStatus();
        if (status == null
                || !STATUSES.contains(status.trim().toUpperCase())) {
            throw new IllegalArgumentException("Invalid tournament match status.");
        }

        tournamentMatch.setStatus(status.trim().toUpperCase());

        return tournamentMatchRepository.update(tournamentMatch);
    }

    public void deleteTournamentMatch(Long id) {
        requireTournamentMatch(id);
        tournamentMatchRepository.delete(id);
    }

    private void validateMatch(
            Tournament tournament, Match match,
            Integer roundNumber, Integer matchNumber) {

        if (roundNumber == null || roundNumber < 1) {
            throw new IllegalArgumentException("Round number must be at least 1.");
        }

        if (matchNumber == null || matchNumber < 1) {
            throw new IllegalArgumentException("Match number must be at least 1.");
        }

        if (tournament.getSport() == null || match.getSport() == null
                || !tournament.getSport().getId()
                        .equals(match.getSport().getId())) {
            throw new IllegalArgumentException("Match sport must match tournament sport.");
        }

        boolean duplicateNumber = tournamentMatchRepository.findAll().stream()
                .anyMatch(tm -> tm.getTournament() != null
                        && tm.getMatchNumber() != null
                        && tournament.getId().equals(
                                tm.getTournament().getId())
                        && matchNumber.equals(tm.getMatchNumber()));

        if (duplicateNumber) {
            throw new IllegalStateException("Match number is already used in this tournament.");
        }
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

    private TournamentMatch requireTournamentMatch(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Tournament match ID is required.");
        }

        TournamentMatch tournamentMatch = tournamentMatchRepository.findById(id);

        if (tournamentMatch == null) {
            throw new IllegalArgumentException("Tournament match not found.");
        }

        return tournamentMatch;
    }
}
