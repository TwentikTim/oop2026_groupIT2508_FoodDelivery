package edu.aitu.oop3.BillingComponent;


import edu.aitu.oop3.OrderingComponent.OrderInterface;
import edu.aitu.oop3.OrderingComponent.OrderStatus;
import edu.aitu.oop3.exceptions.InsufficientBalanceException;
import edu.aitu.oop3.CustomerComponent.CustomerRepository;
import edu.aitu.oop3.OrderingComponent.OrderRepository;

public class PaymentService {

    private CustomerRepository customerRepo;
    private OrderRepository orderRepo;

    public PaymentService(CustomerRepository customerRepo, OrderRepository orderRepo) {
        this.customerRepo = customerRepo;
        this.orderRepo = orderRepo;
    }

    public void addMoneyByOrderId(OrderInterface order, double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Invalid amount");
        customerRepo.addBalance(order.getCustomerId(), amount);
    }

    public void pay(OrderInterface order, double finalPriceWithTax) {

        double balance = customerRepo.getBalance(order.getCustomerId());
        if (balance < finalPriceWithTax) {
            throw new InsufficientBalanceException(balance, finalPriceWithTax);
        }

        customerRepo.subtractBalance(order.getCustomerId(), finalPriceWithTax);
        orderRepo.updateStatus(order.getId(), OrderStatus.COOKING);
    }
}
