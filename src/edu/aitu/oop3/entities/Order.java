package edu.aitu.oop3.entities;

public class Order {

    private int id;
    private int customerId;
    private OrderStatus status;
    private OrderType type;


    public Order(int id, int customerId, OrderStatus status) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.type = OrderType.PICKUP; // default
    }


    public Order(int id, int customerId, OrderStatus status, OrderType type) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.type = type;
    }

    private Order(Builder builder) {
        this.id = builder.id;
        this.customerId = builder.customerId;
        this.status = builder.status;
        this.type = builder.type;
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

    public OrderType getType() {
        return type;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id;
        private int customerId;
        private OrderStatus status;
        private OrderType type = OrderType.PICKUP;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder customerId(int customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder type(OrderType type) {
            this.type = type;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
