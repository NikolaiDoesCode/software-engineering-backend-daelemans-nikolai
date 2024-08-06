Feature: App
    Scenario: get all rentals when there are already rentals
        Given some rentals
        When I want to get all rentals 
        Then the data of all rental is returned in a JSON format  

    Scenario: get empty rentals when there are no rentals
        Given no rentals
        When I want to get all rentals
        Then the returned JSON is empty

    Scenario: search rentals on email when there are already rentals
        Given some rentals of different users
        When I want to search rentals on email
        Then the data of all rentals of the user with given email is returned in a JSON format

    Scenario: search rentals on startDate when there are already rentals
        Given some rentals of different users
        When I want to search rentals on startDate
        Then the data of all rentals starting on the given startDate is returned in a JSON format

    Scenario: search rentals on endDate when there are already rentals
        Given some rentals of different users
        When I want to search rentals on endDate
        Then the data of all rentals ending on the given endDate is returned in a JSON format

    Scenario: search rentals on car brand when there are already rentals
        Given some rentals of different users
        When I want to search rentals on brand
        Then the data of all rentals using the car with the given brand is returned in a JSON format

    Scenario: get rental with id 1 when there are already rentals
        Given some rentals
        When I want to get rental with id 1
        Then the data of the first rental is returned in a JSON format  
    
    Scenario: add rental to a car
        Given some rentals
        When I add a rental to it
        Then the rental is added to the car

    Scenario: get all notifications when there are already notifications
        Given some notifications
        When I want to get all notifications
        Then the data of all notifications is returned in a JSON format 

    Scenario: get empty notifications when there are no notifications
        Given no notifications
        When I want to get all notifications
        Then the returned JSON is empty

    Scenario: remove a notification by id
        Given a notification with id 1
        When I remove the notification with id 1
        Then the notification is successfully removed

    Scenario: update status of a notification by id
        Given a notification with id 1 and status UNREAD
        When I update the status of the notification with id 1
        Then the notification status is updated to ARCHIVED and returned in a JSON format