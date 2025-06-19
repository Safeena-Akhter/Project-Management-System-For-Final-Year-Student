import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReceiveFeedbackScreen extends JFrame {
    private JComboBox<String> cbVersion;
    private JButton btnViewFeedback;
    private JTextArea txtFeedbackText; // changed to JTextArea for multiline text display
    private JLabel lblDate;
    private JLabel lblMessage;
    private JButton btnOpenFile;
    private JLabel lblProjectName;

    private final String project = "FYP Portal";

    private ProjectController controller;

    public ReceiveFeedbackScreen(ProjectController controller) {
        this.controller = controller;

        setTitle("Receive Feedback");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top Panel (Input section)
        JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        selectionPanel.setBackground(new Color(255, 253, 208)); // Cream
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblProjTitle = new JLabel("Project:");
        lblProjTitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lblProjectName = new JLabel(project);
        lblProjectName.setFont(new Font("SansSerif", Font.BOLD, 15));

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
        selectionPanel.add(lblProjectName);
        selectionPanel.add(lblVerTitle);
        selectionPanel.add(cbVersion);
        selectionPanel.add(new JLabel(""));  // empty filler
        selectionPanel.add(btnViewFeedback);

        // Feedback Text Area (multi-line)
        txtFeedbackText = new JTextArea();
        txtFeedbackText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtFeedbackText.setEditable(false);
        txtFeedbackText.setLineWrap(true);
        txtFeedbackText.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(txtFeedbackText);

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

        // Button actions
        btnViewFeedback.addActionListener(e -> loadFeedback());
        btnOpenFile.addActionListener(e -> openFeedbackFile());
    }

    private ProjectController.Feedback currentFeedback = null;

    private void loadFeedback() {
        String version = (String) cbVersion.getSelectedItem();

        if ("Select Version".equals(version)) {
            lblMessage.setText("Please select a version.");
            txtFeedbackText.setText("");
            lblDate.setText("");
            btnOpenFile.setEnabled(false);
            currentFeedback = null;
            return;
        }

        ProjectController.Feedback feedback = controller.getFeedback(project, version);
        if (feedback != null) {
            txtFeedbackText.setText(feedback.text);
            lblDate.setText("Date: " + feedback.date.format(DateTimeFormatter.ISO_DATE));
            lblMessage.setText("Feedback Available");
            btnOpenFile.setEnabled(feedback.filePath != null && !feedback.filePath.trim().isEmpty());
            currentFeedback = feedback;
        } else {
            txtFeedbackText.setText("No feedback available.");
            lblDate.setText("");
            lblMessage.setText("Please check back later.");
            btnOpenFile.setEnabled(false);
            currentFeedback = null;
        }
    }

    private void openFeedbackFile() {
        if (currentFeedback != null && currentFeedback.filePath != null && !currentFeedback.filePath.trim().isEmpty()) {
            try {
                File file = new File(currentFeedback.filePath);
                if (file.exists()) {
                    Desktop.getDesktop().open(file);
                } else {
                    JOptionPane.showMessageDialog(this, "Feedback file not found.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error opening file.");
            }
        }
    }

    public static void main(String[] args) {
        // Example usage:
        ProjectController controller = new ProjectController();

        // Add some feedback for testing:
        controller.addFeedback("FYP Portal", "Proposal",
                new ProjectController.Feedback("Great initial proposal. Please add more details in next version.", LocalDate.now(), null));

        controller.addFeedback("FYP Portal", "Design",
                new ProjectController.Feedback("Design looks solid. See attached diagram.", LocalDate.now(), "design_review.pdf"));

        SwingUtilities.invokeLater(() -> new ReceiveFeedbackScreen(controller).setVisible(true));
    }
}

