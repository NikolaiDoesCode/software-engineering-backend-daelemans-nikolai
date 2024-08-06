package com.team17.backend.User.service;

import com.team17.backend.ServiceException.ServiceException;

public class UserServiceException extends ServiceException {

    public UserServiceException(String field, String message) {
        super(field, message);
    }

}
