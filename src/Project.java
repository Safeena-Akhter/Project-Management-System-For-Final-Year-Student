import java.sql.*;

public class Project {
    private String title, supervisor, status, description;

    public Project(String title, String supervisor, String status, String description) {
        this.title = title;
        this.supervisor = supervisor;
        this.status = status;
        this.description = description;
    }

    public static Project getProjectByStudentId(String studentId) {
        String projectId = null;

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sessionQuery = "SELECT project_id FROM session WHERE user_id = ?";
            PreparedStatement sessionPs = conn.prepareStatement(sessionQuery);
            sessionPs.setString(1, studentId);
            ResultSet sessionRs = sessionPs.executeQuery();
            if (sessionRs.next()) {
                projectId = sessionRs.getString("project_id");
            } else {
                return null;
            }

            String projectQuery = "SELECT * FROM project WHERE project_id = ?";
            PreparedStatement projectPs = conn.prepareStatement(projectQuery);
            projectPs.setString(1, projectId);
            ResultSet projectRs = projectPs.executeQuery();

            if (projectRs.next()) {
                String supervisorId = projectRs.getString("supervisor_id");
                String title = projectRs.getString("title");
                String status = projectRs.getString("status");
                String description = projectRs.getString("description");

                // Fetch supervisor name
                String supervisorName = "Unknown";
                PreparedStatement supPs = conn.prepareStatement("SELECT name FROM user WHERE user_id = ?");
                supPs.setString(1, supervisorId);
                ResultSet supRs = supPs.executeQuery();
                if (supRs.next()) {
                    supervisorName = supRs.getString("name");
                }

                return new Project(title, supervisorName, status, description);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTitle() { return title; }
    public String getSupervisor() { return supervisor; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
}