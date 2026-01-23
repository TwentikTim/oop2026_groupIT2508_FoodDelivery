package edu.aitu.oop3.repositories;

import edu.aitu.oop3.db.IDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            throw new RuntimeException("Failed to add orders item", e);
        }
    }

    @Override
    public double calculateTotal(int orderId) {
        String sql = """
                SELECT COALESCE(SUM(oi.quantity * mi.price), 0) AS total
                FROM order_items oi
                JOIN menu_items mi ON mi.id = oi.menu_item_id
                WHERE oi.order_id = ?;
                """;
        try(Connection con = db.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getDouble("total");
        }  catch (SQLException e) {
            throw new RuntimeException("Failed to calculate total", e);
        }
    }
}
