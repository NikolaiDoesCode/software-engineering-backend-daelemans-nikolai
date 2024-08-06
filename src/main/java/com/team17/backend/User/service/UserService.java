package com.team17.backend.User.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team17.backend.config.JwtGenerator;
import com.team17.backend.Car.model.Car;
import com.team17.backend.Groupchat.model.Groupchat;
import com.team17.backend.Message.model.Message;
import com.team17.backend.Notification.model.Notification;
import com.team17.backend.Notification.model.Notification.NotificationStatus;
import com.team17.backend.Rent.model.Rent;
import com.team17.backend.Rental.model.Rental;
import com.team17.backend.Rental.service.RentalService;
import com.team17.backend.User.model.LoginResponse;
import com.team17.backend.User.model.Role;
import com.team17.backend.User.model.User;

import jakarta.annotation.PostConstruct;
import com.team17.backend.User.model.User;

@Service
public class UserService {

    private JwtGenerator jwtGenerator = new JwtGenerator();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalService rentalService;

    public UserService() {
    }

    @PostConstruct
    public void init() throws UserServiceException {
        User existingUser = userRepository.findUserByEmail("admin@admin.com");
        if (existingUser != null) {
            return;
        }
        User admin = new User(
                "admin@admin.com",
                "admin",
                "Admin",
                "Admin",
                "1234567890",
                java.time.LocalDate.of(2000, 1, 1),
                Role.OWNER);
        admin.setAdmin(true);
        admin.setAccountant(true);
        userRepository.save(admin);

        User renter = new User(
                "renter@renter.com",
                "renter",
                "renter",
                "renter",
                "1234567890",
                java.time.LocalDate.of(2000, 1, 1),
                Role.RENTER);
        admin.setAdmin(false);
        admin.setAccountant(false);
        userRepository.save(renter);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllAdmins() {
        return userRepository.findAllByIsAdminTrue();
    }

    public User getUser(String email) throws UserServiceException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserServiceException("email", "User with given email does not exist");
        }
        System.out.println("Found user with id " + email + ": " + user);
        return user;
    }

    public User addUser(User user) throws UserServiceException {
        String email = user.getEmail();
        User existingUser = userRepository.findUserByEmail(email);
        if (existingUser != null) {
            throw new UserServiceException("email", "User with given email already exists");
        }
        userRepository.save(user);
        return user;
    }

    public LoginResponse logUser(String email, String password) throws UserServiceException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserServiceException("email", "invalid email");
        }
        if (!user.checkPwd(password)) {
            throw new UserServiceException("password", "invalid password");
        }
        return new LoginResponse(user, jwtGenerator.generateToken(user));
    }

    public void deleteUser(String email) throws UserServiceException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserServiceException("email", "User with given email does not exist");
        }
        userRepository.delete(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public User setAdmin(String email, boolean isAdmin) throws UserServiceException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserServiceException("email", "User with given email does not exist");
        }
        // TODO once we can log in, don't let user remove their own admin rights
        user.setAdmin(isAdmin);
        return userRepository.save(user);
    }

    public User setAccountant(String email, boolean isAccountant) throws UserServiceException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserServiceException("email", "User with given email does not exist");
        }
        user.setAccountant(isAccountant);
        return userRepository.save(user);
    }

    public User setRole(String email, String role) throws UserServiceException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserServiceException("email", "User with given email does not exist");
        }
        Role roleToSet = Role.valueOf(role);
        if (roleToSet == null) {
            throw new UserServiceException("role", "Role with given name does not exist");
        }
        user.setRole(roleToSet);
        return userRepository.save(user);
    }

    public List<Car> getCarsUser(String email) throws UserServiceException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserServiceException("email", "User with given email does not exist");
        }

        if (user.getRole() != Role.OWNER) {
            throw new UserServiceException("Role", "User is not an owner");
        }

        return user.getCars();
    }

    public List<Rent> getRentsUser(String email) throws UserServiceException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserServiceException("email", "User with given email does not exist");
        }

        if (user.getRole() != Role.RENTER) {
            throw new UserServiceException("Role", "User is not an renter");
        }

        return user.getRents();
    }

    public List<Rental> getRentalsUser(String email) throws UserServiceException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserServiceException("email", "User with given email does not exist");
        }

        System.out.println(user.getRole());

        if (user.getRole() != Role.OWNER) {
            throw new UserServiceException("Role", "User is not an owner");
        }

        List<Rental> userRentals = new ArrayList<>();
        List<Rental> allRentals = rentalService.findAllRentalsWithoutRentedOnes();

        List<Car> userCars = user.getCars();

        for (Rental rental : allRentals) {
            for (Car car : userCars) {
                if (rental.getCar().id == car.id) {
                    userRentals.add(rental);
                }
            }
        }

        return userRentals;
    }

    public List<Notification> getNotificationsUser(String email) throws UserServiceException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserServiceException("email", "User with given email does not exist");
        }

        return user.getNotifications();
    }

    public List<Notification> getNewNotificationsUser(String email) throws UserServiceException {
        List<Notification> notifications = getNotificationsUser(email);

        List<Notification> newNotifications = new ArrayList<>();

        for (Notification notification : notifications) {
            System.out.println(notification.getNotificationStatus());
            System.out.println(NotificationStatus.UNREAD);
            if (notification.getNotificationStatus() == NotificationStatus.UNREAD) {
                newNotifications.add(notification);
            }
        }

        return newNotifications;
    }

    public List<Message> getMessagesUser(String email) throws UserServiceException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserServiceException("email", "User with given email does not exist");
        }
        return user.getMessages();
    }

    public List<Groupchat> getGroupchatsUser(String email) throws UserServiceException {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UserServiceException("email", "User with given email does not exist");
        }

        return user.getJoinedGroupchats();
    }

}
