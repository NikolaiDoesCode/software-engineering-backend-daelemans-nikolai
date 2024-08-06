package com.team17.backend.Billing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team17.backend.Billing.model.Billing;
import com.team17.backend.Billing.service.BillingService;
import com.team17.backend.Billing.service.BillingServiceException;
import com.team17.backend.Greeting.model.Greeting;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/billing")
public class BillingServiceController {

    @Autowired
    private BillingService billingService;

    @GetMapping()
    public List<Billing> getAllBillings() {
        return billingService.findAllBillings();
    }

    @GetMapping("/owner/{email}")
    public List<Billing> getBillingByOwnerId(@PathVariable String email) throws BillingServiceException {
        return billingService.findBillingsByOwnerId(email);
    }

    @GetMapping("/renter/{email}")
    public List<Billing> getBillingByRenterId(@PathVariable String email) throws BillingServiceException {
        return billingService.findBillingsByRenterId(email);
    }
}
