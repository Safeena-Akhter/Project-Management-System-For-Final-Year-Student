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
        topPanel.setBackground(new Color(245, 245, 245));

        // QAU Logo (Loaded from resources)
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/qau_logo.jpg"));
        Image logo = logoIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logo));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(logoLabel, BorderLayout.WEST);

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

        // === Center Panel ===
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(255, 253, 208));

        // Project Image (Loaded from resources)
        ImageIcon projectIcon = new ImageIcon(getClass().getResource("/project.jpg"));
        Image scaledImage = projectIcon.getImage().getScaledInstance(240, 160, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        centerPanel.add(imageLabel, BorderLayout.CENTER);

        JLabel welcomeMessage = new JLabel("Welcome! Manage and track your final year projects with ease.", JLabel.CENTER);
        welcomeMessage.setFont(new Font("SansSerif", Font.PLAIN, 15));
        welcomeMessage.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        centerPanel.add(welcomeMessage, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // === Bottom Panel ===
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 245, 245));
        JButton signInButton = new JButton("Sign In");
        JButton signUpButton = new JButton("Sign Up");
        Color green = new Color(46, 125, 50);
        Font buttonFont = new Font("SansSerif", Font.BOLD, 14);
        styleButton(signInButton, green, buttonFont);
        styleButton(signUpButton, green, buttonFont);
        bottomPanel.add(signInButton);
        bottomPanel.add(signUpButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // === Actions ===
        signInButton.addActionListener(e -> {
            dispose();
            new SigninScreen(); // Add your SigninScreen class separately
        });

        signUpButton.addActionListener(e -> {
            dispose();
            new SignupScreen(); // Uncomment and implement when ready
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
        SwingUtilities.invokeLater(() -> new WelcomeScreen());
    }
}
