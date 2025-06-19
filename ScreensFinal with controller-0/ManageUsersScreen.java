import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class ManageUsersScreen extends JFrame {
    private JTextField nameField, emailField, roleField;
    private JButton addButton, editButton, updateButton, discardButton;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private MainController controller;
    private JLabel statusLabel; // Added for messages

    public ManageUsersScreen(MainController controller) {
        this.controller = controller;
        setTitle("Manage Users");
        setSize(750, 520); // Adjusted size to match WelcomeScreen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Theme colors - inspired by WelcomeScreen for consistency
        Color topBottomColor = new Color(245, 245, 245);   // Light gray
        Color centerColor = new Color(255, 253, 208);      // Cream
        Color buttonColor = new Color(46, 125, 50);        // QAU green
        Color lightPurple = new Color(220, 220, 255);      // Light purple for text field backgrounds

        // --- Top Panel (Mimicking WelcomeScreen's Title Area) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(topBottomColor);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Manage System Users", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 22));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // --- Center Panel (Main Content Area) ---
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(centerColor);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form Panel with light purple boxes
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(centerColor);

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        nameField.setBackground(lightPurple);
        nameField.setBorder(BorderFactory.createLineBorder(lightPurple.darker(), 1)); // Subtle border
        nameField.setPreferredSize(new Dimension(200, 30)); // Set preferred size for text fields

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        emailField.setBackground(lightPurple);
        emailField.setBorder(BorderFactory.createLineBorder(lightPurple.darker(), 1));
        emailField.setPreferredSize(new Dimension(200, 30));

        JLabel roleLabel = new JLabel("Role:");
        roleField = new JTextField();
        roleField.setBackground(lightPurple);
        roleField.setBorder(BorderFactory.createLineBorder(lightPurple.darker(), 1));
        roleField.setPreferredSize(new Dimension(200, 30));


        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(roleLabel);
        formPanel.add(roleField);

        centerPanel.add(formPanel, BorderLayout.NORTH);

        // Table Model and Table
        tableModel = new DefaultTableModel(new String[]{"Name", "Email", "Role"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make cells non-editable directly in the table
                return false;
            }
        };
        userTable = new JTable(tableModel);
        userTable.setFillsViewportHeight(true);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only one row can be selected
        userTable.getTableHeader().setBackground(new Color(100, 180, 100)); // Greenish header
        userTable.getTableHeader().setForeground(Color.WHITE);
        userTable.setRowHeight(25);
        userTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        userTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    nameField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    emailField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    roleField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(userTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Status Label
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(statusLabel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);


        // --- Bottom Panel (Buttons) ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(topBottomColor); // Light gray
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButton = new JButton("Add User");
        editButton = new JButton("Edit User");
        updateButton = new JButton("Update User");
        discardButton = new JButton("Discard");

        styleButton(addButton, buttonColor);
        styleButton(editButton, buttonColor);
        styleButton(updateButton, buttonColor);
        styleButton(discardButton, new Color(200, 50, 50)); // Red for discard

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(discardButton);

        addButton.addActionListener(e -> controller.handleAddUser(this));
        editButton.addActionListener(e -> controller.handleEditUser(this));
        updateButton.addActionListener(e -> controller.handleUpdateUser(this));
        discardButton.addActionListener(e -> controller.handleDiscardChanges(this));


        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);

        // Populate with some dummy data initially
        addDummyUsers();
    }

    // Helper method to style buttons
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 35)); // Consistent button size
    }

    // Helper to add dummy data
    private void addDummyUsers() {
        tableModel.addRow(new Object[]{"John Doe", "john.doe@example.com", "Student"});
        tableModel.addRow(new Object[]{"Jane Smith", "jane.smith@example.com", "Supervisor"});
        tableModel.addRow(new Object[]{"Admin User", "admin@example.com", "Admin"});
    }

    public JTextField getNameField() { return nameField; }
    public JTextField getEmailField() { return emailField; }
    public JTextField getRoleField() { return roleField; }
    public JButton getAddButton() { return addButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getUpdateButton() { return updateButton; }
    public JButton getDiscardButton() { return discardButton; }
    public JTable getUserTable() { return userTable; }
    public DefaultTableModel getTableModel() { return tableModel; }

    public void showMessage(String msg) {
        statusLabel.setText(msg);
        statusLabel.setForeground(new Color(46, 125, 50)); // QAU green
    }

    public void showError(String msg) {
        statusLabel.setText(msg);
        statusLabel.setForeground(Color.RED);
    }
}
