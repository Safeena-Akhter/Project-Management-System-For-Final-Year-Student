import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignupScreen extends JFrame {
    private JTextField firstNameField, lastNameField, emailField, idField, ageField;
    private JPasswordField passwordField, confirmPasswordField;
    private JRadioButton maleRadio, femaleRadio, otherRadio;
    private JRadioButton studentRadio, supervisorRadio, adminRadio;
    private ButtonGroup genderGroup, roleGroup;
    private JButton signupButton, backButton;
    private JLabel statusLabel;

    public SignupScreen() {
        setTitle("Signup");
        setSize(450, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color centerColor = new Color(255, 253, 208);
        Color green = new Color(46, 125, 50);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(centerColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        panel.add(createLabeledField("First Name:", firstNameField = new JTextField()));
        panel.add(createLabeledField("Last Name:", lastNameField = new JTextField()));
        panel.add(createLabeledField("Email:", emailField = new JTextField()));
        panel.add(createLabeledField("ID:", idField = new JTextField()));
        panel.add(createLabeledField("Age:", ageField = new JTextField()));
        panel.add(createLabeledField("Password:", passwordField = new JPasswordField()));
        panel.add(createLabeledField("Confirm Password:", confirmPasswordField = new JPasswordField()));

        // Gender
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

        // Role
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

        // Buttons
        signupButton = new JButton("Sign Up");
        signupButton.setBackground(green);
        signupButton.setForeground(Color.WHITE);
        signupButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        signupButton.addActionListener(e -> handleSignup());

        backButton = new JButton("Back");
        backButton.setBackground(Color.GRAY);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backButton.addActionListener((ActionEvent e) -> {
            dispose();
            new WelcomeScreen();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(centerColor);
        buttonPanel.add(signupButton);
        buttonPanel.add(backButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(buttonPanel);

        // Status
        statusLabel = new JLabel(" ");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setForeground(Color.RED);
        panel.add(statusLabel);

        add(panel);
        setVisible(true);
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

        String gender = "";
        if (maleRadio.isSelected()) gender = "Male";
        else if (femaleRadio.isSelected()) gender = "Female";
        else if (otherRadio.isSelected()) gender = "Other";

        String role = "";
        if (studentRadio.isSelected()) role = "Student";
        else if (supervisorRadio.isSelected()) role = "Supervisor";
        else if (adminRadio.isSelected()) role = "Admin";

        // Validation
        if (firstName.isEmpty() || lastName.isEmpty()) {
            showError("Name fields cannot be empty.");
            return;
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            showError("Invalid email format.");
            return;
        }
        if (emailExists(email)) {
            showError("Email already exists.");
            return;
        }
        if (userId.isEmpty()) {
            showError("ID cannot be empty.");
            return;
        }
        if (role.isEmpty()) {
            showError("Select a role.");
            return;
        }
        if (role.equals("Student") && !userId.startsWith("s_")) {
            showError("Student ID must start with 's_'.");
            return;
        }
        if (role.equals("Supervisor") && !userId.startsWith("sup_")) {
            showError("Supervisor ID must start with 'sup_'.");
            return;
        }
        if (role.equals("Admin") && !userId.startsWith("a_")) {
            showError("Admin ID must start with 'a_'.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }
        if (gender.isEmpty()) {
            showError("Please select gender.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age <= 0) {
                showError("Age must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Invalid age.");
            return;
        }

        // Insert into database
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO users (UserId, Name, Email, Passward, role, age) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, userId);
                stmt.setString(2, firstName + " " + lastName);
                stmt.setString(3, email);
                stmt.setString(4, password); // Use hashing in real apps
                stmt.setString(5, role);
                stmt.setInt(6, age);
                stmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Signup successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new SigninScreen();

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database error occurred.");
        }
    }

    private boolean emailExists(String email) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE Email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error checking email.");
            return true;
        }
    }

    private void showError(String msg) {
        statusLabel.setText(msg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignupScreen::new);
    }
}
