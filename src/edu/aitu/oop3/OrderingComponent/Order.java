package edu.aitu.oop3.OrderingComponent;

public class Order implements OrderInterface {
    private final int id;
    private final int customerId;
    private final OrderStatus status;


    private Order(Builder builder) {
        this.id = builder.id;
        this.customerId = builder.customerId;
        this.status = builder.status;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public OrderStatus getStatus() {return status;}

    public String getDeliveryAddress() {return null;}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id;
        private int customerId;
        private OrderStatus status;

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

        public Order build() {
            if (customerId <= 0) {
                throw new IllegalStateException("customerId must be set.");
            }
            return new Order(this);
        }
    }

}