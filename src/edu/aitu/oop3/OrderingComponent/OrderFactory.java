package edu.aitu.oop3.OrderingComponent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderFactory {

    public static OrderInterface createOrderFromResultSet(ResultSet rs) throws SQLException {

        int id = rs.getInt("id");
        int customerId = rs.getInt("customer_id");
        OrderStatus status =
                OrderStatus.valueOf(rs.getString("status"));

        String orderType = rs.getString("order_type");
        String address = rs.getString("delivery_address");


        Order baseOrder = Order.builder()
                .id(id)
                .customerId(customerId)
                .status(status)
                .build();


        if ("delivery".equalsIgnoreCase(orderType)) {
            return new DeliveryOrder(
                    baseOrder.getId(),
                    baseOrder.getCustomerId(),
                    baseOrder.getStatus(),
                    address
            );
        }

        return new PickupOrder(
                baseOrder.getId(),
                baseOrder.getCustomerId(),
                baseOrder.getStatus()
        );
    }
}
