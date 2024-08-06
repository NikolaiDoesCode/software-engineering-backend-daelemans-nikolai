package com.team17.backend.Notification.service;

import com.team17.backend.ServiceException.ServiceException;

public class NotificationServiceException extends ServiceException {

    public NotificationServiceException(String field, String message) {
        super(field, message);
    }

}
