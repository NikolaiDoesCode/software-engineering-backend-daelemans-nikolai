package com.team17.backend.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.team17.backend.Billing.model.Billing;
import com.team17.backend.Billing.service.BillingRepository;
import com.team17.backend.Billing.service.BillingService;
import com.team17.backend.Billing.service.BillingServiceException;
import com.team17.backend.Car.model.Car;
import com.team17.backend.User.model.Role;
import com.team17.backend.User.model.User;
import com.team17.backend.User.service.UserServiceException;

@ExtendWith(MockitoExtension.class)
public class BillingServiceTest {

    @Mock
    private BillingRepository billingRepository;

    @InjectMocks
    private BillingService billingService;

    private Billing billing1;
    private Billing billing2;

    private User owner;
    private User renter;

    @BeforeEach
    public void setUp() throws UserServiceException {
        owner = new User("owner@example.com", "password", "Owner", "LastName", "1234567890", LocalDate.of(1990, 1, 1), Role.RENTER);
        renter = new User("renter@example.com", "password", "Renter", "LastName", "1234567890", LocalDate.of(1990, 1, 1), Role.RENTER);

        billing1 = new Billing(owner, renter, new Car(), LocalDate.now());
        billing2 = new Billing(owner, renter, new Car(), LocalDate.now());
    }

    //findAllBillings
    @Test
    public void givenExistingBillings_whenFindAllBillings_thenListOfBillingsIsReturned() {
        // Given
        List<Billing> billings = new ArrayList<>();
        billings.add(billing1);
        billings.add(billing2);
        
        when(billingRepository.findAll()).thenReturn(billings);

        // When
        List<Billing> foundBillings = billingService.findAllBillings();

        // Then
        assertEquals(2, foundBillings.size());
        assertEquals(billing1, foundBillings.get(0));
        assertEquals(billing2, foundBillings.get(1));
    }

    //findBillingsByOwnerId
    @Test
    public void givenExistingBillings_whenFindBillingsByOwnerId_thenListOfBillingsIsReturned() throws BillingServiceException {
        // Given
        List<Billing> billings = new ArrayList<>();
        billings.add(billing1);
        
        when(billingRepository.findByOwner_Email(owner.getEmail())).thenReturn(billings);

        // When
        List<Billing> foundBillings = billingService.findBillingsByOwnerId(owner.getEmail());

        // Then
        assertEquals(1, foundBillings.size());
        assertEquals(billing1, foundBillings.get(0));
    }

    @Test
    public void givenNullOwnerEmail_whenFindBillingsByOwnerId_thenBillingServiceExceptionIsThrown() {
        // Given
        String email = null;

        // When
        BillingServiceException ex = assertThrows(BillingServiceException.class, () -> billingService.findBillingsByOwnerId(email));

        // Then
        assertEquals("ownerEmail", ex.getField());
        assertEquals("Email cannot be null", ex.getMessage());
    }

    @Test
    public void givenNonExistingOwnerEmail_whenFindBillingsByOwnerId_thenBillingServiceExceptionIsThrown() {
        // Given
        String email = "nonexistent@example.com";
        when(billingRepository.findByOwner_Email(email)).thenReturn(new ArrayList<>());

        // When
        BillingServiceException ex = assertThrows(BillingServiceException.class, () -> billingService.findBillingsByOwnerId(email));

        // Then
        assertEquals("ownerEmail", ex.getField());
        assertEquals("Billing with email " + email + " not found", ex.getMessage());
    }

    // Test findBillingsByRenterId
    @Test
    public void givenExistingBillings_whenFindBillingsByRenterId_thenListOfBillingsIsReturned() throws BillingServiceException {
        // Given
        List<Billing> billings = new ArrayList<>();
        billings.add(billing1);
        
        when(billingRepository.findByRenter_Email(renter.getEmail())).thenReturn(billings);

        // When
        List<Billing> foundBillings = billingService.findBillingsByRenterId(renter.getEmail());

        // Then
        assertEquals(1, foundBillings.size());
        assertEquals(billing1, foundBillings.get(0));
    }

    @Test
    public void givenNullRenterEmail_whenFindBillingsByRenterId_thenBillingServiceExceptionIsThrown() {
        // Given
        String email = null;

        // When
        BillingServiceException ex = assertThrows(BillingServiceException.class, () -> billingService.findBillingsByRenterId(email));

        // Then
        assertEquals("renterEmail", ex.getField());
        assertEquals("Email cannot be null", ex.getMessage());
    }

    @Test
    public void givenNonExistingRenterEmail_whenFindBillingsByRenterId_thenBillingServiceExceptionIsThrown() {
        // Given
        String email = "nonexistent@example.com";
        when(billingRepository.findByRenter_Email(email)).thenReturn(new ArrayList<>());

        // When
        BillingServiceException ex = assertThrows(BillingServiceException.class, () -> billingService.findBillingsByRenterId(email));

        // Then
        assertEquals("renterEmail", ex.getField());
        assertEquals("Billing with email " + email + " not found", ex.getMessage());
    }
}
