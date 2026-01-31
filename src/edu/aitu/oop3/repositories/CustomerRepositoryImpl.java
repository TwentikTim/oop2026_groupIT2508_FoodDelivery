package edu.aitu.oop3.repositories;

import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.entities.Customer;

import java.sql.*;

public class CustomerRepositoryImpl implements CustomerRepository {

    private final IDB db;

    public CustomerRepositoryImpl(IDB db) {
        this.db = db;
    }

    @Override
    public int create(Customer customer) {
        String sql = "INSERT INTO customers(name, balance) VALUES (?, 0) RETURNING id";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, customer.getName());
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("id");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create customer", e);
        }
    }


    @Override
    public double getBalance(int customerId) {
        String sql = "SELECT balance FROM customers WHERE id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("balance");
            throw new RuntimeException("Customer not found: " + customerId);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to get balance", e);
        }
    }

    @Override
    public void addBalance(int customerId, double amount) {
        String sql = "UPDATE customers SET balance = balance + ? WHERE id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, amount);
            ps.setInt(2, customerId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to add balance", e);
        }
    }

    @Override
    public void subtractBalance(int customerId, double amount) {
        String sql = "UPDATE customers SET balance = balance - ? WHERE id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, amount);
            ps.setInt(2, customerId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to subtract balance", e);
        }
    }
}
