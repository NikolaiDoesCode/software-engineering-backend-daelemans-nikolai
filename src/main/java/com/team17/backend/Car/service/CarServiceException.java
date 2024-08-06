package com.team17.backend.Car.service;

import com.team17.backend.ServiceException.ServiceException;

public class CarServiceException extends ServiceException {

    public CarServiceException(String field, String message) {
        super(field, message);
    }

}
