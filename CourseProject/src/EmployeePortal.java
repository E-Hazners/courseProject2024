import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EmployeePortal extends JPanel {
    private JFrame frame;

    public EmployeePortal(JFrame frame) {
        this.frame = frame;
        setLayout(new GridLayout(8, 1, 10, 10));

        JButton createCustomerButton = new JButton("Create Customer");
        JButton deleteCustomerButton = new JButton("Delete Customer");
        JButton editCustomerButton = new JButton("Edit Customer");
        JButton searchByIdButton = new JButton("Search Customer by ID");
        JButton filterByBalanceButton = new JButton("Filter Customers by Balance");
        JButton calculateRecordsButton = new JButton("Count Records");
        JButton viewAllCustomersButton = new JButton("View All Customers");
        JButton backButton = new JButton("Back to Main Menu");

        createCustomerButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter Customer Name:");
            String balanceInput = JOptionPane.showInputDialog("Enter Initial Balance:");
            if (name != null && balanceInput != null) {
                try {
                    double balance = Double.parseDouble(balanceInput);
                    String id = DataHandler.addCustomer(name, balance);
                    JOptionPane.showMessageDialog(this, "Customer Created. ID: " + id);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid balance. Please try again.");
                }
            }
        });

        deleteCustomerButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Customer ID to Delete:");
            if (id != null) {
                boolean success = DataHandler.deleteCustomer(id);
                JOptionPane.showMessageDialog(this, success ? "Customer Deleted." : "Customer Not Found.");
            }
        });

        editCustomerButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Customer ID to Edit:");
            Customer customer = DataHandler.getCustomer(id);
            if (customer != null) {
                String newName = JOptionPane.showInputDialog("Enter New Name:", customer.getName());
                String newBalanceInput = JOptionPane.showInputDialog("Enter New Balance:", customer.getBalance());
                if (newName != null && newBalanceInput != null) {
                    try {
                        double newBalance = Double.parseDouble(newBalanceInput);
                        customer.setName(newName);
                        customer.setBalance(newBalance);
                        DataHandler.saveCustomers();
                        JOptionPane.showMessageDialog(this, "Customer Updated.");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid balance. Please try again.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Customer Not Found.");
            }
        });

        searchByIdButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("Enter Customer ID:");
            Customer customer = DataHandler.getCustomer(id);
            if (customer != null) {
                JOptionPane.showMessageDialog(this, "Customer Found:\nID: " + customer.getId()
                        + "\nName: " + customer.getName() + "\nBalance: " + customer.getBalance());
            } else {
                JOptionPane.showMessageDialog(this, "Customer Not Found.");
            }
        });

        filterByBalanceButton.addActionListener(e -> {
            String thresholdInput = JOptionPane.showInputDialog("Enter Balance Threshold:");
            String aboveInput = JOptionPane.showInputDialog("Filter Above Threshold? (yes/no):");
            if (thresholdInput != null && aboveInput != null) {
                try {
                    double threshold = Double.parseDouble(thresholdInput);
                    boolean above = aboveInput.equalsIgnoreCase("yes");
                    List<Customer> results = DataHandler.searchCustomersByBalance(threshold, above);
                    StringBuilder sb = new StringBuilder("Filtered Customers:\n");
                    for (Customer customer : results) {
                        sb.append("ID: ").append(customer.getId())
                                .append(", Name: ").append(customer.getName())
                                .append(", Balance: ").append(customer.getBalance()).append("\n");
                    }
                    JOptionPane.showMessageDialog(this, sb.toString());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid threshold. Please try again.");
                }
            }
        });

        calculateRecordsButton.addActionListener(e -> {
            String valueInput = JOptionPane.showInputDialog("Enter Balance Value:");
            String aboveInput = JOptionPane.showInputDialog("Count Above Value? (yes/no):");
            if (valueInput != null && aboveInput != null) {
                try {
                    double value = Double.parseDouble(valueInput);
                    boolean above = aboveInput.equalsIgnoreCase("yes");
                    int count = DataHandler.countCustomersByBalance(value, above);
                    JOptionPane.showMessageDialog(this, "Number of Records: " + count);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid value. Please try again.");
                }
            }
        });

        viewAllCustomersButton.addActionListener(e -> {
            List<Customer> customers = DataHandler.getAllCustomers();
            StringBuilder sb = new StringBuilder("Customer List:\n");
            for (Customer customer : customers) {
                sb.append("ID: ").append(customer.getId())
                        .append(", Name: ").append(customer.getName())
                        .append(", Balance: ").append(customer.getBalance()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        });

        backButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(new MainMenu(frame));
            frame.revalidate();
            frame.repaint();
        });

        add(createCustomerButton);
        add(deleteCustomerButton);
        add(editCustomerButton);
        add(searchByIdButton);
        add(filterByBalanceButton);
        add(calculateRecordsButton);
        add(viewAllCustomersButton);
        add(backButton);
    }
}
