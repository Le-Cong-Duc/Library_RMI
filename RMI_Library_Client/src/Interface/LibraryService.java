package Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

import Model.Books;
import Model.BorrowBooks;
import Model.Users;

public interface LibraryService extends Remote {
    Users login(String username, String password) throws RemoteException;

    boolean register(Users user) throws RemoteException;

    //  USER METHOD ====================================================================================================
    List<Users> getAllUser() throws RemoteException;

    List<Users> searchUser(String key) throws RemoteException;

    Users getUserByUsername(String username) throws RemoteException;

    boolean addUser(Users user, String roleCurrentUser) throws RemoteException;

    boolean deleteUser(String username, String currentRole) throws RemoteException;

    boolean updateUser(Users user, String currentRole) throws RemoteException;

    //  BOOK METHOD ====================================================================================================
    List<Books> getAllBook() throws RemoteException;

    Books getBookById(int id) throws RemoteException;

    List<Books> searchBooks(String key) throws RemoteException;

    boolean addBook(Books book) throws RemoteException;

    boolean updateBook(Books book) throws RemoteException;

    boolean deleteBook(int bookId, String bookName, Users currentUser) throws RemoteException;

    boolean borrowBook(int bookId, String bookName, String username, LocalDate returnDate) throws RemoteException;

    boolean returnBook(int bookId, String bookName, String username) throws RemoteException;

    //  BORROW BOOK METHOD =============================================================================================
    public List<BorrowBooks> getAllBorrowBook() throws RemoteException;

    public List<BorrowBooks> searchBorrowBook(String key) throws RemoteException;

    List<BorrowBooks> getBorrowHistory(String username) throws RemoteException;

    List<BorrowBooks> getCurrentBorrows(String username) throws RemoteException;


    //  SUM METHOD ====================================================================================================
    int totalBook() throws RemoteException;

    int totalUser() throws RemoteException;

    int totalBorrowBook() throws RemoteException;

    int getAvailableBook() throws RemoteException;


    //  REGISTER METHOD ================================================================================================
    void registerCallback(String username, Notify client) throws RemoteException;

    void unregisterCallback(String username) throws RemoteException;

}
