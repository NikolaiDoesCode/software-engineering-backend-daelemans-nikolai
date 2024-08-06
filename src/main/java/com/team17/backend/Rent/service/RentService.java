package com.team17.backend.Rent.service;

import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team17.backend.Billing.model.Billing;
import com.team17.backend.Billing.service.BillingRepository;
import com.team17.backend.Car.model.Car;
import com.team17.backend.Mail.model.EmailRequest;
import com.team17.backend.Mail.service.MailService;
import com.team17.backend.Notification.model.Notification;
import com.team17.backend.Notification.model.Notification.NotificationStatus;
import com.team17.backend.Notification.model.Notification.NotificationType;
import com.team17.backend.Notification.service.NotificationService;
import com.team17.backend.Notification.service.NotificationServiceException;
import com.team17.backend.Rent.model.Rent;
import com.team17.backend.Rental.model.Rental;
import com.team17.backend.Rental.service.RentalService;
import com.team17.backend.User.model.Role;
import com.team17.backend.User.model.User;
import com.team17.backend.User.service.UserRepository;
import com.team17.backend.User.service.UserService;
import com.team17.backend.User.service.UserServiceException;

import jakarta.validation.Valid;

@Service
public class RentService {

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Autowired
    private BillingRepository billingRepository;

    public RentService() {
    }

    public List<Rent> findAllRents() {
        return rentRepository.findAll();
    }

    public Rent getRent(Long id) throws RentServiceException {
        if (!rentRepository.existsById(id)) {
            throw new RentServiceException("rentId", "Rent with id %s does not exist".formatted(id));
        }

        return rentRepository.findById(id).orElse(null);
    }

    public Rent addRent(Rent rent, String email)
            throws RentServiceException, UserServiceException, NotificationServiceException, MessagingException {

        User user = userService.getUser(email);
        if (user == null) {
            throw new UserServiceException("email", "User with given email does not exist");
        }

        if (user.getRole() != Role.RENTER) {
            throw new UserServiceException("Role", "User is not an renter");
        }

        Rental rental = rentalService.getRental(rent.getRental().getId());
        rent.setCheckedIn(false);
        // Check if rental exists
        if (rental == null) {
            throw new RentServiceException("rentalId",
                    "Rental with id %s does not exist".formatted(rent.getRental().getId()));
        }
        // Check if rental id matches the rental in the rent object
        if (!rent.getRental().equals(rental)) {
            throw new RentServiceException("rentalId", "Rental id does not match the rental id in the rent object");
        }

        List<Rent> allRents = findAllRents();

        for (Rent existingRent : allRents) {
            if (existingRent.getRental() == rental) {
                throw new RentServiceException("rental", "Rental is already taken");
            }
        }

        // // Check that the start date/time is before the end date/time
        // if (rent.getStartDateTime().isAfter(rent.getEndDateTime())) {
        // throw new RentServiceException("rentDate", "Start date/time must be before
        // end date/time");
        // }
        // // Check that the rent date/time does not overlap with other rents
        // if (rental.getRents().stream().anyMatch(r ->
        // (r.getStartDateTime().isBefore(rent.getStartDateTime()) &&
        // r.getEndDateTime().isAfter(rent.getStartDateTime())) ||
        // (r.getStartDateTime().isEqual(rent.getStartDateTime())
        // && r.getEndDateTime().isEqual(rent.getEndDateTime())))) {
        // throw new RentServiceException("rentDate", "Rent date must not overlap with
        // other rents");
        // }
        // // Check that the rent date/time is within the rental period
        // if (rent.getStartDateTime().isBefore(rental.getStartDateTime()) ||
        // rent.getStartDateTime().isAfter(rental.getEndDateTime()) ||
        // rent.getEndDateTime().isBefore(rental.getStartDateTime()) ||
        // rent.getEndDateTime().isAfter(rental.getEndDateTime())) {
        // throw new RentServiceException("rentDate", "Rent date must be within the
        // rental period");
        // }
        rent.setUser(user);
        rentRepository.save(rent);

        Notification notification = new Notification(rent.getId(), rental.getCar().getBrand(),
                rental.getCar().getType(),
                rental.getCar().getLicensePlate(), rent.getRental().getStartDate(), rent.getRental().getEndDate(),
                rental.getEmail(),
                rent.getEmailRenter(), NotificationType.RENT, NotificationStatus.UNREAD);

        notificationService.addNotificationToUser(rental.getEmail(), notification);

        String subject = "A user rented your car !".formatted();
        String text = "Your %s %s was rented from %s until %s. Go on our site to confirm or cancel the rent !"
                .formatted(
                        rental.getCar().getBrand(), rental.getCar().getLicensePlate(), rent.getRental().getStartDate(),
                        rent.getRental().getEndDate());

        String ownerEmail = rental.getEmail();

        EmailRequest toSendEmail = new EmailRequest(ownerEmail, subject, text);
        mailService.sendEmail(toSendEmail);

        return rent;
    }

