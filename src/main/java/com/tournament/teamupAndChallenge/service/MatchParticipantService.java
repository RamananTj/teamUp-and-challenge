package com.tournament.teamupAndChallenge.service;

import java.util.List;

import com.tournament.teamupAndChallenge.entity.Match;
import com.tournament.teamupAndChallenge.entity.MatchParticipant;
import com.tournament.teamupAndChallenge.entity.Player;
import com.tournament.teamupAndChallenge.entity.Team;
import com.tournament.teamupAndChallenge.repository.MatchParticipantRepository;
import com.tournament.teamupAndChallenge.repository.MatchRepository;
import com.tournament.teamupAndChallenge.repository.PlayerRepository;
import com.tournament.teamupAndChallenge.repository.TeamRepository;

public class MatchParticipantService {

    private final MatchParticipantRepository participantRepository;
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public MatchParticipantService(
            MatchParticipantRepository participantRepository,
            MatchRepository matchRepository,
            PlayerRepository playerRepository,
            TeamRepository teamRepository) {

        this.participantRepository = participantRepository;
        this.matchRepository = matchRepository;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    // Individual match: e.g., shuttle singles
    public void addPlayer(Long matchId, Long playerId, String side) {

        Match match = matchRepository.findById(matchId);
        Player player = playerRepository.findById(playerId);

        if (match == null) {
            throw new IllegalArgumentException("Match not found.");
        }

        if (player == null) {
            throw new IllegalArgumentException("Player not found.");
        }

        MatchParticipant participant = new MatchParticipant(match, player, side);

        participantRepository.save(participant);
    }

    // Team match: e.g., football
    public void addTeam(Long matchId, Long teamId, String side) {

        Match match = matchRepository.findById(matchId);
        Team team = teamRepository.findById(teamId);

        if (match == null) {
            throw new IllegalArgumentException("Match not found.");
        }

        if (team == null) {
            throw new IllegalArgumentException("Team not found.");
        }

        if (!match.getSport().getId().equals(team.getSport().getId())) {
            throw new IllegalArgumentException("Team sport must match the match sport.");
        }

        MatchParticipant participant = new MatchParticipant(match, team, side);

        participantRepository.save(participant);
    }

    public MatchParticipant getParticipant(Long id) {
        return participantRepository.findById(id);
    }

    public List<MatchParticipant> getAllParticipants() {
        return participantRepository.findAll();
    }

    public MatchParticipant updateParticipant(
            MatchParticipant participant) {
        return participantRepository.update(participant);
    }

    public void deleteParticipant(Long id) {
        participantRepository.delete(id);
    }
}