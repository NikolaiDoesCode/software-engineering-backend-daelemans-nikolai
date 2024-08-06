package com.team17.backend.Rental.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.aspectj.lang.annotation.After;
import org.springframework.cglib.core.Local;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.team17.backend.Car.model.Car;
import com.team17.backend.Rent.model.Rent;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "rentals")
public class Rental {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public long id;

    @NotNull(message = "Start date is required")
    @Future(message = "Start date is invalid, it has to be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    private LocalTime startTime;

    @NotNull(message = "End date is required")
    @Future(message = "End date is invalid, it has to be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private LocalTime endTime;

    private String street;
    private int number;
    private int postal;

    private float price;

    @NotBlank(message = "City is required")
    private String city;

    @NotNull(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email value is invalid, it has to be of the following format xxx@yyy.zzz")
    private String email;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Rent> rents;

    public Rental() {
    }

    public Rental(long l, Car car1, String string, LocalDate localDate, LocalDate localDate2) {
    }

    public Rental(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String city,
            String phoneNumber,
            String email, String street, int number, int postal, float price) {

        LocalTime defaultTimeString = LocalTime.of(0, 0);

        this.endDate = startDate;
        this.startDate = endDate;

        this.startTime = startTime != null ? startTime : defaultTimeString;
        this.endTime = endTime != null ? endTime : defaultTimeString;

        // if (this.startDate.isAfter(this.endDate)) {
        // throw new RentalException("Start date", "Start date must be before the end
        // date");
        // }

        // if (this.endDate.isBefore(this.startDate)) {
        // throw new RentalException("End date", "End date must be after the start
        // date");
        // }

        this.street = street;
        this.number = number;
        this.postal = postal;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.price = price;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    @JsonIgnore
    public LocalDateTime getStartDateTime() {
        if (this.startTime != null)
            return LocalDateTime.of(this.startDate, this.startTime);
        else
            return null; // or handle this case according to your logic
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @JsonIgnore
    public LocalDateTime getEndDateTime() {
        if (this.startTime != null)
            return LocalDateTime.of(this.endDate, this.endTime);
        else
            return null; // or handle this case according to your logic
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPostal() {
        return this.postal;
    }

    public void setPostal(int postal) {
        this.postal = postal;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Car getCar() {
        return this.car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @JsonIgnore
    public Set<Rent> getRents() {
        return this.rents;
    }

    public boolean equals(Rental rental) {
        return this.id == rental.getId() &&
                this.startDate.equals(rental.getStartDate()) &&
                this.startTime.equals(rental.getStartTime()) &&
                this.endDate.equals(rental.getEndDate()) &&
                this.endTime.equals(rental.getEndTime()) &&
                this.street.equals(rental.getStreet()) &&
                this.number == rental.getNumber() &&
                this.postal == rental.getPostal() &&
                this.city.equals(rental.getCity()) &&
                this.phoneNumber.equals(rental.getPhoneNumber()) &&
                this.email.equals(rental.getEmail());
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float newPrice) {
        this.price = newPrice;

    }

}
