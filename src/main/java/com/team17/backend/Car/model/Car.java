package com.team17.backend.Car.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.team17.backend.Rental.model.Rental;
import com.team17.backend.User.model.User;

import jakarta.persistence.*;

@Entity
@Table(name = "cars")
public class Car {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public long id;

    @NotBlank(message = "Brand is required")
    private String brand;

    private String model;

    @NotNull(message = "Type is required")
    private CarType type;

    public enum CarType {
        SUV,
        COUPE,
        SPORT,
    }

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    @Min(value = 1, message = "Number of seats is required")
    private int numberOfSeats;

    private int numberOfChildSeats;
    private boolean foldingRearSeat;
    private boolean towBar;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Rental> rentals;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "car", nullable = false)
    private User user;

    public Car(String brand, String model, CarType type, String licensePlate, int numberOfSeats, int numberOfChildSeats,
            boolean foldingRearSeat, boolean towBar) {
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.licensePlate = licensePlate;
        this.numberOfSeats = numberOfSeats;
        this.numberOfChildSeats = numberOfChildSeats;
        this.foldingRearSeat = foldingRearSeat;
        this.towBar = towBar;
    }

    public Car() {
    }

    public long getId() {
        return this.id;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public CarType getType() {
        return this.type;
    }

    public void setType(CarType type) {
        this.type = type;
    }

    public String getLicensePlate() {
        return this.licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getNumberOfSeats() {
        return this.numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public int getNumberOfChildSeats() {
        return this.numberOfChildSeats;
    }

    public void setNumberOfChildSeats(int numberOfChildSeats) {
        this.numberOfChildSeats = numberOfChildSeats;
    }

    public boolean getFoldingRearSeat() {
        return this.foldingRearSeat;
    }

    public void setFoldingRearSeat(boolean foldingRearSeat) {
        this.foldingRearSeat = foldingRearSeat;
    }

    public boolean getTowBar() {
        return this.towBar;
    }

    public void setTowBar(boolean towBar) {
        this.towBar = towBar;
    }

    @JsonIgnore
    public Set<Rental> getRentals() {
        if (this.rentals == null) {
            this.rentals = new HashSet<>();
        }

        return this.rentals;
    }

    public void addRental(Rental rental) {
        this.rentals.add(rental);
    }

    public void removeRental(Rental rental) {
        this.rentals.remove(rental);
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
