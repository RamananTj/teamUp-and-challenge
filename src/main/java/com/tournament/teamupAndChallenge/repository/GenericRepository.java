package com.tournament.teamupAndChallenge.repository;

import java.util.List;

public interface GenericRepository<T,ID> {
	void save(T entity);
	
	T findById(ID id);
	
	List<T> findAll();
	
	T update(T entity);
	
	void delete(ID id);
}
