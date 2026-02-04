package edu.aitu.oop3.repositories;

import edu.aitu.oop3.entities.OrderInterface;
import edu.aitu.oop3.entities.OrderStatus;

import java.util.List;

public interface OrderRepository extends Repository<OrderInterface, Integer> {

    int create(int customerId, String orderType, String address);

    List<OrderInterface> findActive();

    void updateStatus(int id, OrderStatus status);
}