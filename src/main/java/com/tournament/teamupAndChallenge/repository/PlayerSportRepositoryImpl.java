package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.PlayerSport;
import org.hibernate.Session;

import java.util.List;

public class PlayerSportRepositoryImpl
        extends GenericRepositoryImpl<PlayerSport, Long>
        implements PlayerSportRepository {

    public PlayerSportRepositoryImpl() {
        super(PlayerSport.class);
    }

    @Override
    public List<PlayerSport> findByPlayerId(Long playerId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                "FROM PlayerSport ps JOIN FETCH ps.sport " +      //JOIN FETCH - Eager loading. If fetch not used - lazy loading of sport join happens for PlayerSport - 2 Separate roundabout database call happens on that case - When your code loops through the list and calls playerSport.getSport().getName(), Hibernate is forced to fire an extra, separate SQL query for every single unique sport to fetch its data. If you have 50 players, that could mean 50 extra database hits. 
                "WHERE ps.player.id = :playerId",
                PlayerSport.class
            )
            .setParameter("playerId", playerId)
            .list();
        }
    }

    @Override
    public List<PlayerSport> findBySportId(Long sportId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                "FROM PlayerSport ps JOIN FETCH ps.player " +
                "WHERE ps.sport.id = :sportId",
                PlayerSport.class
            )
            .setParameter("sportId", sportId)
            .list();
        }
    }

    @Override
    public boolean existsByPlayerIdAndSportId(
            Long playerId, Long sportId) {

        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                "SELECT COUNT(ps) FROM PlayerSport ps " +
                "WHERE ps.player.id = :playerId " +
                "AND ps.sport.id = :sportId",
                Long.class
            )
            .setParameter("playerId", playerId)
            .setParameter("sportId", sportId)
            .uniqueResult();

            return count != null && count > 0;
        }
    }
}