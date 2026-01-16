package edu.aitu.oop3.repositories;

import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.entities.Order;
import edu.aitu.oop3.entities.OrderStatus;
import edu.aitu.oop3.exceptions.OrderNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {

    private final IDB db;

    public OrderRepositoryImpl(IDB db) {
        this.db = db;
    }

    @Override
    public int create(Order order) {
        String sql = "INSERT INTO orders(customer_id, status) VALUES (?, ?) RETURNING id";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, order.getCustomerId());
            ps.setString(2, order.getStatus().name());

            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("id");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create an order", e);
        }
    }

    @Override
    public Order findById(int id) {
        String sql = "SELECT id, customer_id, status FROM orders WHERE id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new OrderNotFoundException(id);
                }
                return mapOrder(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find the order by id", e);
        }
    }

    @Override
    public List<Order> findActiveOrders() {
        String sql = "SELECT id, customer_id, status FROM orders WHERE status <> 'DELIVERED' ORDER BY id";

        List<Order> orders = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                orders.add(mapOrder(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while getting active orders", e);
        }

        return orders;
    }

    @Override
    public void updateStatus(int orderId, OrderStatus status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status.name()); // enum -> String
            ps.setInt(2, orderId);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new OrderNotFoundException(orderId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while updating order status", e);
        }
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int customerId = rs.getInt("customer_id");
        OrderStatus status = OrderStatus.valueOf(rs.getString("status"));

        return new Order(id, customerId, status);
    }
}
