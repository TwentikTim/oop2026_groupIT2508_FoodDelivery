package edu.aitu.oop3.repositories;

import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.entities.Order;
import edu.aitu.oop3.entities.OrderStatus;

import java.sql.*;
import java.util.List;

public class OrderRepositoryImpl extends BaseRepository<Order, Integer> implements OrderRepository {

    public OrderRepositoryImpl(IDB db) {
        super(db);
    }

    private final RowMapper<Order> orderMapper = rs -> Order.builder()
            .id(rs.getInt("id"))
            .customerId(rs.getInt("customer_id"))
            .status(OrderStatus.valueOf(rs.getString("status")))
            .build();

    @Override
    public int create(int customerId) {
        String sql = "INSERT INTO orders(customer_id, status) VALUES (?, ?) RETURNING id";
        List<Order> orders = query(sql, rs -> {
            try {
                return Order.builder()
                        .id(rs.getInt("id"))
                        .customerId(customerId)
                        .build();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, customerId, OrderStatus.PENDING_PAYMENT.name());
        return orders.get(0).getId();
    }

    @Override
    public Order findById(Integer id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        List<Order> orders = query(sql, orderMapper, id);
        return orders.isEmpty() ? null : orders.get(0);
    }

    @Override
    public List<Order> findActive() {
        String sql = "SELECT * FROM orders WHERE status IN ('COOKING','ON_THE_WAY') ORDER BY id";
        return query(sql, orderMapper);
    }

    @Override
    public void updateStatus(int id, OrderStatus status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        executeUpdate(sql, status.name(), id);
    }
}
