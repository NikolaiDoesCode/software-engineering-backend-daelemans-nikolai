package com.team17.backend.Notification.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.team17.backend.Car.model.Car;
import com.team17.backend.Car.model.Car.CarType;
import com.team17.backend.Rental.model.Rental;
import com.team17.backend.User.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "notifications")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Notification {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    public long id;

    public long rentId;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotNull(message = "Type is required")
    private CarType type;

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    @NotNull(message = "Start date is required")
    @Future(message = "Start date is invalid, it has to be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date is invalid, it has to be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotBlank(message = "Email of owner is required")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email value is invalid, it has to be of the following format xxx@yyy.zzz")
    private String emailOwner;

    @NotBlank(message = "Email of renter is required")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email value is invalid, it has to be of the following format xxx@yyy.zzz")
    private String emailRenter;

    private NotificationType notificationType;

    public enum NotificationType {
        RENT,
        CONFIRM,
        CANCEL,
    }

    private NotificationStatus notificationStatus;

    public enum NotificationStatus {
        UNREAD,
        ARCHIVED,
    }

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_email", nullable = false)
    private User user;

    public Notification(long rentId, String brand, Car.CarType type, String licensePlate, LocalDate startDate,
            LocalDate endDate, String emailOwner, String emailRenter, NotificationType notificationType,
            NotificationStatus notificationStatus) {
        this.rentId = rentId;
        this.brand = brand;
        this.type = type;
        this.licensePlate = licensePlate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.emailOwner = emailOwner;
        this.emailRenter = emailRenter;
        this.notificationType = notificationType;
        this.notificationStatus = notificationStatus;
    }

    public Notification() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRentId() {
        return this.rentId;
    }

    public void setRentId(long rentId) {
        this.rentId = rentId;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getEmailOwner() {
        return this.emailOwner;
    }

    public void setEmailOwner(String emailOwner) {
        this.emailOwner = emailOwner;
    }

    public String getEmailRenter() {
        return this.emailRenter;
    }

    public void setEmailRenter(String emailRenter) {
        this.emailRenter = emailRenter;
    }

    public NotificationType getNotificationType() {
        return this.notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public NotificationStatus getNotificationStatus() {
        return this.notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
