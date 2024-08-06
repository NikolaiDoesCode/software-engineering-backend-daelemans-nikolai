package com.team17.backend.Notification.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.team17.backend.Notification.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    public List<Notification> findAll();

    public Notification getById(long id);

    public Long deleteNotificationById(long id);
}
