import javax.swing.*;
import java.awt.*;

public class ProjectDashboard extends JFrame {
    private User currentUser;
    private Project currentProject;

    public ProjectDashboard() {
        this.currentUser = SessionManager.currentUser;

        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "No user session found. Redirecting to login.");
            dispose();
            new SigninScreen().setVisible(true);
            return;
        }

        this.currentProject = Project.getProjectByStudentId(currentUser.getUserId());
        SessionManager.projectTitle = currentProject != null ? currentProject.getTitle() : "No Project";

        initComponents(currentUser.getName());
    }

    private void initComponents(String nameToShow) {
        setTitle("Project Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Color qauGreen = new Color(46, 125, 50);
        Color cream = new Color(255, 253, 208);
        Color subBackground = new Color(246, 246, 247);
        Color fontColor = Color.BLACK;
        Color optionalColor = new Color(216, 217, 218);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(cream);

        JLabel titleLabel = new JLabel("Welcome, " + nameToShow, JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(qauGreen);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        String projectText;
        if (currentProject != null) {
            projectText = "Your Final Year Project:\n" +
                    "Title: " + currentProject.getTitle() + "\n" +
                    "Supervisor: " + currentProject.getSupervisor() + "\n" +
                    "Status: " + currentProject.getStatus() + "\n" +
                    "Description: " + currentProject.getDescription();
        } else {
            projectText = "No project assigned.";
        }

        JTextArea projectDetails = new JTextArea(projectText);
        projectDetails.setFont(new Font("SansSerif", Font.PLAIN, 16));
        projectDetails.setEditable(false);
        projectDetails.setBackground(subBackground);
        projectDetails.setForeground(fontColor);
        projectDetails.setBorder(BorderFactory.createLineBorder(optionalColor, 2));
        projectDetails.setMargin(new Insets(10, 10, 10, 10));
        mainPanel.add(projectDetails, BorderLayout.CENTER);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(cream);

        JMenu homeMenu = new JMenu("Home");
        JMenu projectMenu = new JMenu("Project");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem dashboardItem = new JMenuItem("Dashboard");
        dashboardItem.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "You are already on Dashboard.")
        );

        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            dispose();
            new SigninScreen().setVisible(true);
        });

        JMenuItem exploreItem = new JMenuItem("Explore Projects");
        exploreItem.addActionListener(e -> {
            new ExploreProject().setVisible(true);
            dispose();
        });

        JMenuItem submitWorkItem = new JMenuItem("Submit Work Product");
        submitWorkItem.addActionListener(e -> {
            new SWP(null).setVisible(true);
            dispose();
        });

        JMenuItem feedbackItem = new JMenuItem("Feedbacks");
        feedbackItem.addActionListener(e -> {
            new ReceiveFeedbackScreen().setVisible(true);
            dispose();
        });

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "<html><b>Quaid-e-Azam University</b><br>" +
                        "Department: Computer Science<br>" +
                        "Admin Contact: +92-300-1234567</html>",
                "About", JOptionPane.INFORMATION_MESSAGE)
        );

        homeMenu.add(dashboardItem);
        homeMenu.add(logoutItem);
        projectMenu.add(exploreItem);
        projectMenu.add(submitWorkItem);
        projectMenu.add(feedbackItem);
        helpMenu.add(aboutItem);

        menuBar.add(homeMenu);
        menuBar.add(projectMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        getContentPane().add(mainPanel);
    }

    public static void main(String[] args) {
        User user = new User("Safeena Akhter", "safeena@qau.edu.pk", "pass123", "Student", "s_04072312008", 21);
        SessionManager.currentUser = user;

        SwingUtilities.invokeLater(() -> new ProjectDashboard().setVisible(true));
    }
}
