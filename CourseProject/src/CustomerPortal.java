import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class CustomerPortal extends JPanel {
    private final JFrame frame;
    private Customer activeCustomer;

    public CustomerPortal(JFrame frame) {
        this.frame = frame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton loginButton = new JButton("Log In");
        JButton checkBalanceButton = new JButton("Check Balance");
        JButton depositButton = new JButton("Deposit Funds");
        JButton withdrawButton = new JButton("Withdraw Funds");
        JButton transferButton = new JButton("Transfer Funds");
        JButton deleteAccountButton = new JButton("Close Account");
        JButton logoutButton = new JButton("Logout");

        loginButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter Your Name:");
            activeCustomer = DataHandler.getCustomerByName(name);
            if (activeCustomer != null) {
                JOptionPane.showMessageDialog(this, "Welcome, " + activeCustomer.getName() + "!");
            } else {
                int choice = JOptionPane.showConfirmDialog(
                        this,
                        "Account not found. Would you like to create a new one?",
                        "Account Not Found",
                        JOptionPane.YES_NO_OPTION
                );
                if (choice == JOptionPane.YES_OPTION) {
                    String newName = JOptionPane.showInputDialog("Enter Your Name:");
                    double initialBalance = Double.parseDouble(JOptionPane.showInputDialog("Enter Initial Balance:"));
                    String newId = DataHandler.addCustomer(newName, initialBalance);
                    JOptionPane.showMessageDialog(this, "Account Created. ID: " + newId);
                }
            }
        });

        checkBalanceButton.addActionListener(e -> {
            if (activeCustomer != null) {
                JOptionPane.showMessageDialog(this, "Current Balance: " + activeCustomer.getBalance());
            } else {
                JOptionPane.showMessageDialog(this, "Please log in first.");
            }
        });

        depositButton.addActionListener(e -> {
            if (activeCustomer != null) {
                double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter Deposit Amount:"));
                activeCustomer.deposit(amount);
                DataHandler.saveCustomers();
                JOptionPane.showMessageDialog(this, "Deposit Successful. New Balance: " + activeCustomer.getBalance());
            } else {
                JOptionPane.showMessageDialog(this, "Please log in first.");
            }
        });

        withdrawButton.addActionListener(e -> {
            if (activeCustomer != null) {
                double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter Withdrawal Amount:"));
                if (activeCustomer.withdraw(amount)) {
                    DataHandler.saveCustomers();
                    JOptionPane.showMessageDialog(this, "Withdrawal Successful. New Balance: " + activeCustomer.getBalance());
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient Balance.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please log in first.");
            }
        });

        transferButton.addActionListener(e -> {
            if (activeCustomer != null) {
                String toName = JOptionPane.showInputDialog("Enter Recipient's Name:");
                Customer recipient = DataHandler.getCustomerByName(toName);
                if (recipient != null) {
                    double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter Transfer Amount:"));
                    if (activeCustomer.withdraw(amount)) {
                        recipient.deposit(amount);
                        DataHandler.saveCustomers();
                        JOptionPane.showMessageDialog(this, "Transfer Successful.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Insufficient Balance.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Recipient not found.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please log in first.");
            }
        });

        deleteAccountButton.addActionListener(e -> {
            if (activeCustomer != null) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to close your account? This action is irreversible.",
                        "Close Account",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    DataHandler.deleteCustomer(activeCustomer.getId());
                    DataHandler.saveCustomers();
                    JOptionPane.showMessageDialog(this, "Account deleted successfully.");
                    activeCustomer = null;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please log in first.");
            }
        });

        logoutButton.addActionListener(e -> {
            activeCustomer = null; // Clear the active customer on logout
            frame.getContentPane().removeAll();
            frame.add(new MainMenu(frame).getPanel()); // Pass the frame to MainMenu
            frame.revalidate();
            frame.repaint();
        });

        add(loginButton);
        add(checkBalanceButton);
        add(depositButton);
        add(withdrawButton);
        add(transferButton);
        add(deleteAccountButton);
        add(logoutButton);
    }

    /**
     * Enables portal functions after successful login.
     */
    private void enablePortalFunctions(JButton depositButton, JButton withdrawButton, JButton transferButton, JButton logoutButton, JButton loginButton) {
        depositButton.setEnabled(true);
        withdrawButton.setEnabled(true);
        transferButton.setEnabled(true);
        logoutButton.setEnabled(true);
        loginButton.setEnabled(false);
    }

    /**
     * Disables portal functions after logout.
     */
    private void disablePortalFunctions(JButton depositButton, JButton withdrawButton, JButton transferButton, JButton logoutButton, JButton loginButton) {
        depositButton.setEnabled(false);
        withdrawButton.setEnabled(false);
        transferButton.setEnabled(false);
        logoutButton.setEnabled(false);
        loginButton.setEnabled(true);
    }

    /**
     * Helper method to get a valid amount from the user.
     */
    private double getAmountFromUser(String message) {
        try {
            String input = JOptionPane.showInputDialog(message);
            if (input == null || input.trim().isEmpty()) return -1;
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount! Please enter a valid number.");
            return -1;
        }
    }
}
