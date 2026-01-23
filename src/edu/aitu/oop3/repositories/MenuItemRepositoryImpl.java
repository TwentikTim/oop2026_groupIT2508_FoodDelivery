package edu.aitu.oop3.repositories;

import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.entities.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemRepositoryImpl implements MenuItemRepository {

    private final IDB db;

    public MenuItemRepositoryImpl(IDB db) {
        this.db = db;
    }

    @Override
    public List<MenuItem> findAll() {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT id, name, price, available FROM menu_items ORDER BY id";

        try (Connection con = db.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                items.add(new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getBoolean("available")
                ));
            }
            return items;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to list menu items", e);
        }
    }

    @Override
    public MenuItem findById(int id) {
        String sql = "SELECT id, name, price, available FROM menu_items WHERE id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getBoolean("available")
                );
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find menu item", e);
        }
    }

    @Override
    public void updateAvailability(int id, boolean available) {
        String sql = "UPDATE menu_items SET available = ? WHERE id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, available);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update availability", e);
        }
    }
}
