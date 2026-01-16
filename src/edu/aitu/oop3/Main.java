package edu.aitu.oop3;

import edu.aitu.oop3.db.DatabaseConnection;
import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.entities.Customer;
import edu.aitu.oop3.entities.Order;
import edu.aitu.oop3.entities.OrderStatus;
import edu.aitu.oop3.exceptions.OrderNotFoundException;
import edu.aitu.oop3.repositories.CustomerRepository;
import edu.aitu.oop3.repositories.CustomerRepositoryImpl;
import edu.aitu.oop3.repositories.OrderRepository;
import edu.aitu.oop3.repositories.OrderRepositoryImpl;
import edu.aitu.oop3.services.OrderService;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final String ADMIN_PASSWORD = "1234";

    public static void main(String[] args) {

        IDB db = new DatabaseConnection();

        CustomerRepository customerRepo = new CustomerRepositoryImpl(db);
        OrderRepository orderRepo = new OrderRepositoryImpl(db);
        OrderService orderService = new OrderService(orderRepo);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nFood Delivery");
            System.out.println("1. Order food");
            System.out.println("2. Check order status");
            System.out.println("3. Admin menu");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int choice = readInt(sc);

            switch (choice) {
                case 1 -> placeOrder(sc, customerRepo, orderService);
                case 2 -> checkStatus(sc, orderService);
                case 3 -> adminMenu(sc, orderService);
                case 0 -> {
                    System.out.println("Goodbye.");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    //USER

    private static void placeOrder(
            Scanner sc,
            CustomerRepository customerRepo,
            OrderService orderService
    ) {
        sc.nextLine();
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        if (!isValidName(name)) {
            System.out.println("Invalid name. Use letters only.");
            return;
        }

        Customer customer = new Customer(name);
        int customerId = customerRepo.create(customer);

        int orderId = orderService.createOrder(customerId);

        System.out.println("Order created successfully.");
        System.out.println("Your order ID: " + orderId);
        System.out.println("Current status: COOKING");
    }

    private static void checkStatus(Scanner sc, OrderService orderService) {
        try {
            System.out.print("Enter order ID: ");
            int orderId = readInt(sc);

            OrderStatus status = orderService.getStatus(orderId);
            System.out.println("Order status: " + status);

        } catch (OrderNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    //ADMIN

    private static void adminMenu(Scanner sc, OrderService orderService) {
        System.out.print("Enter admin password: ");
        String pass = sc.next();

        if (!ADMIN_PASSWORD.equals(pass)) {
            System.out.println("Access denied.");
            return;
        }

        while (true) {
            System.out.println("\nAdmin menu");
            System.out.println("1. View active orders");
            System.out.println("2. Update order status");
            System.out.println("0. Back");
            System.out.print("Choose option: ");

            int choice = readInt(sc);

            switch (choice) {
                case 1 -> {
                    List<Order> orders = orderService.getActiveOrders();
                    if (orders.isEmpty()) {
                        System.out.println("No active orders.");
                    } else {
                        for (Order o : orders) {
                            System.out.println(
                                    "Order " + o.getId() +
                                            ", Customer " + o.getCustomerId() +
                                            ", Status " + o.getStatus()
                            );
                        }
                    }
                }

                case 2 -> {
                    try {
                        System.out.print("Enter order ID: ");
                        int orderId = readInt(sc);

                        System.out.println("1. COOKING");
                        System.out.println("2. ON_THE_WAY");
                        System.out.println("3. DELIVERED");
                        System.out.print("Choose status: ");

                        int s = readInt(sc);

                        OrderStatus newStatus = switch (s) {
                            case 1 -> OrderStatus.COOKING;
                            case 2 -> OrderStatus.ON_THE_WAY;
                            case 3 -> OrderStatus.DELIVERED;
                            default -> null;
                        };

                        if (newStatus == null) {
                            System.out.println("Invalid status.");
                            break;
                        }

                        orderService.updateStatus(orderId, newStatus);
                        System.out.println("Status updated.");

                    } catch (OrderNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 0 -> {
                    return;
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }



    private static boolean isValidName(String name) {
        return name != null && name.matches("[A-Za-z ]+");
    }

    private static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Enter a number: ");
        }
        return sc.nextInt();
    }
}
