package com.team17.backend.Rent.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.team17.backend.Rent.model.Rent;

import jakarta.validation.Valid;

public interface RentRepository extends JpaRepository<Rent, Long> {

    public List<Rent> findAll(Sort sort);

    public Rent findByEmailRenter(String email);
}
