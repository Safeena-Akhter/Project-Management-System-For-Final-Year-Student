import javax.swing.*;
import java.awt.*;
//import java.awt.event.*;
import java.util.List;

public class ApproveProjectScreen extends JFrame {

    private JTabbedPane tabbedPane;
    private JPanel pendingPanel, approvedPanel;

    private ProjectController controller;

    public ApproveProjectScreen(ProjectController controller) {
        this.controller = controller;

        setTitle("Approve Projects");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load sample projects into controller (optional)
        controller.loadSampleProjects();

        tabbedPane = new JTabbedPane();

        pendingPanel = new JPanel();
        pendingPanel.setLayout(new BoxLayout(pendingPanel, BoxLayout.Y_AXIS));
        populatePendingProjects();

        approvedPanel = new JPanel();
        approvedPanel.setLayout(new BoxLayout(approvedPanel, BoxLayout.Y_AXIS));
        populateApprovedProjects();

        JScrollPane pendingScroll = new JScrollPane(pendingPanel);
        JScrollPane approvedScroll = new JScrollPane(approvedPanel);

        tabbedPane.addTab("Pending Projects", pendingScroll);
        tabbedPane.addTab("Approved Projects", approvedScroll);

        add(tabbedPane);
        setVisible(true);
    }

    private void populatePendingProjects() {
        pendingPanel.removeAll();

        List<ProjectController.Project> pendingProjects = controller.getPendingProjects();

        for (ProjectController.Project project : pendingProjects.toArray(new ProjectController.Project[0])) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createTitledBorder(project.title));
            card.setBackground(new Color(255, 253, 208));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

            JTextArea info = new JTextArea(
                "Student Justification: " + project.studentJustification
            );
            info.setEditable(false);
            info.setBackground(new Color(255, 253, 208));
            info.setLineWrap(true);
            info.setWrapStyleWord(true);

            card.add(new JScrollPane(info), BorderLayout.CENTER);

            JPanel decisionPanel = new JPanel(new GridBagLayout());
            decisionPanel.setBackground(new Color(255, 253, 208));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5,5,5,5);
            gbc.anchor = GridBagConstraints.WEST;

            JRadioButton approveBtn = new JRadioButton("Approve");
            JRadioButton rejectBtn = new JRadioButton("Reject");
            ButtonGroup group = new ButtonGroup();
            group.add(approveBtn);
            group.add(rejectBtn);

            gbc.gridx = 0;
            gbc.gridy = 0;
            decisionPanel.add(approveBtn, gbc);
            gbc.gridx = 1;
            decisionPanel.add(rejectBtn, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            decisionPanel.add(new JLabel("Supervisor Comments:"), gbc);

            JTextArea supervisorComments = new JTextArea(3, 30);
            JScrollPane scrollPane = new JScrollPane(supervisorComments);
            gbc.gridy = 2;
            decisionPanel.add(scrollPane, gbc);

            JButton submitBtn = new JButton("Submit Decision");
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.CENTER;
            decisionPanel.add(submitBtn, gbc);

            submitBtn.addActionListener(e -> {
                if (!approveBtn.isSelected() && !rejectBtn.isSelected()) {
                    JOptionPane.showMessageDialog(this, "Please select Approve or Reject.");
                    return;
                }
                String comments = supervisorComments.getText().trim();
                if (comments.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Supervisor comments are required.");
                    return;
                }

                boolean approve = approveBtn.isSelected();
                // Call controller to approve/reject
                ProjectController.ValidationResult result = controller.approveProject(project, approve, comments);
                if (!result.valid) {
                    JOptionPane.showMessageDialog(this, result.message);
                    return;
                }

                // Refresh UI
                populatePendingProjects();
                populateApprovedProjects();
                revalidate();
                repaint();
            });

            card.add(decisionPanel, BorderLayout.SOUTH);

            pendingPanel.add(card);
            pendingPanel.add(Box.createVerticalStrut(10));
        }

        pendingPanel.revalidate();
        pendingPanel.repaint();
    }

    private void populateApprovedProjects() {
        approvedPanel.removeAll();

        List<ProjectController.Project> approvedProjects = controller.getApprovedProjects();

        for (ProjectController.Project project : approvedProjects) {
            JPanel card = new JPanel(new GridLayout(0, 1));
            card.setBorder(BorderFactory.createTitledBorder(project.title));
            card.setBackground(new Color(220, 255, 220));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

            card.add(new JLabel("Student Reg No: " + project.studentRegNo));
            card.add(new JLabel("Status: " + project.status));
            card.add(new JLabel("Supervisor Comments: " + project.supervisorComments));

            approvedPanel.add(card);
            approvedPanel.add(Box.createVerticalStrut(10));
        }

        approvedPanel.revalidate();
        approvedPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProjectController controller = new ProjectController();
            new ApproveProjectScreen(controller);
        });
    }
}



