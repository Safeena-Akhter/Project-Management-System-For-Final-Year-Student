import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private String name;
    private String email;
    private String password;
    private String role;
    private String userId;
    private int age;

    public User(String name, String email, String password, String role, String userId, int age) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.userId = userId;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getUserId() {
        return userId;
    }

    public int getAge() {
        return age;
    }

    // Authenticate user against database
    public static User authenticate(String email, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();

            // Make sure to match exact column names, including 'Passward'
            String sql = "SELECT Name, Email, Passward, role, UserId, age FROM users WHERE Email = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);

            rs = stmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("Passward"); // note the typo in column name
                if (dbPassword.equals(password)) {
                    String name = rs.getString("Name");
                    String role = rs.getString("role");
                    String userId = rs.getString("UserId");
                    int age = rs.getInt("age");

                    return new User(name, email, password, role, userId, age);
                } else {
                    System.out.println("Password mismatch for user: " + email);
                }
            } else {
                System.out.println("No user found with email: " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }

        return null;
    }
}
