package com.team17.backend.Billing.service;

import com.team17.backend.ServiceException.ServiceException;

public class BillingServiceException extends ServiceException {

    public BillingServiceException(String field, String message) {
        super(field, message);
    }

}
