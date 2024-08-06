package com.team17.backend.Car.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team17.backend.Car.model.Car;
import com.team17.backend.Rental.model.Rental;
import com.team17.backend.User.model.Role;
import com.team17.backend.User.model.User;
import com.team17.backend.User.service.UserService;
import com.team17.backend.User.service.UserServiceException;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserService userService;

    public CarService() {
    }

    public List<Car> findAllCars() {
        List<Car> cars = carRepository.findAll();

        return cars;
    }

    public Car getCar(long id) {
        Car car = carRepository.findCarById(id);
        System.out.println("Found car with id " + id + ": " + car);
        return car;
    }

    public Car addCar(Car car, String userEmail) throws CarServiceException, UserServiceException {
        User user = userService.getUser(userEmail);
        if (user == null) {
            throw new CarServiceException("User", "User with email %s does not exist".formatted(userEmail));
        }

        if (user.getRole() != Role.OWNER) {
            throw new CarServiceException("User", "User is not an owner");
        }
        System.out.println("CAR");
        System.out.println(car.id);
        car.setUser(user);
        carRepository.save(car);
        return car;

    }

    public Car[] addCars(Car[] cars, String userEmail) throws CarServiceException, UserServiceException {
        for (Car car : cars) {
            addCar(car, userEmail);
        }
        return cars;
    }

    @Transactional
    public Car removeCar(long id) throws CarServiceException {
        Car removedCar = getCar(id);
        if (removedCar == null) {
            throw new CarServiceException("id", "Car with given id does not exist");
        }
        carRepository.deleteCarById(id);
        return removedCar;
    }
}