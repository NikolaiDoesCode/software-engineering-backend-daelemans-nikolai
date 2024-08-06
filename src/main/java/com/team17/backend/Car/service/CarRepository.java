package com.team17.backend.Car.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.team17.backend.Car.model.Car;

public interface CarRepository extends JpaRepository<Car, Long> {

    public Car findCarById(long id);

    public List<Car> findAll(Sort sort);

    public long deleteCarById(long id);
}