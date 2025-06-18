import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApproveProjectScreen extends JFrame {
    private List<ProjectRequest> pendingProjects = new ArrayList<>();
    private List<ProjectRequest> approvedProjects = new ArrayList<>();
    private List<ProjectRequest> rejectedProjects = new ArrayList<>();

    private JPanel pendingPanel, approvedPanel, rejectedPanel;

    public ApproveProjectScreen() {
        setTitle("Approve Projects");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadProjectsFromDatabase();

        JTabbedPane tabbedPane = new JTabbedPane();

        pendingPanel = new JPanel();
        pendingPanel.setLayout(new BoxLayout(pendingPanel, BoxLayout.Y_AXIS));
        populatePendingProjects();

        approvedPanel = new JPanel();
        approvedPanel.setLayout(new BoxLayout(approvedPanel, BoxLayout.Y_AXIS));
        populateApprovedProjects();

        rejectedPanel = new JPanel();
        rejectedPanel.setLayout(new BoxLayout(rejectedPanel, BoxLayout.Y_AXIS));
        populateRejectedProjects();

        tabbedPane.addTab("Pending Projects", new JScrollPane(pendingPanel));
        tabbedPane.addTab("Approved Projects", new JScrollPane(approvedPanel));
        tabbedPane.addTab("Rejected Projects", new JScrollPane(rejectedPanel));

        add(tabbedPane);
        // === Menu Bar ===
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
        new SupervisorDashboard(new MainController()).setVisible(true);
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
    // === End Menu Bar ===

        setVisible(true);
    }

    private void loadProjectsFromDatabase() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = """
                SELECT pr.request_id, pr.project_id, pr.student_id, pr.justification, pr.status,
                       pr.supervisor_comments, p.title
                FROM project_requests pr
                JOIN projects p ON pr.project_id = p.project_id
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProjectRequest request = new ProjectRequest(
                    rs.getString("request_id"),
                    rs.getString("project_id"),
                    rs.getString("student_id"),
                    rs.getString("justification"),
                    rs.getString("status"),
                    rs.getString("supervisor_comments"),
                    rs.getString("title")
                );

                switch (request.status) {
                    case "Approved" -> approvedProjects.add(request);
                    case "Rejected" -> rejectedProjects.add(request);
                    default -> pendingProjects.add(request);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    private void populatePendingProjects() {
        pendingPanel.removeAll();

        for (ProjectRequest request : new ArrayList<>(pendingProjects)) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createTitledBorder(request.projectTitle));
            card.setBackground(new Color(255, 253, 208));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

            JTextArea info = new JTextArea("Justification: " + request.justification);
            info.setEditable(false);
            info.setBackground(new Color(255, 253, 208));
            info.setLineWrap(true);
            info.setWrapStyleWord(true);
            card.add(new JScrollPane(info), BorderLayout.CENTER);

            JPanel decisionPanel = new JPanel(new GridBagLayout());
            decisionPanel.setBackground(new Color(255, 253, 208));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
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

                request.status = approveBtn.isSelected() ? "Approved" : "Rejected";
                request.supervisorComments = comments;

                updateProjectRequestInDB(request);

                if (approveBtn.isSelected()) {
                    approvedProjects.add(request);
                } else {
                    rejectedProjects.add(request);
                }

                pendingProjects.remove(request);
                populatePendingProjects();
                populateApprovedProjects();
                populateRejectedProjects();
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
        for (ProjectRequest request : approvedProjects) {
            JPanel card = new JPanel(new GridLayout(0, 1));
            card.setBorder(BorderFactory.createTitledBorder(request.projectTitle));
            card.setBackground(new Color(220, 255, 220));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

            card.add(new JLabel("Student ID: " + request.studentId));
            card.add(new JLabel("Status: " + request.status));
            card.add(new JLabel("Supervisor Comments: " + request.supervisorComments));

            approvedPanel.add(card);
            approvedPanel.add(Box.createVerticalStrut(10));
        }
        approvedPanel.revalidate();
        approvedPanel.repaint();
    }

    private void populateRejectedProjects() {
        rejectedPanel.removeAll();
        for (ProjectRequest request : rejectedProjects) {
            JPanel card = new JPanel(new GridLayout(0, 1));
            card.setBorder(BorderFactory.createTitledBorder(request.projectTitle));
            card.setBackground(new Color(255, 220, 220));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

            card.add(new JLabel("Student ID: " + request.studentId));
            card.add(new JLabel("Status: " + request.status));
            card.add(new JLabel("Supervisor Comments: " + request.supervisorComments));

            rejectedPanel.add(card);
            rejectedPanel.add(Box.createVerticalStrut(10));
        }
        rejectedPanel.revalidate();
        rejectedPanel.repaint();
    }

    private void updateProjectRequestInDB(ProjectRequest request) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE project_requests SET status = ?, supervisor_comments = ? WHERE request_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, request.status);
            ps.setString(2, request.supervisorComments);
            ps.setString(3, request.requestId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update project request in database.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ApproveProjectScreen::new);
    }

    // === Inner Data Class ===
    static class ProjectRequest {
        String requestId;
        String projectId;
        String studentId;
        String justification;
        String status;
        String supervisorComments;
        String projectTitle;

        public ProjectRequest(String requestId, String projectId, String studentId, String justification,
                              String status, String supervisorComments, String projectTitle) {
            this.requestId = requestId;
            this.projectId = projectId;
            this.studentId = studentId;
            this.justification = justification;
            this.status = status;
            this.supervisorComments = supervisorComments;
            this.projectTitle = projectTitle;
        }
    }
}
