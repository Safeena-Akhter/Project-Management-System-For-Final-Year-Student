import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SelectScreen extends JFrame {
    private JPanel projectPanel;
    private List<Project> availableProjects = new ArrayList<>();

    private String studentRegNo;

    public SelectScreen(List<ExploreProject.Project> projects) {
        this.studentRegNo = studentRegNo;

        setTitle("Select Available Project");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        projectPanel = new JPanel();
        projectPanel.setLayout(new BoxLayout(projectPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(projectPanel);
        add(scrollPane);

        loadAvailableProjects();

        setVisible(true);
    }

    private void loadAvailableProjects() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT p.*, u.name AS supervisor_name FROM project p " +
                         "JOIN user u ON p.supervisor_id = u.user_id " +
                         "WHERE p.status = 'Available'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Project project = new Project(
                        rs.getString("title"),
                        rs.getString("supervisor_name"),
                        rs.getString("status"),
                        rs.getString("description")
                );
                availableProjects.add(project);
            }

            displayProjects();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading available projects.");
        }
    }

    private void displayProjects() {
        for (Project project : availableProjects) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createTitledBorder(project.getTitle()));
            card.setBackground(new Color(240, 248, 255));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

            JTextArea description = new JTextArea("Description: " + project.getDescription() + 
                "\nSupervisor: " + project.getSupervisor());
            description.setEditable(false);
            description.setBackground(new Color(240, 248, 255));
            description.setLineWrap(true);
            description.setWrapStyleWord(true);
            card.add(new JScrollPane(description), BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

            JTextArea justificationArea = new JTextArea(3, 50);
            justificationArea.setBorder(BorderFactory.createTitledBorder("Your Justification"));
            bottomPanel.add(justificationArea);

            JButton selectButton = new JButton("Submit Project Request");
            bottomPanel.add(selectButton);

            selectButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String justification = justificationArea.getText().trim();
                    if (justification.isEmpty()) {
                        JOptionPane.showMessageDialog(SelectScreen.this, "Please enter a justification.");
                        return;
                    }

                    insertProjectRequest(project, justification);
                }
            });

            card.add(bottomPanel, BorderLayout.SOUTH);
            projectPanel.add(card);
            projectPanel.add(Box.createVerticalStrut(10));
        }

        projectPanel.revalidate();
        projectPanel.repaint();
    }

    private void insertProjectRequest(Project project, String justification) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO project_requests (title, student_reg_no, description, student_justification, status) " +
                         "VALUES (?, ?, ?, ?, 'Pending')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, project.getTitle());
            ps.setString(2, studentRegNo);
            ps.setString(3, project.getDescription());
            ps.setString(4, justification);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Project request submitted for approval.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting request.");
        }
    }

    public static void main(String[] args) {
        //SwingUtilities.invokeLater(() -> new SelectScreen()); // Replace with real reg number
    }
}
