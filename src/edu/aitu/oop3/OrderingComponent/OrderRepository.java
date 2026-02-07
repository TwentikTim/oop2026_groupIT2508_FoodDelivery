package edu.aitu.oop3.OrderingComponent;

import edu.aitu.oop3.DataComponent.Repository;

import java.util.List;

public interface OrderRepository extends Repository<OrderInterface, Integer> {

    int create(int customerId, String orderType, String address);

    List<OrderInterface> findActive();

    void updateStatus(int id, OrderStatus status);
}