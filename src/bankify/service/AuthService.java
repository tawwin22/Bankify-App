package bankify.service;

import bankify.Customer;
import bankify.dao.CustomerDao;

public class AuthService {
    private CustomerDao customerDao;

    public AuthService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    // Register a new customer
    public boolean register(Customer customer) {
        if (customer == null ||
            customer.getFirstName() == null || customer.getFirstName().isEmpty() ||
            customer.getLastName() == null || customer.getLastName().isEmpty() ||
            customer.getEmail() == null || customer.getEmail().isEmpty() ||
            customer.getPassword() == null || customer.getPassword().isEmpty()) {
            return false;
        }

        // Always mark new users as first-time login
        customer.setFirstTimeLogin(true);

        return customerDao.register(customer);
    }

    // Authenticate and return the Customer object
    public Customer authenticate(String email, String password) {
        if (email == null || password == null ||
            email.isEmpty() || password.isEmpty()) {
            return null;
        }
        return customerDao.findByEmailAndPassword(email, password);
    }
}