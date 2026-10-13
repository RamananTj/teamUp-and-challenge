
package com.tournament.teamupAndChallenge.service;

import java.util.List;
import java.util.Set;

import com.tournament.teamupAndChallenge.entity.Match;
import com.tournament.teamupAndChallenge.entity.Referee;
import com.tournament.teamupAndChallenge.entity.RefereeAssignment;
import com.tournament.teamupAndChallenge.repository.MatchRepository;
import com.tournament.teamupAndChallenge.repository.RefereeAssignmentRepository;
import com.tournament.teamupAndChallenge.repository.RefereeRepository;

public class RefereeAssignmentService {

    private static final Set<String> ROLES = Set.of(
            "MAIN_REFEREE", "ASSISTANT_REFEREE", "LINE_JUDGE");

    private final RefereeAssignmentRepository assignmentRepository;
    private final RefereeRepository refereeRepository;
    private final MatchRepository matchRepository;

    public RefereeAssignmentService(
            RefereeAssignmentRepository assignmentRepository,
            RefereeRepository refereeRepository,
            MatchRepository matchRepository) {
        this.assignmentRepository = assignmentRepository;
        this.refereeRepository = refereeRepository;
        this.matchRepository = matchRepository;
    }

    public void assignReferee(Long matchId, Long refereeId, String role) {
        Match match = matchRepository.findById(matchId);
        Referee referee = refereeRepository.findById(refereeId);

        if (match == null) {
            throw new IllegalArgumentException("Match not found.");
        }

        if (referee == null) {
            throw new IllegalArgumentException("Referee not found.");
        }

        if (!"ACTIVE".equalsIgnoreCase(referee.getStatus())) {
            throw new IllegalStateException("Referee is not active.");
        }

        String normalizedRole = normalizeRole(role);

        boolean alreadyAssigned = assignmentRepository.findAll().stream()
                .anyMatch(a -> a.getMatch() != null
                        && matchId.equals(a.getMatch().getId()));

        if (alreadyAssigned) {
            throw new IllegalStateException("This match already has a referee assignment.");
        }

        assignmentRepository.save(new RefereeAssignment(match, referee, normalizedRole));
    }

    public RefereeAssignment getAssignment(Long id) {
        return requireAssignment(id);
    }

    public List<RefereeAssignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public RefereeAssignment updateAssignment(RefereeAssignment assignment) {

        if (assignment == null || assignment.getId() == null) {
            throw new IllegalArgumentException("Assignment and assignment ID are required.");
        }

        requireAssignment(assignment.getId());

        if (assignment.getMatch() == null
                || matchRepository.findById(
                        assignment.getMatch().getId()) == null) {
            throw new IllegalArgumentException("Valid match is required.");
        }

        if (assignment.getReferee() == null
                || refereeRepository.findById(assignment.getReferee().getId()) == null) {
            throw new IllegalArgumentException("Valid referee is required.");
        }

        if (!"ACTIVE".equalsIgnoreCase(assignment.getReferee().getStatus())) {
            throw new IllegalStateException("Referee is not active.");
        }

        assignment.setRole(normalizeRole(assignment.getRole()));

        boolean duplicate = assignmentRepository.findAll().stream()		//stream() converts a collection (like a List or Set returned by findAll()) into a Stream of elements, which allows you to process the data sequentially using functional-style operations (like filtering, matching, or mapping) without manually writing a for or while loop.
                .anyMatch(a -> !assignment.getId().equals(a.getId())
                        && a.getMatch() != null
                        && assignment.getMatch().getId()
                                .equals(a.getMatch().getId()));

        if (duplicate) {
            throw new IllegalStateException("This match already has a referee assignment.");
        }

        return assignmentRepository.update(assignment);
    }

    public void deleteAssignment(Long id) {
        requireAssignment(id);
        assignmentRepository.delete(id);
    }

    private String normalizeRole(String role) {
        if (role == null || !ROLES.contains(role.trim().toUpperCase())) {
            throw new IllegalArgumentException(
                    "Role must be MAIN_REFEREE, ASSISTANT_REFEREE, or LINE_JUDGE.");
        }

        return role.trim().toUpperCase();
    }

    private RefereeAssignment requireAssignment(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Assignment ID is required.");
        }

        RefereeAssignment assignment = assignmentRepository.findById(id);

        if (assignment == null) {
            throw new IllegalArgumentException("Referee assignment not found.");
        }

        return assignment;
    }
}
