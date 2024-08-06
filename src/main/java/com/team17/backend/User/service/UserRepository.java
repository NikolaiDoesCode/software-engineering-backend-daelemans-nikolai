package com.team17.backend.User.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team17.backend.User.model.User;

public interface UserRepository extends JpaRepository<User, String> {
    public User findUserByEmail(String email);

    public List<User> findAllByIsAdminTrue();
}
