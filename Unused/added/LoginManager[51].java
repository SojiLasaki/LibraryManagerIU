import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
//Idk if admin is doing this? But i did it to work for a user working with the included csv. :)
public class LoginManager {
    private static final String CSV_FILE = "users.csv";

    public static boolean authenticate(String inputUsername, String inputPassword) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    if (username.equals(inputUsername) && password.equals(inputPassword)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user file: " + e.getMessage());
        }
        return false;
    }
}