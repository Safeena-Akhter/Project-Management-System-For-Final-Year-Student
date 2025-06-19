import javax.swing.*;
import java.awt.*;

public class RegisterProjectScreen extends JFrame {

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextArea languagesArea;
    private JTextArea coursesArea;
    private JTextField cgpaField;

    private ProjectController controller;

    public RegisterProjectScreen(ProjectController controller) {
        this.controller = controller;

        setTitle("Register Project");
        setSize(700, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
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

        // Form Panel
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

        // Bottom Panel with Register Button
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

        ProjectController.ValidationResult result = controller.registerProject(title, description, languages, courses, cgpaText);

        if (result.valid) {
            showCustomDialog(result.message, "Success", false);
            clearForm();
        } else {
            showCustomDialog(result.message, "Validation Error", true);
        }
    }

    private void showCustomDialog(String message, String title, boolean isError) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 253, 208)); // Cream background

        JLabel messageLabel = new JLabel("<html><body style='text-align:center;'>" + message + "</body></html>", JLabel.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(messageLabel, BorderLayout.CENTER);

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
        SwingUtilities.invokeLater(() -> {
            ProjectController controller = new ProjectController();
            new RegisterProjectScreen(controller);
        });
    }
}
















/*import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class RegisterProjectScreen extends JFrame {

    // Declare all fields here
    private ProjectController controller;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JCheckBox htmlCheck, cssCheck, jsCheck, reactCheck, pythonCheck, javaCheck;
    private JCheckBox[] academicChecks;
    private JTextField creditHoursField;
    private JTextField cgpaField;
    private JLabel statusLabel;

    public RegisterProjectScreen(ProjectController controller) {
        this.controller = controller;

        // Theme Colors
        Color primaryBackground = Color.decode("#102039");
        Color cardBackground = Color.decode("#f6f6f7");
        Color textPrimary = Color.WHITE;
        Color textSecondary = Color.BLACK;

        // Theme Fonts
        Font titleFont = new Font("Arial", Font.BOLD, 24);
        Font cardTitleFont = new Font("Arial", Font.BOLD, 16);
        Font cardDescFont = new Font("Arial", Font.PLAIN, 14);

        // Borders
        Border cardBorderNormal = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Border cardBorderHover = BorderFactory.createLineBorder(Color.BLACK, 2);

        setTitle("Register Project");
        setSize(650, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(primaryBackground);

        JLabel titleLabel = new JLabel("Register Project");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(textPrimary);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        contentPanel.setBackground(primaryBackground);

        // Project Title
        JLabel lbl1 = new JLabel("Project Title (max 50 characters):");
        lbl1.setForeground(textPrimary);
        lbl1.setFont(cardTitleFont);
        contentPanel.add(lbl1);
        titleField = new JTextField();
        titleField.setFont(cardDescFont);
        contentPanel.add(titleField);

        // Description
        JLabel lbl2 = new JLabel("Project Description (optional):");
        lbl2.setForeground(textPrimary);
        lbl2.setFont(cardTitleFont);
        contentPanel.add(lbl2);
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(cardDescFont);
        descriptionArea.setBackground(cardBackground);
        descriptionArea.setBorder(cardBorderNormal);
        contentPanel.add(new JScrollPane(descriptionArea));

        // Required Languages
        JLabel lbl3 = new JLabel("Required Languages (at least one):");
        lbl3.setForeground(textPrimary);
        lbl3.setFont(cardTitleFont);
        contentPanel.add(lbl3);
        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        langPanel.setBackground(primaryBackground);
        htmlCheck = new JCheckBox("HTML");
        cssCheck = new JCheckBox("CSS");
        jsCheck = new JCheckBox("JavaScript");
        reactCheck = new JCheckBox("React");
        pythonCheck = new JCheckBox("Python");
        javaCheck = new JCheckBox("Java");

        JCheckBox[] langChecks = { htmlCheck, cssCheck, jsCheck, reactCheck, pythonCheck, javaCheck };
        for (JCheckBox cb : langChecks) {
            cb.setBackground(primaryBackground);
            cb.setForeground(textPrimary);
            cb.setFont(cardDescFont);
            langPanel.add(cb);
        }
        contentPanel.add(langPanel);

        // Academic Requirements
        JLabel lbl4 = new JLabel("Academic Requirements (select all that apply):");
        lbl4.setForeground(textPrimary);
        lbl4.setFont(cardTitleFont);
        contentPanel.add(lbl4);
        JPanel academicPanel = new JPanel(new GridLayout(0, 1));
        academicPanel.setBackground(cardBackground);
        academicPanel.setBorder(cardBorderNormal);
        String[] academicSubjects = {
            "Programming Fundamentals", "Object-Oriented Programming",
            "Data Structures and Algorithms", "Database Systems",
            "Operating Systems", "Computer Networks",
            "Software Engineering", "Web Development",
            "Artificial Intelligence", "Machine Learning"
        };
        academicChecks = new JCheckBox[academicSubjects.length];
        for (int i = 0; i < academicSubjects.length; i++) {
            academicChecks[i] = new JCheckBox(academicSubjects[i]);
            academicChecks[i].setFont(cardDescFont);
            academicChecks[i].setBackground(cardBackground);
            academicChecks[i].setForeground(textSecondary);
            academicPanel.add(academicChecks[i]);
        }
        JScrollPane academicScroll = new JScrollPane(academicPanel);
        academicScroll.setPreferredSize(new Dimension(400, 150));
        contentPanel.add(academicScroll);

        // Credit Hours
        JLabel lbl5 = new JLabel("Credit Hours Completion (120 to 136):");
        lbl5.setForeground(textPrimary);
        lbl5.setFont(cardTitleFont);
        contentPanel.add(lbl5);
        creditHoursField = new JTextField();
        creditHoursField.setFont(cardDescFont);
        contentPanel.add(creditHoursField);

        // CGPA
        JLabel lbl6 = new JLabel("Minimum CGPA (2.5 to 4.0):");
        lbl6.setForeground(textPrimary);
        lbl6.setFont(cardTitleFont);
        contentPanel.add(lbl6);
        cgpaField = new JTextField();
        cgpaField.setFont(cardDescFont);
        contentPanel.add(cgpaField);

        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        contentPanel.add(statusLabel);

        // Submit button
        JButton submitBtn = new JButton("Register Project");
        submitBtn.setFont(cardTitleFont);
        submitBtn.setBackground(cardBackground);
        submitBtn.setFocusPainted(false);
        submitBtn.setBorder(cardBorderNormal);
        contentPanel.add(submitBtn);

        // Hover effect for button
        submitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                submitBtn.setBorder(cardBorderHover);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                submitBtn.setBorder(cardBorderNormal);
            }
        });

        submitBtn.addActionListener(new SubmitListener());

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    // Inner class to handle submit action
    private class SubmitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Example logic (you can expand this)
            String title = titleField.getText().trim();
            if (title.isEmpty()) {
                statusLabel.setText("Project title is required.");
            } else {
                statusLabel.setText("Project registered successfully!");
            }
        }
    }

    // MAIN METHOD to run the screen
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProjectController dummyController = new ProjectController(); // Replace with actual implementation
            new RegisterProjectScreen(dummyController);
        });
    }
}
*/