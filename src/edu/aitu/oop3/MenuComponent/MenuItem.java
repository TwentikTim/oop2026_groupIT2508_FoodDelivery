package edu.aitu.oop3.MenuComponent;

public class MenuItem {
    private int id;
    private String name;
    private double price;
    private boolean available;


    public MenuItem(int id, String name, double price, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getName() {return name;}

    public double getPrice() {return price;}
}