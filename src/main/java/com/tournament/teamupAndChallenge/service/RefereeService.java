package com.tournament.teamupAndChallenge.service;

import java.util.List;

import com.tournament.teamupAndChallenge.entity.Player;
import com.tournament.teamupAndChallenge.entity.Referee;
import com.tournament.teamupAndChallenge.repository.PlayerRepository;
import com.tournament.teamupAndChallenge.repository.RefereeRepository;

public class RefereeService {

    private final RefereeRepository refereeRepository;
    private final PlayerRepository playerRepository;

    public RefereeService(
            RefereeRepository refereeRepository,
            PlayerRepository playerRepository) {
        this.refereeRepository = refereeRepository;
        this.playerRepository = playerRepository;
    }

    public void createReferee(Long playerId, String certificationLevel, Integer experienceYears) {

        Player player = playerRepository.findById(playerId);

        if (player == null) {
            throw new IllegalArgumentException("Player not found.");
        }

        if (certificationLevel == null || certificationLevel.isBlank()) {
            throw new IllegalArgumentException("Certification level is required.");
        }

        if (experienceYears == null || experienceYears < 0) {
            throw new IllegalArgumentException("Experience years cannot be negative.");
        }

        boolean alreadyRegistered = refereeRepository.findAll().stream()
                .anyMatch(r -> r.getPlayer() != null
                        && playerId.equals(r.getPlayer().getId()));

        if (alreadyRegistered) {
            throw new IllegalStateException("This player is already registered as a referee.");
        }

        refereeRepository.save(new Referee(player, certificationLevel, experienceYears));
    }

    public Referee getReferee(Long id) {
        return requireReferee(id);
    }

    public List<Referee> getAllReferees() {
        return refereeRepository.findAll();
    }

    public Referee updateReferee(Referee referee) {
        if (referee == null || referee.getId() == null) {
            throw new IllegalArgumentException("Referee and referee ID are required.");
        }

        requireReferee(referee.getId());

        if (referee.getCertificationLevel() == null || referee.getCertificationLevel().isBlank()) {
            throw new IllegalArgumentException("Certification level is required.");
        }

        if (referee.getExperienceYears() == null || referee.getExperienceYears() < 0) {
            throw new IllegalArgumentException("Experience years cannot be negative.");
        }

        if (referee.getPlayer() == null|| playerRepository.findById(referee.getPlayer().getId()) == null) {
            throw new IllegalArgumentException("Valid player is required.");
        }

        //to prevent duplicate referees - more than one player_id shouldn't exist in Refree as player_id is having unique constraint (existing iruka player id thirumba podratha prevent panrathukaaga eg:player_id = 20 for referee_id =3, referee_id = 1 la player_id = 20 nu update panira koodathungurathukaga)
        boolean duplicate = refereeRepository.findAll().stream()	  //findAll - collection -> stream - stram of elements - .stream(): Opens a pipeline to process that collection item-by-item.
                .anyMatch(r -> !referee.getId().equals(r.getId())	  //.anyMatch(...): Iterates through the stream and checks if at least one element (a) matches the complex boolean condition you provided inside the lambda expression
                        && r.getPlayer() != null
                        && referee.getPlayer().getId()
                                .equals(r.getPlayer().getId()));

        if (duplicate) {
            throw new IllegalStateException("This player already has a referee record.");
        }

        return refereeRepository.update(referee);
    }

    public void deleteReferee(Long id) {
        requireReferee(id);
        refereeRepository.delete(id);
    }

    private Referee requireReferee(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Referee ID is required.");
        }

        Referee referee = refereeRepository.findById(id);

        if (referee == null) {
            throw new IllegalArgumentException("Referee not found.");
        }

        return referee;
    }
}
