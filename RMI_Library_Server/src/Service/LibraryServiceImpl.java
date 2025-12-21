package Service;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    private static final String book_file = "C:\\Users\\hieup\\eclipse-workspace\\Library_RMI\\RMI_Library_Server\\src\\Data\\books.txt";
    private static final String user_file = "C:\\Users\\hieup\\eclipse-workspace\\Library_RMI\\RMI_Library_Server\\src\\Data\\users.txt";
    private static final String borrow_file = "C:\\Users\\hieup\\eclipse-workspace\\Library_RMI\\RMI_Library_Server\\src\\Data\\borrows.txt";

    public LibraryServiceImpl() throws RemoteException {
        super();
        loadData();
    }

    private void loadData() {
        loadUser();
        loadBook();
        loadBorrowBooks();
        System.out.println("Users : " + users.size());
        System.out.println("Books : " + books.size());
        System.out.println("Borrowed Book:  : " + borrowBooks.size());

    }

    @Override
    public Users login(String username, String password) throws RemoteException {
        Users user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login succesful: " + username + "(" + user.getRole() + ")");
            return user;
        } else {
            System.out.println("Login failed !!!" + username + " - " + password);
        }
        return null;
    }

    @Override
    public boolean register(Users user) throws RemoteException {
        if (users.containsKey(user.getUserName())) {
            System.out.println("Username is exists : " + user.getUserName());
            return false;
        }
        users.put(user.getUserName(), user);
        saveUser();
        System.out.println("Register successfully!!!");
        return true;
    }

    private void loadUser() {
        File file = new File(user_file);
        if (!file.exists()) {
            users.clear();
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
        return new ArrayList<>(users.values());
    }

    @Override
    public List<Users> searchUser(String key) throws RemoteException {
        String lowerKeyword = key.toLowerCase();
        return users.values().stream()
                .filter(b -> b.getUserName().toLowerCase().contains(lowerKeyword) ||
                        b.getFullName().toLowerCase().contains(lowerKeyword) ||
                        b.getEmail().toLowerCase().contains(lowerKeyword) ||
                        b.getRole().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    @Override
    public boolean addUser(Users user, String roleCurrent) throws RemoteException {
        for (Users oldUser : users.values()) {
            if (oldUser.getUserName().equalsIgnoreCase(user.getUserName())) {
                return false;
            }
        }
        users.put(user.getUserName(), user);

        saveUser();
        System.out.println("User added: " + user.getUserName());

        notifyAddUser(user.getUserName(), roleCurrent);
        return true;
    }

    @Override
    public boolean deleteUser(String username, String currentRole) throws RemoteException {
        users.remove(username);
        saveUser();
        System.out.println("username:  " + username + " is deleted");

        notifyDeleteUser(username, currentRole);
        return true;
    }

    @Override
    public boolean updateUser(Users user, String currentRole) throws RemoteException {
        Users oldUser = users.get(user.getUserName());
        if (oldUser == null) {
            return false;
        }

        users.put(user.getUserName(), user);

        saveUser();
        System.out.println(": User: " + user.getUserName() + " is edited!");
        notifyEditUser(user.getUserName(), currentRole);
        return true;
    }

    private void loadBook() {
        File file = new File(book_file);
        if (!file.exists()) {
            System.out.println("Error in loadBook - file not exists !!!");
            books.clear();
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    int id = Integer.parseInt(parts[0].trim());
                    Books book = new Books(
                            id,
                            parts[1].trim(),
                            parts[2].trim(),
                            parts[3].trim(),
                            Integer.parseInt(parts[4].trim()),
                            Integer.parseInt(parts[5].trim()));
                    books.put(id, book);
                    if (id >= nextBookId) {
                        nextBookId = id + 1;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in loadBook: " + e.getMessage());
        }
    }

    private void saveBook() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(book_file))) {
            for (Books book : books.values()) {
                bw.write(book.toString());
                bw.newLine();
            }
        } catch (Exception e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }

    @Override
    public List<Books> getAllBook() throws RemoteException {
        return new ArrayList<>(books.values());
    }

    @Override
    public Books getBookById(int id) throws RemoteException {
        return books.get(id);
    }

    @Override
    public List<Books> searchBooks(String keyword) throws RemoteException {
        String lowerKeyword = keyword.toLowerCase();
        return books.values().stream()
                .filter(b -> b.getBookName().toLowerCase().contains(lowerKeyword) ||
                        b.getAuthor().toLowerCase().contains(lowerKeyword) ||
                        b.getCategory().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    @Override
    public boolean addBook(Books book) throws RemoteException {

        for (Books oldBook : books.values()) {
            if (oldBook.getBookName().equalsIgnoreCase(book.getBookName())
                    && oldBook.getAuthor().equalsIgnoreCase(book.getAuthor())
                    && oldBook.getCategory().equalsIgnoreCase(book.getCategory())) {

                // cộng số lượng
                oldBook.setQuantity(oldBook.getQuantity() + book.getQuantity());
                oldBook.setAvailable(oldBook.getAvailable() + book.getQuantity());

                saveBook();
                notifyAllClient("Book updated quantity: " + oldBook.getBookName());
                return true;
            }
        }

        book.setBookId(nextBookId++);
        book.setAvailable(book.getQuantity());
        books.put(book.getBookId(), book);

        saveBook();
        System.out.println("Book added: " + book.getBookName() + "(ID: " + book.getBookId() + ")");

        notifyBookAddedToClients(book.getBookName());
        return true;
    }

    @Override
    public boolean updateBook(Books updateBook) throws RemoteException {
        Books old_book = books.get(updateBook.getBookId());
        if (old_book == null) {
            return false;
        }
        int borrowed = old_book.getQuantity() - old_book.getAvailable();

        int newAvailable = updateBook.getQuantity() - borrowed;
        if (newAvailable < 0) {
            newAvailable = 0; // chặn âm
        }
        updateBook.setAvailable(newAvailable);

        books.put(updateBook.getBookId(), updateBook);

        saveBook();
        System.out.println(": Book ID: " + updateBook.getBookId() + " is edited!");
        notifyBookEditToClients(updateBook.getBookName());
        return true;
    }

    @Override
    public boolean deleteBook(int bookId, String bookName, Users currentUser) throws RemoteException {
        boolean isBorrowed = borrowBooks.stream()
                .anyMatch(r -> r.getBookId() == bookId && "BORROWED".equals(r.getStatus()));

        if (isBorrowed) {
            System.out.println("Can not delete book : curretly borrowed");
            return false;
        }
        books.remove(bookId);
        saveBook();

        System.out.println(currentUser.getUserName() + ": Book ID: " + bookId + " is deleted");

        notifyBookDeleteToClients(bookName);
        return true;
    }

    public List<BorrowBooks> getAllBorrowBook() throws RemoteException {
        return borrowBooks.stream()
                .sorted(Comparator.comparing(BorrowBooks::getBorrowDate).reversed())
                .collect(Collectors.toList());
    }

    public List<BorrowBooks> searchBorrowBook(String key) throws RemoteException {
        if (key == null || key.trim().isEmpty()) {
            return getAllBorrowBook();
        }
        String lowerkey = key.toLowerCase();

        return borrowBooks.stream()
                .filter(b -> b.getUsername().toLowerCase().contains(lowerkey) ||
                        b.getBookTitle().toLowerCase().contains(lowerkey) ||
                        b.getStatus().toLowerCase().contains(lowerkey)
                ).sorted(Comparator.comparing(BorrowBooks::getBorrowDate).reversed())
                .collect(Collectors.toList());
    }

    private void loadBorrowBooks() {
        File file = new File(borrow_file);
        if (!file.exists()) {
            borrowBooks.clear();
            nextBorrowId = 1;
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 7) {
                    int id = Integer.parseInt(parts[0].trim());
                    BorrowBooks record = new BorrowBooks(
                            id, parts[1].trim(), Integer.parseInt(parts[2].trim()),
                            parts[3].trim(), LocalDate.parse(parts[4].trim()),
                            LocalDate.parse(parts[5].trim()), parts[6].trim());
                    borrowBooks.add(record);
                    if (id >= nextBorrowId) {
                        nextBorrowId = id + 1;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading borrow records: " + e.getMessage());
        }
    }

    private void saveBorrowBooks() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(borrow_file))) {
            for (BorrowBooks record : borrowBooks) {
                bw.write(record.toString());
                bw.newLine();
            }
        } catch (Exception e) {
            System.err.println("Error in saveBorrowBooks() : " + e.getMessage());
        }
    }

    @Override
    public boolean returnBook(int bookId, String bookName, String username) throws RemoteException {

        for (BorrowBooks borrow : borrowBooks) {
            if (borrow.getUsername().equals(username)
                    && borrow.getBookId() == bookId
                    && "BORROWED".equals(borrow.getStatus())) {

                // cập nhật số sách còn lại
                Books book = books.get(bookId);
                if (book != null) {
                    book.setAvailable(book.getAvailable() + 1);
                    saveBook();
                }

                // cập nhật trạng thái mượn
                borrow.setStatus("RETURNED");
                borrow.setRetrunDate(LocalDate.now());
                saveBorrowBooks();

                System.out.println("Book " + borrow.getBookTitle() + " is returned");

                notifyBookReturnToClients(bookName, bookId);

                return true;
            }
        }

        System.out.println("Return failed: No borrow record found");
        return false;
    }

    @Override
    public boolean borrowBook(int bookId, String bookName, String username, LocalDate returnDate) throws RemoteException {
        Books book = books.get(bookId);
        if (book == null || book.getAvailable() <= 0) {
            System.out.println("Borrow failed: Book not available (ID: " + bookId + ")");
            return false;
        }
        boolean alreadyBorrowed = borrowBooks.stream()
                .anyMatch(r -> r.getUsername().equals(username) &&
                        r.getBookId() == bookId &&
                        "BORROWED".equals(r.getStatus()));
        if (alreadyBorrowed) {
            System.out.println("Borrow failed: User already borrowed this book");
            return false;
        }

        book.setAvailable(book.getAvailable() - 1);

        BorrowBooks borrow = new BorrowBooks();
        borrow.setId(nextBorrowId++);
        borrow.setUsername(username);
        borrow.setBookId(bookId);
        borrow.setBookTitle(book.getBookName());
        borrow.setBorrowDate(LocalDate.now());
        borrow.setRetrunDate(returnDate);
        borrow.setStatus("BORROWED");

        borrowBooks.add(borrow);
        saveBook();
        saveBorrowBooks();

        notifyBookBorrowToClients(bookName, bookId);

        return true;
    }

    @Override
    public List<BorrowBooks> getBorrowHistory(String username) throws RemoteException {
        return borrowBooks.stream().
                filter(r -> r.getUsername().equals(username))
                .sorted(Comparator.comparing(BorrowBooks::getBorrowDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowBooks> getCurrentBorrows(String username) throws RemoteException {
        return borrowBooks.stream()
                .filter(r -> r.getUsername().equals(username) && "BORROWED".equals(r.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public int totalBook() throws RemoteException {
        return books.values().stream().mapToInt(Books::getQuantity).sum();
    }

    @Override
    public int totalUser() throws RemoteException {
        return users.size();
    }

    @Override
    public int totalBorrowBook() throws RemoteException {
        return borrowBooks.size();
    }

    @Override
    public int getAvailableBook() throws RemoteException {
        return books.values().stream().mapToInt(Books::getAvailable).sum();
    }

    @Override
    public void registerCallback(String username, Notify client) throws RemoteException {
        clientCallBack.put(username, client);
        System.out.println("Client callback registered: " + username);
    }

    @Override
    public void unregisterCallback(String username) throws RemoteException {
        clientCallBack.remove(username);
        System.out.println("Client callback unregistered: " + username);
    }

    private void notifyAddUser(String username, String role) {
        for (Notify client : clientCallBack.values()) {
            try {
                client.notifyUserAdded(username, role);
            } catch (Exception e) {
                System.out.println("Failed to notify user added");
            }
        }
    }

    private void notifyDeleteUser(String username, String role) {
        for (Notify client : clientCallBack.values()) {
            try {
                client.notifyUserDelete(username, role);
            } catch (Exception e) {
                System.out.println("Failed to notify user added");
            }
        }
    }

    private void notifyEditUser(String username, String role) {
        for (Notify client : clientCallBack.values()) {
            try {
                client.notifyUserUpdated(username, role);
            } catch (Exception e) {
                System.out.println("Failed to notify user added");
            }
        }
    }

    private void notifyAllClient(String mesage) {
        for (Map.Entry<String, Notify> entry : clientCallBack.entrySet()) {
            try {
                entry.getValue().notifyMessage(mesage);
            } catch (Exception e) {
                System.out.println("Failed to notify client " + entry.getKey());
            }
        }
    }

    private void notifyBookAddedToClients(String bookTitle) {
        for (Notify client : clientCallBack.values()) {
            try {
                client.notifyBookAdded(bookTitle);
            } catch (Exception e) {
                System.out.println("Failed to notify book added");
            }
        }
    }

    private void notifyBookDeleteToClients(String bookTitle) {
        for (Notify client : clientCallBack.values()) {
            try {
                client.notifyBookDeleted(bookTitle);
            } catch (Exception e) {
                System.out.println("Failed to notify book added");
            }
        }
    }

    private void notifyBookEditToClients(String bookTitle) {
        for (Notify client : clientCallBack.values()) {
            try {
                client.notifyBookEdit(bookTitle);
            } catch (Exception e) {
                System.out.println("Failed to notify book added");
            }
        }
    }

    private void notifyBookBorrowToClients(String bookTitle, int bookId) {
        for (Notify client : clientCallBack.values()) {
            try {
                client.notifyBookBorrowed(bookTitle, bookId);
            } catch (Exception e) {
                System.out.println("Failed to notify book added");
            }
        }
    }

    private void notifyBookReturnToClients(String bookTitle, int bookId) {
        for (Notify client : clientCallBack.values()) {
            try {
                client.notifyBookReturned(bookTitle, bookId);
            } catch (Exception e) {
                System.out.println("Failed to notify book added");
            }
        }
    }
}
