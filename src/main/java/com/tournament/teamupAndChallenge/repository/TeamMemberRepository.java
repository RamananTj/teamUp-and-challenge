package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.TeamMember;

public interface TeamMemberRepository extends GenericRepository<TeamMember, Long> {
	TeamMember getexistingTeamMember(Long player_id, Long team_id);
}