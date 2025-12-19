package Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import Model.Books;
import Model.BorrowBooks;
import Model.Users;

public interface LibraryService extends Remote {
    //	Auth method
    Users login(String username, String password) throws RemoteException;

    boolean register(Users user) throws RemoteException;

    //  User method
    List<Users> getAllUser() throws RemoteException;

    Users getUserById(int id) throws RemoteException;

    List<Users> getAllUserByName(String name) throws RemoteException;

    boolean addUser(Users user) throws RemoteException;

    boolean deleteUser(int userId) throws RemoteException;

    boolean updateUser(Users user) throws RemoteException;

    //	Book method
    List<Books> getAllBook() throws RemoteException;

    Books getBookById(int id) throws RemoteException;

    List<Books> searchBooks(String name) throws RemoteException;

    boolean addBook(Books book) throws RemoteException;

    boolean updateBook(Books book) throws RemoteException;

    boolean deleteBook(int bookId, Users currentUser) throws RemoteException;

    boolean borrowBook(int bookId, String username, String returnDate) throws RemoteException;

    boolean returnBook(int bookId, String username) throws RemoteException;

    //   Borrow Book
    List<BorrowBooks> getBorrowHistory(String username) throws RemoteException;

    List<BorrowBooks> getCurrentBorrows(String username) throws RemoteException;

    //	Admin method
    public int totalBook() throws RemoteException;

    public int totalUser() throws RemoteException;

    public int totalBorrowBook() throws RemoteException;

    public int getAvailableBook() throws RemoteException;

//  Callback

    void registerCallback(String username, Notify client) throws RemoteException;

    ;

    void unregisterCallback(String username) throws RemoteException;


}
