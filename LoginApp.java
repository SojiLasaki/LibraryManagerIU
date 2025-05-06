import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
// import java.sql.*;

public class LoginApp extends JFrame implements ActionListener {
    JTextField userTextField;
    JPasswordField passField;
    JButton loginButton, registerButton;
    JLabel messageLabel;

    public LoginApp() {
        setTitle("Login");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel userLabel = new JLabel("Username:");
        userTextField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        passField = new JPasswordField(15);

        // Create a background frame (fullscreen, behind login)
        JFrame bgFrame = new JFrame();
        bgFrame.setUndecorated(true); // No title bar
        bgFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen

        // Load and scale image to fit screen size
        ImageIcon bgIcon = new ImageIcon("assets/IMG_8529.png");
        Image bgImage = bgIcon.getImage().getScaledInstance(
            Toolkit.getDefaultToolkit().getScreenSize().width,
            Toolkit.getDefaultToolkit().getScreenSize().height,
            Image.SCALE_SMOOTH
        );

        JLabel bgLabel = new JLabel(new ImageIcon(bgImage));
        bgFrame.setContentPane(bgLabel);

        bgFrame.setVisible(true); // Show background first

                

        loginButton = new JButton("Login");
        // loginButton.addActionListener(this);
        loginButton.addActionListener(this);

        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            dispose();
            new RegisterApp(); // Open register form
        });

        messageLabel = new JLabel();

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(userLabel);
        panel.add(userTextField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(loginButton);
        panel.add(registerButton);
        panel.add(messageLabel);

        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String user = userTextField.getText().trim();
        String pass = new String(passField.getPassword()).trim();

        File file = new File("assets/csv/admins.csv");

        if (!file.exists()) {
            messageLabel.setText("ADMIN.csv file not found.");
            messageLabel.setForeground(Color.RED);
            return;
        }

        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String csvUser = parts[0].trim();
                    String csvPass = parts[1].trim();

                    if (csvUser.equals(user) && csvPass.equals(pass)) {
                        found = true;
                        break;
                    }
                }
            }

            if (found) {
                // LoginManager.authenticate();
                JOptionPane.showMessageDialog(this, "Login successful!");
                new NavApp(); 
                this.dispose();
            } else {
                messageLabel.setText("Invalid credentials.");
                messageLabel.setForeground(Color.RED);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            messageLabel.setText("Error reading Database.");
            messageLabel.setForeground(Color.RED);
        }
    }


    public static void main(String[] args) {
        new LoginApp();
    }
}
