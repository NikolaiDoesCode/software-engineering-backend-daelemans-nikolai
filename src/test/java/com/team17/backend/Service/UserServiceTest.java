package com.team17.backend.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import com.team17.backend.User.model.LoginResponse;
import com.team17.backend.User.model.Role;
import com.team17.backend.User.model.User;
import com.team17.backend.User.service.UserRepository;
import com.team17.backend.User.service.UserService;
import com.team17.backend.User.service.UserServiceException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() throws UserServiceException {
        Role mockRole = Mockito.mock(Role.class);
        user1 = new User("user1@example.com", "password1", "John", "doe", "+1234567890", LocalDate.of(1990, 5, 15),
                mockRole);
        user2 = new User("user2@example.com", "password2", "matt", "moantain", "+1234567890", LocalDate.of(1990, 6, 15),
                mockRole);
    }

    // getAllUsers
    @Test
    public void givenExistingUsers_whenGetAllUsers_thenListOfUsersIsReturned() {
        // Given
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> foundUsers = userService.getAllUsers();

        // Then
        assertEquals(2, foundUsers.size());
        assertEquals(user1, foundUsers.get(0));
        assertEquals(user2, foundUsers.get(1));
    }

    // getUser(email)
    @Test
    public void givenExistingEmail_whenGetUser_thenUserIsReturned() throws UserServiceException {
        // Given
        String email = "user1@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(user1);

        // When
        User foundUser = userService.getUser(email);

        // Then
        assertEquals(user1, foundUser);
    }

    @Test
    public void givenNonExistingEmail_whenGetUser_thenThrowException() {
        // Given
        String email = "nonexistent@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(null);

        // Then
        assertThrows(UserServiceException.class, () -> userService.getUser(email));
    }

    // addUser()
    @Test
    public void givenNewUser_whenAddUser_thenUserIsAddedSuccessfully() throws UserServiceException {
        // Given
        when(userRepository.findUserByEmail(user1.getEmail())).thenReturn(null);

        // When
        User addedUser = userService.addUser(user1);

        // Then
        assertEquals(user1, addedUser);
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    public void givenExistingUser_whenAddUser_thenThrowException() {
        // Given
        when(userRepository.findUserByEmail(user1.getEmail())).thenReturn(user1);

        // Then
        assertThrows(UserServiceException.class, () -> userService.addUser(user1));
    }

    @Test
    public void givenExistingUserWithCorrectPassword_whenLogUser_thenUserIsReturned() throws UserServiceException {
        // Given
        String email = "user1@example.com";
        String password = "password1";
        when(userRepository.findUserByEmail(email)).thenReturn(user1);

        // When
        LoginResponse loggedInUser = userService.logUser(email, password);

        // Then
        assertEquals(user1, loggedInUser.getUser());
    }

    @Test
    public void givenNonExistingUser_whenLogUser_thenThrowException() {
        // Given
        String email = "nonexcistent@example.com";
        String password = "password";
        when(userRepository.findUserByEmail(email)).thenReturn(null);

        // Then
        assertThrows(UserServiceException.class, () -> userService.logUser(email, password));
    }

    @Test
    public void givenExistingUserWithIncorrectPassword_whenLoginUser_thenThrowException() {
        // Given
        String email = "user1@example.com";
        String incorrectPassword = "wrongpassword";
        when(userRepository.findUserByEmail(email)).thenReturn(user1);

        // Then
        assertThrows(UserServiceException.class, () -> userService.logUser(email, incorrectPassword));
    }

    @Test
    public void givenExistingEmail_whenDeleteUser_thenUserIsDeletedSuccessfully() throws UserServiceException {
        // Given
        String email = "user1@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(user1);

        // When
        userService.deleteUser(email);

        // Then
        verify(userRepository, times(1)).delete(user1);
    }

    @Test
    public void givenNonExistingEmail_whenDeleteUser_thenThrowException() {
        // Given
        String email = "nonexistent@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(null);

        // Then
        assertThrows(UserServiceException.class, () -> userService.deleteUser(email));
    }
}
