import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.regex.Pattern;

public class ManageUsersScreen extends JFrame {
    private JTextField idField, nameField, emailField, roleField;
    private JButton addButton, editButton, updateButton, deleteButton;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public ManageUsersScreen() {
        setTitle("Manage Users");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color topBottomColor = new Color(245, 245, 245);
        Color centerColor = new Color(255, 253, 208);
        Color buttonColor = new Color(46, 125, 50);
        Color lightPurple = new Color(220, 220, 255);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(topBottomColor);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Manage System Users", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 22));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(centerColor);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(centerColor);

        idField = new JTextField();
        nameField = new JTextField();
        emailField = new JTextField();
        roleField = new JTextField();

        styleField(idField, lightPurple);
        styleField(nameField, lightPurple);
        styleField(emailField, lightPurple);
        styleField(roleField, lightPurple);

        formPanel.add(new JLabel("User ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleField);

        centerPanel.add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Role"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(tableModel);
        userTable.setFillsViewportHeight(true);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setBackground(new Color(100, 180, 100));
        userTable.getTableHeader().setForeground(Color.WHITE);
        userTable.setRowHeight(25);
        userTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        userTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    emailField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    roleField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(userTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(statusLabel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(topBottomColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButton = new JButton("Add User");
        editButton = new JButton("Edit User");
        updateButton = new JButton("Update User");
        deleteButton = new JButton("Delete User");

        styleButton(addButton, buttonColor);
        styleButton(editButton, buttonColor);
        styleButton(updateButton, buttonColor);
        styleButton(deleteButton, new Color(200, 50, 50));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> handleAddUser());
        editButton.addActionListener(e -> handleEditUser());
        updateButton.addActionListener(e -> handleUpdateUser());
        deleteButton.addActionListener(e -> handleDeleteUser());

        setVisible(true);
        addDummyUsers();
    }

    private void styleField(JTextField field, Color bgColor) {
        field.setBackground(bgColor);
        field.setBorder(BorderFactory.createLineBorder(bgColor.darker(), 1));
        field.setPreferredSize(new Dimension(200, 30));
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 35));
    }

    private void addDummyUsers() {
        tableModel.addRow(new Object[]{"s_04072312345", "Ali Khan", "ali@student.com", "Student"});
        tableModel.addRow(new Object[]{"sup_040723001", "Dr. Sara", "sara@qau.edu.pk", "Supervisor"});
        tableModel.addRow(new Object[]{"a_0001", "Admin", "admin@qau.edu.pk", "Admin"});
    }

    private boolean isValidEmail(String email) {
        return Pattern.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", email);
    }

    private boolean isValidRole(String role) {
        return role.equalsIgnoreCase("Student") ||
               role.equalsIgnoreCase("Supervisor") ||
               role.equalsIgnoreCase("Admin");
    }

    private boolean isValidId(String id, String role) {
        if (role.equalsIgnoreCase("Student")) return id.toLowerCase().startsWith("s_");
        if (role.equalsIgnoreCase("Supervisor")) return id.toLowerCase().startsWith("sup_");
        if (role.equalsIgnoreCase("Admin")) return id.toLowerCase().startsWith("a_");
        return false;
    }

    private void handleAddUser() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String role = roleField.getText().trim();

        if (name.isEmpty()) {
            showError("Name cannot be empty.");
        } else if (!isValidEmail(email)) {
            showError("Invalid email format.");
        } else if (!isValidRole(role)) {
            showError("Role must be Student, Supervisor, or Admin.");
        } else if (!isValidId(id, role)) {
            showError("ID must start with s_, sup_, or a_ according to role.");
        } else {
            tableModel.addRow(new Object[]{id, name, email, role});
            showMessage("User added successfully.");
        }
    }

    private void handleEditUser() {
        int row = userTable.getSelectedRow();
        if (row != -1) {
            idField.setText(tableModel.getValueAt(row, 0).toString());
            nameField.setText(tableModel.getValueAt(row, 1).toString());
            emailField.setText(tableModel.getValueAt(row, 2).toString());
            roleField.setText(tableModel.getValueAt(row, 3).toString());
            showMessage("User loaded for editing.");
        } else {
            showError("Please select a user to edit.");
        }
    }

    private void handleUpdateUser() {
        int row = userTable.getSelectedRow();
        if (row != -1) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String role = roleField.getText().trim();

            if (name.isEmpty()) {
                showError("Name cannot be empty.");
            } else if (!isValidEmail(email)) {
                showError("Invalid email format.");
            } else if (!isValidRole(role)) {
                showError("Role must be Student, Supervisor, or Admin.");
            } else if (!isValidId(id, role)) {
                showError("ID must start with s_, sup_, or a_ according to role.");
            } else {
                tableModel.setValueAt(id, row, 0);
                tableModel.setValueAt(name, row, 1);
                tableModel.setValueAt(email, row, 2);
                tableModel.setValueAt(role, row, 3);
                showMessage("User updated successfully.");
            }
        } else {
            showError("Please select a user to update.");
        }
    }

    private void handleDeleteUser() {
        int row = userTable.getSelectedRow();
        if (row != -1) {
            tableModel.removeRow(row);
            showMessage("User deleted successfully.");
        } else {
            showError("Please select a user to delete.");
        }
    }

    public void showMessage(String msg) {
        statusLabel.setText(msg);
        statusLabel.setForeground(new Color(46, 125, 50));
    }

    public void showError(String msg) {
        statusLabel.setText(msg);
        statusLabel.setForeground(Color.RED);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManageUsersScreen::new);
    }
}
