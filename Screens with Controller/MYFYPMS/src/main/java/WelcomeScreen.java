import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JFrame {

    public WelcomeScreen() {
        setTitle("Final Year Project Management System - QAU");
        setSize(750, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === Top Panel ===
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245)); // Light gray for top panel

        // QAU Logo with updated absolute path and extension
        String logoPath = "C:\\Users\\AL SYED\\Desktop\\MYFYPMS\\src\\main\\java\\qau_logo.jpg";
        ImageIcon logoIcon = new ImageIcon(logoPath);
        if (logoIcon.getIconWidth() == -1) {
            System.out.println("QAU logo not found!");
        }
        Image logo = logoIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logo));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(logoLabel, BorderLayout.WEST);

        // Title and Slogan
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(new Color(245, 245, 245));

        JLabel titleLabel = new JLabel("Final Year Project Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 22));

        JLabel sloganLabel = new JLabel("Plan, Track, and Succeed — Your Project Journey Starts Here.", JLabel.CENTER);
        sloganLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));

        textPanel.add(titleLabel);
        textPanel.add(sloganLabel);
        topPanel.add(textPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // === Center Panel with Image and Welcome Message ===
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Cream background color: RGB(255, 253, 208)
        centerPanel.setBackground(new Color(255, 253, 208));

        // Project image with absolute path
        String projectImagePath = "C:\\Users\\AL SYED\\Desktop\\MYFYPMS\\src\\main\\java\\project.jpg";
        ImageIcon projectIcon = new ImageIcon(projectImagePath);
        if (projectIcon.getIconWidth() == -1) {
            System.out.println("Project image not found!");
        }
        Image scaledImage = projectIcon.getImage().getScaledInstance(240, 160, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(imageLabel, BorderLayout.CENTER);

        JLabel welcomeMessage = new JLabel("Welcome! Manage and track your final year projects with ease.", JLabel.CENTER);
        welcomeMessage.setFont(new Font("SansSerif", Font.PLAIN, 15));
        welcomeMessage.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        centerPanel.add(welcomeMessage, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // === Bottom Panel with Buttons ===
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 245, 245));  // light gray bottom panel

        JButton signInButton = new JButton("Sign In");
        JButton signUpButton = new JButton("Sign Up");

        // Button Styling
        Color green = new Color(46, 125, 50); // QAU green
        Font buttonFont = new Font("SansSerif", Font.BOLD, 14);

        styleButton(signInButton, green, buttonFont);
        styleButton(signUpButton, green, buttonFont);

        bottomPanel.add(signInButton);
        bottomPanel.add(signUpButton);

        // Add to frame
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void styleButton(JButton button, Color bgColor, Font font) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setPreferredSize(new Dimension(120, 35));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeScreen());
    }
}
