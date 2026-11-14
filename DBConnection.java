import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {

    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/emergency_contact_db";

    private DBConnection() {
    }

    public static Connection getConnection() {
        String url = System.getenv().getOrDefault("DB_URL", DEFAULT_URL);
        String username = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");
        try {
            Connection connection = DriverManager.getConnection(
                    url, username, password);
            System.out.println("Database connected successfully!");
            return connection;
        } catch (SQLException ex) {
            System.err.println("Database connection failed: "
                    + ex.getMessage());
            return null;
        }
    }
}