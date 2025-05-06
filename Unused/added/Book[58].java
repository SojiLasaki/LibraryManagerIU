import java.time.LocalDate; //We using real time or?? idk

public class Book {
    private String isbn, title, author, category;
    private boolean available;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    //Gonna be passed to admin? coz admins job to handle this but I have the book csv so here u go. 
    public Book(String isbn, String title, String author, String category, boolean available) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.available = available;
    }

    public String getISBN() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return available; }

    public void setBorrowed(boolean borrowed) { this.available = !borrowed; }
    public void setBorrowDate(LocalDate date) { this.borrowDate = date; }
    public void setDueDate(LocalDate date) { this.dueDate = date; }

    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
}