    public void deleteRent(Long id) throws RentServiceException {
        if (!rentRepository.existsById(id)) {
            throw new RentServiceException("rentId", "Rent with id %s does not exist".formatted(id));
        }
        rentRepository.deleteById(id);
    }

    public Rent getRentByEmail(@Valid String email) throws RentServiceException {
        if (email == null) {
            throw new RentServiceException("rentEmail", "Email cannot be null");
        }
        Rent rent = rentRepository.findByEmailRenter(email);
        if (rent != null) {
            return rent;
        } else {
            throw new RentServiceException("rentEmail", "Rent with email " + email + " not found");
        }
    }

    public Rent checkIn(Long id, float fuelCheckIn, float mileageCheckIn) throws RentServiceException {
        Rent rent = getRent(id);
        if (rent == null) {
            throw new RentServiceException("rentId", "Rent with id %s does not exist".formatted(id));
        }
        if (rent.getCheckedIn()) {
            throw new RentServiceException("rentId", "Rent with id %s is already checked in".formatted(id));
        }
        rent.setFuelCheckIn(fuelCheckIn);
        rent.setMileageCheckIn(mileageCheckIn);
        rent.setCheckedIn(true);
        rent.setDateCheckIn();

        Rental rental = rent.getRental();

        Car car = rental.getCar();

        User carOwner = car.getUser();
        User renter = rent.getUser();

        Billing billing = new Billing(carOwner, renter, car, rent.getDateCheckIn());

        billingRepository.save(billing);

        rentRepository.save(rent);
        return rent;
    }

    public Rent checkOut(Long id, float fuelCheckOut, float mileageCheckOut) throws RentServiceException {
        Rent rent = getRent(id);
        if (rent == null) {
            throw new RentServiceException("rentId", "Rent with id %s does not exist".formatted(id));
        }
        rent.setFuelCheckOut(fuelCheckOut);
        rent.setMileageCheckOut(mileageCheckOut);
        rent.setDateCheckOut();

        float rentalCost = calculateRentalCost(rent);

        Rental rental = rent.getRental();

        Car car = rental.getCar();

        User carOwner = car.getUser();
        User renter = rent.getUser();

        Billing billing = billingRepository.findByOwnerAndRenterAndCarAndStartDate(carOwner, renter, car,
                rent.getDateCheckIn());

        billing.setEndDate();
        billing.setCost(rentalCost);

        billingRepository.save(billing);
        rentRepository.save(rent);
        return rent;
    }

    public float calculateRentalCost(Rent rent) {
        Rental rental = rent.getRental();
        int days = rent.getDateCheckIn().until(rent.getDateCheckOut()).getDays();
        float cost = (float) ((days * rental.getPrice())
                + (0.3 * (rent.getMileageCheckOut() - rent.getMileageCheckIn()))
                + (0.5 * ((rent.getFuelCheckIn() - rent.getFuelCheckOut()) > 0
                        ? (rent.getFuelCheckIn() - rent.getFuelCheckOut())
                        : 0)));

        return cost;
    }

}
