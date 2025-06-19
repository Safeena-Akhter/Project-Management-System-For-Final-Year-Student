import javax.swing.*;
import java.awt.*;
import java.util.HashSet;

public class RegisterProjectScreen extends JFrame {

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextArea languagesArea;
    private JTextArea coursesArea;
    private JTextField cgpaField;
    private static HashSet<String> registeredTitles = new HashSet<>(); // Simulate existing project titles

    public RegisterProjectScreen() {
        setTitle("Register Project");
        setSize(700, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === Header Panel ===
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Register Final Year Project", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        JLabel sloganLabel = new JLabel("Define your project and set academic requirements", JLabel.CENTER);
        sloganLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));

        topPanel.add(titleLabel);
        topPanel.add(sloganLabel);

        add(topPanel, BorderLayout.NORTH);

        // === Form Panel ===
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 12, 12));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        formPanel.setBackground(new Color(255, 253, 208));  // Cream background

        formPanel.add(new JLabel("Project Title *:"));
        titleField = new JTextField();
        formPanel.add(titleField);

        formPanel.add(new JLabel("Project Description:"));
        descriptionArea = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(descriptionArea));

        formPanel.add(new JLabel("Required Languages *:"));
        languagesArea = new JTextArea(2, 20);
        formPanel.add(new JScrollPane(languagesArea));

        formPanel.add(new JLabel("Prerequisite Courses *:"));
        coursesArea = new JTextArea(2, 20);
        formPanel.add(new JScrollPane(coursesArea));

        formPanel.add(new JLabel("Minimum CGPA *:"));
        cgpaField = new JTextField();
        formPanel.add(cgpaField);

        add(formPanel, BorderLayout.CENTER);

        // === Bottom Panel with Register Button ===
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        JButton registerBtn = new JButton("Register");
        styleButton(registerBtn);
        registerBtn.addActionListener(e -> validateAndSubmit());

        buttonPanel.add(registerBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(46, 125, 50));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 35));
        button.setFocusPainted(false);
    }

    private void validateAndSubmit() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String languages = languagesArea.getText().trim();
        String courses = coursesArea.getText().trim();
        String cgpaText = cgpaField.getText().trim();

        // Validate Project Title
        if (title.isEmpty()) {
            showCustomDialog("Project Title is required.", "Validation Error", true);
            return;
        }
        if (title.length() > 50) {
            showCustomDialog("Project Title must be at most 50 characters.", "Validation Error", true);
            return;
        }
        if (title.matches(".*\\d.*")) {
            showCustomDialog("Project Title must not contain digits.", "Validation Error", true);
            return;
        }
        if (registeredTitles.contains(title.toLowerCase())) {
            showCustomDialog("Project Title already exists. Please use a different title.", "Validation Error", true);
            return;
        }

        // Validate Description (optional, max 100 chars)
        if (!description.isEmpty() && description.length() > 100) {
            showCustomDialog("Project Description must be at most 100 characters.", "Validation Error", true);
            return;
        }

        // Validate Required Languages
        if (languages.isEmpty()) {
            showCustomDialog("Required Languages are mandatory.", "Validation Error", true);
            return;
        }

        // Validate Prerequisite Courses
        if (courses.isEmpty()) {
            showCustomDialog("Prerequisite Courses are mandatory.", "Validation Error", true);
            return;
        }

        // Validate CGPA
        double cgpa;
        try {
            cgpa = Double.parseDouble(cgpaText);
            if (cgpa < 2.5 || cgpa > 4.0) {
                showCustomDialog("Minimum CGPA must be between 2.5 and 4.0.", "Validation Error", true);
                return;
            }
        } catch (NumberFormatException e) {
            showCustomDialog("Minimum CGPA must be a valid number.", "Validation Error", true);
            return;
        }

        // Register project
        registeredTitles.add(title.toLowerCase());
        showCustomDialog("Project registered successfully!", "Success", false);
        clearForm();
    }
    private void showCustomDialog(String message, String title, boolean isError) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.setResizable(false);

        // Background panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 253, 208)); // Cream background

        // Message label
        JLabel messageLabel = new JLabel("<html><body style='text-align:center;'>" + message + "</body></html>", JLabel.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(messageLabel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 253, 208));

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        okButton.setPreferredSize(new Dimension(90, 35));
        okButton.setFocusPainted(false);
        okButton.setBackground(isError ? new Color(211, 47, 47) : new Color(46, 125, 50));
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(okButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        languagesArea.setText("");
        coursesArea.setText("");
        cgpaField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterProjectScreen::new);
    }
}
