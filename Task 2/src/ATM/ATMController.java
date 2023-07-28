package ATM;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * @author Aditya Poli
 *
 */

public class ATMController {
    private Scanner scanner;
    private UserManager userManager;
    private AccountManager accountManager;
    private String currentUser;
    
    private Connection connection = null;
    

    public ATMController() {
        scanner = new Scanner(System.in);
        userManager = new UserManager();
        accountManager = new AccountManager();
    }

    public void run() {
        System.out.println("Welcome to the ATM System!");

        while (true) {
            System.out.print("Enter your user ID: ");
            String userID = scanner.nextLine();
            System.out.print("Enter your PIN: ");
            String userPIN = scanner.nextLine();

            if (userManager.authenticateUser(userID, userPIN)) {
                currentUser = userID;
                break;
            } else {
                System.err.println("Invalid user ID or PIN. Please try again.");
            }
        }

        System.out.println("\nWelcome, " + currentUser + "!");
        showOptions();
    }



    private void showOptions() {
        while (true) {
            System.out.println("\nATM Functionalities:");
            System.out.println("1. Transactions History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Balance Enquiry");
            System.out.println("5. Transfer");
            System.out.println("6. Quit");

            System.out.print("Choose an option (1-6): ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    showTransactionsHistory();
                    break;
                case 2:
                    withdraw();
                    break;
                case 3:
                    deposit();
                    break;
                case 4:
                	balanceEnquiry();
                	break;
                case 5:
                    transfer();
                    break;
                case 6:
                    System.out.println("Thank you for using the ATM system. Goodbye!");
                    System.exit(0);
                default:
                    System.err.println("Invalid choice. Please choose a valid option.");
            }
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
    	
    	try {
    		connection = DriverManager.getConnection(
    				"jdbc:mysql://localhost:3306/atm_database",
    				System.getenv("USERNAME"), System.getenv("PASSWORD"));
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
        List<Transaction> transactions = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM transactions WHERE user_id = ?");
            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String type = resultSet.getString("type");
                double amount = resultSet.getDouble("amount");
                String date = resultSet.getString("date");
                transactions.add(new Transaction(type, amount, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    private void showTransactionsHistory() {
        List<Transaction> transactions = getTransactionHistory(currentUser);

        // Print the table header
        System.out.println("\nTransaction History:");
        System.out.println("+-----------------------+----------+----------+");
        System.out.println("|        Date           |   Type   |  Amount  |");
        System.out.println("+-----------------------+----------+----------+");

        // Populate and print the table body with transaction data
        for (Transaction transaction : transactions) {
            String formattedDate = String.format("%-21s", transaction.getDate());
            String formattedType = String.format("%-8s", transaction.getType());
            String formattedAmount = String.format("%-8.2f", transaction.getAmount());
            System.out.println("| " + formattedDate + " | " + formattedType + " | " + formattedAmount + " |");
        }

        // Print the table footer and border
        System.out.println("+-----------------------+----------+----------+");
        
     // Print the current balance with proper alignment
        double accountBalance = accountManager.getAccountBalance(currentUser);
        String formattedBalance = String.format("| Current Balance: %26.2f |", accountBalance);
        System.out.println(formattedBalance);
        System.out.println("+---------------------------------------------+");
    }



    private void withdraw() {
        System.out.print("Enter the amount to withdraw: ");
        double amount = Double.parseDouble(scanner.nextLine());

        double currentBalance = accountManager.getAccountBalance(currentUser);

        if (currentBalance >= amount) {
            accountManager.updateAccountBalance(currentUser, currentBalance - amount);
            accountManager.addTransaction(currentUser, "Withdraw", amount); // Add Withdraw transaction to the database
            System.out.println("Withdrawal successful. Updated balance: " + accountManager.getAccountBalance(currentUser));
        } else {
            System.err.println("Insufficient funds. Withdrawal failed.");
        }
    }

    private void balanceEnquiry() {
        double accountBalance = accountManager.getAccountBalance(currentUser);
        System.out.println("Account Balance: " + accountBalance);
    }
    
    private void deposit() {
        System.out.print("Enter the amount to deposit: ");
        double amount = Double.parseDouble(scanner.nextLine());

        double currentBalance = accountManager.getAccountBalance(currentUser);
        accountManager.updateAccountBalance(currentUser, currentBalance + amount);
        accountManager.addTransaction(currentUser, "Deposit", amount); // Add Deposit transaction to the database

        System.out.println("Deposit successful. Updated balance: " + accountManager.getAccountBalance(currentUser));
    }

    private void transfer() {
        System.out.print("Enter the recipient's user ID: ");
        String recipientID = scanner.nextLine();
        recipientID = recipientID.trim();

        if (userManager.authenticateUser(recipientID)) {
            System.out.print("Enter the amount to transfer: ");
            double amount = Double.parseDouble(scanner.nextLine());

            double senderBalance = accountManager.getAccountBalance(currentUser);
            double recipientBalance = accountManager.getAccountBalance(recipientID);

            if (senderBalance >= amount) {
                accountManager.updateAccountBalance(currentUser, senderBalance - amount);
                accountManager.updateAccountBalance(recipientID, recipientBalance + amount);
                accountManager.addTransaction(currentUser, "Transfer", -amount); // Add Transfer (Outgoing) transaction to the database
                accountManager.addTransaction(recipientID, "Transfer", amount); // Add Transfer (Incoming) transaction to the database
                System.out.println("Transfer successful. Updated balance: " + accountManager.getAccountBalance(currentUser));
            } else {
                System.err.println("Insufficient funds. Transfer failed.");
            }
        } else {
            System.err.println("Invalid recipient user ID. Transfer failed.");
        }
    }
}
