package com.tournament.teamupAndChallenge.service;

import java.time.LocalDate;
import java.util.List;

import com.tournament.teamupAndChallenge.entity.Player;
import com.tournament.teamupAndChallenge.entity.Team;
import com.tournament.teamupAndChallenge.entity.TeamMember;
import com.tournament.teamupAndChallenge.repository.PlayerRepository;
import com.tournament.teamupAndChallenge.repository.TeamMemberRepository;
import com.tournament.teamupAndChallenge.repository.TeamRepository;

public class TeamMemberService {

    private final TeamMemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public TeamMemberService(
            TeamMemberRepository memberRepository,
            TeamRepository teamRepository,
            PlayerRepository playerRepository) {

        this.memberRepository = memberRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    public void addMember(Long teamId, Long playerId, String role) {

        Team team = teamRepository.findById(teamId);
        Player player = playerRepository.findById(playerId);

        if (team == null) {
            throw new IllegalArgumentException("Team not found.");
        }

        if (player == null) {
            throw new IllegalArgumentException("Player not found.");
        }

        if (!List.of("PLAYER", "CAPTAIN", "VICE_CAPTAIN")
                .contains(role.toUpperCase())) {
            throw new IllegalArgumentException("Invalid team role.");
        }

        TeamMember existingTeamMember = memberRepository.getexistingTeamMember(playerId, teamId);
		  if(existingTeamMember!=null)
		  {
			  throw new IllegalArgumentException("Player is already in this team.");
		  }               
        
        TeamMember member = new TeamMember(team, player, role.toUpperCase());
        member.setJoinedAt(LocalDate.now());

        memberRepository.save(member);
    }

    public TeamMember getMember(Long id) {
        return memberRepository.findById(id);
    }

    public List<TeamMember> getAllMembers() {
        return memberRepository.findAll();
    }

    public TeamMember updateMember(TeamMember member) {
        return memberRepository.update(member);
    }

    public void deleteMember(Long id) {
        memberRepository.delete(id);
    }
}