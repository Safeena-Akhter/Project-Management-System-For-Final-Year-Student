import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    // This could store user roles (e.g., from a database or mock data)
    private static String currentUserRole = "Guest"; // Default role

    // In a real application, this would come from a backend or a user management system
    private List<User> users;

    public MainController() {
        users = new ArrayList<>();
        // Add some initial users for demonstration, including an admin
        users.add(new User("Admin User", "admin@example.com", "Admin"));
        users.add(new User("Student One", "student1@example.com", "Student"));
        users.add(new User("Supervisor A", "supervisorA@example.com", "Supervisor"));
    }

    public static void setCurrentUserRole(String role) {
        currentUserRole = role;
        System.out.println("Current user role set to: " + currentUserRole); // For debugging
    }

    public static String getCurrentUserRole() {
        return currentUserRole;
    }

    // --- Actions for SigninScreen ---
    public boolean authenticateUser(String email, String password) {
        // Simple mock authentication:
        // Admin credentials
        if ("admin@example.com".equals(email) && "admin123".equals(password)) {
            setCurrentUserRole("Admin");
            return true;
        }
        // General user credentials
        if ("user@example.com".equals(email) && "password123".equals(password)) {
            setCurrentUserRole("Student"); // Or any non-admin role
            return true;
        }
        // Check against the dummy users list
        for (User user : users) {
            if (user.getEmail().equals(email) && "password".equals(password)) { // Assuming a default password for dummy users
                setCurrentUserRole(user.getRole());
                return true;
            }
        }
        setCurrentUserRole("Guest"); // Reset role if authentication fails
        return false;
    }

    // --- Actions for SignupScreen ---
    public boolean registerUser(String name, String email, String password, String role) {
        // In a real app, you'd add this to a database
        // For this example, we'll just check if email is unique and add
        boolean emailExists = users.stream().anyMatch(user -> user.getEmail().equals(email));
        if (emailExists) {
            return false; // User with this email already exists
        }
        users.add(new User(name, email, role));
        setCurrentUserRole(role); // Set the role of the newly signed-up user
        return true;
    }

    // --- Actions for ManageUsersScreen ---
    public void handleAddUser(ManageUsersScreen screen) {
        String name = screen.getNameField().getText().trim();
        String email = screen.getEmailField().getText().trim();
        String role = screen.getRoleField().getText().trim();

        if (name.isEmpty() || email.isEmpty() || role.isEmpty()) {
            screen.showError("All fields must be filled for new user.");
            return;
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            screen.showError("Invalid email format.");
            return;
        }
        if (users.stream().anyMatch(user -> user.getEmail().equals(email))) {
            screen.showError("User with this email already exists.");
            return;
        }

        User newUser = new User(name, email, role);
        users.add(newUser);
        screen.getTableModel().addRow(new Object[]{newUser.getName(), newUser.getEmail(), newUser.getRole()});
        screen.showMessage("User added: " + name);
        screen.getNameField().setText("");
        screen.getEmailField().setText("");
        screen.getRoleField().setText("");
    }

    public void handleEditUser(ManageUsersScreen screen) {
        int selectedRow = screen.getUserTable().getSelectedRow();
        if (selectedRow == -1) {
            screen.showError("Please select a user to edit.");
            return;
        }
        // Fields are already populated by the table selection listener
        screen.showMessage("Edit mode: Modify fields and click 'Update User'.");
    }

    public void handleUpdateUser(ManageUsersScreen screen) {
        int selectedRow = screen.getUserTable().getSelectedRow();
        if (selectedRow == -1) {
            screen.showError("No user selected for update.");
            return;
        }

        String oldEmail = (String) screen.getTableModel().getValueAt(selectedRow, 1);
        String newName = screen.getNameField().getText().trim();
        String newEmail = screen.getEmailField().getText().trim();
        String newRole = screen.getRoleField().getText().trim();

        if (newName.isEmpty() || newEmail.isEmpty() || newRole.isEmpty()) {
            screen.showError("All fields must be filled to update user.");
            return;
        }
        if (!newEmail.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            screen.showError("Invalid email format.");
            return;
        }

        // Check if new email conflicts with existing users (excluding the one being edited)
        boolean emailConflict = users.stream()
            .filter(user -> !user.getEmail().equals(oldEmail))
            .anyMatch(user -> user.getEmail().equals(newEmail));

        if (emailConflict) {
            screen.showError("New email already in use by another user.");
            return;
        }

        // Find the user object and update it
        User userToUpdate = users.stream()
                               .filter(user -> user.getEmail().equals(oldEmail))
                               .findFirst()
                               .orElse(null);

        if (userToUpdate != null) {
            userToUpdate.setName(newName);
            userToUpdate.setEmail(newEmail);
            userToUpdate.setRole(newRole);

            // Update the table model
            screen.getTableModel().setValueAt(newName, selectedRow, 0);
            screen.getTableModel().setValueAt(newEmail, selectedRow, 1);
            screen.getTableModel().setValueAt(newRole, selectedRow, 2);

            screen.showMessage("User updated: " + newName);
            screen.getNameField().setText("");
            screen.getEmailField().setText("");
            screen.getRoleField().setText("");
            screen.getUserTable().clearSelection();
        } else {
            screen.showError("User not found in data. Please try again.");
        }
    }

    public void handleDiscardChanges(ManageUsersScreen screen) {
        screen.getNameField().setText("");
        screen.getEmailField().setText("");
        screen.getRoleField().setText("");
        screen.getUserTable().clearSelection();
        screen.showMessage("Changes discarded.");
    }

    // Method to load users into the table (called when ManageUsersScreen is opened)
    public void loadUsersIntoTable(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // Clear existing data
        for (User user : users) {
            tableModel.addRow(new Object[]{user.getName(), user.getEmail(), user.getRole()});
        }
    }

    // User class to hold user data
    private static class User {
        private String name;
        private String email;
        private String role;

        public User(String name, String email, String role) {
            this.name = name;
            this.email = email;
            this.role = role;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}
