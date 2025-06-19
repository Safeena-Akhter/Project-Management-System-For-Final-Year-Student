import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ApproveProjectScreen extends JFrame {

    private JTabbedPane tabbedPane;
    private JPanel pendingPanel, approvedPanel;
    private java.util.List<ProjectRequest> pendingProjects = new ArrayList<>();
    private java.util.List<ProjectRequest> approvedProjects = new ArrayList<>();

    public ApproveProjectScreen() {
        setTitle("Approve Projects");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Sample data
        loadSampleProjects();

        tabbedPane = new JTabbedPane();

        pendingPanel = new JPanel();
        pendingPanel.setLayout(new BoxLayout(pendingPanel, BoxLayout.Y_AXIS));
        populatePendingProjects();

        approvedPanel = new JPanel();
        approvedPanel.setLayout(new BoxLayout(approvedPanel, BoxLayout.Y_AXIS));
        populateApprovedProjects();

        JScrollPane pendingScroll = new JScrollPane(pendingPanel);
        JScrollPane approvedScroll = new JScrollPane(approvedPanel);

        tabbedPane.addTab("Pending Projects", pendingScroll);
        tabbedPane.addTab("Approved Projects", approvedScroll);

        add(tabbedPane);
        setVisible(true);
    }

    private void loadSampleProjects() {
        pendingProjects.add(new ProjectRequest(
            "Online Voting System",
            "04072312010",
            "Build secure voting app",
           "I am passionate about elections."
        ));
        pendingProjects.add(new ProjectRequest(
            "AI Chatbot",
            "04072312011",
            "Create chatbot with NLP",
          
            "Chatbots are my FYP interest."
        ));
    }

    private void populatePendingProjects() {
        pendingPanel.removeAll();

        for (ProjectRequest request : new ArrayList<>(pendingProjects)) { // copy to avoid concurrent mod
            JPanel card = new JPanel(new BorderLayout());
            card.setBorder(BorderFactory.createTitledBorder(request.title));
            card.setBackground(new Color(255, 253, 208));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

            JTextArea info = new JTextArea(
               
                "Student Justification: " + request.studentJustification
            );
            info.setEditable(false);
            info.setBackground(new Color(255, 253, 208));
            info.setLineWrap(true);
            info.setWrapStyleWord(true);

            card.add(new JScrollPane(info), BorderLayout.CENTER);

            JPanel decisionPanel = new JPanel(new GridBagLayout());
            decisionPanel.setBackground(new Color(255, 253, 208));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5,5,5,5);
            gbc.anchor = GridBagConstraints.WEST;

            JRadioButton approveBtn = new JRadioButton("Approve");
            JRadioButton rejectBtn = new JRadioButton("Reject");
            ButtonGroup group = new ButtonGroup();
            group.add(approveBtn);
            group.add(rejectBtn);

            gbc.gridx = 0;
            gbc.gridy = 0;
            decisionPanel.add(approveBtn, gbc);
            gbc.gridx = 1;
            decisionPanel.add(rejectBtn, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            decisionPanel.add(new JLabel("Supervisor Comments:"), gbc);

            JTextArea supervisorComments = new JTextArea(3, 30);
            JScrollPane scrollPane = new JScrollPane(supervisorComments);
            gbc.gridy = 2;
            decisionPanel.add(scrollPane, gbc);

            JButton submitBtn = new JButton("Submit Decision");
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.CENTER;
            decisionPanel.add(submitBtn, gbc);

            submitBtn.addActionListener(e -> {
                if (!approveBtn.isSelected() && !rejectBtn.isSelected()) {
                    JOptionPane.showMessageDialog(this, "Please select Approve or Reject.");
                    return;
                }
                String comments = supervisorComments.getText().trim();
                if (comments.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Supervisor comments are required.");
                    return;
                }
                request.status = approveBtn.isSelected() ? "Approved" : "Rejected";
                request.supervisorComments = comments;

                // Move project to approved list
                approvedProjects.add(request);
                pendingProjects.remove(request);

                populatePendingProjects();
                populateApprovedProjects();
                revalidate();
                repaint();
            });

            card.add(decisionPanel, BorderLayout.SOUTH);

            pendingPanel.add(card);
            pendingPanel.add(Box.createVerticalStrut(10));
        }

        pendingPanel.revalidate();
        pendingPanel.repaint();
    }

    private void populateApprovedProjects() {
        approvedPanel.removeAll();

        for (ProjectRequest request : approvedProjects) {
            JPanel card = new JPanel(new GridLayout(0, 1));
            card.setBorder(BorderFactory.createTitledBorder(request.title));
            card.setBackground(new Color(220, 255, 220));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

            card.add(new JLabel("Student Reg No: " + request.studentRegNo));
            card.add(new JLabel("Status: " + request.status));
            card.add(new JLabel("Supervisor Comments: " + request.supervisorComments));

            approvedPanel.add(card);
            approvedPanel.add(Box.createVerticalStrut(10));
        }

        approvedPanel.revalidate();
        approvedPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ApproveProjectScreen::new);
    }

    // === Helper Class ===
    static class ProjectRequest {
        String title, studentRegNo, description, studentJustification;
       
        String supervisorComments = "";
        String status = "Pending";

        public ProjectRequest(String title, String studentRegNo, String description,
                               String justification) {
            this.title = title;
            this.studentRegNo = studentRegNo;
            this.description = description;
            this.studentJustification = justification;
        }
    }
}
