package edu.aitu.oop3.repositories;

import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.entities.MenuItem;

import java.sql.*;
import java.util.*;

public class MenuItemRepositoryImpl implements MenuItemRepository {

    private final IDB db;

    public MenuItemRepositoryImpl(IDB db) {
        this.db = db;
    }

    @Override
    public List<MenuItem> findAll() {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM menu_items";

        try (Connection con = db.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                items.add(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return items;
    }

    @Override
    public MenuItem findById(int id) {
        String sql = "SELECT * FROM menu_items WHERE id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
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
            throw new RuntimeException(e);
        }
    }

    private MenuItem map(ResultSet rs) throws SQLException {
        return new MenuItem(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getBoolean("available")
        );
    }
}
