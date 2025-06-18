import javax.swing.*;
import java.awt.*;

public class SigninScreen extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton signinButton;
    private JCheckBox showPasswordCheckBox;
    private JLabel statusLabel;

    public SigninScreen() {
        setTitle("Sign In");
        setSize(400, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color green = new Color(46, 125, 50);
        Color cream = new Color(255, 253, 208);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBackground(cream);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        emailField = new JTextField();
        passwordField = new JPasswordField();

        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setBackground(cream);
        showPasswordCheckBox.addActionListener(e -> {
            passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? (char) 0 : '*');
        });
        panel.add(showPasswordCheckBox);

        signinButton = new JButton("Sign In");
        signinButton.setBackground(green);
        signinButton.setForeground(Color.WHITE);
        signinButton.addActionListener(e -> handleSignin());

        panel.add(signinButton);
        statusLabel = new JLabel(" ", JLabel.CENTER);
        panel.add(statusLabel);

        add(panel);
        setVisible(true);
    }

    private void handleSignin() {
    String email = emailField.getText().trim();
    String password = new String(passwordField.getPassword());

    if (email.isEmpty() || password.isEmpty()) {
        showError("Email and password cannot be empty.");
        return;
    }

    User user = User.authenticate(email, password);
    if (user != null) {
        SessionManager.currentUser = user;
        dispose();

        switch (user.getRole().toUpperCase()) {
            case "SUPERVISOR":
                new SupervisorDashboard(new MainController()).setVisible(true);
                break;
            case "ADMIN":
                new ManageUsersScreen().setVisible(true);
                break;
            default:
                new ProjectDashboard().setVisible(true);
        }
    } else {
        showError("Invalid credentials.");
    }
}


    private void showError(String msg) {
        statusLabel.setText(msg);
        statusLabel.setForeground(Color.RED);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SigninScreen::new);
    }
}
