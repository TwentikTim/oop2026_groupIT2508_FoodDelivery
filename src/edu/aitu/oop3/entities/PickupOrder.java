package edu.aitu.oop3.entities;

public class PickupOrder implements OrderInterface {
    private final int id;
    private final int customerId;
    private final OrderStatus status;

    public PickupOrder(int id, int customerId, OrderStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
    }

    @Override
    public int getId() { return id; }
    @Override
    public int getCustomerId() { return customerId; }
    @Override
    public OrderStatus getStatus() { return status; }

    @Override
    public String toString() {
        return "PickupOrder{" +
                "id=" + id +
                ", status=" + status +
                '}';
    }
}
