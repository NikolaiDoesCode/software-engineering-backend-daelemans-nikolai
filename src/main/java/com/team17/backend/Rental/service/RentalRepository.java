package com.team17.backend.Rental.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.team17.backend.Rental.model.Rental;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("SELECT r FROM Rental r JOIN r.car c WHERE "
            + "(:email IS NULL OR r.email = :email) AND "
            + "(:startDate IS NULL OR r.startDate = :startDate) AND "
            + "(:endDate IS NULL OR r.endDate = :endDate) AND "
            + "(:brand IS NULL OR c.brand = :brand)")
    List<Rental> searchRentals(String email, LocalDate startDate, LocalDate endDate, String brand);

    public List<Rental> findAll(Sort sort);
}
