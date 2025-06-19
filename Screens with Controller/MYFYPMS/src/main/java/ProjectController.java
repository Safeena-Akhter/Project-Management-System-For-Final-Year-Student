import java.util.*;
import java.time.LocalDate;

public class ProjectController {

    private Set<String> registeredTitles = new HashSet<>();
    private List<Project> pendingProjects = new ArrayList<>();
    private List<Project> approvedProjects = new ArrayList<>();
    private Map<String, Feedback> feedbacks = new HashMap<>();  // key: project title + version

    // ===== Register Project =====
    public ValidationResult registerProject(String title, String description, String languages,
                                            String courses, String cgpaText) {
        // Validate title
        if (title == null || title.trim().isEmpty())
            return new ValidationResult(false, "Project Title is required.");

        title = title.trim();
        if (title.length() > 50)
            return new ValidationResult(false, "Project Title must be at most 50 characters.");

        if (title.matches(".*\\d.*"))
            return new ValidationResult(false, "Project Title must not contain digits.");

        if (registeredTitles.contains(title.toLowerCase()))
            return new ValidationResult(false, "Project Title already exists.");

        // Description optional max 100 chars
        if (description != null && description.length() > 100)
            return new ValidationResult(false, "Project Description must be at most 100 characters.");

        if (languages == null || languages.trim().isEmpty())
            return new ValidationResult(false, "Required Languages are mandatory.");

        if (courses == null || courses.trim().isEmpty())
            return new ValidationResult(false, "Prerequisite Courses are mandatory.");

        // Validate CGPA
        double cgpa;
        try {
            cgpa = Double.parseDouble(cgpaText);
            if (cgpa < 2.5 || cgpa > 4.0)
                return new ValidationResult(false, "Minimum CGPA must be between 2.5 and 4.0.");
        } catch (NumberFormatException e) {
            return new ValidationResult(false, "Minimum CGPA must be a valid number.");
        }

        // Passed validation, register project
        registeredTitles.add(title.toLowerCase());
        return new ValidationResult(true, "Project registered successfully!");
    }

    // ===== Approve Project =====
    public ValidationResult approveProject(Project project, boolean approve, String supervisorComments) {
        if (supervisorComments == null || supervisorComments.trim().isEmpty())
            return new ValidationResult(false, "Supervisor comments are required.");

        if (!pendingProjects.contains(project))
            return new ValidationResult(false, "Project not found in pending projects.");

        project.status = approve ? "Approved" : "Rejected";
        project.supervisorComments = supervisorComments.trim();

        // Move to approved list if approved
        if (approve) {
            approvedProjects.add(project);
        }
        pendingProjects.remove(project);

        return new ValidationResult(true, "Project decision submitted successfully.");
    }

    // ===== Load pending projects (sample) =====
    public void loadSampleProjects() {
        pendingProjects.add(new Project(
            "Online Voting System",
            "04072312010",
            "Build secure voting app",
            "I am passionate about elections."
        ));
        pendingProjects.add(new Project(
            "AI Chatbot",
            "04072312011",
            "Create chatbot with NLP",
            "Chatbots are my FYP interest."
        ));
    }

    public List<Project> getPendingProjects() {
        return pendingProjects;
    }

    public List<Project> getApprovedProjects() {
        return approvedProjects;
    }

    // ===== Receive Feedback =====
    public Feedback getFeedback(String projectTitle, String version) {
        return feedbacks.get(projectTitle + "_" + version);
    }

    public void addFeedback(String projectTitle, String version, Feedback feedback) {
        feedbacks.put(projectTitle + "_" + version, feedback);
    }

    // === Helper classes ===
    public static class ValidationResult {
        public boolean valid;
        public String message;

        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
    }

    public static class Project {
        public String title, studentRegNo, description, studentJustification;
        public String supervisorComments = "";
        public String status = "Pending";

        public Project(String title, String studentRegNo, String description, String justification) {
            this.title = title;
            this.studentRegNo = studentRegNo;
            this.description = description;
            this.studentJustification = justification;
        }
    }

    public static class Feedback {
        public String text;
        public LocalDate date;
        public String filePath;  // Optional

        public Feedback(String text, LocalDate date, String filePath) {
            this.text = text;
            this.date = date;
            this.filePath = filePath;
        }
    }
}

