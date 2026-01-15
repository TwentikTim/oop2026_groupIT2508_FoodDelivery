package edu.aitu.oop3.entities;

public class OrderItem {
    private int id;
    private int orderId;
    private int menuItemId;
    private int quantity;

    public OrderItem(int orderId, int menuItemId, int quantity) {
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
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
    public void setId(int id) {
        this.id = id;
    }

}