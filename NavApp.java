import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public class NavApp extends JFrame {
    JPanel mainPanel;
    CardLayout cardLayout;

    public NavApp() {
        setTitle("Toosi-e Slide Library Management App");
        setSize(2000, 1800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Navigation
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton libraryBtn = new JButton("Library");
        JButton usersBtn = new JButton("Users");
        JButton transactionsBtn = new JButton("Transactions");
        JButton logoutBtn = new JButton("Logout");
        navBar.add(libraryBtn);
        navBar.add(usersBtn);
        navBar.add(transactionsBtn);
        navBar.add(logoutBtn);
        add(navBar, BorderLayout.NORTH);

        // Main Panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(getLibraryPage(), "library");
        mainPanel.add(getUsersPage(), "users");
        mainPanel.add(getTransactionsPage(), "transactions");
        add(mainPanel, BorderLayout.CENTER);

        // Navigation actions
        libraryBtn.addActionListener(e -> cardLayout.show(mainPanel, "library"));
        usersBtn.addActionListener(e -> cardLayout.show(mainPanel, "users"));
        transactionsBtn.addActionListener(e -> cardLayout.show(mainPanel, "transactions"));
        logoutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logout successful!");
            dispose();
            new LoginApp();
        });

        setVisible(true);
    }

    private JPanel getLibraryPage() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ISBN", "Title", "Author", "Genre", "Action"}, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(30);
        table.getColumn("Action").setCellRenderer(new ActionCellRenderer());
        table.getColumn("Action").setCellEditor(new ActionCellEditor(table, model));
        panel.add(scrollPane, BorderLayout.CENTER);

        loadCSV("assets/csv/books.csv", model, 4);

        JButton addBookBtn = new JButton("Add Book");
        addBookBtn.addActionListener(e -> openAddBookDialog(model));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(addBookBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        return panel;
    }

    private void loadCSV(String filePath, DefaultTableModel model, int fieldCount) {
        File file = new File(filePath);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, file.getName() + " not found!");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= fieldCount) {
                    Object[] rowData = new Object[fieldCount + 1];
                    System.arraycopy(data, 0, rowData, 0, fieldCount);
                    rowData[fieldCount] = "View | Edit | Delete";
                    model.addRow(rowData);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading CSV.");
        }
    }

    private void openAddBookDialog(DefaultTableModel model) {
        JTextField isbn = new JTextField(15);
        JTextField title = new JTextField(15);
        JTextField author = new JTextField(15);
        JTextField genre = new JTextField(15);

        JPanel panel = new JPanel();
        panel.add(new JLabel("ISBN:")); panel.add(isbn);
        panel.add(new JLabel("Title:")); panel.add(title);
        panel.add(new JLabel("Author:")); panel.add(author);
        panel.add(new JLabel("Genre:")); panel.add(genre);

        if (JOptionPane.showConfirmDialog(this, panel, "Add Book", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            if (!isbn.getText().isEmpty() && !title.getText().isEmpty() && !author.getText().isEmpty() && !genre.getText().isEmpty()) {
                model.addRow(new Object[]{isbn.getText(), title.getText(), author.getText(), genre.getText(), "View | Edit | Delete"});
                saveToCSV("assets/csv/books.csv", new String[]{isbn.getText(), title.getText(), author.getText(), genre.getText()});
            } else {
                JOptionPane.showMessageDialog(this, "All fields must be filled.");
            }
        }
    }

    private JPanel getUsersPage() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new Object[]{"First Name", "Last Name", "Phone", "Email", "Address", "Action"}, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getColumn("Action").setCellRenderer(new ActionCellRenderer());
        table.getColumn("Action").setCellEditor(new ActionCellEditor(table, model));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        loadCSV("assets/csv/users.csv", model, 5);

        JButton addUserBtn = new JButton("Create User");
        addUserBtn.addActionListener(e -> openCreateUserDialog(model));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(addUserBtn);
        panel.add(topPanel, BorderLayout.NORTH);

        return panel;
    }

    private void openCreateUserDialog(DefaultTableModel model) {
        JTextField[] fields = {
            new JTextField(15), new JTextField(15),
            new JTextField(15), new JTextField(15),
            new JTextField(15)
        };
        String[] labels = {"First Name", "Last Name", "Phone", "Email", "Address"};

        JPanel panel = new JPanel();
        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i] + ":"));
            panel.add(fields[i]);
        }

        if (JOptionPane.showConfirmDialog(this, panel, "Create User", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            boolean allFilled = true;
            for (JTextField field : fields) {
                if (field.getText().isEmpty()) {
                    allFilled = false;
                    break;
                }
            }
            if (allFilled) {
                String[] data = new String[fields.length];
                for (int i = 0; i < fields.length; i++) data[i] = fields[i].getText();
                model.addRow(new Object[]{data[0], data[1], data[2], data[3], data[4], "View | Edit | Delete"});
                saveToCSV("assets/csv/users.csv", data);
            } else {
                JOptionPane.showMessageDialog(this, "All fields must be filled.");
            }
        }
    }

    // Tina Oyatobo //
    public class Transaction {
        private String bookTitle;
        private String userFullName;
        private String dateTime;
        private String status; // "Rented" or "Returned"
    
        public Transaction(String bookTitle, String userFullName, String dateTime, String status) {
            this.bookTitle = bookTitle;
            this.userFullName = userFullName;
            this.dateTime = dateTime;
            this.status = status;
        }
    
        public String[] toCSVRow() {
            return new String[]{bookTitle, userFullName, dateTime, status};
        }
    
        // Getters
        public String getBookTitle() { return bookTitle; }
        public String getUserFullName() { return userFullName; }
        public String getDateTime() { return dateTime; }
        public String getStatus() { return status; }
    
        // Setters
        public void setStatus(String status) { this.status = status; }
    }
    
    private JPanel getTransactionsPage() {
        JPanel panel = new JPanel(new BorderLayout());
    
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Book", "User", "DateTime", "Status"}, 0);
        JTable table = new JTable(model);
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        // Top navigation: All, Rented, Returned, New Transaction
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton allBtn = new JButton("All");
        JButton rentedBtn = new JButton("Rented");
        JButton returnedBtn = new JButton("Returned");
        JButton newTransBtn = new JButton("New Transaction");
        JButton markReturnedButton = new JButton("Mark as Returned");
        // markReturnedButton.setVisible(false); // Initially hidden

        // Add all buttons to navPanel
        navPanel.add(allBtn);
        navPanel.add(rentedBtn);
        navPanel.add(returnedBtn);
        navPanel.add(newTransBtn);
        navPanel.add(markReturnedButton); // Add it here, not to newPanel

        // Add navPanel to the top of the main panel
        panel.add(navPanel, BorderLayout.NORTH);

        // Load all transactions by default
        loadTransactions(model, "All");
    
        allBtn.addActionListener(e -> loadTransactions(model, "All"));
        rentedBtn.addActionListener(e -> loadTransactions(model, "Rented"));
        returnedBtn.addActionListener(e -> loadTransactions(model, "Returned"));
    
        newTransBtn.addActionListener(e -> openNewTransactionDialog(model));
    
        return panel;
    }
    
    private void loadTransactions(DefaultTableModel model, String filter) {
        model.setRowCount(0); // Clear table
        File file = new File("assets/csv/transactions.csv");
        if (!file.exists()) return;
    
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4 && (filter.equals("All") || data[3].equals(filter))) {
                    if (filter.equals("All")) {
                        model.addRow(new Object[]{data[0], data[1], data[2], data[3], "Return"});
                    } else {
                        model.addRow(data);
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading transactions.");
        }
    }
    
    // Method to get the list of rented books from the transactions CSV
    private Set<String> getRentedBooks() {
        Set<String> rentedBooks = new HashSet<>();
        List<String> transactions = readCSV("assets/csv/transactions.csv");

        // Add a conditional column for "Return" button
        DefaultTableModel model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                // Only the "Return" column is editable (if it exists)
                return getColumnName(column).equals("Return");
            }
        };

        model.setColumnIdentifiers(new Object[]{"Book", "User", "DateTime", "Status", "Return"});

        JTable table = new JTable(model);
        table.setRowHeight(25);
        // JScrollPane scrollPane = new JScrollPane(table);
        // JPanel panel.add(scrollPane, BorderLayout.CENTER);

        // Attach the button editor only to "All" tab
        table.getColumn("Return").setCellRenderer(new ButtonRenderer());
        table.getColumn("Return").setCellEditor(new ButtonEditor(new JCheckBox(), model));

        for (String transaction : transactions) {
            String[] columns = transaction.split(",");
            if (columns.length >= 4 && "Rented".equals(columns[3])) {
                rentedBooks.add(columns[0]); 
            }
        }
        return rentedBooks;
    }
    private void openNewTransactionDialog(DefaultTableModel model) {
        // Read books and users from CSV files
        List<String> bookLines = readCSV("assets/csv/books.csv");
        List<String> userLines = readCSV("assets/csv/users.csv");
    
        // Extract book titles
        List<String> bookTitles = new ArrayList<>();
        for (String bookLine : bookLines) {
            String[] bookData = bookLine.split(",");
            if (bookData.length > 1) {
                bookTitles.add(bookData[1]); // Assuming title is the second column
            }
        }
    
        // Extract user full names
        List<String> userNames = new ArrayList<>();
        for (String userLine : userLines) {
            String[] userData = userLine.split(",");
            if (userData.length > 2) {
                userNames.add(userData[0] + " " + userData[1]); // first_name + last_name
            }
        }
    
        // Get list of rented books
        Set<String> rentedBooks = getRentedBooks();
    
        // Filter out rented books from the bookTitles list
        bookTitles.removeIf(rentedBooks::contains);
    
        // Create combo boxes for book and user
        JComboBox<String> bookBox = new JComboBox<>(bookTitles.toArray(new String[0]));
        JComboBox<String> userBox = new JComboBox<>(userNames.toArray(new String[0]));
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Rented", "Returned"});
    
        // Build the form panel
        JPanel panel = new JPanel();
        panel.add(new JLabel("Book Title:")); panel.add(bookBox);
        panel.add(new JLabel("User Name:")); panel.add(userBox);
        panel.add(new JLabel("Status:")); panel.add(statusBox);
        panel.add(new JLabel("Action:"));

        // Work on view, edit and delete transaction
        // Show dialog and process result
        if (JOptionPane.showConfirmDialog(this, panel, "New Transaction", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            String book = (String) bookBox.getSelectedItem();
            String user = (String) userBox.getSelectedItem();
            String dateTime = java.time.LocalDateTime.now().toString();
            String status = (String) statusBox.getSelectedItem();
    
            if (book != null && user != null) {
                Transaction t = new Transaction(book, user, dateTime, status);
                model.addRow(t.toCSVRow());
                saveToCSV("assets/csv/transactions.csv", t.toCSVRow());
            } else {
                JOptionPane.showMessageDialog(this, "All fields are required.");
            }
        }
    }
    
// IVAN //
    // Method to read CSV file and return a list of strings (book titles or user names)
    private List<String> readCSV(String filePath) {
        List<String> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    // Method to save transaction data to CSV
    private void saveToCSV(String filePath, String[] row) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            String line = String.join(",", row);
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Main 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginApp::new);
    }
} 

