package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.Sport;
import org.hibernate.Session;

public class SportRepositoryImpl
        extends GenericRepositoryImpl<Sport, Long>
        implements SportRepository {

    public SportRepositoryImpl() {
        super(Sport.class);
    }

    @Override
    public Sport findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                "FROM Sport s WHERE s.name = :name",
                Sport.class
            )
            .setParameter("name", name)
            .uniqueResult();
        }
    }
}