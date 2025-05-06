private List<Book> books = new ArrayList<>();
    public void addBook(Book book) {
        books.add(book);
        System.out.println("Book Added! WOWZA!");
    }
    public void removeBook(String id) {
        for (Book a: books) {
            if (a.getId().equals(id)) {
                books.remove(a);
                System.out.println("Beep boop, Book removed.");
                break;
            }
        }
        System.out.println("Book has returned to the void...");
    }
    public Book searchByTitle(String Title) {
        for (Book b: books) {
            if (b.getTitle().equals(Title)) {
                return b;
            }
        }
        System.out.println("Book not found... I swear on everyone at IUI's life (Yes Including you) one day we will have it.");
        return null;
    }
    public void issueBook(String id) {
        for (Book c : books) {
            if (c.getId().equals(id)) {
                if (c.isAvailable()) {
                    c.setAvailability(false);
                    System.out.println("Congrats early bird! Book issued successfully.");
                } else {
                    System.out.println("YOU'RE TOO SLOW! Book's already taken.");
                }
            return;
            }
        }
        System.out.println("Book with that ID doesn't exist. Maybe it never did...");
    }
    public void returnBook(String id) {

        for (Book d : books) {
            if (d.getId().equals(id)) {
                if (!d.isAvailable()) {
                    d.setAvailability(true); 
                    System.out.println("Book returned successfully. You have done well.");
                } else {
                    System.out.println("Book was never issued. Don't lie you fraud.");
                }
            return;
            }
        }
        System.out.println("Book with that ID doesn't exist. Maybe it never did...");
    }
    public void displayBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available. You have failed me for the last time.");
        } else {
            System.out.println("Available Books:");
            for (Book e : books) {
                System.out.println("ID: " + e.getId() + ", Title: " + e.getTitle() + ", Author: " + e.getAuthor() + ", Category: " + e.getCategory() + ", Availability: " + (e.isAvailable() ? "Available" : "Not Available"));
            }
        }
    }


 