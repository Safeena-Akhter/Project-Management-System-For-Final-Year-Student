import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExploreProject extends JFrame {

    static class Project {
        String title, description, supervisor, status;

        Project(String title, String desc, String sup, String status) {
            this.title = title;
            this.description = desc;
            this.supervisor = sup;
            this.status = status;
        }
    }

    private List<Project> projects;
    private List<Project> allProjects;
    private JPanel projectsPanel;

    public ExploreProject() {
        initProjects();
        initComponents();
    }

    private void initProjects() {
        allProjects = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) throw new SQLException("Connection is null");

            String sql = "SELECT title, description, supervisor, status FROM projects";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                String supervisor = rs.getString("supervisor");
                String status = rs.getString("status");

                allProjects.add(new Project(title, description, supervisor, status));
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        projects = new ArrayList<>(allProjects);
    }

    private void initComponents() {
        setTitle("Explore Projects");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.decode("#f6f6f6")); // light background

        JLabel titleLabel = new JLabel("Explore Projects", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.decode("#102039"));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JTextField searchField = new JTextField("Search...");
        searchField.setForeground(Color.GRAY);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setPreferredSize(new Dimension(300, 30));
        searchField.setToolTipText("Search by title or supervisor...");

        Box topBox = Box.createVerticalBox();
        topBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topBox.add(titleLabel);
        topBox.add(Box.createVerticalStrut(10));
        topBox.add(searchField);
        mainPanel.add(topBox, BorderLayout.NORTH);

        projectsPanel = new JPanel();
        projectsPanel.setBackground(Color.decode("#f6f6f6")); // match background
        projectsPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 15, 15));

        if (projects.isEmpty()) {
            JLabel noProjectsLabel = new JLabel("No projects available.");
            noProjectsLabel.setForeground(Color.DARK_GRAY);
            projectsPanel.add(noProjectsLabel);
        } else {
            for (Project project : projects) {
                projectsPanel.add(createProjectCard(project));
            }
        }

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String query = searchField.getText().toLowerCase();
                projects.clear();
                for (Project p : allProjects) {
                    if (p.title.toLowerCase().contains(query) || p.supervisor.toLowerCase().contains(query)) {
                        projects.add(p);
                    }
                }
                updateProjectCards();
            }
        });

        JScrollPane scrollPane = new JScrollPane(projectsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setJMenuBar(createMenuBar());
        getContentPane().add(mainPanel);
    }

    private void updateProjectCards() {
        projectsPanel.removeAll();

        if (projects.isEmpty()) {
            JLabel noProjectsLabel = new JLabel("No matching projects.");
            noProjectsLabel.setForeground(Color.DARK_GRAY);
            projectsPanel.add(noProjectsLabel);
        } else {
            for (Project project : projects) {
                projectsPanel.add(createProjectCard(project));
            }
        }

        projectsPanel.revalidate();
        projectsPanel.repaint();
    }

    private JPanel createProjectCard(Project project) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(300, 150));
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(project.title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.decode("#102039"));

        JTextArea descArea = new JTextArea(project.description);
        descArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descArea.setBackground(Color.WHITE);
        descArea.setForeground(Color.BLACK);
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(null);

        JLabel supervisorLabel = new JLabel("Supervisor: " + project.supervisor);
        supervisorLabel.setForeground(Color.DARK_GRAY);
        JLabel statusLabel = new JLabel("Status: " + project.status);
        statusLabel.setForeground(Color.DARK_GRAY);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setOpaque(false);
        infoPanel.add(supervisorLabel);
        infoPanel.add(statusLabel);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(descArea, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            Border normalBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            );
            Border hoverBorder = BorderFactory.createLineBorder(new Color(0x1B74E4), 2); // blue highlight

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(hoverBorder);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(normalBorder);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (project.status.equalsIgnoreCase("Available")) {
                    List<Project> availableProjects = new ArrayList<>();
                    for (Project p : allProjects) {
                        if (p.status.equalsIgnoreCase("Available")) {
                            availableProjects.add(p);
                        }
                    }
                    new SelectScreen(availableProjects).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(card,
                        "This project is already registered by another student.",
                        "Permission Denied",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return card;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu homeMenu = new JMenu("Home");
        JMenuItem dashboardItem = new JMenuItem("Dashboard");
        dashboardItem.addActionListener(e -> {
            dispose();
            JOptionPane.showMessageDialog(null, "Dashboard clicked (mock)");
        });

        JMenuItem logoutItem = new JMenuItem("Logout");
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

        exploreItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Already on Explore Projects."));
        submitWorkItem.addActionListener(e -> {
            MainController controller = MainController.getInstance();
            new SWP(controller).setVisible(true);
            dispose();
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
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "QAU - CS Department\nContact: +92-300-1234567", "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        menuBar.add(homeMenu);
        menuBar.add(projectMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExploreProject().setVisible(true));
    }

    public static class WrapLayout extends FlowLayout {
        public WrapLayout() { super(); }
        public WrapLayout(int align) { super(align); }
        public WrapLayout(int align, int hgap, int vgap) { super(align, hgap, vgap); }

        @Override
        public Dimension preferredLayoutSize(Container target) {
            return layoutSize(target, true);
        }

        @Override
        public Dimension minimumLayoutSize(Container target) {
            Dimension minimum = layoutSize(target, false);
            minimum.width -= (getHgap() + 1);
            return minimum;
        }

        private Dimension layoutSize(Container target, boolean preferred) {
            synchronized (target.getTreeLock()) {
                int hgap = getHgap(), vgap = getVgap();
                Insets insets = target.getInsets();
                int maxWidth = target.getWidth() > 0 ? target.getWidth() : Integer.MAX_VALUE;
                maxWidth -= (insets.left + insets.right + hgap * 2);
                Dimension dim = new Dimension(0, 0);
                int rowWidth = 0, rowHeight = 0;

                for (Component m : target.getComponents()) {
                    if (m.isVisible()) {
                        Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                        if (rowWidth + d.width > maxWidth) {
                            dim.width = Math.max(dim.width, rowWidth);
                            dim.height += rowHeight + vgap;
                            rowWidth = 0;
                            rowHeight = 0;
                        }
                        rowWidth += d.width + hgap;
                        rowHeight = Math.max(rowHeight, d.height);
                    }
                }

                dim.width = Math.max(dim.width, rowWidth);
                dim.height += rowHeight + insets.top + insets.bottom + vgap;
                return dim;
            }
        }
    }
}
