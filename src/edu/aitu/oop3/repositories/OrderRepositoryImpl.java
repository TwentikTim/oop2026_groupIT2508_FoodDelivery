package edu.aitu.oop3.repositories;

import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.entities.Order;
import edu.aitu.oop3.entities.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {

    private final IDB db;

    public OrderRepositoryImpl(IDB db) {
        this.db = db;
    }

    @Override
    public int create(int customerId) {
        String sql =
                "INSERT INTO orders(customer_id, status) VALUES (?, ?) RETURNING id";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ps.setString(2, OrderStatus.COOKING.name());

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("id");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create order", e);
        }
    }

    @Override
    public Order findById(int id) {
        String sql = "SELECT * FROM orders WHERE id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Order(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        OrderStatus.valueOf(rs.getString("status"))
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public List<Order> findActive() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE status != 'DELIVERED'";

        try (Connection con = db.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        OrderStatus.valueOf(rs.getString("status"))
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orders;
    }

    @Override
    public void updateStatus(int id, OrderStatus status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
