package com.team17.backend.Notification.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

import com.team17.backend.Car.model.Car;
import com.team17.backend.Car.service.CarServiceException;
import com.team17.backend.Notification.model.Notification;
import com.team17.backend.Notification.model.Notification.NotificationStatus;
import com.team17.backend.User.model.User;
import com.team17.backend.User.service.UserService;
import com.team17.backend.User.service.UserServiceException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    public NotificationService() {
    }

    public List<Notification> findAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        Collections.reverse(notifications);
        return notifications;
    }

    public Notification addNotificationToUser(String userEmail, Notification notification)
            throws NotificationServiceException, UserServiceException {

        User user = userService.getUser(userEmail);
        if (user == null) {
            throw new NotificationServiceException("User", "User with email %s does not exist".formatted(userEmail));
        }

        notification.setUser(user);
        notificationRepository.save(notification);
        return notification;
    }

    @Transactional
    public Long removeNotification(long id) {
        return notificationRepository.deleteNotificationById(id);
    }

    public Notification updateStatusNotification(long id) throws NotificationServiceException {

        Notification notification = notificationRepository.getReferenceById(id);

        if (notification == null) {
            throw new NotificationServiceException("Id", "Notification with id %s does not exist".formatted(id));
        }

        if (notification.getNotificationStatus() == NotificationStatus.ARCHIVED) {
            notification.setNotificationStatus(NotificationStatus.UNREAD);
        } else {
            notification.setNotificationStatus(NotificationStatus.ARCHIVED);
        }

        notificationRepository.save(notification);
        return notification;
    }

}
