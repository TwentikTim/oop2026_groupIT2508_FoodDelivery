package edu.aitu.oop3.entities;

public class DeliveryOrder implements OrderInterface {
    private final int id;
    private final int customerId;
    private final OrderStatus status;
    private final String deliveryAddress;

    public DeliveryOrder(int id, int customerId, OrderStatus status, String deliveryAddress) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.deliveryAddress = deliveryAddress;
    }


    @Override
    public int getId() { return id; }
    @Override
    public int getCustomerId() { return customerId; }
    @Override
    public OrderStatus getStatus() { return status; }

    @Override
    public String toString() {
        return "DeliveryOrder{" +
                "id=" + id +
                ", status=" + status +
                ", address='" + deliveryAddress + '\'' +
                '}';
    }
}
