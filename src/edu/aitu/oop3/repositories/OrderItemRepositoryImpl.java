package edu.aitu.oop3.repositories;

import edu.aitu.oop3.db.IDB;
import java.sql.*;

public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final IDB db;

    public OrderItemRepositoryImpl(IDB db) {
        this.db = db;
    }

    @Override
    public void add(int orderId, int menuItemId, int quantity) {
        String sql = "INSERT INTO order_items(order_id, menu_item_id, quantity) VALUES (?, ?, ?)";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, menuItemId);
            ps.setInt(3, quantity);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
