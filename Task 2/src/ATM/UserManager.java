package ATM;
import java.sql.*;


/**
 * 
 * @author Aditya Poli
 *
 */

public class UserManager {
    private Connection connection;

    public UserManager() {
        try {
            // Establish a connection to MySQL database (replace with your database credentials)
        	Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm_database", System.getenv("USERNAME"), System.getenv("PASSWORD"));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticateUser(String userID, String userPIN) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE user_id = ? AND user_pin = ?");
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, userPIN);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean authenticateUser(String userID) {
        try {
            // For transfer operation, skip PIN verification
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE user_id = ?");
            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to add a new user to the database
    public void addUser(String userID, String userPIN) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (user_id, user_pin) VALUES (?, ?)");
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, userPIN);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update the user's PIN in the database
    public void updateUserPIN(String userID, String newPIN) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET user_pin = ? WHERE user_id = ?");
            preparedStatement.setString(1, newPIN);
            preparedStatement.setString(2, userID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
