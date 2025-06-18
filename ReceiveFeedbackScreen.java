import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class ReceiveFeedbackScreen extends JFrame {
    private JComboBox<String> cbProjectTitle;
    private JComboBox<String> cbVersion;
    private JButton btnViewFeedback;
    private JLabel lblFeedbackText;
    private JLabel lblDate;
    private JLabel lblMessage;
    private JButton btnOpenFile;

    private String currentScreen = "ReceiveFeedback"; // Track current screen

    public ReceiveFeedbackScreen() {
        setTitle("Receive Feedback");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel
        JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        selectionPanel.setBackground(new Color(255, 253, 208)); // Cream
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblProjTitle = new JLabel("Project:");
        lblProjTitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        cbProjectTitle = new JComboBox<>(new String[]{
                "Select Project", "FYP Portal", "AI Chatbot", "Library Management"
        });
        cbProjectTitle.setFont(new Font("SansSerif", Font.PLAIN, 15));

        JLabel lblVerTitle = new JLabel("Version:");
        lblVerTitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        cbVersion = new JComboBox<>(new String[]{
                "Select Version", "Proposal", "SRS", "Design", "Final Report"
        });
        cbVersion.setFont(new Font("SansSerif", Font.PLAIN, 15));

        btnViewFeedback = new JButton("View Feedback");
        btnViewFeedback.setBackground(new Color(46, 125, 50));
        btnViewFeedback.setForeground(Color.WHITE);
        btnViewFeedback.setFocusPainted(false);
        btnViewFeedback.setFont(new Font("SansSerif", Font.BOLD, 14));

        selectionPanel.add(lblProjTitle);
        selectionPanel.add(cbProjectTitle);
        selectionPanel.add(lblVerTitle);
        selectionPanel.add(cbVersion);
        selectionPanel.add(new JLabel(""));
        selectionPanel.add(btnViewFeedback);

        // Feedback Text Area
        lblFeedbackText = new JLabel();
        lblFeedbackText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblFeedbackText.setVerticalAlignment(SwingConstants.TOP);
        lblFeedbackText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(lblFeedbackText);

        // Bottom Panel
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        bottomPanel.setBackground(new Color(245, 245, 245)); // Light gray
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        lblDate = new JLabel("");
        lblDate.setFont(new Font("SansSerif", Font.PLAIN, 13));

        btnOpenFile = new JButton("Open Attached File");
        btnOpenFile.setBackground(new Color(46, 125, 50));
        btnOpenFile.setForeground(Color.WHITE);
        btnOpenFile.setFocusPainted(false);
        btnOpenFile.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnOpenFile.setEnabled(false);

        lblMessage = new JLabel("");
        lblMessage.setFont(new Font("SansSerif", Font.ITALIC, 13));
        lblMessage.setForeground(Color.RED);

        bottomPanel.add(lblDate);
        bottomPanel.add(btnOpenFile);
        bottomPanel.add(lblMessage);

        add(selectionPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnViewFeedback.addActionListener(e -> loadFeedback());
        btnOpenFile.addActionListener(e -> openFeedbackFile());

        // === MENU BAR SETUP ===
        JMenuBar menuBar = new JMenuBar();

        JMenu homeMenu = new JMenu("Home");
        JMenuItem dashboardItem = new JMenuItem("Dashboard");
        JMenuItem logoutItem = new JMenuItem("Logout");

        dashboardItem.addActionListener(e -> {
            new ProjectDashboard().setVisible(true);
            dispose();
        });

        logoutItem.addActionListener(e -> {
            dispose();
            new WelcomeScreen().setVisible(true);
        });

        homeMenu.add(dashboardItem);
        homeMenu.add(logoutItem);

        JMenu projectMenu = new JMenu("Project");
        JMenuItem exploreItem = new JMenuItem("Explore Projects");
        JMenuItem submitWorkItem = new JMenuItem("Submit Work Product");
        JMenuItem feedbackItem = new JMenuItem("Feedbacks");

        exploreItem.addActionListener(e -> {
            if (currentScreen.equals("ExploreProject")) {
                JOptionPane.showMessageDialog(this, "Already in Explore Projects screen.");
            } else {
                currentScreen = "ExploreProject";
                new ExploreProject().setVisible(true);
                dispose();
            }
        });
        MainController controller = new MainController(); 

        submitWorkItem.addActionListener(e -> {
            
            if (currentScreen.equals("SubmitWorkProduct")) {
                JOptionPane.showMessageDialog(this, "Already in Work Product screen.");
            } else {
                currentScreen = "SubmitWorkProduct";
                new SWP(controller).setVisible(true);
                dispose();
            }
        });

        feedbackItem.addActionListener(e -> {
            if (currentScreen.equals("ReceiveFeedback")) {
                JOptionPane.showMessageDialog(this, "Already in Feedback screen.");
            } else {
                currentScreen = "ReceiveFeedback";
                new ReceiveFeedbackScreen().setVisible(true);
                dispose();
            }
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

        // Create dummy files for testing
        createDummyFiles();
    }

    private void loadFeedback() {
        String project = (String) cbProjectTitle.getSelectedItem();
        String version = (String) cbVersion.getSelectedItem();

        if (project.equals("Select Project") || version.equals("Select Version")) {
            lblMessage.setText("Please select both project and version.");
            lblFeedbackText.setText("");
            lblDate.setText("");
            btnOpenFile.setEnabled(false);
            return;
        }

        File feedbackFile = new File(project + "_" + version + "_Review.pdf");
        if (feedbackFile.exists()) {
            lblFeedbackText.setText("<html><b>Feedback:</b><br>Feedback for " + version + " of " + project + "</html>");
            lblDate.setText("Date: " + LocalDate.now());
            lblMessage.setText("Feedback Available");
            btnOpenFile.setEnabled(true);
        } else {
            lblFeedbackText.setText("<html><b>No feedback available.</b></html>");
            lblDate.setText("");
            lblMessage.setText("Please check back later.");
            btnOpenFile.setEnabled(false);
        }
    }

    private void openFeedbackFile() {
        try {
            String project = (String) cbProjectTitle.getSelectedItem();
            String version = (String) cbVersion.getSelectedItem();
            File file = new File(project + "_" + version + "_Review.pdf");
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            } else {
                JOptionPane.showMessageDialog(this, "File not found.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error opening file.");
        }
    }

    private void createDummyFiles() {
        try {
            createFile("FYP Portal_Proposal_Review.pdf");
            createFile("AI Chatbot_Design_Review.pdf");
            createFile("Library Management_Final Report_Review.pdf");
        } catch (IOException e) {
            System.out.println("Error creating dummy files.");
        }
    }

    private void createFile(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            try (FileWriter fw = new FileWriter(file)) {
                fw.write("This is dummy feedback content for " + filename);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReceiveFeedbackScreen().setVisible(true));
    }
}
