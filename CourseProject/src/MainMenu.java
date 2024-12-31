import javax.swing.*;
import java.awt.*;

/**
 * Main menu for the Bank Customer Service System.
 */
public class MainMenu extends JPanel {
    private final JFrame frame;

    public MainMenu(JFrame frame) {
        this.frame = frame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton customerPortalButton = new JButton("Customer Portal");
        JButton employeePortalButton = new JButton("Employee Portal");
        JButton exitButton = new JButton("Exit");

        customerPortalButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(new CustomerPortal(frame)); // Pass the frame to CustomerPortal
            frame.revalidate();
            frame.repaint();
        });

        employeePortalButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(new EmployeePortal(frame)); // Pass the frame to EmployeePortal
            frame.revalidate();
            frame.repaint();
        });

        exitButton.addActionListener(e -> System.exit(0));

        add(customerPortalButton);
        add(employeePortalButton);
        add(exitButton);
    }

    public JPanel getPanel() {
        return this;
    }

    public static void main(String[] args) {
        DataHandler.loadCustomers(); // Load existing customers
        // Initialize the main application frame
        JFrame frame = new JFrame("Bank System Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        // Add the main menu panel
        frame.add(new MainMenu(frame));
        frame.setVisible(true);
    }
}
