import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SignupScreen extends JFrame {
    private JTextField firstNameField, lastNameField, emailField, idField, ageField;
    private JPasswordField passwordField, confirmPasswordField;
    private JRadioButton maleRadio, femaleRadio, otherRadio;
    private JRadioButton studentRadio, supervisorRadio, adminRadio;
    private ButtonGroup genderGroup, roleGroup;
    private JButton signupButton;
    private JLabel statusLabel;

    // Hardcoded list of existing users
    private static List<User> existingUsers = new ArrayList<>();

    public SignupScreen() {
        setTitle("Signup");
        setSize(450, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Theme Colors
        Color centerColor = new Color(255, 253, 208);     // cream
        Color green = new Color(46, 125, 50);             // QAU green

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(centerColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Input fields
        panel.add(createLabeledField("First Name:", firstNameField = new JTextField()));
        panel.add(createLabeledField("Last Name:", lastNameField = new JTextField()));
        panel.add(createLabeledField("Email:", emailField = new JTextField()));
        panel.add(createLabeledField("ID:", idField = new JTextField()));
        panel.add(createLabeledField("Age:", ageField = new JTextField()));
        panel.add(createLabeledField("Password:", passwordField = new JPasswordField()));
        panel.add(createLabeledField("Confirm Password:", confirmPasswordField = new JPasswordField()));

        // Gender row (label + options)
        JPanel genderRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderRow.setBackground(centerColor);
        JLabel genderLabel = new JLabel("Gender:");
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        otherRadio = new JRadioButton("Other");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderGroup.add(otherRadio);
        genderRow.add(genderLabel);
        genderRow.add(maleRadio);
        genderRow.add(femaleRadio);
        genderRow.add(otherRadio);
        panel.add(genderRow);

        // Role row (label + options)
        JPanel roleRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        roleRow.setBackground(centerColor);
        JLabel roleLabel = new JLabel("Role:");
        studentRadio = new JRadioButton("Student");
        supervisorRadio = new JRadioButton("Supervisor");
        adminRadio = new JRadioButton("Admin");
        roleGroup = new ButtonGroup();
        roleGroup.add(studentRadio);
        roleGroup.add(supervisorRadio);
        roleGroup.add(adminRadio);
        roleRow.add(roleLabel);
        roleRow.add(studentRadio);
        roleRow.add(supervisorRadio);
        roleRow.add(adminRadio);
        panel.add(roleRow);

        // Signup Button
        signupButton = new JButton("Sign Up");
        signupButton.setBackground(green);
        signupButton.setForeground(Color.WHITE);
        signupButton.setFocusPainted(false);
        signupButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        signupButton.addActionListener(e -> handleSignup());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(centerColor);
        buttonPanel.add(signupButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(buttonPanel);

        // Status Label
        statusLabel = new JLabel(" ");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setForeground(Color.RED);
        panel.add(statusLabel);

        add(panel);
        setVisible(true);

        // Add one hardcoded user
        existingUsers.add(new User("abc@gmail.com", "s_1234"));
    }

    private JPanel createLabeledField(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(255, 253, 208));
        JLabel label = new JLabel(labelText);
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void handleSignup() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String userId = idField.getText().trim();
        String ageText = ageField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String role = "";

        if (studentRadio.isSelected()) role = "Student";
        else if (supervisorRadio.isSelected()) role = "Supervisor";
        else if (adminRadio.isSelected()) role = "Admin";

        if (firstName.isEmpty()) {
            showError("First name cannot be empty.");
            return;
        }
        if (lastName.isEmpty()) {
            showError("Last name cannot be empty.");
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
        if (userId.isEmpty()) {
            showError("ID cannot be empty.");
            return;
        }
        if (role.isEmpty()) {
            showError("Please select a role.");
            return;
        }

        if (role.equals("Student") && !userId.startsWith("s_")) {
            showError("Student ID must start with 's_'.");
            return;
        }
        if (role.equals("Supervisor") && !userId.startsWith("sp_")) {
            showError("Supervisor ID must start with 'sup_'.");
            return;
        }
        if (role.equals("Admin") && !userId.startsWith("a_")) {
            showError("Admin ID must start with 'a_'.");
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

        // Check if email or ID already exists
        for (User u : existingUsers) {
            if (u.email.equalsIgnoreCase(email)) {
                showError("Email already exists. Please Sign In.");
                return;
            }
            if (u.userId.equalsIgnoreCase(userId)) {
                showError("ID already exists.");
                return;
            }
        }

        // Signup successful
        existingUsers.add(new User(email, userId));
        JOptionPane.showMessageDialog(this, "Signup successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        statusLabel.setText(" ");
    }

    private void showError(String msg) {
        statusLabel.setText(msg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignupScreen::new);
    }
}

// ==== User class ====
class User {
    String email;
    String userId;

    public User(String email, String userId) {
        this.email = email;
        this.userId = userId;
    }
}
