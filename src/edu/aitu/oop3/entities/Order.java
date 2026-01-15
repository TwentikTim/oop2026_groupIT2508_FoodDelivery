package edu.aitu.oop3.entities;

public class Order {
    private int id;
    private int customerId;
    private OrderStatus status;

    public Order(int customerId, OrderStatus status) {
        this.customerId = customerId;
        this.status = status;
    }

    public Order(int id, int customerId, OrderStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
