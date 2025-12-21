package Model;

import java.io.Serializable;
import java.time.LocalDate;

public class BorrowBooks implements Serializable {

	private int id;
	private String username;
	private int bookId;
	private String bookTitle;
	private LocalDate borrowDate;
	private LocalDate returnDate;
	private String status;

	public BorrowBooks() {
	}

	public BorrowBooks(int id, String username, int bookId, String bookTitle, LocalDate borrowDate,
					   LocalDate returnDate, String status) {
		super();
		this.id = id;
		this.username = username;
		this.bookId = bookId;
		this.bookTitle = bookTitle;
		this.borrowDate = borrowDate;
		this.returnDate = returnDate;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public LocalDate getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(LocalDate borrowDate) {
		this.borrowDate = borrowDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setRetrunDate(LocalDate retrunDate) {
		this.returnDate = retrunDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return id + "|" +
				username + "|" +
				bookId + "|" +
				bookTitle + "|" +
				borrowDate + "|" +
				returnDate + "|" +
				status;
	}
}
