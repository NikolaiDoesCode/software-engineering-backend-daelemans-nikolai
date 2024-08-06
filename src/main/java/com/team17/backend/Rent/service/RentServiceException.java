package com.team17.backend.Rent.service;

import com.team17.backend.ServiceException.ServiceException;

public class RentServiceException extends ServiceException {

    public RentServiceException(String field, String message) {
        super(field, message);
    }

}
