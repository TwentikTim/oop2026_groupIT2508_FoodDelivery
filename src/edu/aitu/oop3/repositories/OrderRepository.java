package edu.aitu.oop3.repositories;

import edu.aitu.oop3.entities.Order;
import edu.aitu.oop3.entities.OrderStatus;

import java.util.List;

public interface OrderRepository {

    int create(Order order);

    Order findById(int id);

    List<Order> findActiveOrders();

    void updateStatus(int orderId, OrderStatus status);
}
