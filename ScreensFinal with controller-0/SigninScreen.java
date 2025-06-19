import javax.swing.*;
import java.awt.*;

public class SigninScreen extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton signinButton;
    private JLabel statusLabel;
    private MainController controller;

    public SigninScreen(MainController controller) {
        this.controller = controller;
        setTitle("Sign In");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Theme colors
        Color green = new Color(46, 125, 50);       // QAU green
        Color cream = new Color(255, 253, 208);     // Cream background

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBackground(cream);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        emailField = new JTextField();
        passwordField = new JPasswordField();

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        signinButton = new JButton("Sign In");
        signinButton.setBackground(green);
        signinButton.setForeground(Color.WHITE);
        signinButton.setFocusPainted(false);
        signinButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        signinButton.setPreferredSize(new Dimension(120, 35));
        signinButton.addActionListener(e -> handleSignin());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(cream);
        buttonPanel.add(signinButton);
        panel.add(buttonPanel);

        statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(statusLabel);

        add(panel);
        setVisible(true);
    }

    private void handleSignin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty()) {
            showError("Email cannot be empty.");
            return;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            showError("Invalid email format.");
            return;
        }

        if (password.isEmpty()) {
            showError("Password cannot be empty.");
            return;
        }

        // Authenticate user through the controller
        if (controller.authenticateUser(email, password)) {
            showMessage("Sign-in successful!");
            dispose();
            // Check user role after successful sign-in
            if ("Admin".equals(MainController.getCurrentUserRole())) {
                new ManageUsersScreen(controller);
            } else {
                new SelectScreen(controller); // For non-admin users
            }
        } else {
            showError("Invalid email or password.");
        }
    }

    private void showError(String msg) {
        statusLabel.setText(msg);
        statusLabel.setForeground(Color.RED);
    }

    private void showMessage(String msg) {
        statusLabel.setText(msg);
        statusLabel.setForeground(new Color(46, 125, 50)); // green
    }
}
