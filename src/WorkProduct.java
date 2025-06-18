import java.io.File;
import java.util.Date;
import java.util.List;

public class WorkProduct  {
    private String id;
    private String fileName;
    private String fileType;
    private Date uploadDate;
    private int version; // e.g., 1, 2, 3, etc.
    private User submittedBy; // role = "STUDENT"
    private Project project;
    private String description;
    private List<File> files;

    // No-arg constructor
    public WorkProduct() {
        this.uploadDate = new Date();
        this.version = 1;
    }

    // Constructor with parameters
    public WorkProduct(String id, String fileName, String fileType, User submittedBy, Project project) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.uploadDate = new Date(); // default to now
        this.version = 1; // default initial version
        this.submittedBy = submittedBy;
        this.project = project;
    }

    // Dummy validation method
    public void validateFileFormat() {
        if (!fileName.endsWith("." + fileType)) {
            throw new IllegalArgumentException("File extension does not match file type.");
        }
    }

    // Placeholder save logic
    public void save() {
        System.out.println("Saving work product: " + fileName);
        // Implement actual persistence logic here (e.g., to DB or file system)
    }

    public void incrementVersion() {
        this.version++;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        if (version <= 0) {
            throw new IllegalArgumentException("Version must be positive.");
        }
        this.version = version;
    }

    // Setters for new fields
    public void setDescription(String description) {
        this.description = description;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    // Getters for new fields
    public String getDescription() {
        return description;
    }

    public List<File> getFiles() {
        return files;
    }

    // Optionally, add getters for other fields if needed
    public String getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public User getSubmittedBy() {
        return submittedBy;
    }

    public Project getProject() {
        return project;
    }
}
