package edu.aitu.oop3.repositories;

import edu.aitu.oop3.entities.MenuItem;
import java.util.List;

public interface MenuItemRepository {
    List<MenuItem> findAll();
    MenuItem findById(int id);
    void updateAvailability(int id, boolean available);
}
