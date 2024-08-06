package com.team17.backend.User.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCrypt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.team17.backend.Car.model.Car;
import com.team17.backend.Groupchat.model.Groupchat;
import com.team17.backend.Message.model.Message;
import com.team17.backend.Notification.model.Notification;
import com.team17.backend.Rent.model.Rent;
import com.team17.backend.User.service.UserServiceException;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Past;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Email
    public String email;

    @NotBlank(message = "Password is required")
    public String password;

    @NotBlank(message = "First name is required")
    public String firstName;

    @NotBlank(message = "Last name is required")
    public String lastName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$", message = "Phone number is not a valid format")
    public String phoneNumber;

    @Past(message = "Date of birth must be in the past")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate dateOfBirth;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Notification> notifications;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Car> cars;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Rent> rents;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "joined_groupchats", joinColumns = @JoinColumn(name = "user_email"), inverseJoinColumns = @JoinColumn(name = "groupchat_id"))
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<Groupchat> joined_groupchats;

    @JsonManagedReference(value = "user-messages")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Message> messages;

    @NotNull(message = "Role is required")
    public Role role;
    public boolean isAdmin;
    public boolean isAccountant;

    public User(String email, String password, String firstName, String lastName, String phoneNumber,
            LocalDate dateOfBirth, Role role) throws UserServiceException {
        this.email = email;
        if (password.strip().length() < 1) {
            throw new UserServiceException("password", "Password is required");
        }
        this.password = encryptPwd(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.isAdmin = false;
        this.isAccountant = false;
        this.role = role;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isAccountant() {
        return isAccountant;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setPassword(String password) {
        this.password = encryptPwd(password);
    }

    public void setAccountant(boolean isAccountant) {
        this.isAccountant = isAccountant;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Notification> getNotifications() {
        return this.notifications;
    }

    public List<Car> getCars() {
        return this.cars;
    }

    public List<Rent> getRents() {
        return this.rents;
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public List<Groupchat> getJoinedGroupchats() {
        return this.joined_groupchats;
    }

    public void addJoinedGroupchat(Groupchat groupchat) {
        this.getJoinedGroupchats().add(groupchat);
    }

    // password encryption
    private String encryptPwd(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public Boolean checkPwd(String pasword) {
        return BCrypt.checkpw(pasword, this.password);
    }
}