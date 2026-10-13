package com.tournament.teamupAndChallenge.repository;

import com.tournament.teamupAndChallenge.entity.Player;
//import com.tournament.teamupAndChallenge.util.HibernateUtil;

import org.hibernate.Session;

import java.util.List;

public class PlayerRepositoryImpl
        extends GenericRepositoryImpl<Player, Long>
        implements PlayerRepository {

    public PlayerRepositoryImpl() {
        super(Player.class);	//super(...) calls the constructor of the parent class - It initializes the repository by telling its parent (super) class exactly which database entity it is responsible for managing—in this case, the Player class
        //Player.class - Reflection Class object
    } 
    //above constructor - assign classObj value for GenericRepositoryImpl constructor
//    public GenericRepositoryImpl(Class<T> entityClass) {
//		this.entityClass = entityClass;
//		this.sessionFactory = HibernateUtil.getSessionFactory();
//	}

    @Override
    public List<Player> findByCity(String city) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                "FROM Player p WHERE p.city = :city",
                Player.class
            )
            .setParameter("city", city)
            .list();
        }
    }
}