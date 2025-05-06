import javax.swing.*;
import java.awt.*;
// import java.awt.event.*;
import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class RegisterApp extends JFrame {
    JTextField userField;
    JPasswordField passField;
    JButton registerButton, loginButton;
    JLabel messageLabel;

    public RegisterApp() {
        setTitle("Register");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel userLabel = new JLabel("Username:");
        userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        passField = new JPasswordField(15);

        registerButton = new JButton("Register");
        messageLabel = new JLabel();

        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            dispose();
            new LoginApp(); // Open login form
        });
        registerButton.addActionListener(e -> registerUser());

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(registerButton);
        panel.add(loginButton);
        panel.add(messageLabel);

        add(panel);
        setVisible(true);
    }

    private void registerUser() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();
        String filePath = "assets/csv/admins.csv";

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in both fields.");
            messageLabel.setForeground(Color.RED);
            return;
        }

        File file = new File(filePath);
        try {
            // Create file and directory if they don't exist
            file.getParentFile().mkdirs();
            file.createNewFile();

            // Check if username already exists
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].trim().equalsIgnoreCase(username)) {
                    messageLabel.setText("Username already exists.");
                    messageLabel.setForeground(Color.RED);
                    reader.close();
                    return;
                }
            }
            reader.close();

            // Append the new user
            FileWriter writer = new FileWriter(file, true); // append mode
            writer.append(username).append(",").append(password).append("\n");
            writer.flush();
            writer.close();

            messageLabel.setText("User registered successfully.");
            messageLabel.setForeground(Color.GREEN);

            JOptionPane.showMessageDialog(this, "Registration successful!");
            this.dispose();
            new NavApp();

        } catch (IOException ex) {
            ex.printStackTrace();
            messageLabel.setText("Error writing to admins.csv.");
            messageLabel.setForeground(Color.RED);
        }
    }

    public static void main(String[] args) {
        new RegisterApp();
    }
}
