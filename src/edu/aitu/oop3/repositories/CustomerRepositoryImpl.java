package edu.aitu.oop3.repositories;

import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.entities.Customer;

import java.sql.SQLException;
import java.util.List;


public class CustomerRepositoryImpl extends BaseRepository<Customer, Integer> implements CustomerRepository {

    public CustomerRepositoryImpl(IDB db) {
        super(db);
    }

    private final RowMapper<Customer> customerMapper = rs -> new Customer(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getDouble("balance")
    );

    private final RowMapper<Double> balanceMapper = rs -> rs.getDouble("balance");

    @Override
    public List<Customer> findAll(){
        String sql = "SELECT * FROM customers ORDER BY id";
        return query(sql, customerMapper);
    }
    @Override
    public int create(Customer customer) {
        String sql = "INSERT INTO customers(name, balance) VALUES (?, 0) RETURNING id";
        List<Customer> customers = query(sql, rs -> {
            try {
                return new Customer(rs.getInt("id"), null, 0);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, customer.getName());
        return customers.get(0).getId();
    }

    @Override
    public double getBalance(int customerId) {
        String sql = "SELECT balance FROM customers WHERE id = ?";

        List<Double> balances = queryOtherType(sql, balanceMapper, customerId);

        if (balances.isEmpty()) {
            throw new RuntimeException("Customer not found: " + customerId);
        }
        return balances.get(0);
    }

    @Override
    public Customer findById(Integer id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        List<Customer> customers = query(sql, customerMapper, id);
        return customers.isEmpty() ? null : customers.get(0);
    }

    @Override
    public void addBalance(int customerId, double amount) {
        String sql = "UPDATE customers SET balance = balance + ? WHERE id = ?";
        executeUpdate(sql, amount, customerId);
    }

    @Override
    public void subtractBalance(int customerId, double amount) {
        String sql = "UPDATE customers SET balance = balance - ? WHERE id = ?";
        executeUpdate(sql, amount, customerId);
    }
}
