package services;

import edu.aitu.oop3.entities.Order;
import edu.aitu.oop3.entities.OrderStatus;
import edu.aitu.oop3.exceptions.InvalidQuantityException;
import edu.aitu.oop3.repositories.OrderRepository;

import java.util.List;

public class OrderService {

    private final OrderRepository orderRepo;

    public OrderService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }


    public int createOrder(int customerId) {
        Order order = new Order(customerId, OrderStatus.COOKING);
        return orderRepo.create(order);
    }


    public OrderStatus getStatus(int orderId) {
        Order order = orderRepo.findById(orderId);
        return order.getStatus();
    }


    public List<Order> getActiveOrders() {
        return orderRepo.findActiveOrders();
    }


    public void updateStatus(int orderId, OrderStatus newStatus) {
        Order order = orderRepo.findById(orderId);


        OrderStatus current = order.getStatus();
        if (!isValidTransition(current, newStatus)) {
            throw new RuntimeException(
                    "Invalid transition: " + current + " -> " + newStatus
            );
        }

        orderRepo.updateStatus(orderId, newStatus);
    }


    public void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException();
        }
    }


    private boolean isValidTransition(OrderStatus current, OrderStatus next) {
        return true;
    }

}
