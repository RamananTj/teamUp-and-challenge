package com.tournament.teamupAndChallenge.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.tournament.teamupAndChallenge.util.HibernateUtil;

public class GenericRepositoryImpl <T, ID> implements GenericRepository<T, ID> {

	private final Class<T> entityClass;
	protected final SessionFactory sessionFactory;
	
	public GenericRepositoryImpl(Class<T> entityClass) {
		this.entityClass = entityClass;	//Class object that contains metadata about the entity(Referee, player,..) types.
		this.sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	@Override
	public void save(T entity) {
		executeTransaction(session -> session.persist(entity));				
	}

	@Override
	public T findById(ID id) {
		try(Session session = sessionFactory.openSession())
		{
			return session.find(entityClass, id);   //.get(entityClass, id)
		}			
	}

	@Override
	public List<T> findAll() {
		try(Session session = sessionFactory.openSession()){
			return session.createQuery(
					"FROM " + entityClass.getSimpleName(), entityClass        //getSimpleName() - returns class name alone without package names before it   
			).list();
		}
		
	}

	@Override
	public T update(T entity) {
		Transaction tx = null;
		
		try(Session session = sessionFactory.openSession())
		{
			tx = session.beginTransaction();
			T updatedEntity = session.merge(entity);
			
			tx.commit();
			return updatedEntity;
		} 
		catch(RuntimeException e) {			
			rollback(tx);
			 throw e;
		}
		
	}

	@Override
	public void delete(ID id) {
		Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            T entity = session.find(entityClass, id);

            if (entity != null) {
                session.remove(entity);
            }

            tx.commit();
        } catch (RuntimeException e) {
        	rollback(tx);
            throw e;
        }
		
	}
	
	protected void executeTransaction(
            java.util.function.Consumer<Session> operation) {

        Transaction tx = null;

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            operation.accept(session);

            tx.commit();
        } catch (RuntimeException e) {
            rollback(tx);
            throw e;
        }
    }

    private void rollback(Transaction tx) {
        if (tx != null && tx.isActive()) {
            tx.rollback();
        }
    }

}
