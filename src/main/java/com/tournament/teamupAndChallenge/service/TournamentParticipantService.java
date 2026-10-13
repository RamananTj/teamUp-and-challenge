
package com.tournament.teamupAndChallenge.service;

import java.util.List;
import java.util.Set;

import com.tournament.teamupAndChallenge.entity.Player;
import com.tournament.teamupAndChallenge.entity.Sport;
import com.tournament.teamupAndChallenge.entity.Team;
import com.tournament.teamupAndChallenge.entity.Tournament;
import com.tournament.teamupAndChallenge.entity.TournamentParticipant;
import com.tournament.teamupAndChallenge.repository.PlayerRepository;
import com.tournament.teamupAndChallenge.repository.PlayerSportRepository;
import com.tournament.teamupAndChallenge.repository.TeamRepository;
import com.tournament.teamupAndChallenge.repository.TournamentParticipantRepository;
import com.tournament.teamupAndChallenge.repository.TournamentRepository;

public class TournamentParticipantService {

    private static final Set<String> STATUSES = Set.of("REGISTERED", "CONFIRMED", "WITHDRAWN", "DISQUALIFIED");

    private final TournamentParticipantRepository participantRepository;
    private final TournamentRepository tournamentRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerSportRepository playerSportRepository;

    public TournamentParticipantService(
            TournamentParticipantRepository participantRepository,
            TournamentRepository tournamentRepository,
            PlayerRepository playerRepository,
            TeamRepository teamRepository,
            PlayerSportRepository playerSportRepository) {
        this.participantRepository = participantRepository;
        this.tournamentRepository = tournamentRepository;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.playerSportRepository = playerSportRepository;
    }

    public void registerPlayer(Long tournamentId, Long playerId) {
        Tournament tournament = requireTournament(tournamentId);
        Player player = playerRepository.findById(playerId);

        if (player == null) {
            throw new IllegalArgumentException("Player not found.");
        }

        boolean playsSport = playerSportRepository
                .findBySportId(tournament.getSport().getId())
                .stream()
                .anyMatch(p -> playerId.equals(p.getId()));

        if (!playsSport) {
            throw new IllegalArgumentException("Player is not registered for this tournament's sport.");
        }

        checkCapacity(tournament); //checking maximum reg got finished or not

        boolean duplicate = participantRepository.findAll().stream()
                .anyMatch(p -> p.getTournament() != null
                        && tournamentId.equals(p.getTournament().getId())
                        && p.getPlayer() != null
                        && playerId.equals(p.getPlayer().getId()));

        if (duplicate) {
            throw new IllegalStateException("Player is already registered for this tournament.");
        }

        participantRepository.save(new TournamentParticipant(tournament, player));
    }

    public void registerTeam(Long tournamentId, Long teamId) {
        Tournament tournament = requireTournament(tournamentId);
        Team team = teamRepository.findById(teamId);

        if (team == null) {
            throw new IllegalArgumentException("Team not found.");
        }

        if (team.getSport() == null
                || !tournament.getSport().getId()
                        .equals(team.getSport().getId())) {
            throw new IllegalArgumentException("Team's sport does not match the tournament's sport.");
        }

        checkCapacity(tournament); 	//checking maximum reg got finished or not

        boolean duplicate = participantRepository.findAll().stream()
                .anyMatch(p -> p.getTournament() != null
                        && tournamentId.equals(p.getTournament().getId())
                        && p.getTeam() != null
                        && teamId.equals(p.getTeam().getId()));

        if (duplicate) {
            throw new IllegalStateException("Team is already registered for this tournament.");
        }

        participantRepository.save(new TournamentParticipant(tournament, team));
    }

    public TournamentParticipant getParticipant(Long id) {
        return requireParticipant(id);
    }

    public List<TournamentParticipant> getAllParticipants() {
        return participantRepository.findAll();
    }

    public TournamentParticipant updateParticipant(TournamentParticipant participant) {

        if (participant == null || participant.getId() == null) {
            throw new IllegalArgumentException("Participant and participant ID are required.");
        }

        requireParticipant(participant.getId());

        if (participant.getTournament() == null) {
            throw new IllegalArgumentException("Tournament is required.");
        }

        Tournament tournament = requireTournament(
                participant.getTournament().getId());

        boolean hasPlayer = participant.getPlayer() != null;
        boolean hasTeam = participant.getTeam() != null;

        if (hasPlayer == hasTeam) {
            throw new IllegalArgumentException("Specify exactly one player or one team.");
        }

        if (hasPlayer) {
            Long playerId = participant.getPlayer().getId();
            Player player = playerRepository.findById(playerId);

            if (player == null) {
                throw new IllegalArgumentException("Player not found.");
            }

            boolean playsSport = playerSportRepository
                    .findBySportId(tournament.getSport().getId())
                    .stream()
                    .anyMatch(p -> playerId.equals(p.getId()));

            if (!playsSport) {
                throw new IllegalArgumentException("Player is not registered for this tournament's sport.");
            }
        } else {
            Team team = teamRepository.findById(
                    participant.getTeam().getId());

            if (team == null || team.getSport() == null
                    || !tournament.getSport().getId()
                            .equals(team.getSport().getId())) {
                throw new IllegalArgumentException("Valid team for this tournament's sport is required.");
            }
        }

        String status = participant.getStatus();
        if (status == null
                || !STATUSES.contains(status.trim().toUpperCase())) {
            throw new IllegalArgumentException("Invalid tournament participant status.");
        }

        participant.setStatus(status.trim().toUpperCase());

        boolean duplicate = participantRepository.findAll().stream()
                .anyMatch(p -> !participant.getId().equals(p.getId())
                        && p.getTournament() != null
                        && tournament.getId().equals(p.getTournament().getId())
                        && ((hasPlayer && p.getPlayer() != null
                                && participant.getPlayer().getId()
                                        .equals(p.getPlayer().getId()))
                            || (hasTeam && p.getTeam() != null
                                && participant.getTeam().getId()
                                        .equals(p.getTeam().getId()))));

        if (duplicate) {
            throw new IllegalStateException("This participant is already registered.");
        }

        return participantRepository.update(participant);
    }

    public void deleteParticipant(Long id) {
        requireParticipant(id);
        participantRepository.delete(id);
    }

    private void checkCapacity(Tournament tournament) {
        if (tournament.getMaximumParticipants() == null) {
            return;
        }

        long count = participantRepository.findAll().stream()
                .filter(p -> p.getTournament() != null
                        && tournament.getId().equals(
                                p.getTournament().getId()))
                .filter(p -> !"WITHDRAWN".equalsIgnoreCase(p.getStatus())
                        && !"DISQUALIFIED".equalsIgnoreCase(p.getStatus()))
                .count();

        if (count >= tournament.getMaximumParticipants()) {
            throw new IllegalStateException("Tournament has reached its participant limit.");
        }
    }

    private Tournament requireTournament(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "Tournament ID is required.");
        }

        Tournament tournament = tournamentRepository.findById(id);

        if (tournament == null) {
            throw new IllegalArgumentException("Tournament not found.");
        }

        return tournament;
    }

    private TournamentParticipant requireParticipant(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(
                    "Participant ID is required.");
        }

        TournamentParticipant participant =
                participantRepository.findById(id);

        if (participant == null) {
            throw new IllegalArgumentException(
                    "Tournament participant not found.");
        }

        return participant;
    }
}
