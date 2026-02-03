package edu.aitu.oop3.repositories;

import edu.aitu.oop3.entities.Customer;

public interface CustomerRepository extends Repository<Customer, Integer> {
    int create(Customer customer);
    double getBalance(int customerId);
    void addBalance(int customerId, double amount);
    void subtractBalance(int customerId, double amount);
}