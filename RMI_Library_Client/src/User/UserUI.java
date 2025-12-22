package User;

import Interface.LibraryService;
import Model.Books;
import Model.BorrowBooks;
import Model.Users;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserUI extends JPanel {

    private LibraryService libraryService;
    private Users currentUser;
    private JTextArea txtNotify;
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JTabbedPane tabbedPane;

    public UserUI(LibraryService libraryService, Users currentUser, JTextArea txtNotify) {
        super();
        this.libraryService = libraryService;
        this.currentUser = currentUser;
        this.txtNotify = txtNotify;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(26, 140, 200));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("LIBRARY");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);

        JLabel lblUser = new JLabel(currentUser.getFullName());
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(0, 0, 0));
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> System.exit(0));

        rightPanel.add(lblUser);
        rightPanel.add(Box.createHorizontalStrut(10));
        rightPanel.add(btnLogout);

        panel.add(lblTitle, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createCenterPanel() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        tabbedPane.addTab("Sach", showBookPanel());
        tabbedPane.addTab("Dang Muon", showBorrowedPanel());
        tabbedPane.addTab("Lich Su", showHistoryPanel());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tabbedPane);
        return panel;
    }

    private JPanel showBookPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JTextField txtSearch = new JTextField(25);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(24, 140, 200));
        btnSearch.setForeground(Color.BLACK);
        btnSearch.setFocusPainted(false);
        toolbar.add(new JLabel("Search :"));
        toolbar.add(txtSearch);
        toolbar.add(btnSearch);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(155, 89, 182));
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFocusPainted(false);

        JButton btnBorrow = new JButton("Borrow");
        btnBorrow.setBackground(new Color(46, 204, 113));
        btnBorrow.setForeground(Color.BLACK);
        btnBorrow.setFocusPainted(false);

        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(btnBorrow);
        toolbar.add(btnRefresh);

        String[] columns = {"ID", "Title", "Author", "Category", "Available"};
        bookTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(bookTableModel);
        bookTable.setFont(new Font("Arial", Font.PLAIN, 13));
        bookTable.setRowHeight(25);
        bookTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        bookTable.getTableHeader().setBackground(new Color(26, 188, 156));
        bookTable.getTableHeader().setForeground(Color.BLACK);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(bookTable);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        loadBooks();

        btnSearch.addActionListener(e -> searchBooks(txtSearch.getText()));
        txtSearch.addActionListener(e -> searchBooks(txtSearch.getText()));
        btnRefresh.addActionListener(e -> loadBooks());
        btnBorrow.addActionListener(e -> borrowBook());

        return panel;
    }

    private JPanel showBorrowedPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(155, 89, 182));
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFocusPainted(false);

        toolbar.add(btnRefresh);

        String[] columns = {"Title", "Borrow Date", "Return Date"};
        DefaultTableModel borrowTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable borrowTable = new JTable(borrowTableModel);
        borrowTable.setFont(new Font("Arial", Font.PLAIN, 13));
        borrowTable.setRowHeight(25);
        borrowTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        borrowTable.getTableHeader().setBackground(new Color(26, 188, 156));
        borrowTable.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(borrowTable);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        loadBorrowedBooks(borrowTableModel);

        btnRefresh.addActionListener(e -> loadBorrowedBooks(borrowTableModel));
        return panel;
    }

    private JPanel showHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== Toolbar =====
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(155, 89, 182));
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFocusPainted(false);

        toolbar.add(btnRefresh);

        // ===== Table =====
        String[] columns = {
                "Title",
                "Borrow Date",
                "Return Date",
                "Status"
        };

        DefaultTableModel historyTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable historyTable = new JTable(historyTableModel);
        historyTable.setRowHeight(25);
        historyTable.setFont(new Font("Arial", Font.PLAIN, 13));
        historyTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        historyTable.getTableHeader().setBackground(new Color(26, 188, 156));
        historyTable.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(historyTable);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // ===== Load data =====
        loadBorrowedHistory(historyTableModel);

        btnRefresh.addActionListener(e -> loadBorrowedHistory(historyTableModel));

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        txtNotify.setEditable(false);
        txtNotify.setFont(new Font("Arial", Font.PLAIN, 12));
        txtNotify.setLineWrap(true);
        txtNotify.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(txtNotify);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Notify: "));
        scrollPane.setPreferredSize(new Dimension(0, 120));

        panel.add(scrollPane);
        return panel;
    }

    private void loadBooks() {
        try {
            List<Books> list = libraryService.getAllBook();
            bookTableModel.setRowCount(0);
            for (Books book : list) {
                bookTableModel.addRow(new Object[]{
                        book.getBookId(),
                        book.getBookName(),
                        book.getAuthor(),
                        book.getCategory(),
                        book.getAvailable()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error in UserUI - loadBook() : " + e.getMessage());
        }
    }

    private void searchBooks(String keyword) {
        if (keyword.trim().isEmpty()) {
            loadBooks();
            return;
        }
        try {
            List<Books> books = libraryService.searchBooks(keyword);
            bookTableModel.setRowCount(0);
            for (Books book : books) {
                bookTableModel.addRow(new Object[]{
                        book.getBookId(),
                        book.getBookName(),
                        book.getAuthor(),
                        book.getCategory(),
                        book.getAvailable()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error in UserUI -searchBook: " + e.getMessage());
        }
    }

    private void borrowBook() {
        int selectedRow = bookTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select the book you wish to borrow!");
            return;
        }

        int bookId = (int) bookTableModel.getValueAt(selectedRow, 0);
        String bookName = (String) bookTableModel.getValueAt(selectedRow, 1);

        int available = (int) bookTableModel.getValueAt(selectedRow, 4);

        if (available <= 0) {
            JOptionPane.showMessageDialog(this, "out of books!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "You want to borrow: :\n" + bookName,
                "Yes",
                JOptionPane.YES_NO_OPTION);

        SpinnerDateModel dateModel = new SpinnerDateModel(
                new Date(),
                null,
                null,
                Calendar.DAY_OF_MONTH
        );

        JSpinner dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(editor);

        LocalDate returnDate;
        while (true) {
            int option = JOptionPane.showConfirmDialog(
                    this,
                    dateSpinner,
                    "Select return date",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (option != JOptionPane.OK_OPTION) {
                return;
            }

            Date selectedDate = (Date) dateSpinner.getValue();

            returnDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (returnDate.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(
                        this,
                        "Return date must be after borrow day!",
                        "Invalid return date",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                break;
            }
        }

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (libraryService.borrowBook(bookId, bookName, currentUser.getUserName(), returnDate)) {
                    JOptionPane.showMessageDialog(this, "Borrow Book Successfully !\n Vui lòng trả sách đúng hạn.");
                    loadBooks();
                } else {
                    JOptionPane.showMessageDialog(this, "Borrow Book Failed!\n Bạn có thể đã mượn sách này rồi.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }

    private static void extracted() {
        int option;
    }

    private void loadBorrowedBooks(DefaultTableModel tableModel) {
        try {
            List<BorrowBooks> list = libraryService.getCurrentBorrows(currentUser.getUserName());
            tableModel.setRowCount(0);
            for (BorrowBooks book : list) {
                tableModel.addRow(new Object[]{
                        book.getBookTitle(),
                        book.getBorrowDate(),
                        book.getReturnDate()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error in UserUI - loadBorrowedBooks: " + e.getMessage());
        }
    }

    private void loadBorrowedHistory(DefaultTableModel model) {
        try {
            List<BorrowBooks> list =
                    libraryService.getBorrowHistory(currentUser.getUserName());

            model.setRowCount(0);

            for (BorrowBooks borrow : list) {
                model.addRow(new Object[]{
                        borrow.getBookTitle(),
                        borrow.getBorrowDate(),
                        borrow.getReturnDate(),
                        borrow.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error in UserUI - loadBorrowedHistory: " + e.getMessage()
            );
        }
    }


}
