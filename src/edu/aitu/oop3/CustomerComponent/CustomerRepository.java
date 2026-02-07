package edu.aitu.oop3.CustomerComponent;

import edu.aitu.oop3.DataComponent.Repository;

public interface CustomerRepository extends Repository<Customer, Integer> {
    int create(Customer customer);
    double getBalance(int customerId);
    void addBalance(int customerId, double amount);
    void subtractBalance(int customerId, double amount);
}