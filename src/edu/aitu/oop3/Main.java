package edu.aitu.oop3;

import edu.aitu.oop3.db.DatabaseConnection;
import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.entities.Customer;
import edu.aitu.oop3.entities.MenuItem;
import edu.aitu.oop3.entities.Order;
import edu.aitu.oop3.entities.OrderStatus;
import edu.aitu.oop3.exceptions.OrderNotFoundException;
import edu.aitu.oop3.repositories.*;
import edu.aitu.oop3.service.OrderService;
import edu.aitu.oop3.service.PaymentService;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String ADMIN_PASSWORD = "1234";

    public static void main(String[] args) {

        IDB db = new DatabaseConnection();

        CustomerRepository customerRepo = new CustomerRepositoryImpl(db);
        MenuItemRepository menuRepo = new MenuItemRepositoryImpl(db);
        OrderRepository orderRepo = new OrderRepositoryImpl(db);
        OrderItemRepository orderItemRepo = new OrderItemRepositoryImpl(db);

        OrderService orderService = new OrderService(orderRepo, menuRepo, orderItemRepo);
        PaymentService paymentService = new PaymentService(customerRepo, orderRepo);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nFood Delivery");
            System.out.println("1. Order Food");
            System.out.println("2. Check Order Status");
            System.out.println("3. Add Money");
            System.out.println("4. Pay for Order");
            System.out.println("5. Admin Menu");
            System.out.println("0. Exit");
            System.out.print("Enter: ");

            int choice = readInt(sc);

            switch (choice) {
                case 1 -> orderFood(sc, customerRepo, menuRepo, orderService);
                case 2 -> checkStatus(sc, orderService);
                case 3 -> addMoney(sc, orderService,paymentService);
                case 4 -> payOrder(sc, orderService, paymentService);
                case 5 -> adminMenu(sc, menuRepo, orderService);
                case 0 -> {
                    System.out.println("Goodbye.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void orderFood(
            Scanner sc,
            CustomerRepository customerRepo,
            MenuItemRepository menuRepo,
            OrderService orderService
    ) {
        sc.nextLine();
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        if (!name.matches("[A-Za-z ]+")) {
            System.out.println("Invalid name. Use letters only.");
            return;
        }

        int customerId = customerRepo.create(new Customer(name));
        int orderId = orderService.createOrder(customerId);

        Integer currentItemId = null;

        while (true) {

            if (currentItemId == null) {
                showMenu(menuRepo);
                System.out.print("Choose item: ");
                int chosen = readInt(sc);

                MenuItem item = menuRepo.findById(chosen);
                if (item == null) {
                    System.out.println("Invalid menu item.");
                    continue;
                }
                if (!item.isAvailable()) {
                    System.out.println("This item is not available.");
                    continue;
                }
                currentItemId = chosen;
            }

            System.out.print("Enter quantity: ");
            int quantity = readInt(sc);

            if (quantity <= 0) {
                System.out.println("Invalid quantity.");
                continue;
            }

            orderService.addItemToOrder(orderId, currentItemId, quantity);

            while (true) {
                System.out.println("\nAnything else?");
                showMenu(menuRepo);
                System.out.println("5. Order the food");
                System.out.print("Enter: ");

                int next = readInt(sc);

                if (next == 5) {
                    double total = orderService.calculateTotal(orderId);
                    System.out.println("Order waiting for payment.");
                    System.out.println("Your order ID: " + orderId);
                    System.out.println("Status:" + OrderStatus.PENDING_PAYMENT);
                    System.out.println("Total price: " + total);
                    System.out.println("Use 3 to add money and menu 4 to pay");
                    return;
                }

                MenuItem nextItem = menuRepo.findById(next);
                if (nextItem != null) {
                    if (!nextItem.isAvailable()) {
                        System.out.println("This item is not available.");
                        continue;
                    }
                    currentItemId = next;
                    break;
                }

                System.out.println("Invalid option.");
            }
        }
    }

    private static void checkStatus(Scanner sc, OrderService orderService) {
        System.out.print("Enter order ID: ");
        int orderId = readInt(sc);

        try {
            System.out.println("Order status: " + orderService.getStatus(orderId));
        } catch (OrderNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Database error. Try again.");
        }
    }

    private static void addMoney(Scanner sc, OrderService orderService,PaymentService paymentService) {
        System.out.println("Enter order ID: ");
        int orderId = readInt(sc);

        try {
            Order order = orderService.getOrderOrThrow(orderId);

            System.out.println("Enter amount: ");
            double amount = readDouble(sc);
            if (amount <= 0) {
                System.out.println("Invalid amount.");
                return;
            }

            paymentService.addMoneyByOrderId(order, amount);
            System.out.println("Money added successfully.");
        } catch (OrderNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Database error. Try again.");
        }
    }

    private static void payOrder(Scanner sc, OrderService orderService, PaymentService paymentService) {
        System.out.println("Enter Order ID: ");
        int orderId = readInt(sc);

        try {
            Order order = orderService.getOrderOrThrow(orderId);
            double total = orderService.calculateTotal(orderId);

            System.out.println("Total price: " + total);

            paymentService.pay(order, total);
            System.out.println("Payment successful. Order placed");
            System.out.println("Current Status:" + OrderStatus.COOKING);
        } catch (OrderNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Not enough balance.");
        }
    }

//AdminMenu
    private static void adminMenu(
            Scanner sc,
            MenuItemRepository menuRepo,
            OrderService orderService
    ) {
        System.out.print("Enter admin password: ");
        String pass = sc.next();

        if (!ADMIN_PASSWORD.equals(pass)) {
            System.out.println("Access denied.");
            return;
        }

        while (true) {
            System.out.println("\nAdmin Menu");
            System.out.println("1. View menu items");
            System.out.println("2. Change menu item availability");
            System.out.println("3. View active orders");
            System.out.println("4. Update order status");
            System.out.println("0. Back");
            System.out.print("Enter: ");

            int choice = readInt(sc);

            switch (choice) {

                case 1 -> showMenu(menuRepo);

                case 2 -> {
                    showMenu(menuRepo);
                    System.out.print("Enter item ID: ");
                    int id = readInt(sc);

                    MenuItem item = menuRepo.findById(id);
                    if (item == null) {
                        System.out.println("Invalid menu item.");
                        continue;
                    }

                    System.out.print("1. Available  2. Not available: ");
                    int a = readInt(sc);

                    if (a == 1 || a == 2) {
                        menuRepo.updateAvailability(id, a == 1);
                        System.out.println("Availability updated.");
                    } else {
                        System.out.println("Invalid option.");
                    }
                }

                case 3 -> {
                    try {
                        List<Order> orders = orderService.getActiveOrders();
                        if (orders.isEmpty()) {
                            System.out.println("There is no active orders");
                        } else {
                            for (Order o : orders) {
                                System.out.println(
                                        "Order ID: " + o.getId() +
                                                ", Customer ID: " + o.getCustomerId() +
                                                ", Status: " + o.getStatus()
                                );
                            }
                        }
                    } catch (RuntimeException e) {
                        System.out.println("Database error. Try again.");
                    }
                }

                case 4 -> {
                    try {
                        List<Order> active = orderService.getActiveOrders();
                        if (active.isEmpty()) {
                            System.out.println("There is no order at the time");
                            continue;
                        }

                        System.out.print("Enter order ID: ");
                        int orderId = readInt(sc);

                        boolean exists = false;
                        for (Order o : active) {
                            if (o.getId() == orderId) {
                                exists = true;
                                break;
                            }
                        }
                        if (!exists) {
                            System.out.println("There is no order at the time");
                            continue;
                        }

                        System.out.println("1. COOKING");
                        System.out.println("2. ON_THE_WAY");
                        System.out.println("3. DELIVERED");
                        System.out.print("Choose status: ");

                        int s = readInt(sc);

                        OrderStatus status = switch (s) {
                            case 1 -> OrderStatus.COOKING;
                            case 2 -> OrderStatus.ON_THE_WAY;
                            case 3 -> OrderStatus.DELIVERED;
                            default -> null;
                        };

                        if (status == null) {
                            System.out.println("Invalid status.");
                            continue;
                        }

                        orderService.updateStatus(orderId, status);
                        System.out.println("Status updated.");

                    } catch (OrderNotFoundException e) {
                        System.out.println("There is no order at the time");
                    } catch (RuntimeException e) {
                        System.out.println("Database error. Try again.");
                    }
                }

                case 0 -> {
                    return;
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void showMenu(MenuItemRepository repo) {
        System.out.println("\nMenu:");
        for (MenuItem item : repo.findAll()) {
            System.out.println(
                    item.getId() + ". " +
                            item.getName() + " - " +
                            item.getPrice() +
                            (item.isAvailable() ? "" : " [NOT AVAILABLE]")
            );
        }
    }

    private static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Enter a number: ");
        }
        return sc.nextInt();
    }

    private static double readDouble(Scanner sc) {
        while (!sc.hasNextDouble()) {
            sc.next();
            System.out.println("Enter a number: ");
        }
        return sc.nextDouble();
    }

}
//test