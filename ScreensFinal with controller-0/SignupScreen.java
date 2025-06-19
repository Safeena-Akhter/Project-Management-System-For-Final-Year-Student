import javax.swing.*;
import java.awt.*;

public class SignupScreen extends JFrame {
    private JTextField nameField, emailField, idField, ageField;
    private JPasswordField passwordField, confirmPasswordField;
    private JRadioButton maleRadio, femaleRadio, otherRadio;
    private JRadioButton studentRadio, supervisorRadio, adminRadio;
    private ButtonGroup genderGroup, roleGroup;
    private JButton signupButton;
    private JLabel statusLabel;
    private MainController controller;

    public SignupScreen(MainController controller) {
        this.controller = controller;
        setTitle("Signup");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Theme Colors
        Color topBottomColor = new Color(245, 245, 245);  // light gray
        Color centerColor = new Color(255, 253, 208);     // cream
        Color green = new Color(46, 125, 50);             // QAU green

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBackground(centerColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        nameField = new JTextField();
        emailField = new JTextField();
        idField = new JTextField();
        ageField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        otherRadio = new JRadioButton("Other");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderGroup.add(otherRadio);

        studentRadio = new JRadioButton("Student");
        supervisorRadio = new JRadioButton("Supervisor");
        adminRadio = new JRadioButton("Admin");
        roleGroup = new ButtonGroup();
        roleGroup.add(studentRadio);
        roleGroup.add(supervisorRadio);
        roleGroup.add(adminRadio);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("University ID:"));
        panel.add(idField);

        panel.add(new JLabel("Age:"));
        panel.add(ageField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);

        panel.add(new JLabel("Gender:"));
        JPanel genderPanel = new JPanel();
        genderPanel.setBackground(centerColor);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        genderPanel.add(otherRadio);
        panel.add(genderPanel);

        panel.add(new JLabel("Role:"));
        JPanel rolePanel = new JPanel();
        rolePanel.setBackground(centerColor);
        rolePanel.add(studentRadio);
        rolePanel.add(supervisorRadio);
        rolePanel.add(adminRadio);
        panel.add(rolePanel);

        signupButton = new JButton("Sign Up");
        signupButton.setBackground(green);
        signupButton.setForeground(Color.WHITE);
        signupButton.setFocusPainted(false);
        signupButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        signupButton.setPreferredSize(new Dimension(120, 35));
        signupButton.addActionListener(e -> handleSignup());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(centerColor);
        buttonPanel.add(signupButton);
        panel.add(buttonPanel);

        statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(statusLabel);

        add(panel);
        setVisible(true);
    }

    private void handleSignup() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String ageText = ageField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String role = "";

        if (studentRadio.isSelected()) {
            role = "Student";
        } else if (supervisorRadio.isSelected()) {
            role = "Supervisor";
        } else if (adminRadio.isSelected()) {
            role = "Admin";
        }


        // Validation
        if (name.isEmpty()) {
            showError("Name cannot be empty.");
            return;
        }

        if (email.isEmpty()) {
            showError("Email cannot be empty.");
            return;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            showError("Invalid email format.");
            return;
        }

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Password fields cannot be empty.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            if (age <= 0) {
                showError("Age must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Age must be numeric.");
            return;
        }

        if (role.isEmpty()) {
            showError("Please select a role.");
            return;
        }

        // Register user through the controller
        if (controller.registerUser(name, email, password, role)) {
            showMessage("Signup successful!");
            dispose();
            // After signup, determine which screen to show based on the registered role
            if ("Admin".equals(MainController.getCurrentUserRole())) {
                new ManageUsersScreen(controller);
            } else {
                new SelectScreen(controller);
            }
        } else {
            showError("Signup failed. Email might already be in use.");
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
