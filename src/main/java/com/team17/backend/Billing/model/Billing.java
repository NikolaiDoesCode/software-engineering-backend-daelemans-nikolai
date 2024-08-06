package com.team17.backend.Billing.model;

import java.time.LocalDate;

import org.springframework.context.annotation.Profile;

import com.team17.backend.Car.model.Car;
import com.team17.backend.User.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    public User owner;
    @ManyToOne
    @JoinColumn(name = "renter_id")
    public User renter;
    @ManyToOne
    @JoinColumn(name = "car_id")
    public Car car;
    public float cost;
    public LocalDate startDate;
    public LocalDate endDate;

    public Billing() {
    }

    public Billing(User owner, User renter, Car car, LocalDate startDate) {
        this.owner = owner;
        this.renter = renter;
        this.car = car;
        this.startDate = startDate;

    }

    public void setEndDate() {
        this.endDate = LocalDate.now();
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
}
