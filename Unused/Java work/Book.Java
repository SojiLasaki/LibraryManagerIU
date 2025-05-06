public class Book {

    private String bookId;
    private String title;
    private String author;
    private String category;
    private boolean availability; 

    public Book(String bookId, String title, String author, String category, boolean availability) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.boolean = availability;
    }

public String getId() {
    return bookId;
}

public String getTitle() {
    return title;
}

public boolean isavailable() {
    return availability;
}

public void setAvailability(boolean availability) {
    this.availability = availability
}


}

