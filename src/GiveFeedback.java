import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.util.List;
public class GiveFeedback extends JFrame {

    private String supervisorName = "Dr. John Doe";

    private JComboBox<String> projectComboBox;
    private DefaultListModel<String> submissionListModel;
    private JList<String> submissionList;
    private DefaultListModel<String> previousFeedbacksModel;
    private JList<String> previousFeedbacksList;
    private JTextArea feedbackArea;
    private JLabel attachedFilesLabel;
    private JButton attachFileButton, submitFeedbackButton;

    private List<File> attachedFiles = new ArrayList<>();
    private final MainController controller;

    private final Color qauGreen = new Color(46, 125, 50);
    private final Color cream = new Color(255, 253, 208);
    private final Color lightGray = new Color(246, 246, 247);
    private final Color black = Color.BLACK;

    public GiveFeedback(MainController controller) {
        this.controller = controller;

        setTitle("Supervisor Feedback Interface");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(cream);

        createUI();
        loadApprovedProjects();
    }

    private void createUI() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(cream);
        JLabel supervisorLabel = new JLabel("Supervisor: " + supervisorName, SwingConstants.RIGHT);
        supervisorLabel.setForeground(qauGreen);
        supervisorLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        top.add(supervisorLabel, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2));
        center.setBackground(cream);

        // LEFT: Project & Submissions
        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(lightGray);

        projectComboBox = new JComboBox<>();
        projectComboBox.addActionListener(e -> loadSubmissions());
        left.add(projectComboBox, BorderLayout.NORTH);

        submissionListModel = new DefaultListModel<>();
        submissionList = new JList<>(submissionListModel);
        submissionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        submissionList.addListSelectionListener(e -> loadFeedbacks());
        submissionList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) openSubmission();
            }
        });

        left.add(new JScrollPane(submissionList), BorderLayout.CENTER);
        center.add(left);

        // RIGHT: Feedback Panel
        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(lightGray);

        previousFeedbacksModel = new DefaultListModel<>();
        previousFeedbacksList = new JList<>(previousFeedbacksModel);
        right.add(new JScrollPane(previousFeedbacksList), BorderLayout.NORTH);

        feedbackArea = new JTextArea("Enter feedback here...");
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setForeground(Color.GRAY);
        feedbackArea.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (feedbackArea.getText().equals("Enter feedback here...")) {
                    feedbackArea.setText("");
                    feedbackArea.setForeground(black);
                }
            }

            public void focusLost(FocusEvent e) {
                if (feedbackArea.getText().trim().isEmpty()) {
                    feedbackArea.setText("Enter feedback here...");
                    feedbackArea.setForeground(Color.GRAY);
                }
            }
        });

        right.add(new JScrollPane(feedbackArea), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(cream);

        attachFileButton = new JButton("Attach File");
        attachFileButton.setBackground(qauGreen);
        attachFileButton.setForeground(Color.WHITE);
        attachFileButton.addActionListener(e -> attachFile());
        bottom.add(attachFileButton, BorderLayout.WEST);

        attachedFilesLabel = new JLabel("Attached Files: 0");
        bottom.add(attachedFilesLabel, BorderLayout.CENTER);

        submitFeedbackButton = new JButton("Submit Feedback");
        submitFeedbackButton.setBackground(qauGreen);
        submitFeedbackButton.setForeground(Color.WHITE);
        submitFeedbackButton.addActionListener(e -> submitFeedback());
        bottom.add(submitFeedbackButton, BorderLayout.EAST);

        right.add(bottom, BorderLayout.SOUTH);
        center.add(right);
        add(center, BorderLayout.CENTER);
    }

    private void loadApprovedProjects() {
        projectComboBox.removeAllItems();
        List<String> projects = controller.getApprovedProjects(supervisorName);
        for (String p : projects) {
            projectComboBox.addItem(p);
        }
    }

    private void loadSubmissions() {
        String project = (String) projectComboBox.getSelectedItem();
        if (project == null) return;

        submissionListModel.clear();
        previousFeedbacksModel.clear();
        List<String> submissions = controller.getProjectSubmissions(project);
        for (String sub : submissions) {
            submissionListModel.addElement(sub);
        }
        resetFeedbackArea();
    }

    private void loadFeedbacks() {
        String submission = submissionList.getSelectedValue();
        if (submission == null) return;

        previousFeedbacksModel.clear();
        List<String> feedbacks = controller.getSubmissionFeedbacks(submission);
        for (String fb : feedbacks) {
            previousFeedbacksModel.addElement(fb);
        }
        resetFeedbackArea();
    }

    private void openSubmission() {
        String submission = submissionList.getSelectedValue();
        if (submission == null) return;

        File doc = controller.getSubmissionDocument(submission);
        if (doc != null && doc.exists()) {
            try {
                Desktop.getDesktop().open(doc);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cannot open document.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Document not found.");
        }
    }

    private void resetFeedbackArea() {
        feedbackArea.setText("Enter feedback here...");
        feedbackArea.setForeground(Color.GRAY);
        attachedFiles.clear();
        attachedFilesLabel.setText("Attached Files: 0");
    }

    private void attachFile() {
        if (attachedFiles.size() >= 3) {
            JOptionPane.showMessageDialog(this, "Max 3 attachments.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Allowed files", "pdf", "docx", "txt", "jpg", "png"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            attachedFiles.add(chooser.getSelectedFile());
            attachedFilesLabel.setText("Attached Files: " + attachedFiles.size());
        }
    }

    private void submitFeedback() {
        String submission = submissionList.getSelectedValue();
        String feedback = feedbackArea.getText().trim();

        if (submission == null) {
            JOptionPane.showMessageDialog(this, "Select a submission.");
            return;
        }
        if (feedback.isEmpty() || feedback.equals("Enter feedback here...")) {
            JOptionPane.showMessageDialog(this, "Feedback cannot be empty.");
            return;
        }

        boolean success = controller.addFeedbackToSubmission(submission, feedback);
        if (!success) {
            JOptionPane.showMessageDialog(this, "Max feedbacks reached or invalid input.");
            return;
        }

        JOptionPane.showMessageDialog(this, "Feedback submitted.");
        loadFeedbacks();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GiveFeedback(MainController.getInstance()).setVisible(true));
    }
}
