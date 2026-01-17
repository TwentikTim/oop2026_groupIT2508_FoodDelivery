package edu.aitu.oop3.service;

import edu.aitu.oop3.entities.MenuItem;
import edu.aitu.oop3.entities.Order;
import edu.aitu.oop3.entities.OrderStatus;
import edu.aitu.oop3.exceptions.InvalidQuantityException;
import edu.aitu.oop3.exceptions.MenuItemNotAvailableException;
import edu.aitu.oop3.exceptions.OrderNotFoundException;
import edu.aitu.oop3.repositories.MenuItemRepository;
import edu.aitu.oop3.repositories.OrderItemRepository;
import edu.aitu.oop3.repositories.OrderRepository;

import java.util.List;

public class OrderService {

    private final OrderRepository orderRepo;
    private final MenuItemRepository menuRepo;
    private final OrderItemRepository orderItemRepo;

    public OrderService(
            OrderRepository orderRepo,
            MenuItemRepository menuRepo,
            OrderItemRepository orderItemRepo
    ) {
        this.orderRepo = orderRepo;
        this.menuRepo = menuRepo;
        this.orderItemRepo = orderItemRepo;
    }

    public int createOrder(int customerId) {
        return orderRepo.create(customerId);
    }

    public void addItemToOrder(int orderId, int menuItemId, int quantity) {

        if (quantity <= 0) {
            throw new InvalidQuantityException();
        }

        MenuItem item = menuRepo.findById(menuItemId);

        if (item == null || !item.isAvailable()) {
            throw new MenuItemNotAvailableException(
                    item == null ? "Unknown item" : item.getName()
            );
        }

        orderItemRepo.add(orderId, menuItemId, quantity);
    }

    public OrderStatus getStatus(int orderId) {
        Order order = orderRepo.findById(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        return order.getStatus();
    }

    public List<Order> getActiveOrders() {
        return orderRepo.findActive();
    }

    public void updateStatus(int orderId, OrderStatus status) {
        if (orderRepo.findById(orderId) == null) {
            throw new OrderNotFoundException(orderId);
        }
        orderRepo.updateStatus(orderId, status);
    }
}
