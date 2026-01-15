package edu.aitu.oop3.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(int orderId) {
        super("Order number " + orderId + " was not found.");
    }
}
