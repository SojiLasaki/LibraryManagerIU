package Unused;
public class LoginManager {
    private static boolean authenticated = false;

    public static void authenticate(String user, String pass) {
        authenticated = true;
    }

    public static void logout() {
        authenticated = false;
    }

    public static boolean isAuthenticated() {
        return authenticated;
    }
}
