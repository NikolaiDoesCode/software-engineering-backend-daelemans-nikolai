package com.team17.backend.bdd.steps;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.team17.backend.BackendApplication;
import com.team17.backend.Billing.model.Billing;
import com.team17.backend.Billing.service.BillingRepository;
import com.team17.backend.Car.model.Car;
import com.team17.backend.User.model.User;
import com.team17.backend.User.model.Role;
import com.team17.backend.Car.model.Car.CarType;
import com.team17.backend.Car.service.CarRepository;
import com.team17.backend.Notification.model.Notification;
import com.team17.backend.Notification.service.NotificationRepository;
import com.team17.backend.Rental.model.Rental;
import com.team17.backend.Rental.service.RentalRepository;
import com.team17.backend.Rental.service.RentalServiceException;
import com.team17.backend.User.service.UserRepository;
import com.team17.backend.User.service.UserServiceException;
import com.team17.backend.config.JwtGenerator;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@CucumberContextConfiguration
@SpringBootTest(classes = BackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
// @AutoConfigureWebTestClient
public class appSteps {

    @Autowired
    private WebTestClient client;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private BillingRepository billingRepository;

    private Rental rental;
    
    private User user1;

    private User user2;

    private Billing billing;

    private long rental_id1;

    private long user_id1;

    private long car_id1;

    private long notification_id1;

    private JwtGenerator jwtGenerator = new JwtGenerator();

    private String token;

 


    private Car car = new Car("Toyota", "Corolla", CarType.SUV, "XHA-412", 5, 2, false, false);
    private Car car2 = new Car("Ferrari", "Speed", CarType.SPORT, "GTN-215", 3, 1, false, false);
    
    {
        try {
            user1 = new User("owner1@owner.com", "owner", "owner1", "owner1", "0492929680", LocalDate.now().minusYears(18), Role.RENTER);
        } catch (UserServiceException e) {
            e.printStackTrace();
        }
    }

    {
        try {
            user2 = new User("owner2@owner.com", "owner", "owner2", "owner2", "0987654321", LocalDate.now().minusYears(18), Role.OWNER);
        } catch (UserServiceException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setup() {
        rental = new Rental();
    }
    
    @After
    public void deleteEntities() {
        billingRepository.deleteAll(); // Delete billings first
        rentalRepository.deleteAll(); // Then rentals
        carRepository.deleteAll(); // Then cars
        userRepository.deleteAll(); // Then users
        notificationRepository.deleteAll(); // Then notifications
    }
    

    private WebTestClient.ResponseSpec response;

    LocalDate startDate_1 = LocalDate.of(2025, 6, 8);
    LocalDate endDate_1 = LocalDate.of(2025, 6, 10);
    LocalTime startTime_1 = LocalTime.of(10, 0, 0);
    LocalTime endTime_1 = LocalTime.of(11, 0, 0);

    LocalDate startDate_2 = LocalDate.of(2025, 8, 8);
    LocalDate endDate_2 = LocalDate.of(2025, 8, 10);
    LocalTime startTime_2 = LocalTime.of(10, 0, 0);
    LocalTime endTime_2 = LocalTime.of(11, 0, 0);

    @Given("some rentals")
    public void some_rentals() {
        userRepository.save(user1);
        car.setUser(user1);
        Car car_response = carRepository.save(car);

        car_id1 = car_response.getId();

        Rental rental1 = new Rental(endDate_1, startTime_1, startDate_1, endTime_1, "Leuven", "0491 12 13 14", "owner1@owner.com", "Bondgenotenlaan", 16, 3000, 1);
        rental1.setCar(car);
        Rental rental2 = new Rental(endDate_2, startTime_2, startDate_2, endTime_2, "Heist", "0491 12 13 14", "owner1@owner.com", "Moerstraat", 62, 2220, 1);
        rental2.setCar(car);

        Rental rentalResponse = rentalRepository.save(rental1);
        rentalRepository.save(rental2);

        rental_id1 = rentalResponse.getId();


        token = jwtGenerator.generateToken(user1);
    }

    @When("I want to get all rentals")
    public void i_want_to_get_all_rentals() {
        response = client.get().uri("api/rental").headers(headers -> headers.setBearerAuth(token)).exchange();
    }

    @Then("the data of all rental is returned in a JSON format")
    public void the_data_of_all_rentals_is_returned_in_json_format() {
        response.expectStatus().isOk().expectBody().json("[{\"startDate\": \"2025-06-08\", \"startTime\": \"10:00:00\", \"endDate\": \"2025-06-10\", \"endTime\": \"11:00:00\", \"city\": \"Leuven\", \"phoneNumber\": \"0491 12 13 14\", \"email\": \"owner1@owner.com\", \"street\": \"Bondgenotenlaan\", \"number\": 16, \"postal\": 3000, \"price\": 1}, {\"startDate\": \"2025-08-08\", \"startTime\": \"10:00:00\", \"endDate\": \"2025-08-10\", \"endTime\": \"11:00:00\", \"city\": \"Heist\", \"phoneNumber\": \"0491 12 13 14\", \"email\": \"owner1@owner.com\", \"street\": \"Moerstraat\", \"number\": 62, \"postal\": 2220, \"price\": 1}]");
    }

    @Given("no rentals")
    public void no_rentals() {
        userRepository.save(user1);
        token = jwtGenerator.generateToken(user1);
    }

    @Then("the returned JSON is empty")
    public void the_returned_JSON_is_empty() {
        response.expectStatus().isOk().expectBody().json("[]");
    }

    @Given("some rentals of different users")
    public void some_rentals_to_different_users() {
        userRepository.save(user1);
        userRepository.save(user2);
        car.setUser(user1);
        carRepository.save(car);

        car2.setUser(user2);
        carRepository.save(car2);

        Rental rental1 = new Rental(endDate_1, startTime_1, startDate_1, endTime_1, "Leuven", "0491 12 13 14", "owner1@owner.com", "Bondgenotenlaan", 16, 3000, 1);
        rental1.setCar(car);
        Rental rental2 = new Rental(endDate_2, startTime_2, startDate_2, endTime_2, "Heist", "0491 12 13 14", "owner1@owner.com", "Moerstraat", 62, 2220, 1);
        rental2.setCar(car);

        rentalRepository.save(rental1);
        rentalRepository.save(rental2);

        Rental rental3 = new Rental(endDate_1, startTime_1, startDate_1, endTime_1, "Liège", "0492 92 96 80", "owner2@owner.com", "rue du Cimetière", 5, 4430, 1);
        rental3.setCar(car2);
        Rental rental4 = new Rental(endDate_2, startTime_2, startDate_2, endTime_2, "Ans", "0492 92 96 80", "owner2@owner.com", "rue du Fort", 16, 4000, 1);
        rental4.setCar(car2);

        rentalRepository.save(rental3);
        rentalRepository.save(rental4);

        token = jwtGenerator.generateToken(user1);
    }

    @When("I want to search rentals on email")
    public void i_want_to_search_rentals_on_email() {
        response = client.get().uri("api/rental/search?email=owner1@owner.com").headers(headers -> headers.setBearerAuth(token)).exchange();
    }

    @Then("the data of all rentals of the user with given email is returned in a JSON format")
    public void the_data_of_all_rentals_of_the_user_with_given_email_is_returned_in_json_format() {
        response.expectStatus().isOk().expectBody().json("[{\"startDate\": \"2025-06-08\", \"startTime\": \"10:00:00\", \"endDate\": \"2025-06-10\", \"endTime\": \"11:00:00\", \"city\": \"Leuven\", \"phoneNumber\": \"0491 12 13 14\", \"email\": \"owner1@owner.com\", \"street\": \"Bondgenotenlaan\", \"number\": 16, \"postal\": 3000, \"price\": 1}, {\"startDate\": \"2025-08-08\", \"startTime\": \"10:00:00\", \"endDate\": \"2025-08-10\", \"endTime\": \"11:00:00\", \"city\": \"Heist\", \"phoneNumber\": \"0491 12 13 14\", \"email\": \"owner1@owner.com\", \"street\": \"Moerstraat\", \"number\": 62, \"postal\": 2220, \"price\": 1}]");
    }

    @When("I want to search rentals on startDate")
    public void i_want_to_search_rentals_on_startDate() {
        response = client.get().uri("api/rental/search?startDate=2025-06-08").headers(headers -> headers.setBearerAuth(token)).exchange();
    }

    @Then("the data of all rentals starting on the given startDate is returned in a JSON format")
    public void the_data_of_all_rentals_starting_on_the_given_startDate_is_returned_in_json_format() {
        response.expectStatus().isOk().expectBody().json("[{\"startDate\": \"2025-06-08\", \"startTime\": \"10:00:00\", \"endDate\": \"2025-06-10\", \"endTime\": \"11:00:00\", \"city\": \"Leuven\", \"phoneNumber\": \"0491 12 13 14\", \"email\": \"owner1@owner.com\", \"street\": \"Bondgenotenlaan\", \"number\": 16, \"postal\": 3000, \"price\": 1}, {\"startDate\": \"2025-06-08\", \"startTime\": \"10:00:00\", \"endDate\": \"2025-06-10\", \"endTime\": \"11:00:00\", \"city\": \"Liège\", \"phoneNumber\": \"0492 92 96 80\", \"email\": \"owner2@owner.com\", \"street\": \"rue du Cimetière\", \"number\": 5, \"postal\": 4430, \"price\": 1}]");
    }

    @When("I want to search rentals on endDate")
    public void i_want_to_search_rentals_on_endDate() {
        response = client.get().uri("api/rental/search?endDate=2025-08-10").headers(headers -> headers.setBearerAuth(token)).exchange();
    }

    @Then("the data of all rentals ending on the given endDate is returned in a JSON format")
    public void the_data_of_all_rentals_ending_on_the_given_endDate_is_returned_in_json_format() {
        response.expectStatus().isOk().expectBody().json("[{\"startDate\": \"2025-08-08\", \"startTime\": \"10:00:00\", \"endDate\": \"2025-08-10\", \"endTime\": \"11:00:00\", \"city\": \"Heist\", \"phoneNumber\": \"0491 12 13 14\", \"email\": \"owner1@owner.com\", \"street\": \"Moerstraat\", \"number\": 62, \"postal\": 2220, \"price\": 1}, {\"startDate\": \"2025-08-08\", \"startTime\": \"10:00:00\", \"endDate\": \"2025-08-10\", \"endTime\": \"11:00:00\", \"city\": \"Ans\", \"phoneNumber\": \"0492 92 96 80\", \"email\": \"owner2@owner.com\", \"street\": \"rue du Fort\", \"number\": 16, \"postal\": 4000, \"price\": 1}]");
    }

    @When("I want to search rentals on brand")
    public void i_want_to_search_rentals_on_brand() {
        response = client.get().uri("api/rental/search?brand=Toyota").headers(headers -> headers.setBearerAuth(token)).exchange();
    }

    @Then("the data of all rentals using the car with the given brand is returned in a JSON format")
    public void the_data_of_all_rentals_using_the_car_with_the_given_brand_is_returned_in_a_JSON_format() {
        response.expectStatus().isOk().expectBody().json("[{\"startDate\": \"2025-06-08\", \"startTime\": \"10:00:00\", \"endDate\": \"2025-06-10\", \"endTime\": \"11:00:00\", \"city\": \"Leuven\", \"phoneNumber\": \"0491 12 13 14\", \"email\": \"owner1@owner.com\", \"street\": \"Bondgenotenlaan\", \"number\": 16, \"postal\": 3000, \"price\": 1}, {\"startDate\": \"2025-08-08\", \"startTime\": \"10:00:00\", \"endDate\": \"2025-08-10\", \"endTime\": \"11:00:00\", \"city\": \"Heist\", \"phoneNumber\": \"0491 12 13 14\", \"email\": \"owner1@owner.com\", \"street\": \"Moerstraat\", \"number\": 62, \"postal\": 2220, \"price\": 1}]");
    }

    @When("I want to get rental with id 1")
    public void I_want_to_get_rental_with_id_1() {
        response = client.get().uri("api/rental/{id}", rental_id1).headers(headers -> headers.setBearerAuth(token)).exchange();
    }

    @Then("the data of the first rental is returned in a JSON format")
    public void the_data_of_the_first_rental_is_returned_in_a_JSON_format() {
        response.expectStatus().isOk().expectBody().json("{\"startDate\": \"2025-06-08\", \"startTime\": \"10:00:00\", \"endDate\": \"2025-06-10\", \"endTime\": \"11:00:00\", \"city\": \"Leuven\", \"phoneNumber\": \"0491 12 13 14\", \"email\": \"owner1@owner.com\", \"street\": \"Bondgenotenlaan\", \"number\": 16, \"postal\": 3000, \"price\": 1}");
    }

    @When("I add a rental to it")
    public void I_add_a_rental_to_it() {
        response = client.post().uri("api/rental/{carId}/addRental", car_id1).headers(headers -> headers.setBearerAuth(token)).bodyValue("{\"startDate\": \"2025-06-08\", \"startTime\": \"10:00:00\", \"endDate\": \"2025-06-10\", \"endTime\": \"11:00:00\", \"city\": \"Leuven\", \"phoneNumber\": \"0491 12 13 14\", \"email\": \"owner1@owner.com\", \"street\": \"Bondgenotenlaan\", \"number\": 16, \"postal\": 3000, \"price\": 1}").exchange();
        response = client.get().uri("api/rental/search?startDate=2025-06-08").headers(headers -> headers.setBearerAuth(token)).exchange();
    }
    
    @Then("the rental is added to the car")
    public void the_rental_is_added_to_the_car() {
        response.expectStatus().isOk().expectBody().json("[{\"startDate\": \"2025-06-08\", \"startTime\": \"10:00:00\", \"endDate\": \"2025-06-10\", \"endTime\": \"11:00:00\", \"city\": \"Leuven\", \"phoneNumber\": \"0491 12 13 14\", \"email\": \"owner1@owner.com\", \"street\": \"Bondgenotenlaan\", \"number\": 16, \"postal\": 3000, \"price\": 1}]");
    }


 
    @Given("some notifications")
    public void some_notifications() {
        userRepository.save(user1);
        car.setUser(user1);
        Car car_response = carRepository.save(car);

        car_id1 = car_response.getId();

        Rental rental1 = new Rental(endDate_1, startTime_1, startDate_1, endTime_1, "Leuven", "0491 12 13 14", "owner1@owner.com", "Bondgenotenlaan", 16, 3000, 1);
        rental1.setCar(car);
        Rental savedRental = rentalRepository.save(rental1);

        Notification notification1 = new Notification(savedRental.getId(), "Toyota", CarType.SUV, "XHA-412", startDate_1, endDate_1, "owner1@owner.com", "renter1@renter.com", Notification.NotificationType.RENT, Notification.NotificationStatus.UNREAD);
        notification1.setUser(user1);
        notificationRepository.save(notification1);

        Notification notification2 = new Notification(savedRental.getId(), "Toyota", CarType.SUV, "XHA-412", startDate_1, endDate_1, "owner1@owner.com", "renter2@renter.com", Notification.NotificationType.CONFIRM, Notification.NotificationStatus.UNREAD);
        notification2.setUser(user1);
        notificationRepository.save(notification2);

        token = jwtGenerator.generateToken(user1);
    }


    @When("I want to get all notifications")
    public void i_want_to_get_all_notifications() {
        response = client.get().uri("api/notification").headers(headers -> headers.setBearerAuth(token)).exchange();
    }

    @Then("the data of all notifications is returned in a JSON format")
    public void the_data_of_all_notifications_is_returned_in_a_json_format() {
        Rental savedRental = rentalRepository.findAll().get(0); 
        long actualRentId = savedRental.getId();
        
        response.expectStatus().isOk().expectBody().json("[{\"rentId\": " + actualRentId + ", \"brand\": \"Toyota\", \"type\": \"SUV\", \"licensePlate\": \"XHA-412\", \"startDate\": \"2025-06-08\", \"endDate\": \"2025-06-10\", \"emailOwner\": \"owner1@owner.com\", \"emailRenter\": \"renter1@renter.com\", \"notificationType\": \"RENT\", \"notificationStatus\": \"UNREAD\"}, {\"rentId\": " + actualRentId + ", \"brand\": \"Toyota\", \"type\": \"SUV\", \"licensePlate\": \"XHA-412\", \"startDate\": \"2025-06-08\", \"endDate\": \"2025-06-10\", \"emailOwner\": \"owner1@owner.com\", \"emailRenter\": \"renter2@renter.com\", \"notificationType\": \"CONFIRM\", \"notificationStatus\": \"UNREAD\"}]");
    }


    @When("I add a notification to the user")
    public void i_add_a_notification_to_the_user() {
        Notification newNotification = new Notification(rental_id1, "Toyota", CarType.SUV, "XHA-412", startDate_1, endDate_1, "owner1@owner.com", "renter3@renter.com", Notification.NotificationType.CANCEL, Notification.NotificationStatus.UNREAD);
        response = client.post().uri("api/notification/{userEmail}", "owner1@owner.com").headers(headers -> headers.setBearerAuth(token)).bodyValue(newNotification).exchange();
    }


    @Given("no notifications")
    public void no_notifications() {
        notificationRepository.deleteAll();
        userRepository.save(user1);
        token = jwtGenerator.generateToken(user1);
    }


    @Given("a notification with id {int}")
    public void a_notification_with_id(Integer id) {
        userRepository.save(user1); 
        Notification notification = new Notification(rental_id1, "Toyota", CarType.SUV, "XHA-412", startDate_1, endDate_1, "owner1@owner.com", "renter3@renter.com", Notification.NotificationType.CANCEL, Notification.NotificationStatus.UNREAD);
        notification.setUser(user1);
        Notification savedNotification = notificationRepository.save(notification);
        notification_id1 = savedNotification.getId(); 
        token = jwtGenerator.generateToken(user1);
    }
    
    @When("I remove the notification with id {int}")
    public void i_remove_the_notification_with_id(Integer id) {
        response = client.delete().uri("api/notification/{notificationId}", notification_id1).headers(headers -> headers.setBearerAuth(token)).exchange();
    }
    
    @Then("the notification is successfully removed")
    public void the_notification_is_successfully_removed() {
        response.expectStatus().isOk();
        assert(notificationRepository.findById(notification_id1).isEmpty());
    }

    @Given("a notification with id {int} and status UNREAD")
    public void a_notification_with_id_and_status_UNREAD(Integer id) {
        userRepository.save(user1);
        Notification notification = new Notification(rental_id1, "Toyota", CarType.SUV, "XHA-412", startDate_1, endDate_1, "owner1@owner.com", "renter3@renter.com", Notification.NotificationType.CANCEL, Notification.NotificationStatus.UNREAD);
        notification.setUser(user1);
        Notification savedNotification = notificationRepository.save(notification);
        notification_id1 = savedNotification.getId(); 
        token = jwtGenerator.generateToken(user1);
    }

    @When("I update the status of the notification with id {int}")
    public void i_update_the_status_of_the_notification_with_id(Integer id) {
        response = client.put().uri("api/notification/{notificationId}", notification_id1).headers(headers -> headers.setBearerAuth(token)).exchange();
    }

    @Then("the notification status is updated to ARCHIVED and returned in a JSON format")
    public void the_notification_status_is_updated_to_archived_and_returned_in_a_json_format() {
        response.expectStatus().isOk().expectBody().jsonPath("$.notificationStatus").isEqualTo("ARCHIVED");
    }

}