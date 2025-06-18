import java.io.File;
import java.util.Date;

import java.util.*;

public class MainController {

    // Singleton Instance
    private static MainController instance;

    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    // Simulated Data Storage
    private Map<String, List<String>> approvedProjects = new HashMap<>();
    private Map<String, List<String>> projectSubmissions = new HashMap<>();
    private Map<String, List<String>> submissionFeedbacks = new HashMap<>();
    private Map<String, File> submissionDocuments = new HashMap<>();

    private List<String> swpData = new ArrayList<>();
    private List<String> exploreProjectsData = new ArrayList<>();
    private Map<String, String> dashboardStats = new HashMap<>();
    private int submissionCount = 0;

    public MainController() {
        // Approved Projects by Supervisor
        approvedProjects.put("Dr. John Doe", Arrays.asList("AI Chatbot", "Health App"));

        // Submissions under each project
        projectSubmissions.put("AI Chatbot", Arrays.asList("Submission 1", "Submission 2"));
        projectSubmissions.put("Health App", Arrays.asList("Submission A", "Submission B"));

        // Feedback history
        submissionFeedbacks.put("Submission 1", new ArrayList<>(List.of("Initial feedback.")));
        submissionFeedbacks.put("Submission A", new ArrayList<>(List.of("Looks good.")));

        // Mock document paths
        submissionDocuments.put("Submission 1", new File("C:/path/to/Submission1.docx"));
        submissionDocuments.put("Submission 2", new File("C:/path/to/Submission2.docx"));
        submissionDocuments.put("Submission A", new File("C:/path/to/SubmissionA.docx"));
        submissionDocuments.put("Submission B", new File("C:/path/to/SubmissionB.docx"));

        // Extra dummy data
        swpData.add("SWP Record 1");
        swpData.add("SWP Record 2");

        exploreProjectsData.add("Explore Project Alpha");
        exploreProjectsData.add("Explore Project Beta");

        dashboardStats.put("totalProjects", "10");
        dashboardStats.put("activeSupervisors", "3");
    }

    // Dashboard Launch (Mock)
    public void showDashboard() {
        // Stub
        System.out.println("Dashboard opened.");
    }

    // === GiveFeedback Logic ===
    public List<String> getApprovedProjects(String supervisorName) {
        return approvedProjects.getOrDefault(supervisorName, new ArrayList<>());
    }

    public List<String> getProjectSubmissions(String projectName) {
        return projectSubmissions.getOrDefault(projectName, new ArrayList<>());
    }

    public List<String> getSubmissionFeedbacks(String submissionName) {
        return submissionFeedbacks.getOrDefault(submissionName, new ArrayList<>());
    }

    public File getSubmissionDocument(String submissionName) {
        return submissionDocuments.get(submissionName);
    }

    public boolean addFeedbackToSubmission(String submissionName, String feedback) {
        if (submissionName == null || feedback == null || feedback.trim().isEmpty()) return false;

        List<String> feedbacks = submissionFeedbacks.getOrDefault(submissionName, new ArrayList<>());
        if (feedbacks.size() >= 2) return false; // Max 2 feedbacks

        String timestamp = new Date().toString();
        feedbacks.add(feedback + " (Submitted on: " + timestamp + ")");
        submissionFeedbacks.put(submissionName, feedbacks);
        return true;
    }

    // === Other Modules ===
    public List<String> getSWPRecords() {
        return new ArrayList<>(swpData);
    }

    public void addSWPRecord(String record) {
        if (record != null && !record.trim().isEmpty()) {
            swpData.add(record);
        }
    }

    public List<String> getExploreProjects() {
        return new ArrayList<>(exploreProjectsData);
    }

    public void addExploreProject(String projectName) {
        if (projectName != null && !projectName.trim().isEmpty()) {
            exploreProjectsData.add(projectName);
        }
    }

    public Map<String, String> getDashboardStats() {
        return new HashMap<>(dashboardStats);
    }

    public void updateDashboardStat(String key, String value) {
        if (key != null && value != null) {
            dashboardStats.put(key, value);
        }
    }

    public boolean submitWorkProduct(String description, List<File> attachedFiles) {
        submissionCount++;
        return true;
    }

    public int getSubmissionCount() {
        return submissionCount;
    }
}
