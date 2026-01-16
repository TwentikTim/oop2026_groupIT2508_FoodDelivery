package edu.aitu.oop3.service;

import edu.aitu.oop3.entities.MenuItem;
import edu.aitu.oop3.exceptions.MenuItemNotAvailableException;

public class MenuService {

    public void checkAvailability(MenuItem item) {
        if (item == null) {
            throw new RuntimeException("Menu item is null");
        }

        if (!item.isAvailable()) {
            throw new MenuItemNotAvailableException(item.getId(), item.getName());
        }
    }
}
