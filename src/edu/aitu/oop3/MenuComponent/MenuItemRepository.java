package edu.aitu.oop3.MenuComponent;

import java.util.List;

public interface MenuItemRepository {
    List<MenuItem> findAll();
    MenuItem findById(Integer id);
    void updateAvailability(int id, boolean available);
}
