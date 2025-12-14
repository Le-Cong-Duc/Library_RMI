package Model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Books implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private String author;
    private String category;
    private int quantity;
    private int available;

    public Books() {
    }

    public Books(int id, String title, String author, String category, int quantity, int available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.quantity = quantity;
        this.available = available;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        if (this.available > quantity) {
            this.available = quantity;
        }
    }

    public int getAvailable() { return available; }
    public void setAvailable(int available) { this.available = available; }

    public String getStatus() {
        return available > 0 ? "Còn sách (" + available + ")" : "Hết sách";
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | %s | Category: %s | Có sẵn: %d/%d",
                id, title, author, category, available, quantity);
    }
}