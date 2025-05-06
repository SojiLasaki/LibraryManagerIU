package Unused;
import javax.swing.*;

public class UserPage extends JFrame {
    public UserPage() {
        setTitle("User Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new JLabel("Welcome, User!", SwingConstants.CENTER));
    }
}
