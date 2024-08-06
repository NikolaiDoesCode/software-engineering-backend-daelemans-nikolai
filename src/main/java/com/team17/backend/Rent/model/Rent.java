package com.team17.backend.Rent.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.team17.backend.Rent.service.RentServiceException;
import com.team17.backend.Rental.model.Rental;
import com.team17.backend.User.model.User;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "rents")
public class Rent {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public long id;

    @NotBlank(message = "Phone number is required")
    private String phoneNumberRenter;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email value is invalid, it has to be of the following format xxx@yyy.zzz")
    private String emailRenter;

    @NotNull(message = "Identification number of national register is required")
    @Pattern(regexp = "^\\d{2}\\.\\d{2}\\.\\d{2}-\\d{3}\\.\\d{2}$", message = "Identification number of national register is invalid, it has to be of the following format yy.mm.dd-xxx.zz")
    private String nationalRegisterId;

    @NotNull(message = "Birthdate is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull(message = "Driving licence number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Driving licence number is invalid, it has to be of the following format 0000000000 (where each 0 is a number between 0 and 9)")
    private String drivingLicenseNumber;

    private Boolean checkedIn;

    private LocalDate dateCheckIn;
    private LocalDate dateCheckOut;

    private Float fuelCheckIn;
    private Float fuelCheckOut;

    private Float mileageCheckIn;
    private Float mileageCheckOut;

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "rent", nullable = false)
    private User user;

    public Rent() {
    }

    public Rent(long l, Rental rental2, String string, boolean b) {
    }

    public Rent(String phoneNumberRenter, String emailRenter, String nationalRegisterId,
            LocalDate birthDate, Rental rental, String drivingLicenseNumber, Boolean checkedIn, Float fuelCheckIn,
            Float fuelCheckOut, Float mileageCheckIn, Float mileageCheckOut)
            throws RentServiceException {
        this.phoneNumberRenter = phoneNumberRenter;
        this.emailRenter = emailRenter;
        this.nationalRegisterId = nationalRegisterId;
        this.birthDate = birthDate;
        this.drivingLicenseNumber = drivingLicenseNumber;
        this.checkedIn = checkedIn;
        this.rental = rental;
        this.fuelCheckIn = fuelCheckIn;
        this.fuelCheckOut = fuelCheckOut;
        this.mileageCheckIn = mileageCheckIn;
        this.mileageCheckOut = mileageCheckOut;

    }

    public Boolean getCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(Boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public Float getFuelCheckIn() {
        return this.fuelCheckIn;
    }

    public void setFuelCheckIn(Float fuelCheckIn) {
        this.fuelCheckIn = fuelCheckIn;
    }

    public Float getFuelCheckOut() {
        return this.fuelCheckOut;
    }

    public void setFuelCheckOut(Float fuelCheckOut) {
        this.fuelCheckOut = fuelCheckOut;
    }

    public Float getMileageCheckIn() {
        return this.mileageCheckIn;
    }

    public Float getMileageCheckOut() {
        return this.mileageCheckOut;
    }

    public void setMileageCheckIn(Float mileageCheckIn) {
        this.mileageCheckIn = mileageCheckIn;
    }

    public void setMileageCheckOut(Float mileageCheckOut) {
        this.mileageCheckOut = mileageCheckOut;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNumberRenter() {
        return this.phoneNumberRenter;
    }

    public void setPhoneNumberRenter(String phoneNumberRenter) {
        this.phoneNumberRenter = phoneNumberRenter;
    }

    public String getEmailRenter() {
        return this.emailRenter;
    }

    public void setEmailRenter(String emailRenter) {
        this.emailRenter = emailRenter;
    }

    public String getNationalRegisterId() {
        return this.nationalRegisterId;
    }

    public void setNationalRegisterId(String nationalRegisterId) {
        this.nationalRegisterId = nationalRegisterId;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getDrivingLicenseNumber() {
        return this.drivingLicenseNumber;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public Rental getRental() {
        return this.rental;
    }

    public void setRental(Rental rental) {
        this.rental = rental;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDateCheckIn() {
        this.dateCheckIn = LocalDate.now();
    }

    public LocalDate getDateCheckIn() {
        return this.dateCheckIn;
    }

    public void setDateCheckOut() {
        this.dateCheckOut = LocalDate.now();
    }

    public LocalDate getDateCheckOut() {
        return this.dateCheckOut;
    }
}
