package edu.aitu.oop3.OrderingComponent;

public class OrderItem {

    private int orderId;
    private int menuItemId;
    private int quantity;

    public OrderItem(int id, int orderId, int menuItemId, int quantity) {
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }
}