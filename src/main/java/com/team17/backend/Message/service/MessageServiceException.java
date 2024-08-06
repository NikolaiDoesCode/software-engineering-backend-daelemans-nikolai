package com.team17.backend.Message.service;

import com.team17.backend.ServiceException.ServiceException;

public class MessageServiceException extends ServiceException {
    
    public MessageServiceException(String field, String message) {
        super(field, message);
    }
}
