package edu.aitu.oop3.exceptions;

public class MenuItemNotAvailableException extends RuntimeException {

    public MenuItemNotAvailableException(String name) {
        super("Menu item not available: " + name);
    }
}
