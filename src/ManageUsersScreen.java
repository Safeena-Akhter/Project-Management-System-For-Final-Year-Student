import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.awt.*;
import java.util.regex.Pattern;

public class ManageUsersScreen extends JFrame {
    private JTextField idField, nameField, emailField, roleField;
    private JButton addButton, editButton, updateButton, deleteButton;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JLabel statusLabel;
    private JComboBox<String> roleSelector;

    public ManageUsersScreen() {
        setTitle("Manage Users");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color topBottomColor = new Color(245, 245, 245);
        Color centerColor   = new Color(255, 253, 208);
        Color buttonColor   = new Color(46, 125, 50);
        Color lightPurple   = new Color(220, 220, 255);

        /* === Top Panel === */
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(topBottomColor);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Manage System Users", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 22));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        roleSelector = new JComboBox<>(new String[]{"All", "Student", "Supervisor", "Admin"});
        roleSelector.addActionListener(e -> filterTableByRole((String) roleSelector.getSelectedItem()));
        topPanel.add(roleSelector, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        /* === Center Panel === */
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(centerColor);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(centerColor);

        idField    = new JTextField();
        nameField  = new JTextField();
        emailField = new JTextField();
        roleField  = new JTextField();

        styleField(idField, lightPurple);
        styleField(nameField, lightPurple);
        styleField(emailField, lightPurple);
        styleField(roleField, lightPurple);

        formPanel.add(new JLabel("User ID:"));   formPanel.add(idField);
        formPanel.add(new JLabel("Name:"));      formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));     formPanel.add(emailField);
        formPanel.add(new JLabel("Role:"));      formPanel.add(roleField);

        centerPanel.add(formPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Role"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        userTable = new JTable(tableModel);
        userTable.setFillsViewportHeight(true);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setBackground(new Color(100, 180, 100));
        userTable.getTableHeader().setForeground(Color.WHITE);
        userTable.setRowHeight(25);
        userTable.setFont(new Font("SansSerif", Font.PLAIN, 12));

        /* convert selected row index from view → model */
        userTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) { handleEditUser(); }
        });

        sorter = new TableRowSorter<>(tableModel);
        userTable.setRowSorter(sorter);

        centerPanel.add(new JScrollPane(userTable), BorderLayout.CENTER);

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        centerPanel.add(statusLabel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        /* === Bottom Panel === */
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(topBottomColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButton    = new JButton("Add User");
        editButton   = new JButton("Edit User");
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

        /* === Button Actions === */
        addButton.addActionListener(e -> handleAddUser());
        editButton.addActionListener(e -> handleEditUser());
        updateButton.addActionListener(e -> handleUpdateUser());
        deleteButton.addActionListener(e -> handleDeleteUser());

        /* === Startup === */
        addDummyUsers();
        setVisible(true);
    }

    /* ---------- Helpers ---------- */

    private void styleField(JTextField f, Color bg) {
        f.setBackground(bg);
        f.setBorder(BorderFactory.createLineBorder(bg.darker(), 1));
        f.setPreferredSize(new Dimension(200, 30));
    }
    private void styleButton(JButton b, Color bg) {
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 14));
        b.setPreferredSize(new Dimension(150, 35));
    }

    private void filterTableByRole(String role) {
        if (role.equals("All")) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)^" + role + "$", 3));
        }
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
        if (role.equalsIgnoreCase("Student"))    return id.matches("s_\\d{11}");
        if (role.equalsIgnoreCase("Supervisor")) return id.matches("sup_\\d{6}");
        if (role.equalsIgnoreCase("Admin"))      return id.matches("a_\\d{6}");
        return false;
    }
    private boolean isUniqueId(String id, int ignoreRow) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (i == ignoreRow) continue;
            if (tableModel.getValueAt(i, 0).equals(id)) return false;
        }
        return true;
    }

    /* ---------- CRUD ---------- */

    private void handleAddUser() {
        String id   = idField.getText().trim();
        String name = nameField.getText().trim();
        String email= emailField.getText().trim();
        String role = roleField.getText().trim();

        if (name.isEmpty())                    { showError("Name cannot be empty."); }
        else if (!isValidEmail(email))         { showError("Invalid email format."); }
        else if (!isValidRole(role))           { showError("Role must be Student, Supervisor, or Admin."); }
        else if (!isValidId(id, role))         { showError("ID format/length incorrect for role."); }
        else if (!isUniqueId(id, -1))          { showError("ID already exists."); }
        else {
            tableModel.addRow(new Object[]{id, name, email, role});
            showMessage("User added successfully.");
        }
    }

    private void handleEditUser() {
        int viewRow = userTable.getSelectedRow();
        if (viewRow == -1) { showError("Select a user to edit."); return; }
        int row = userTable.convertRowIndexToModel(viewRow);

        idField.setText(tableModel.getValueAt(row, 0).toString());
        nameField.setText(tableModel.getValueAt(row, 1).toString());
        emailField.setText(tableModel.getValueAt(row, 2).toString());
        roleField.setText(tableModel.getValueAt(row, 3).toString());
        showMessage("User loaded for editing.");
    }

    private void handleUpdateUser() {
        int viewRow = userTable.getSelectedRow();
        if (viewRow == -1) { showError("Select a user to update."); return; }
        int row = userTable.convertRowIndexToModel(viewRow);

        String id   = idField.getText().trim();
        String name = nameField.getText().trim();
        String email= emailField.getText().trim();
        String role = roleField.getText().trim();

        if (name.isEmpty())                    { showError("Name cannot be empty."); }
        else if (!isValidEmail(email))         { showError("Invalid email format."); }
        else if (!isValidRole(role))           { showError("Role must be Student, Supervisor, or Admin."); }
        else if (!isValidId(id, role))         { showError("ID format/length incorrect."); }
        else if (!id.equals(tableModel.getValueAt(row,0)) && !isUniqueId(id,row)) {
                                                showError("ID already exists."); }
        else {
            tableModel.setValueAt(id,   row, 0);
            tableModel.setValueAt(name, row, 1);
            tableModel.setValueAt(email,row, 2);
            tableModel.setValueAt(role, row, 3);
            showMessage("User updated successfully.");
        }
    }

    private void handleDeleteUser() {
        int viewRow = userTable.getSelectedRow();
        if (viewRow == -1) { showError("Select a user to delete."); return; }
        int row = userTable.convertRowIndexToModel(viewRow);

        tableModel.removeRow(row);
        showMessage("User deleted successfully.");
    }

    /* ---------- Misc ---------- */

    private void addDummyUsers() {
        tableModel.addRow(new Object[]{"s_12345678901", "Ali Khan", "ali@student.com", "Student"});
        tableModel.addRow(new Object[]{"sup_123456",    "Dr. Sara", "sara@qau.edu.pk", "Supervisor"});
        tableModel.addRow(new Object[]{"a_654321",      "Admin",    "admin@qau.edu.pk","Admin"});
    }

    private void showMessage(String msg) { statusLabel.setForeground(new Color(46,125,50)); statusLabel.setText(msg); }
    private void showError  (String msg) { statusLabel.setForeground(Color.RED);             statusLabel.setText(msg); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManageUsersScreen::new);
    }
}
