import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class SigninScreen extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton signinButton;
    private JCheckBox showPasswordCheckBox;
    private JLabel statusLabel;

    // Hardcoded users (email -> [password, role])
    private final HashMap<String, String[]> users = new HashMap<>() {{
        put("admin@qau.edu.pk", new String[]{"admin123", "Admin"});
        put("student@qau.edu.pk", new String[]{"student123", "Student"});
        put("supervisor@qau.edu.pk", new String[]{"supervisor123", "Supervisor"});
    }};

    public SigninScreen() {
        setTitle("Sign In");
        setSize(400, 320);
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

        // Show Password checkbox
        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setBackground(cream);
        showPasswordCheckBox.addActionListener(e -> {
            passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? (char) 0 : '*');
        });
        panel.add(showPasswordCheckBox);

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

        if (users.containsKey(email)) {
            String[] userData = users.get(email);
            if (userData[0].equals(password)) {
                showMessage("Signed in successfully as " + userData[1]);
                dispose();
                JOptionPane.showMessageDialog(null, "Redirecting to " + (userData[1].equals("Admin") ? "ManageUsersScreen" : "SelectScreen"));
                return;
            }
        }

        showError("Invalid email or password.");
    }

    private void showError(String msg) {
        statusLabel.setText(msg);
        statusLabel.setForeground(Color.RED);
    }

    private void showMessage(String msg) {
        statusLabel.setText(msg);
        statusLabel.setForeground(new Color(46, 125, 50)); // green
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SigninScreen::new);
    }
}
