
import java.sql.*;
import java.security.MessageDigest;

public class DBConnection {
    private static final String DB_URL = "jdbc:sqlite:users.db";

    // Connect to SQLite
    public static Connection connect() {
        Connection conn = DBConnection.connect();
        try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("✅ Connected to database.");
        } catch (SQLException e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
        }
        return conn;
    }

    // // Create users table if it doesn't exist
    // public static void initializeDatabase() {
    //     String sql = "CREATE TABLE IF NOT EXISTS users (\n"
    //                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
    //                + " username TEXT UNIQUE NOT NULL,\n"
    //                + " password TEXT NOT NULL\n"
    //                + ");";

    //     try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
    //         stmt.execute(sql);
    //         System.out.println("📂 users table is ready.");
    //     } catch (SQLException e) {
    //         System.out.println("❌ DB init error: " + e.getMessage());
    //     }
    // }

    // Hash password using SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            System.out.println("❌ Hashing error: " + e.getMessage());
            return null;
        }
    }
}
