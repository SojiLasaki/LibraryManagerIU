import java.io.*;
import java.time.LocalDate; //Idk if were using real time or not but thats what i used
import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String name;
    private String username;
    private transient String password;

    private List<Book> borrowedBooks = new ArrayList<>();
    private List<Notification> notifications = new ArrayList<>();//I invcluded coz it shoudl be with use right? 

    private static final String CSV_FILE = "users.csv"; //Contains Ids and logins to be able to login
    private static final String BOOKS_FILE = "books.csv"; //Contains the books themselves, look at csv for format.

    public User(String userId, String name, String username, String password) { //Creating a user/ Should go to admin class? idk
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    // Login method
    public static User login(String inputUsername, String inputPassword) { //Breaks it down by the format its in inside the csv file
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String userId = parts[0].trim();
                    String name = parts[1].trim();
                    String username = parts[2].trim();
                    String password = parts[3].trim();
                    if (username.equals(inputUsername) && password.equals(inputPassword)) {
                        return new User(userId, name, username, password);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return null;
    }

    // Registration method
    public static boolean register(String userId, String name, String username, String password) {//Registration method since I have the user logins🤷‍♂️ Admin wouldnt be the one registering users. 
        if (usernameExists(username) || userIdExists(userId)) return false;
        try (FileWriter writer = new FileWriter(CSV_FILE, true);
             BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write(userId + "," + name + "," + username + "," + password);
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("Registration error: " + e.getMessage());
            return false;
        }
    }

    // Check if username exists
    public static boolean usernameExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[2].trim().equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
        }
        return false;
    }

    // Check if userId exists
    public static boolean userIdExists(String userId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].trim().equals(userId)) {
                    return true;
                }
            }
        } catch (IOException e) {
        }
        return false;
    }

    // Search books in the CSV file
    public List<Book> searchBooks(String query, String criteria) { //Annoying af almsot cried
        List<Book> results = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue;
                String isbn = parts[0].trim();
                String title = parts[1].trim();
                String author = parts[2].trim();
                String category = parts[3].trim();
                boolean available = Boolean.parseBoolean(parts[4].trim());
                Book book = new Book(isbn, title, author, category, available);
                switch (criteria.toLowerCase()) {
                    case "title":
                        if (title.toLowerCase().contains(query.toLowerCase())) results.add(book);
                        break;
                    case "author":
                        if (author.toLowerCase().contains(query.toLowerCase())) results.add(book);
                        break;
                    case "category":
                        if (category.toLowerCase().contains(query.toLowerCase())) results.add(book);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading books file: " + e.getMessage());
        }
        return results;
    }

    // Request book issuance and update CSV
    public boolean requestIssuance(Book requestedBook) {
        List<String> updatedLines = new ArrayList<>();
        boolean bookUpdated = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            String header = reader.readLine();
            updatedLines.add(header);
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue;
                String isbn = parts[0].trim();
                if (isbn.equals(requestedBook.getISBN()) && Boolean.parseBoolean(parts[4].trim())) {
                    borrowedBooks.add(requestedBook);
                    requestedBook.setBorrowed(true);
                    requestedBook.setBorrowDate(LocalDate.now());
                    requestedBook.setDueDate(LocalDate.now().plusWeeks(2));//Dont know how borrowing timeframe is gonna work so i did 2
                    parts[4] = "FALSE"; // Mark as not available
                    bookUpdated = true;
                }
                updatedLines.add(String.join(",", parts));
            }
        } catch (IOException e) {
            System.out.println("Error reading book file: " + e.getMessage());
            return false;
        }

        // Write updated book list back to file 
        if (bookUpdated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
                for (String updatedLine : updatedLines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error writing to book file: " + e.getMessage());
                return false;
            }
        }

        return bookUpdated;
    }

    // Get user's borrowing history
    public List<Book> getBorrowingHistory() {
        return new ArrayList<>(borrowedBooks);
    }

    // Getters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getUsername() { return username; }
}
