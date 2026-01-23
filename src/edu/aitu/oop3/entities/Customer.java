package edu.aitu.oop3.entities;

public class Customer {
    private int id;
    private String name;
    private double balance;


    public Customer(String name) {
        this.name = name;
    }


    public Customer(int id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getBalance() {return balance;}
}
