package com.team17.backend.Rental.service;

import com.team17.backend.ServiceException.ServiceException;

public class RentalServiceException extends ServiceException {

    public RentalServiceException(String field, String message) {
        super(field, message);
    }

}
