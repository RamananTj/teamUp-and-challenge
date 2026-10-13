package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.User;
import org.hibernate.Session;

public class UserRepositoryImpl
        extends GenericRepositoryImpl<User, Long>
        implements UserRepository {

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public User findByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
	            return session.createQuery(
	                "FROM User u WHERE u.email = :email",			//:email - named parameter - allows Hibernate to safely bind the user-supplied email variable later via .setParameter("email", email). This protects your application against SQL/HQL injection attacks by properly escaping the input
	                User.class
	            )
	            .setParameter("email", email)
	            .uniqueResult();         
//uniqueResult	1 Record Found: It returns that single entity object directly [1] (you don't have to extract it from a list).
//	            0 Records Found: It returns null.
//	            More than 1 Record Found: It throws a NonUniqueResultException
        }
    }
}