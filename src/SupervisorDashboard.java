import javax.swing.*;
import java.awt.*;

public class SupervisorDashboard extends JFrame {
    private MainController controller;

       public SupervisorDashboard(MainController controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        setTitle("Supervisor Dashboard");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Theme colors
        Color qauGreen = new Color(46, 125, 50);
        Color cream = new Color(255, 253, 208);
        Color subBackground = new Color(246, 246, 247);
        Color fontColor = Color.BLACK;
        Color optionalColor = new Color(216, 217, 218);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(cream);

        // Title
        JLabel titleLabel = new JLabel("Welcome, Supervisor", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(qauGreen);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Info area
        JTextArea infoArea = new JTextArea(
                "Welcome to Quaid-e-Azam University Project Management System\n\n" +
    "As a Supervisor, you can:\n" +
    "- Review and approve submitted projects\n" +
    "- Register new project proposals\n" +
    "- Provide feedback to students\n" +
    "- Monitor student progress and submissions\n\n" +
    "Use the menu above to navigate through your options.\n\n" +
    "Your role plays a key part in guiding students toward success."
                                        
        );
        infoArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoArea.setEditable(false);
        infoArea.setBackground(subBackground);
        infoArea.setForeground(fontColor);
        infoArea.setBorder(BorderFactory.createLineBorder(optionalColor, 2));
        infoArea.setMargin(new Insets(10, 10, 10, 10));
        mainPanel.add(infoArea, BorderLayout.CENTER);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(cream);

        JMenu homeMenu = new JMenu("Home");
        JMenu projectMenu = new JMenu("Project");
        JMenu helpMenu = new JMenu("Help");

        homeMenu.setForeground(fontColor);
        projectMenu.setForeground(fontColor);
        helpMenu.setForeground(fontColor);

        // Home items
        JMenuItem dashboardItem = new JMenuItem("Dashboard");
        dashboardItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "You are already on the Dashboard."));
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            dispose();
            new SigninScreen(); // Back to login screen
        });

        homeMenu.add(dashboardItem);
        homeMenu.add(logoutItem);
        logoutItem.addActionListener(e -> {
        this.dispose();
        new WelcomeScreen().setVisible(true);
    });

        // Project items
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
    GiveFeedback giveFeedbackScreen = new GiveFeedback(controller); // Pass your controller
    giveFeedbackScreen.setVisible(true);
});



        projectMenu.add(approveProjectItem);
        projectMenu.add(registerProjectItem);
        projectMenu.add(giveFeedbackItem);

        // Help item
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

        add(mainPanel);
    }

   public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        MainController controller = new MainController(); // Or mock it if needed
        new SupervisorDashboard(controller).setVisible(true);
    });
}

}
