package org.example.demo1001.repository;

import org.example.demo1001.factory.UserFactory;
import org.example.demo1001.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepo {

    private static UserRepo instance;
    private final Connection connection;

    private UserRepo() {
        this.connection = DatabaseConnection.getConnection();
    }

    public static synchronized UserRepo getInstance() {
        if (instance == null) {
            instance = new UserRepo();
        }
        return instance;
    }

    /**
     * Login method to validate username and password.
     */
    public boolean login(User user) {
        String sql = "SELECT * FROM users WHERE user_name = ? AND password = ? AND role=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    if(rs.getString("status").equals("pending")){
                        return false;
                    }
                    //session start;
                    SessionRepo.login(UserFactory.getInstance().createUser(rs.getInt("id"),rs.getString("user_name"),rs.getString("password"),rs.getString("role"),rs.getString("status")));
                    System.out.println("Login successful for user: " + user.getUserName());
                    return true;
                } else {
                    System.err.println("Invalid username or password.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
        }
        return false;
    }

    /**
     * Register method to add a new user to the database.
     */

    public boolean addAdmin(User user) {
        // Check if the role is valid

        if(!user.getRole().equalsIgnoreCase("admin")){
            return false;
        }

        String sql = "INSERT INTO users (user_name, password, role, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().toLowerCase());
            stmt.setString(4, "approved");

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User registered successfully: " + user.getUserName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error during registration: " + e.getMessage());
        }
        return false;
    }
    public boolean register(User user) {
        // Check if the role is valid
        if (!user.getRole().equalsIgnoreCase("viewer") && !user.getRole().equalsIgnoreCase("editor")) {
            System.err.println("Invalid role: " + user.getRole());
            return false;
        }
        if(user.getRole().equalsIgnoreCase("admin")){
            return false;
        }

        String sql = "INSERT INTO users (user_name, password, role, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().toLowerCase());
            System.out.println(user.getRole());
            if(user.getRole().equals("viewer")){
                stmt.setString(4, "approved");
            }else if(user.getRole().toLowerCase().equals("editor")){
                stmt.setString(4, "pending");
            }else{
                stmt.setString(4, "approved");
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User registered successfully: " + user.getUserName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error during registration: " + e.getMessage());
        }
        return false;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String userName = rs.getString("user_name");
                String password = rs.getString("password");
                String role = rs.getString("role");
                String status = rs.getString("status");
                System.out.println(status+"<----------");

                // Assuming you have a UserFactory to create User objects
                UserFactory factory = UserFactory.getInstance();
                User user = factory.createUser(id, userName, password, role, status);

                users.add(user);
                System.out.println(user.getUserName() + " fetched");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching users: " + e.getMessage());
        }
        return users;
    }

    public Boolean deleteUser(User userRole){
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userRole.getId());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User deleted from database: " + userRole.getUserName());
                return true;
            } else {
                System.err.println("User not found in database: " + userRole.getUserName());
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting User from database: " + e.getMessage());
            return false;
        }
    }

    public Boolean changeRole(User user, String newRole){
            // Get the current role of the user
            String oldRole = user.getRole();

            // Check if the new role is different from the current one
            if (!oldRole.equals(newRole)) {
                // Update the user's role in the system (in memory)
                user.setRole(newRole);

                // Update the role in the database
                String sql = "UPDATE users SET role = ? WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, newRole);
                    stmt.setInt(2, user.getId());

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("User role updated successfully: " + oldRole + " -> " + newRole);

                        // Update the UI or the document model with the new role (if necessary)
                        return true;
                    } else {
                        System.err.println("User not found in database: " + user.getUserName());
                    }
                } catch (SQLException e) {
                    System.err.println("Error updating user role in database: " + e.getMessage());
                }
            } else {
                System.out.println("The user's role is already set to " + newRole);
            }

            return false;
        }

    public Boolean acceptUser(User user){
        String sql = "UPDATE users SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "approved");
            stmt.setInt(2, user.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User status updated successfully: ");

                // Update the UI or the document model with the new role (if necessary)
                return true;
            } else {
                System.err.println("User not found in database: " + user.getUserName());
            }
        } catch (SQLException e) {
            System.err.println("Error updating user role in database: " + e.getMessage());
        }

        return false;
    }


    }


