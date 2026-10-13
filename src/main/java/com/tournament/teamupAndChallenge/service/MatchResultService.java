package com.tournament.teamupAndChallenge.service;

import java.util.List;
import java.util.Set;

import com.tournament.teamupAndChallenge.entity.Match;
import com.tournament.teamupAndChallenge.entity.MatchResult;
import com.tournament.teamupAndChallenge.repository.MatchRepository;
import com.tournament.teamupAndChallenge.repository.MatchResultRepository;

public class MatchResultService {

    private static final Set<String> OUTCOMES = Set.of("HOME_WIN", "AWAY_WIN", "DRAW");

    private final MatchResultRepository resultRepository;
    private final MatchRepository matchRepository;

    public MatchResultService(
            MatchResultRepository resultRepository,
            MatchRepository matchRepository) {
        this.resultRepository = resultRepository;
        this.matchRepository = matchRepository;
    }

    public void recordResult(Long matchId, Integer homeScore,
                             Integer awayScore, String remarks) {

        Match match = matchRepository.findById(matchId);

        if (match == null) {
            throw new IllegalArgumentException("Match not found.");
        }

        validateScores(homeScore, awayScore);

        boolean exists = resultRepository.findAll().stream()
	                		.anyMatch(r -> r.getMatch() != null
                        			  && matchId.equals(r.getMatch().getId()));

        if (exists) {
            throw new IllegalStateException("A result already exists for this match.");
        }

        String outcome = determineOutcome(homeScore, awayScore);

        MatchResult result = new MatchResult(
                match, homeScore, awayScore, outcome, remarks);

        resultRepository.save(result);
    }

    public MatchResult getResult(Long id) {
        return requireResult(id);
    }

    public List<MatchResult> getAllResults() {
        return resultRepository.findAll();
    }

    public MatchResult updateResult(MatchResult result) {
        if (result == null || result.getId() == null) {
            throw new IllegalArgumentException(
                    "Result and result ID are required.");
        }

        requireResult(result.getId());

        if (result.getMatch() == null
                || matchRepository.findById(
                        result.getMatch().getId()) == null) {
            throw new IllegalArgumentException("Valid match is required.");
        }

        validateScores(result.getHomeScore(), result.getAwayScore());

        boolean duplicate = resultRepository.findAll().stream()
                .anyMatch(r -> !result.getId().equals(r.getId())
                        && r.getMatch() != null
                        && result.getMatch().getId()
                                .equals(r.getMatch().getId()));

        if (duplicate) {
            throw new IllegalStateException("A result already exists for this match.");
        }

        result.setOutcome(determineOutcome(
                result.getHomeScore(), result.getAwayScore()));

        return resultRepository.update(result);
    }

    public void deleteResult(Long id) {
        requireResult(id);
        resultRepository.delete(id);
    }

    private void validateScores(Integer homeScore, Integer awayScore) {
        if (homeScore == null || awayScore == null
                || homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores must be non-negative integers.");
        }
    }

    private String determineOutcome(Integer homeScore, Integer awayScore) {
        if (homeScore > awayScore) {
            return "HOME_WIN";
        }

        if (awayScore > homeScore) {
            return "AWAY_WIN";
        }

        return "DRAW";
    }

    private MatchResult requireResult(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Result ID is required.");
        }

        MatchResult result = resultRepository.findById(id);

        if (result == null) {
            throw new IllegalArgumentException("Match result not found.");
        }

        return result;
    }
}
