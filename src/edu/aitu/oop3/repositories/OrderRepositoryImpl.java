package edu.aitu.oop3.repositories;

import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.entities.OrderInterface;
import edu.aitu.oop3.entities.OrderStatus;
import edu.aitu.oop3.factory.OrderFactory; // Импортируем нашу фабрику

import java.sql.*;
import java.util.List;

public class OrderRepositoryImpl extends BaseRepository<OrderInterface, Integer> implements OrderRepository {

    public OrderRepositoryImpl(IDB db) {
        super(db);
    }


    private final RowMapper<OrderInterface> orderMapper = rs -> OrderFactory.createOrderFromResultSet(rs);

    @Override
    public List<OrderInterface> findAll(){
        String sql = "SELECT * FROM orders ORDER BY id";
        return query(sql, orderMapper);
    }

    @Override
    public int create(int customerId, String orderType,  String address) {

        String sql = "INSERT INTO orders(customer_id, status, order_type, delivery_address) VALUES (?, ?, ?, ?) RETURNING id";

        List<Integer> ids = this.<Integer>queryOtherType(sql, rs -> rs.getInt("id"),
                customerId,
                OrderStatus.PENDING_PAYMENT.name(),
                orderType,
                address);
        return ids.get(0);
    }

    @Override
    public OrderInterface findById(Integer id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        List<OrderInterface> orders = query(sql, orderMapper, id);
        return orders.isEmpty() ? null : orders.get(0);
    }

    @Override
    public List<OrderInterface> findActive() {
        String sql = "SELECT * FROM orders WHERE status IN ('COOKING','ON_THE_WAY') ORDER BY id";
        return query(sql, orderMapper);
    }

    @Override
    public void updateStatus(int id, OrderStatus status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        executeUpdate(sql, status.name(), id);
    }
}
