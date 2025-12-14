package Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BorrowRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String username;
    private int bookId;
    private String bookTitle;
    private long borrowDate;
    private long returnDate;
    private String status; // BORROWED, RETURNED

    public BorrowRecord() {
        this.borrowDate = System.currentTimeMillis();
        this.status = "BORROWED";
    }

    public BorrowRecord(int id, String username, int bookId, String bookTitle,
                        long borrowDate, long returnDate, String status) {
        this.id = id;
        this.username = username;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public long getBorrowDate() { return borrowDate; }
    public void setBorrowDate(long borrowDate) { this.borrowDate = borrowDate; }

    public long getReturnDate() { return returnDate; }
    public void setReturnDate(long returnDate) { this.returnDate = returnDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getBorrowDateFormatted() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(new Date(borrowDate));
    }

    public String getReturnDateFormatted() {
        if (returnDate == 0) return "Chưa trả";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(new Date(returnDate));
    }

    @Override
    public String toString() {
        return String.format("%d|%s|%d|%s|%d|%d|%s",
                id, username, bookId, bookTitle, borrowDate, returnDate, status);
    }
}