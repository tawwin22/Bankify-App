package bankify.service;

import bankify.dao.UserDao;

public class AuthService {
    private UserDao userDAO = new UserDao();

    public boolean register(String first_name, String last_name, String email, String password) {
        if (first_name == null || last_name == null || email == null || password == null ||
            first_name.isEmpty() || last_name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return false;
        }
        return userDAO.register(first_name, last_name, email, password);
    }

    public boolean authenticate(String email, String password) {
        if (email == null || password == null ||
            email.isEmpty() || password.isEmpty()) {
            return false;
        }
        return userDAO.login(email, password);
    }
}