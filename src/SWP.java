import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class SWP extends JFrame {
    private ArrayList<File> attachedFiles = new ArrayList<>();
    private static final String[] ALLOWED_EXTENSIONS = {"docx", "pdf", "xls", "jpg"};
    private MainController controller;

    public SWP(MainController controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        setTitle("Submit Work Product");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(new Color(255, 253, 208));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));

        JLabel headerLabel = new JLabel("Submit Work Product");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        headerLabel.setForeground(new Color(0, 51, 102));
        topPanel.add(headerLabel);

        // Center Panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));

        JTextArea descriptionArea = new JTextArea(4, 25);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JLabel attachmentStatusLabel = new JLabel("Status: No files selected");
        attachmentStatusLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));
        attachmentStatusLabel.setForeground(Color.DARK_GRAY);

        JButton attachButton = new JButton("Choose Files");
        styleButton(attachButton, new Color(200, 200, 200), Color.BLACK);

        JButton submitButton = new JButton("Submit");
        styleButton(submitButton, new Color(46, 125, 50), Color.WHITE);

        // Add to center panel
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        centerPanel.add(new JScrollPane(descriptionArea), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(attachmentStatusLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(attachButton, gbc);

        gbc.gridx = 1;
        centerPanel.add(submitButton, gbc);

        // Attach logic
        attachButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File[] files = chooser.getSelectedFiles();
                attachedFiles.clear();
                if (files.length > 3) {
                    JOptionPane.showMessageDialog(this, "You can only attach up to 3 files.");
                    return;
                }
                for (File file : files) {
                    if (!isAllowedExtension(file)) {
                        JOptionPane.showMessageDialog(this,
                                "Invalid file format: " + file.getName() +
                                        "\nAllowed: .docx, .pdf, .xls, .jpg");
                        attachedFiles.clear();
                        attachmentStatusLabel.setText("Status: No files selected");
                        return;
                    }
                    attachedFiles.add(file);
                }
                attachmentStatusLabel.setText("Status: Files selected (" + attachedFiles.size() + ")");
            }
        });

        // Submit logic
        submitButton.addActionListener(e -> {
            String desc = descriptionArea.getText().trim();

            if (desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a description.");
                return;
            }

            if (desc.length() > 100) {
                JOptionPane.showMessageDialog(this, "Description must be under 100 characters.");
                return;
            }

            if (attachedFiles.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please attach at least one valid file.");
                return;
            }

            boolean success = controller.submitWorkProduct(desc, attachedFiles);

            if (success) {
                int count = controller.getSubmissionCount();
                JOptionPane.showMessageDialog(this,
                        "Work Product Submitted Successfully!\nWork Product Number: " + count);
                descriptionArea.setText("");
                attachmentStatusLabel.setText("Status: No files selected");
                attachedFiles.clear();
            } else {
                JOptionPane.showMessageDialog(this, "Submission failed. Please try again.");
            }
        });

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();

        JMenu homeMenu = new JMenu("Home");
        JMenuItem dashboardItem = new JMenuItem("Dashboard");
        JMenuItem logoutItem = new JMenuItem("Logout");

        dashboardItem.addActionListener(e -> {
            new ProjectDashboard().setVisible(true);
            dispose();
        });

        logoutItem.addActionListener(e -> {
            new WelcomeScreen().setVisible(true);
            dispose();
        });

        homeMenu.add(dashboardItem);
        homeMenu.add(logoutItem);

        JMenu projectMenu = new JMenu("Project");
        JMenuItem exploreItem = new JMenuItem("Explore Projects");
        JMenuItem submitWorkItem = new JMenuItem("Submit Work Product");
        JMenuItem feedbackItem = new JMenuItem("Feedbacks");

        exploreItem.addActionListener(e -> {
            new ExploreProject().setVisible(true);
            dispose();
        });

        submitWorkItem.addActionListener(e -> {
            int count = controller.getSubmissionCount();
            if (count > 0) {
                JOptionPane.showMessageDialog(this, "Work Product already submitted.\nNumber: " + count);
            } else {
                JOptionPane.showMessageDialog(this, "No Work Product submitted yet.");
            }
        });

        feedbackItem.addActionListener(e -> {
            new ReceiveFeedbackScreen().setVisible(true);
            dispose();
        });

        projectMenu.add(exploreItem);
        projectMenu.add(submitWorkItem);
        projectMenu.add(feedbackItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");

        aboutItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "<html><b>Quaid-e-Azam University</b><br>" +
                            "Department: Computer Science<br>" +
                            "Admin Contact: +92-300-1234567</html>",
                    "About",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        helpMenu.add(aboutItem);

        menuBar.add(homeMenu);
        menuBar.add(projectMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private boolean isAllowedExtension(File file) {
        String name = file.getName().toLowerCase();
        for (String ext : ALLOWED_EXTENSIONS) {
            if (name.endsWith("." + ext)) return true;
        }
        return false;
    }

    private void styleButton(JButton button, Color background, Color textColor) {
        button.setBackground(background);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public static void main(String[] args) {
        MainController controller = new MainController();
        SwingUtilities.invokeLater(() -> new SWP(controller).setVisible(true));
    }
}
