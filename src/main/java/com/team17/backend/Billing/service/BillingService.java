package com.team17.backend.Billing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team17.backend.Billing.model.Billing;
import com.team17.backend.Rent.model.Rent;

@Service
public class BillingService {
    @Autowired
    private BillingRepository billingRepository;

    public List<Billing> findAllBillings() {
        return billingRepository.findAll();
    }

    public List<Billing> findBillingsByOwnerId(String email) throws BillingServiceException {
        if (email == null) {
            throw new BillingServiceException("ownerEmail", "Email cannot be null");
        }
        List<Billing> billing = billingRepository.findByOwner_Email(email);
        if (billing != null && billing.size() > 0) {
            return billing;
        } else {
            throw new BillingServiceException("ownerEmail", "Billing with email " + email + " not found");
        }
    }

    public List<Billing> findBillingsByRenterId(String email) throws BillingServiceException {
        if (email == null) {
            throw new BillingServiceException("renterEmail", "Email cannot be null");
        }
        List<Billing> billing = billingRepository.findByRenter_Email(email);
        if (billing != null && billing.size() > 0) {
            return billing;
        } else {
            throw new BillingServiceException("renterEmail", "Billing with email " + email + " not found");
        }

    }

}
