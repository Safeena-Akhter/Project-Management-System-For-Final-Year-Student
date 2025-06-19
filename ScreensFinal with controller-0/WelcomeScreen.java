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

        // QAU Logo - Placeholder if path is invalid
        String logoPath = "C:\\Users\\AL SYED\\Desktop\\MYFYPMS\\src\\main\\java\\qau_logo.jpg";
        ImageIcon logoIcon = new ImageIcon(logoPath);
        Image logo = null;
        if (logoIcon.getIconWidth() == -1) {
            System.out.println("QAU logo not found! Using placeholder.");
            // Create a placeholder image or use a default one if the path is invalid
            logo = new ImageIcon(
                "https://placehold.co/70x70/cccccc/333333?text=QAU"
            ).getImage();
        } else {
            logo = logoIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        }
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
        centerPanel.setBackground(new Color(255, 253, 208)); // Cream color

        String projectImagePath = "C:\\Users\\AL SYED\\Desktop\\MYFYPMS\\src\\main\\java\\project.jpg";
        ImageIcon projectIcon = new ImageIcon(projectImagePath);
        Image scaledImage = null;
        if (projectIcon.getIconWidth() == -1) {
            System.out.println("Project image not found! Using placeholder.");
            scaledImage = new ImageIcon(
                "https://placehold.co/240x160/cccccc/333333?text=Project+Image"
            ).getImage();
        } else {
            scaledImage = projectIcon.getImage().getScaledInstance(240, 160, Image.SCALE_SMOOTH);
        }

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
        bottomPanel.setBackground(new Color(245, 245, 245));  // light gray

        JButton signInButton = new JButton("Sign In");
        JButton signUpButton = new JButton("Sign Up");

        Color green = new Color(46, 125, 50); // QAU green
        Font buttonFont = new Font("SansSerif", Font.BOLD, 14);

        styleButton(signInButton, green, buttonFont);
        styleButton(signUpButton, green, buttonFont);

        bottomPanel.add(signInButton);
        bottomPanel.add(signUpButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // === Button Actions ===
        signInButton.addActionListener(e -> {
            dispose();
            // Pass the controller instance to the new screen
            new SigninScreen(new MainController());
        });

        signUpButton.addActionListener(e -> {
            dispose();
            // Pass the controller instance to the new screen
            new SignupScreen(new MainController());
        });

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
        // Ensure that the Swing GUI is created and updated on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new WelcomeScreen());
    }
}
