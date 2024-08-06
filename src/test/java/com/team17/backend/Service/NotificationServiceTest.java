package com.team17.backend.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.team17.backend.Car.model.Car;
import com.team17.backend.User.model.User;
import com.team17.backend.User.service.UserService;
import com.team17.backend.User.service.UserServiceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.team17.backend.Notification.model.Notification;
import com.team17.backend.Notification.service.NotificationRepository;
import com.team17.backend.Notification.service.NotificationService;
import com.team17.backend.Notification.service.NotificationServiceException;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private NotificationService notificationService;

    private List<Notification> notifications;

    @BeforeEach
    public void setUp() {
        notifications = new ArrayList<>();
        notifications.add(new Notification(1L, "Toyota", Car.CarType.SUV, "1-XHA-412",
                LocalDate.of(2024, 4, 15), LocalDate.of(2024, 4, 20), "owner@example.com", "renter@example.com",
                Notification.NotificationType.CONFIRM, Notification.NotificationStatus.UNREAD));
        notifications.add(new Notification(2L, "BMW", Car.CarType.COUPE, "KU-D8H",
                LocalDate.of(2024, 4, 10), LocalDate.of(2024, 4, 18), "owner2@example.com", "renter2@example.com",
                Notification.NotificationType.CONFIRM, Notification.NotificationStatus.UNREAD));
    }

    @Test
    public void givenExistingNotifications_whenFindAllNotifications_thenListOfNotificationsIsReturned() {
        when(notificationRepository.findAll()).thenReturn(notifications);

        List<Notification> foundNotifications = notificationService.findAllNotifications();

        assertEquals(2, foundNotifications.size());
        assertEquals(notifications, foundNotifications);
    }

    @Test
    public void givenNewNotification_whenAddNotificationToUser_thenNotificationIsAddedToUserSuccessfully()
            throws NotificationServiceException, UserServiceException {
        // Given
        String userEmail = "user@example.com";
        Notification newNotification = new Notification(3L, "Mini Cooper", Car.CarType.COUPE, "1-GTN-653",
                LocalDate.of(2024, 4, 12), LocalDate.of(2024, 4, 22), "owner3@example.com", "renter3@example.com",
                Notification.NotificationType.CONFIRM, Notification.NotificationStatus.UNREAD);

        User mockUser = new User("user@example.com", "password", "John", "Doe", "+1234567890",
                LocalDate.of(1990, 5, 15), null);
        when(userService.getUser(userEmail)).thenReturn(mockUser);

        // When
        Notification addedNotification = notificationService.addNotificationToUser(userEmail, newNotification);

        // Then
        assertEquals(newNotification, addedNotification);
        assertEquals(mockUser, newNotification.getUser());
        verify(notificationRepository, times(1)).save(newNotification);
    }

    @Test
    public void givenExistingNotificationId_whenRemoveNotification_thenNotificationIsRemovedSuccessfully() {
        // Given
        long notificationId = 1L;
        when(notificationRepository.deleteNotificationById(notificationId)).thenReturn(1L);

        // When
        Long removedNotificationId = notificationService.removeNotification(notificationId);

        // Then
        assertEquals(notificationId, removedNotificationId);
        verify(notificationRepository, times(1)).deleteNotificationById(notificationId);
    }

    @Test
    public void givenNonExistingNotificationId_whenRemoveNotification_thenReturnNull() {
        // Given
        long nonExistingNotificationId = 999L;
        when(notificationRepository.deleteNotificationById(nonExistingNotificationId)).thenReturn(0L);

        // When
        Long removedNotificationId = notificationService.removeNotification(nonExistingNotificationId);

        // Then
        assertEquals(0L, removedNotificationId);
        verify(notificationRepository, times(1)).deleteNotificationById(nonExistingNotificationId);
    }

}
