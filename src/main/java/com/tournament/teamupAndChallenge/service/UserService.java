package com.tournament.teamupAndChallenge.service;

import com.tournament.teamupAndChallenge.entity.User;
import com.tournament.teamupAndChallenge.repository.UserRepository;

import java.util.List;

public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void createUser(User user) {
        if (user == null || user.getEmail() == null
                || user.getEmail().isBlank()) {
            throw new IllegalArgumentException(
                "User and email are required"
            );
        }

        if (repository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException(
                "Email already registered"
            );
        }

        repository.save(user);
    }

    public User getUser(Long id) {
        return repository.findById(id);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User updateUser(User user) {
        return repository.update(user);
    }

    public void deleteUser(Long id) {
        repository.delete(id);
    }
}