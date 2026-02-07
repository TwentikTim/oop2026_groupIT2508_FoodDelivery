package edu.aitu.oop3.OrderingComponent;

import edu.aitu.oop3.MenuComponent.MenuItem;
import edu.aitu.oop3.exceptions.InvalidQuantityException;
import edu.aitu.oop3.exceptions.MenuItemNotAvailableException;
import edu.aitu.oop3.exceptions.OrderNotFoundException;
import edu.aitu.oop3.MenuComponent.MenuItemRepository;
import edu.aitu.oop3.BillingComponent.TaxConfig;

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

    public int createOrder(int customerId, String orderType, String address) {
        return orderRepo.create(customerId, orderType, address);
    }

    public void addItemToOrder(int orderId, int menuItemId, int quantity) {
        if (quantity <= 0) throw new InvalidQuantityException();


        MenuItem item = menuRepo.findById(menuItemId);
        if (item == null || !item.isAvailable()) {
            throw new MenuItemNotAvailableException(item == null ? "This item is available." : item.getName());
        }

        orderItemRepo.add(orderId, menuItemId, quantity);
    }

    public OrderStatus getStatus(int orderId) {
        OrderInterface order = orderRepo.findById(orderId);
        if (order == null) throw new OrderNotFoundException(orderId);
        return order.getStatus();
    }

    public List<OrderInterface> getActiveOrders() {
        return orderRepo.findActive();
    }

    public void updateStatus(int orderId, OrderStatus status) {
        if (orderRepo.findById(orderId) == null) throw new OrderNotFoundException(orderId);
        orderRepo.updateStatus(orderId, status);
    }

    public OrderInterface getOrderOrThrow(int orderId) {
        OrderInterface o = orderRepo.findById(orderId);
        if (o == null) throw new OrderNotFoundException(orderId);
        return o;
    }

    public double calculateTotal(int orderId) {
        getOrderOrThrow(orderId);
        return orderItemRepo.calculateTotal(orderId);
    }

    public double calculateTotalWithTax(int orderId) {
        double baseTotal = calculateTotal(orderId);
        double taxRate = TaxConfig.getInstance().getTaxRate();
        return baseTotal * (1 + taxRate);
    }
}