package edu.aitu.oop3;

import edu.aitu.oop3.db.DatabaseConnection;
import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.entities.Customer;
import edu.aitu.oop3.entities.MenuItem;
import edu.aitu.oop3.entities.OrderStatus;
import edu.aitu.oop3.exceptions.OrderNotFoundException;
import edu.aitu.oop3.repositories.*;
import edu.aitu.oop3.service.OrderService;

import java.util.Scanner;

public class Main {

    private static final String ADMIN_PASSWORD = "1234";

    public static void main(String[] args) {

        IDB db = new DatabaseConnection();

        CustomerRepository customerRepo = new CustomerRepositoryImpl(db);
        MenuItemRepository menuRepo = new MenuItemRepositoryImpl(db);
        OrderRepository orderRepo = new OrderRepositoryImpl(db);
        OrderItemRepository orderItemRepo = new OrderItemRepositoryImpl(db);

        OrderService orderService =
                new OrderService(orderRepo, menuRepo, orderItemRepo);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nFood Delivery");
            System.out.println("1. Order Food");
            System.out.println("2. Check Order Status");
            System.out.println("3. Admin Menu");
            System.out.println("0. Exit");
            System.out.print("Enter: ");

            int choice = readInt(sc);

            switch (choice) {
                case 1 -> orderFood(sc, customerRepo, menuRepo, orderService);
                case 2 -> checkStatus(sc, orderService);
                case 3 -> adminMenu(sc, menuRepo, orderService);
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

                if (menuRepo.findById(chosen) == null) {
                    System.out.println("Invalid menu item.");
                    continue;
                }

                if (!menuRepo.findById(chosen).isAvailable()) {
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
                    System.out.println("Order created successfully.");
                    System.out.println("Your order ID: " + orderId);
                    System.out.println("Current status: COOKING");
                    return;
                }

                if (menuRepo.findById(next) != null) {
                    if (!menuRepo.findById(next).isAvailable()) {
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
        }
    }



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

                case 3 -> orderService.getActiveOrders()
                        .forEach(o -> System.out.println(
                                "Order ID: " + o.getId() +
                                        ", Customer ID: " + o.getCustomerId() +
                                        ", Status: " + o.getStatus()
                        ));

                case 4 -> {
                    System.out.print("Enter order ID: ");
                    int orderId = readInt(sc);

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
}
