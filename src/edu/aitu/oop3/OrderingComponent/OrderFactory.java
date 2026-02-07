package edu.aitu.oop3.OrderingComponent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderFactory {


    public static OrderInterface createOrderFromResultSet(ResultSet rs) throws SQLException {

        String orderType = "pickup"; ;

        int id = rs.getInt("id");
        int customerId = rs.getInt("customer_id");
        OrderStatus status = OrderStatus.valueOf(rs.getString("status"));

        switch (orderType) {
            case "pickup":
                return new PickupOrder(id, customerId, status);
            case "delivery":

                String address = "Astana IT University";
                return new DeliveryOrder(id, customerId, status, address);
            default:
                return Order.builder()
                        .id(id)
                        .customerId(customerId)
                        .status(status)
                        .build();
        }
    }
}
