package edu.aitu.oop3.factory;

import edu.aitu.oop3.entities.Order;
import edu.aitu.oop3.entities.OrderStatus;
import edu.aitu.oop3.service.TaxConfig;

public class OrderFactory {

    public static Order createNewOrder(int customerId) {
        return Order.builder()
                .customerId(customerId)
                .status(OrderStatus.PENDING_PAYMENT)
                .taxRate(TaxConfig.getInstance().getTaxRate())
                .build();
    }
}
