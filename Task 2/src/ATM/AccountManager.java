package ATM;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Aditya Poli
 *
 */

public class AccountManager {
    private Connection connection;

    public AccountManager() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm_database", System.getenv("USERNAME"), System.getenv("PASSWORD"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getAccountBalance(String userID) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE user_id = ?");
            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            } else {
                return 0.0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public void updateAccountBalance(String userID, double amount) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE accounts SET balance = ? WHERE user_id = ?");
            preparedStatement.setDouble(1, amount);
            preparedStatement.setString(2, userID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add a transaction record to the database
    public void addTransaction(String userID, String type, double amount) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO transactions (user_id, type, amount) VALUES (?, ?, ?)");
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, type);
            preparedStatement.setDouble(3, amount);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get transaction history for a specific user from the database
    public List<Transaction> getTransactionHistory(String userID) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM transactions WHERE user_id = ?");
            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String type = resultSet.getString("type");
                double amount = resultSet.getDouble("amount");
                String date = resultSet.getString("date"); // Assuming there's a 'date' column in the transactions table
                transactions.add(new Transaction(type, amount, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

}
