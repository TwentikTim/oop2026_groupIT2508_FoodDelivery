package edu.aitu.oop3.OrderingComponent;

public interface OrderItemRepository {
    void add(int orderId, int menuItemId, int quantity);
    double calculateTotal(int orderId);
}
