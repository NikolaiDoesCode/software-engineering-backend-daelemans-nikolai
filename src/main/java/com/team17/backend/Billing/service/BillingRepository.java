package com.team17.backend.Billing.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team17.backend.Billing.model.Billing;
import com.team17.backend.Car.model.Car;
import com.team17.backend.User.model.User;

public interface BillingRepository extends JpaRepository<Billing, Long> {

    List<Billing> findByOwner_Email(String email);

    List<Billing> findByRenter_Email(String email);

    Billing findByOwnerAndRenterAndCarAndStartDate(User carOwner, User renter, Car car, LocalDate localDate);

}
