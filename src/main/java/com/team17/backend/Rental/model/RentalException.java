package com.team17.backend.Rental.model;

public class RentalException extends Exception {
    public String field;

    public RentalException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return this.field;
    }
}
