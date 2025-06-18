public class FileAttachment {
    private String fileName;
    private String fileType;
    private long size;
    private String path; // Alternatively: private byte[] content;

    // Constructor
    public FileAttachment(String fileName, String fileType, long size, String path) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.size = size;
        this.path = path;
    }

    // Check if fileName ends with the correct fileType extension
    public boolean isValidFormat() {
        if (fileName == null || fileType == null) {
            return false;
        }
        return fileName.toLowerCase().endsWith("." + fileType.toLowerCase());
    }

    // Optionally, add getters and setters
    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public long getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }
}
