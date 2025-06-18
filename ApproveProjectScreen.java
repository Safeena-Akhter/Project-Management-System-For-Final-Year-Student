import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// ... [imports remain unchanged]
public class ApproveProjectScreen extends JFrame {
    private MainController controller;

    private JTabbedPane tabbedPane;
    private JPanel pendingPanel, approvedPanel, rejectedPanel;

    private java.util.List<ProjectRequest> pendingProjects = new ArrayList<>();
    private java.util.List<ProjectRequest> approvedProjects = new ArrayList<>();
    private java.util.List<ProjectRequest> rejectedProjects = new ArrayList<>();

    public ApproveProjectScreen() {
        setTitle("Approve Projects");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Sample data
        loadSampleProjects();

        tabbedPane = new JTabbedPane();

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

        // === MENU BAR SETUP ===
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
            new ApproveProjectScreen().setVisible(true);
            dispose();
        });

        JMenuItem registerProjectItem = new JMenuItem("Register Project");
        registerProjectItem.addActionListener(e -> {
            new RegisterProjectScreen().setVisible(true);
            dispose();
        });

        JMenuItem giveFeedbackItem = new JMenuItem("Give Feedback");
        giveFeedbackItem.addActionListener(e -> {
            GiveFeedback giveFeedbackScreen = new GiveFeedback(new MainController());
            giveFeedbackScreen.setVisible(true);
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

        setVisible(true);
    }

    private void loadSampleProjects() {
        pendingProjects.add(new ProjectRequest(
            "Online Voting System", "04072312010", "Build secure voting app", "I am passionate about elections."
        ));
        pendingProjects.add(new ProjectRequest(
            "AI Chatbot", "04072312011", "Create chatbot with NLP", "Chatbots are my FYP interest."
        ));
    }

    private void populatePendingProjects() {
        pendingPanel.removeAll();

        for (ProjectRequest request : new ArrayList<>(pendingProjects)) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createTitledBorder(request.title));
            card.setBackground(new Color(255, 253, 208));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

            JTextArea info = new JTextArea("Student Justification: " + request.studentJustification);
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
                request.status = approveBtn.isSelected() ? "Approved" : "Rejected";
                request.supervisorComments = comments;

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
            card.setBorder(BorderFactory.createTitledBorder(request.title));
            card.setBackground(new Color(220, 255, 220));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

            card.add(new JLabel("Student Reg No: " + request.studentRegNo));
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
            card.setBorder(BorderFactory.createTitledBorder(request.title));
            card.setBackground(new Color(255, 220, 220));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

            card.add(new JLabel("Student Reg No: " + request.studentRegNo));
            card.add(new JLabel("Status: " + request.status));
            card.add(new JLabel("Supervisor Comments: " + request.supervisorComments));

            rejectedPanel.add(card);
            rejectedPanel.add(Box.createVerticalStrut(10));
        }

        rejectedPanel.revalidate();
        rejectedPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ApproveProjectScreen::new);
    }

    // === Helper Class ===
    static class ProjectRequest {
        String title, studentRegNo, description, studentJustification;
        String supervisorComments = "";
        String status = "Pending";

        public ProjectRequest(String title, String studentRegNo, String description, String justification) {
            this.title = title;
            this.studentRegNo = studentRegNo;
            this.description = description;
            this.studentJustification = justification;
        }
    }
}
