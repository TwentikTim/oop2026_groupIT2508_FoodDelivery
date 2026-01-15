package edu.aitu.oop3.exceptions;

public class MenuItemNotAvailableException extends RuntimeException {
    public MenuItemNotAvailableException(int id, String name) {
        super("Unfortunately, '" + name + "' (id=" + id + ") is not available.");
    }
}
