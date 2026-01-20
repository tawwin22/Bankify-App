package bankify.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import bankify.DBConnection;

public class UserDao {
	public boolean register(String first_name,String last_name, String email, String password) {
		 // Check if a user with the same email already exists
        String checkSql = "SELECT * FROM customer WHERE email=?";
	    String insertSql = "INSERT INTO customer (first_name,last_name,email,password) VALUES (?,?, ?, ?)";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement checkStmt = con.prepareStatement(checkSql)) {

	        checkStmt.setString(1, email);
	        ResultSet rs = checkStmt.executeQuery();
	        if (rs.next()) {
	            return false; // email already exists
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement insertStmt = con.prepareStatement(insertSql)) {

	        insertStmt.setString(1, first_name);
	        insertStmt.setString(2,last_name);
	        insertStmt.setString(3, email);
	        insertStmt.setString(4, password);
	        return insertStmt.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean login(String email, String password) {

        String sql = "SELECT * FROM customer WHERE email=? AND password=?";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
