package edu.aitu.oop3.exceptions;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(double balance, double required) {
        super("Not enough balance. Your balance: " + balance + ", required: " + required);
    }
}
