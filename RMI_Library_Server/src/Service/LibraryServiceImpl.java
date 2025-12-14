package Service;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Interface.LibraryService;
import Interface.Notify;
import Model.Books;
import Model.BorrowBooks;
import Model.Users;

public class LibraryServiceImpl extends UnicastRemoteObject implements LibraryService {

    private final Map<String, Notify> clientCallBack = new HashMap<>();
    private Map<Integer, Books> books = new HashMap<>();
    private Map<String, Users> users = new HashMap<>();
    private List<BorrowBooks> borrowBooks = new ArrayList<>();
    private int nextBookId = 1;
    private int nextBorrowId = 1;
    private static final String book_file = "C:\\Users\\hieup\\eclipse-workspace\\RMI_Library_Server\\src\\Data\\books.txt";
    private static final String user_file = "C:\\Users\\hieup\\eclipse-workspace\\RMI_Library_Server\\src\\Data\\users.txt";
    private static final String borrow_file = "C:\\Users\\hieup\\eclipse-workspace\\RMI_Library_Server\\src\\Data\\borrows.txt";

    public LibraryServiceImpl() throws RemoteException {
        super();
        loadData();
    }

    private void loadData() {
    	loadUser();
    	System.out.println("Users : "+ users.size());
    }

    @Override
    public Users login(String username, String password) throws RemoteException {
        Users user = users.get(username);
        if(user != null && user.getPassword().equals(password)) {
        	System.out.println("Login succesful: "+ username + "(" +user.getRole()+")");
        	return user;
        }
        else {
        	System.out.println("Login failed !!!"+ username +" - " + password);
        }
        return null;
    }

    @Override
    public boolean register(Users user) throws RemoteException {
    	if(users.containsKey(user.getUserName())){
    		System.out.println("Username is exists : "+ user.getUserName());
    		return false;
    	}
    	users.put(user.getUserName(),user);
    	saveUser();
    	System.out.println("Register successfully!!!");
        return true;
    }

    private void loadUser() {
        File file = new File(user_file);
        if (!file.exists()) {
            System.out.println("Error in loadUser - file not exists !!!");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    Users user = new Users(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            parts[3].trim(),
                            parts[4].trim());
                    users.put(user.getUserName(), user);
                }
            }
        } catch (Exception e) {
            System.out.println("Error in loadUser: " + e.getMessage());
        }
    }

    private void saveUser() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(user_file))) {
            for (Users user : users.values()) {
                bw.write(user.toString());
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error in saveUser: " + e.getMessage());
        }
    }

    @Override
    public List<Users> getAllUser() throws RemoteException {
        return List.of();
    }

    @Override
    public Users getUserById(int id) throws RemoteException {
        return null;
    }

    @Override
    public List<Users> getAllUserByName(String name) throws RemoteException {
        return List.of();
    }

    @Override
    public boolean addUser(Users user) throws RemoteException {
        return false;
    }

    @Override
    public boolean deleteUser(int userId) throws RemoteException {
        return false;
    }

    @Override
    public boolean updateUser(Users user) throws RemoteException {
        return false;
    }

    private void loadBook() {

    }

    private void saveBook() {

    }

    @Override
    public List<Books> getAllBook() throws RemoteException {
        return List.of();
    }

    @Override
    public Books getBookById(int id) throws RemoteException {
        return null;
    }

    @Override
    public List<Books> getBookByName(String name) throws RemoteException {
        return List.of();
    }

    @Override
    public boolean addBook(Books book) throws RemoteException {
        return false;
    }

    @Override
    public boolean updateBook(Books book) throws RemoteException {
        return false;
    }

    @Override
    public boolean deleteBook(int bookId) throws RemoteException {
        return false;
    }

    @Override
    public boolean borrowBook(int bookId, String username) throws RemoteException {
        return false;
    }

    @Override
    public boolean returnBook(int bookId, String username) throws RemoteException {
        return false;
    }

    @Override
    public int totalBook() throws RemoteException {
        return 0;
    }

    @Override
    public int totalUser() throws RemoteException {
        return 0;
    }

    @Override
    public int totalBorrowBook() throws RemoteException {
        return 0;
    }

    @Override
    public int getAvailableBook() throws RemoteException {
        return 0;
    }

    @Override
    public void registerCallback(String username, Notify client) throws RemoteException {

    }

    @Override
    public void unregisterCallback(String username) throws RemoteException {

    }
}
