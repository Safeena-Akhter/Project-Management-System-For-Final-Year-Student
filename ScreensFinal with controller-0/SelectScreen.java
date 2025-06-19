import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class SelectScreen extends JFrame {
    private JComboBox<String> projectComboBox;
    private JLabel statusLabel;
    private JButton selectButton;
    private JButton startSelectionButton;
    private JPanel selectionPanel;

    private MainController controller;

    // To track assigned projects
    private Set<String> assignedProjects = new HashSet<>();

    public SelectScreen(MainController controller) {
        this.controller = controller;
        setTitle("Select Project");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Theme colors
        Color topBottomColor = new Color(245, 245, 245);   // light gray
        Color centerColor = new Color(255, 253, 208);      // cream
        Color buttonColor = new Color(46, 125, 50);        // QAU green

        // Initial Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(centerColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Status Label
        statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        // Start Selection Button
        startSelectionButton = new JButton("Select Project");
        styleButton(startSelectionButton, buttonColor);
        startSelectionButton.addActionListener(e -> showProjectSelection());

        JPanel topPanel = new JPanel();
        topPanel.setBackground(centerColor);
        topPanel.add(startSelectionButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Selection Panel (Initially hidden)
        selectionPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        selectionPanel.setBackground(centerColor);
        selectionPanel.setVisible(false);

        JLabel selectLabel = new JLabel("Choose from Available Projects:");
        selectLabel.setHorizontalAlignment(SwingConstants.CENTER);
        selectLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        selectionPanel.add(selectLabel);

        projectComboBox = new JComboBox<>();
        selectionPanel.add(projectComboBox);

        selectButton = new JButton("Confirm Selection");
        styleButton(selectButton, buttonColor);
        selectButton.addActionListener(e -> handleProjectConfirm());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(centerColor);
        buttonPanel.add(selectButton);
        selectionPanel.add(buttonPanel);

        mainPanel.add(selectionPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    private void showProjectSelection() {
        updateProjectList();
        selectionPanel.setVisible(true);
        startSelectionButton.setEnabled(false);
    }

    private void updateProjectList() {
        String[] baseProjects = {
            "AI Chatbot", "Smart Attendance System", "E-commerce App",
            "Blockchain Voting", "Weather Forecast App"
        };

        projectComboBox.removeAllItems();
        for (String project : baseProjects) {
            if (assignedProjects.contains(project)) {
                projectComboBox.addItem(project + " (Assigned)");
            } else {
                projectComboBox.addItem(project);
            }
        }
    }

    private void handleProjectConfirm() {
        String selected = (String) projectComboBox.getSelectedItem();

        if (selected == null) {
            showMessage("Please select a project.");
            return;
        }

        if (selected.contains("Assigned")) {
            showError("This project is already assigned.");
            return;
        }

        // Mark as assigned
        assignedProjects.add(selected);
        showMessage("Project '" + selected + "' assigned successfully!");
        updateProjectList();
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(180, 35));
    }

    private void showMessage(String msg) {
        statusLabel.setText(msg);
        statusLabel.setForeground(new Color(46, 125, 50));
    }

    private void showError(String msg) {
        statusLabel.setText(msg);
        statusLabel.setForeground(Color.RED);
    }
}
