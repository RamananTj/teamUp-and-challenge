package com.tournament.teamupAndChallenge.repository;

import org.hibernate.Session;


import com.tournament.teamupAndChallenge.entity.TeamMember;

public class TeamMemberRepositoryImpl
        extends GenericRepositoryImpl<TeamMember, Long>
        implements TeamMemberRepository {

    public TeamMemberRepositoryImpl() {
        super(TeamMember.class);
    }
    
    public TeamMember getexistingTeamMember(Long player_id, Long team_id) {
    	try(Session session = sessionFactory.openSession())  //sessionFactory var is used from GenericRepository
    	{
    		String hql = "From TeamMember tm where tm.player.id = :player_id and tm.team.id = : team_id";
    		return session.createQuery(hql, TeamMember.class)
    				.setParameter("player_id", player_id)
    				.setParameter("team_id", team_id)
    				.uniqueResult();
    	}    	
    };
}