package Model;

import java.io.Serializable;

public class Books implements Serializable {
    private int bookId;
    private String bookName;
    private String author;
    private String category;
    private int quantity;
    private int available;

    public Books() {
    }

    public Books(int bookId, String bookName, String author, String category, int quantity, int available) {
        super();
        this.bookId = bookId;
        this.bookName = bookName;
        this.author = author;
        this.category = category;
        this.quantity = quantity;
        this.available = available;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return String.format(bookId + "|" + bookName + "|" + author + "|" + category + "|" + quantity + "|" + available);
    }


}
