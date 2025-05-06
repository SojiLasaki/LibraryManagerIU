
import javax.swing.*;

public class AdminPage extends JFrame {
    public AdminPage() {
        setTitle("Admin Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new JLabel("Welcome, Admin!", SwingConstants.CENTER));
    }
}

