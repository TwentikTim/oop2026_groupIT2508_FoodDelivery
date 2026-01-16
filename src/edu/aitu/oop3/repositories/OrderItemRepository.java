package edu.aitu.oop3.repositories;

public interface OrderItemRepository {
    void add(int orderId, int menuItemId, int quantity);
}
