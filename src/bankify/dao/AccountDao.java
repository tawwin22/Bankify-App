package bankify.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import bankify.Account;

public class AccountDao {
    private Connection conn;

    public AccountDao(Connection conn) {
        this.conn = conn;
    }

    // ✅ Create account using customer's phone number as account_number
    public void createAccountUsingPhone(long customerId) throws SQLException {
        String sql = "INSERT INTO account (customer_id, account_number, balance, account_type, status) " +
                     "SELECT customer_id, phone_number, 0.00, 'USER', 'ACTIVE' " +
                     "FROM customer WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, customerId);
            stmt.executeUpdate();
        }
    }

    // ✅ Get account by accountId
    public Account getAccountById(long accountId) throws SQLException {
        String sql = "SELECT * FROM account WHERE account_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToAccount(rs);
            }
        }
        return null;
    }

    // ✅ Get account by accountNumber (phone number)
    public Account getAccountByNumber(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM account WHERE account_number = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToAccount(rs);
            }
        }
        return null;
    }

    // ✅ Get all accounts for a customer
    public List<Account> getAccountsByCustomerId(long customerId) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                accounts.add(mapRowToAccount(rs));
            }
        }
        return accounts;
    }

    // ✅ Update balance
    public void updateBalance(long accountId, double newBalance) throws SQLException {
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newBalance);
            stmt.setLong(2, accountId);
            stmt.executeUpdate();
        }
    }

    // ✅ Update account status (ACTIVE, SUSPENDED, CLOSED)
    public void updateStatus(long accountId, String newStatus) throws SQLException {
        String sql = "UPDATE account SET status = ? WHERE account_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setLong(2, accountId);
            stmt.executeUpdate();
        }
    }

    // ✅ Delete account (careful: will fail if transactions exist due to FK constraints)
    public void deleteAccount(long accountId) throws SQLException {
        String sql = "DELETE FROM account WHERE account_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, accountId);
            stmt.executeUpdate();
        }
    }

    // ✅ Helper: map ResultSet row to Account object
    private Account mapRowToAccount(ResultSet rs) throws SQLException {
        return new Account(
            rs.getLong("account_id"),
            rs.getLong("customer_id"),
            rs.getString("account_number"),
            rs.getDouble("balance"),
            rs.getString("account_type"),
            rs.getString("status"),
            rs.getTimestamp("created_at")
        );
    }
}