// Action listenners
class ActionCellRenderer extends JPanel implements TableCellRenderer {
    public ActionCellRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        add(new JButton("View"));
        add(new JButton("Edit"));
        add(new JButton("Delete"));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private DefaultTableModel model;

    public ActionCellEditor(JTable table, DefaultTableModel model) {
        this.model = model;
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

        JButton viewBtn = new JButton("View");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        viewBtn.addActionListener(e -> showDialog(table, false));
        editBtn.addActionListener(e -> showDialog(table, true));
        deleteBtn.addActionListener(e -> deleteRow(table));

        panel.add(viewBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    private void showDialog(JTable table, boolean editable) {
        int row = table.getEditingRow();
        if (row < 0) return;

        int colCount = table.getColumnCount() - 1; // Exclude Action column
        JTextField[] fields = new JTextField[colCount];
        JPanel dialogPanel = new JPanel(new GridLayout(0, 2));

        for (int i = 0; i < colCount; i++) {
            fields[i] = new JTextField(table.getValueAt(row, i).toString());
            fields[i].setEditable(editable);
            dialogPanel.add(new JLabel(table.getColumnName(i) + ":"));
            dialogPanel.add(fields[i]);
        }

        if (editable) {
            int result = JOptionPane.showConfirmDialog(null, dialogPanel, "Edit Record", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                for (int i = 0; i < colCount; i++) {
                    model.setValueAt(fields[i].getText(), row, i);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, dialogPanel, "View Record", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void deleteRow(JTable table) {
        int row = table.getEditingRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(table, "Are you sure you want to delete this record?", "Confirm", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION) {
                model.removeRow(row);
            }
        }
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText("Return");
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private boolean clicked;
    private JTable table;
    private DefaultTableModel model;

    public ButtonEditor(JCheckBox checkBox, DefaultTableModel model) {
        super(checkBox);
        this.model = model;
        button = new JButton("Return");
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        clicked = true;
        return button;
    }

    public Object getCellEditorValue() {
        if (clicked) {
            int row = table.getSelectedRow();
            String book = (String) model.getValueAt(row, 0);
            String user = (String) model.getValueAt(row, 1);
            String dateTime = (String) model.getValueAt(row, 2);
            String status = (String) model.getValueAt(row, 3);

            if (!status.equals("Returned")) {
                model.setValueAt("Returned", row, 3);
                updateCSVTransaction(book, user, dateTime, "Returned");
                JOptionPane.showMessageDialog(button, "Book marked as returned.");
            } else {
                JOptionPane.showMessageDialog(button, "Book is already returned.");
            }
        }
        clicked = false;
        return "Return";
    }

    private void updateCSVTransaction(String book, String user, String dateTime, String newStatus) {
        List<String[]> allLines = new ArrayList<>();
        File file = new File("assets/csv/transactions.csv");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4 && parts[0].equals(book) && parts[1].equals(user) && parts[2].equals(dateTime)) {
                    parts[3] = newStatus;
                }
                allLines.add(parts);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String[] parts : allLines) {
                bw.write(String.join(",", parts));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
