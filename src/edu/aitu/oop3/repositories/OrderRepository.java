package edu.aitu.oop3.repositories;

import edu.aitu.oop3.entities.Order;
import edu.aitu.oop3.entities.OrderStatus;

import java.util.List;


public interface OrderRepository {

    int create(int customerId);

    Order findById(int id);

    List<Order> findActive();

    void updateStatus(int id, OrderStatus status);
}
