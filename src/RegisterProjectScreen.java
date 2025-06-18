import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.sql.*;

public class RegisterProjectScreen extends JFrame {
    private MainController controller;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextArea languagesArea;
    private JTextArea coursesArea;
    private JTextField cgpaField;
    private static HashSet<String> registeredTitles = new HashSet<>();

    public RegisterProjectScreen() {
        setTitle("Register Project");
        setSize(700, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(255, 253, 208));
        Color fontColor = Color.BLACK;

        JMenu homeMenu = new JMenu("Home");
        homeMenu.setForeground(fontColor);
        JMenu projectMenu = new JMenu("Project");
        projectMenu.setForeground(fontColor);
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setForeground(fontColor);

        JMenuItem dashboardItem = new JMenuItem("Dashboard");
        dashboardItem.addActionListener(e -> {
            this.dispose();
            new SupervisorDashboard(controller).setVisible(true);
        });

        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            this.dispose();
            new WelcomeScreen().setVisible(true);
        });

        homeMenu.add(dashboardItem);
        homeMenu.add(logoutItem);

        JMenuItem approveProjectItem = new JMenuItem("Approve Project");
        approveProjectItem.addActionListener(e -> {
            this.dispose();
            new ApproveProjectScreen().setVisible(true);
        });

        JMenuItem registerProjectItem = new JMenuItem("Register Project");
        registerProjectItem.addActionListener(e -> {
            this.dispose();
            new RegisterProjectScreen().setVisible(true);
        });

        JMenuItem giveFeedbackItem = new JMenuItem("Give Feedback");
        giveFeedbackItem.addActionListener(e -> {
            new GiveFeedback(new MainController()).setVisible(true);
        });

        projectMenu.add(approveProjectItem);
        projectMenu.add(registerProjectItem);
        projectMenu.add(giveFeedbackItem);

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "<html><b>Quaid-e-Azam University</b><br>" +
                        "Department: Computer Science<br>" +
                        "Admin Contact: +92-300-1234567</html>",
                "About",
                JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        menuBar.add(homeMenu);
        menuBar.add(projectMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

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

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 12, 12));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        formPanel.setBackground(new Color(255, 253, 208));

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
        if (!description.isEmpty() && description.length() > 100) {
            showCustomDialog("Project Description must be at most 100 characters.", "Validation Error", true);
            return;
        }
        if (languages.isEmpty()) {
            showCustomDialog("Required Languages are mandatory.", "Validation Error", true);
            return;
        }
        if (courses.isEmpty()) {
            showCustomDialog("Prerequisite Courses are mandatory.", "Validation Error", true);
            return;
        }

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

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pms", "root", "")) {

            // 1. Insert into projects
            String projectId = "PRJ" + System.currentTimeMillis(); // Simple unique ID
            PreparedStatement ps1 = conn.prepareStatement("INSERT INTO projects (project_id, title, description, supervisor) VALUES (?, ?, ?, ?)");
            ps1.setString(1, projectId);
            ps1.setString(2, title);
            ps1.setString(3, description);
            ps1.setString(4, "SUP001"); // Dummy supervisor ID, replace with actual if needed
            ps1.executeUpdate();

            // 2. Insert into project_languages
            PreparedStatement ps2 = conn.prepareStatement("INSERT INTO project_languages (project_id, languages) VALUES (?, ?)");
            ps2.setString(1, projectId);
            ps2.setString(2, languages);
            ps2.executeUpdate();

            // 3. Insert into project_prerequisite
            PreparedStatement ps3 = conn.prepareStatement("INSERT INTO project_prerequisite (project_id, prerequisite) VALUES (?, ?)");
            ps3.setString(1, projectId);
            ps3.setString(2, courses);
            ps3.executeUpdate();

            registeredTitles.add(title.toLowerCase());
            showCustomDialog("Project registered successfully!", "Success", false);
            clearForm();

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                showCustomDialog("Project title already exists in the database.", "Database Error", true);
            } else {
                showCustomDialog("Database error: " + e.getMessage(), "Error", true);
            }
        }
    }

    private void showCustomDialog(String message, String title, boolean isError) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.setResizable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 253, 208));

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
        SwingUtilities.invokeLater(RegisterProjectScreen::new);
    }
}
