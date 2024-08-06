package com.team17.backend.Groupchat.service;

import com.team17.backend.ServiceException.ServiceException;

public class GroupchatServiceException extends ServiceException {
    public GroupchatServiceException(String field, String message) {
                super(field, message);
            }
}