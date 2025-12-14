package Interface;

import Model.Books;
import Model.BorrowRecord;
import Model.Users;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface LibraryService extends Remote {

    // Auth
    Users login(String username, String password) throws RemoteException;

    boolean register(Users user) throws RemoteException;

    // User
    List<Users> getAllUsers() throws RemoteException;

    Users getUserById(int id) throws RemoteException;

    boolean addUser(Users user) throws RemoteException;

    boolean deleteUser(int userId) throws RemoteException;

    boolean updateUser(Users users) throws RemoteException;

    List<Users> getUserByName(String name) throws RemoteException;

    // Book Management
    List<Books> getAllBooks() throws RemoteException;

    Books getBookById(int id) throws RemoteException;

    List<Books> searchBooks(String keyword) throws RemoteException;

    boolean addBook(Books book) throws RemoteException;

    boolean updateBook(Books book) throws RemoteException;

    boolean deleteBook(int bookId) throws RemoteException;

    boolean borrowBook(int bookId, String username) throws RemoteException;

    boolean returnBook(int bookId, String username) throws RemoteException;

    List<BorrowRecord> getBorrowHistory(String username) throws RemoteException;

    List<BorrowRecord> getCurrentBorrows(String username) throws RemoteException;

    List<BorrowRecord> getAllBorrowRecords(String role) throws RemoteException;

    // Statistics
    int getTotalBooks() throws RemoteException;

    int getAvailableBooks() throws RemoteException;

    int getTotalBorrowedBooks() throws RemoteException;

    int getTotalUsers() throws RemoteException;

    // Callback
    void registerCallback(String username, NotifyClient client) throws RemoteException;

    void unregisterCallback(String username) throws RemoteException;
}