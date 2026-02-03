package edu.aitu.oop3.repositories;

import edu.aitu.oop3.entities.Order;
import edu.aitu.oop3.entities.OrderStatus;

import java.util.List;

public interface OrderRepository extends Repository<Order, Integer> {

    int create(int customerId);

    List<Order> findActive();

    void updateStatus(int id, OrderStatus status);
}