package bankify.dao;

import bankify.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class CustomerDao {
    private Connection conn;

    public CustomerDao(Connection conn) {
        this.conn = conn;
    }

    // Register a new customer
    public boolean register(Customer customer) {
        String sql = "INSERT INTO customer (first_name, last_name, email, password, first_time_login) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getPassword());
            stmt.setBoolean(5, customer.isFirstTimeLogin());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Find customer by email + password
    public Customer findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM customer WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Customer c = new Customer();
                c.setCustomerId(rs.getInt("customer_id"));
                c.setFirstName(rs.getString("first_name"));
                c.setLastName(rs.getString("last_name"));
                c.setEmail(rs.getString("email"));
                c.setPassword(rs.getString("password"));
                c.setPhoneNumber(rs.getString("phone_number")); // ✅ fixed column name
                c.setAddress(rs.getString("address"));
                if (rs.getDate("dob") != null) {
                    c.setDob(rs.getDate("dob").toLocalDate());
                }
                c.setFirstTimeLogin(rs.getBoolean("first_time_login"));
                return c;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update profile (phone_number, address, dob, and flip first_time_login)
    public boolean updateProfile(Customer customer) {
        String sql = "UPDATE customer SET phone_number = ?, address = ?, dob = ?, first_time_login = ? WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getPhoneNumber()); // ✅ matches DB column
            stmt.setString(2, customer.getAddress());
            if (customer.getDob() != null) {
                stmt.setDate(3, Date.valueOf(customer.getDob()));
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
            }
            stmt.setBoolean(4, false); // once profile is updated, mark as not first-time
            stmt.setString(5, customer.getEmail());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}