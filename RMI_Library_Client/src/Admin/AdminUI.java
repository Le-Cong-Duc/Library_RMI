package Admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import java.util.concurrent.Flow;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Interface.LibraryService;
import Model.Books;
import Model.Users;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class AdminUI extends JPanel {

    private LibraryService libraryService;
    private Users currentUser;
    private JTextArea txtNotify;
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JTextField txtSearch;

    public AdminUI(LibraryService libraryService, Users currentUser, JTextArea txtNotify) {
        this.libraryService = libraryService;
        this.currentUser = currentUser;
        this.txtNotify = txtNotify;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        loadBooks();
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(44, 62, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("LIBRARY MANAGER - ADMIN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);

        JLabel lblUser = new JLabel(currentUser.getFullName());
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(Color.BLUE);
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
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolBar.setBackground(new Color(236, 240, 241));

        txtSearch = new JTextField(25);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnSearchBook = new JButton("Search");
        btnSearchBook.setBackground(new Color(52, 152, 219));
        btnSearchBook.setForeground(Color.BLACK);
        btnSearchBook.addActionListener(e -> searchBook());
        btnSearchBook.setFocusPainted(false);

        JButton btnAddBook = new JButton("Add");
        btnAddBook.setBackground(new Color(52, 152, 219));
        btnAddBook.setForeground(Color.BLACK);
        btnAddBook.addActionListener(e -> showAddBook());
        btnAddBook.setFocusPainted(false);

        JButton btnEditBook = new JButton("Edit");
        btnEditBook.setBackground(new Color(52, 152, 219));
        btnEditBook.setForeground(Color.BLACK);
        btnEditBook.addActionListener(e -> showEditBook());
        btnEditBook.setFocusPainted(false);

        JButton btnDeleteBook = new JButton("Delete");
        btnDeleteBook.setBackground(new Color(52, 152, 219));
        btnDeleteBook.setForeground(Color.BLACK);
        btnDeleteBook.addActionListener(e -> deleteBook());
        btnDeleteBook.setFocusPainted(false);

        JButton btnResetBook = new JButton("Reset");
        btnResetBook.setBackground(new Color(52, 152, 219));
        btnResetBook.setForeground(Color.BLACK);
        btnResetBook.addActionListener(e -> loadBooks());
        btnResetBook.setFocusPainted(false);

        JButton btnStatus = new JButton("Thong ke");
        btnStatus.setBackground(new Color(52, 152, 219));
        btnStatus.setForeground(Color.BLACK);
        btnStatus.addActionListener(e -> showStatus());
        btnStatus.setFocusPainted(false);

        toolBar.add(new JLabel("Search:"));
        toolBar.add(txtSearch);
        toolBar.add(btnSearchBook);
        toolBar.add(Box.createHorizontalStrut(20));
        toolBar.add(btnAddBook);
        toolBar.add(btnEditBook);
        toolBar.add(btnDeleteBook);
        toolBar.add(btnResetBook);
        toolBar.add(btnStatus);

        String[] columns = {"ID", "Name", "Author", "Category", "Quantity", "Available"};
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
        bookTable.getTableHeader().setBackground(new Color(52, 152, 219));
        bookTable.getTableHeader().setForeground(Color.black);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("List book: "));

        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        txtNotify.setEditable(false);
        txtNotify.setFont(new Font("Arial", Font.PLAIN, 12));
        txtNotify.setLineWrap(true);
        txtNotify.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(txtNotify);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Notify"));
        scrollPane.setPreferredSize(new Dimension(0, 120));

        panel.add(scrollPane);
        return panel;

    }

    private void showAddBook() {
        JDialog dialog = new JDialog();
        dialog.setSize(380, 591);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(null);

        JTextField txtName = new JTextField();
        txtName.setBounds(40, 126, 279, 38);
        dialog.add(txtName);
        txtName.setColumns(10);

        JLabel lblName = new JLabel("Book Name");
        lblName.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblName.setBounds(40, 101, 96, 14);
        dialog.add(lblName);

        JTextField txtAuthor = new JTextField();
        txtAuthor.setColumns(10);
        txtAuthor.setBounds(40, 201, 279, 38);
        dialog.add(txtAuthor);

        JLabel lblAuthor = new JLabel("Author");
        lblAuthor.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblAuthor.setBounds(40, 176, 96, 14);
        dialog.add(lblAuthor);

        JTextField txtCategory = new JTextField();
        txtCategory.setColumns(10);
        txtCategory.setBounds(40, 287, 279, 38);
        dialog.add(txtCategory);

        JLabel lblCategory = new JLabel("Category");
        lblCategory.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblCategory.setBounds(40, 262, 96, 19);
        dialog.add(lblCategory);

        JTextField txtQuantity = new JTextField();
        txtQuantity.setColumns(10);
        txtQuantity.setBounds(40, 361, 279, 38);
        dialog.add(txtQuantity);

        JLabel lblQuantity = new JLabel("Quantity");
        lblQuantity.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblQuantity.setBounds(40, 336, 96, 14);
        dialog.add(lblQuantity);

        JLabel lblNewLabel_5 = new JLabel("Add Book");
        lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel_5.setBounds(91, 35, 185, 45);
        dialog.add(lblNewLabel_5);

        JButton btnAdd = new JButton("Add");
        btnAdd.setForeground(new Color(0, 0, 255));
        btnAdd.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnAdd.setBounds(38, 435, 162, 38);
        btnAdd.addActionListener(e -> {
            int id = 0;
            String bookName = txtName.getText();
            String author = txtAuthor.getText();
            String category = txtCategory.getText();
            int quantity = Integer.parseInt(txtQuantity.getText());
            int available = 0;
            try {
                Books book = new Books(id, bookName, author, category, quantity, available);

                if (libraryService.addBook(book)) {
                    JOptionPane.showMessageDialog(dialog, "Add book Successfully !!!!");
                    dialog.dispose();
                    loadBooks();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Add book Failed !!!!");
                }
            } catch (Exception e2) {
                System.out.println("Error in btn add book : " + e2.getMessage());
            }
        });
        dialog.add(btnAdd);

        dialog.setVisible(true);
    }

    private void showEditBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sách cần sửa!");
            return;
        }

        int bookId = (int) bookTableModel.getValueAt(selectedRow, 0);

        try {
            Books book = libraryService.getBookById(bookId);
            if (book == null) {
                JOptionPane.showMessageDialog(this, "Book is null!");
                return;
            }

            JDialog dialog = new JDialog();
            dialog.setSize(380, 591);
            dialog.setLocationRelativeTo(null);
            dialog.setLayout(null);

            JTextField txtName = new JTextField(book.getBookName());
            txtName.setBounds(40, 126, 279, 38);
            dialog.add(txtName);
            txtName.setColumns(10);

            JLabel lblName = new JLabel("Book Name");
            lblName.setFont(new Font("Tahoma", Font.BOLD, 15));
            lblName.setBounds(40, 101, 96, 14);
            dialog.add(lblName);

            JTextField txtAuthor = new JTextField(book.getAuthor());
            txtAuthor.setColumns(10);
            txtAuthor.setBounds(40, 201, 279, 38);
            dialog.add(txtAuthor);

            JLabel lblAuthor = new JLabel("Author");
            lblAuthor.setFont(new Font("Tahoma", Font.BOLD, 15));
            lblAuthor.setBounds(40, 176, 96, 14);
            dialog.add(lblAuthor);

            JTextField txtCategory = new JTextField(book.getCategory());
            txtCategory.setColumns(10);
            txtCategory.setBounds(40, 287, 279, 38);
            dialog.add(txtCategory);

            JLabel lblCategory = new JLabel("Category");
            lblCategory.setFont(new Font("Tahoma", Font.BOLD, 15));
            lblCategory.setBounds(40, 262, 96, 19);
            dialog.add(lblCategory);

            JTextField txtQuantity = new JTextField(String.valueOf(book.getQuantity()));
            txtQuantity.setColumns(10);
            txtQuantity.setBounds(40, 361, 279, 38);
            dialog.add(txtQuantity);

            JLabel lblQuantity = new JLabel("Quantity");
            lblQuantity.setFont(new Font("Tahoma", Font.BOLD, 15));
            lblQuantity.setBounds(40, 336, 96, 14);
            dialog.add(lblQuantity);

            JTextField txtAvailiable = new JTextField(String.valueOf(book.getAvailable()));
            txtAvailiable.setColumns(10);
            txtAvailiable.setBounds(40, 438, 279, 38);
            dialog.add(txtAvailiable);

            JLabel lblAvailible = new JLabel("Availible");
            lblAvailible.setFont(new Font("Tahoma", Font.BOLD, 15));
            lblAvailible.setBounds(40, 413, 96, 14);
            dialog.add(lblAvailible);

            JLabel lblNewLabel_5 = new JLabel("Edit Book");
            lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
            lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 20));
            lblNewLabel_5.setBounds(91, 35, 185, 45);
            dialog.add(lblNewLabel_5);

            JButton btnEdit = new JButton("Edit");
            btnEdit.setForeground(new Color(0, 0, 255));
            btnEdit.setFont(new Font("Tahoma", Font.BOLD, 15));
            btnEdit.setBounds(40, 496, 162, 31);
            btnEdit.addActionListener(e -> {
                int id = bookId;
                String bookName = txtName.getText();
                String author = txtAuthor.getText();
                String category = txtCategory.getText();
                int quantity = Integer.parseInt(txtQuantity.getText());
                int available = book.getAvailable();
                try {
                    Books updateBook = new Books(id, bookName, author, category, quantity, available);

                    if (libraryService.updateBook(updateBook)) {
                        JOptionPane.showMessageDialog(dialog, "Edit book successfully !!!!");
                        dialog.dispose();
                        loadBooks();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Edit book Failed!!!!");
                    }
                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(dialog, "Error in btn edit : " + e2.getMessage());
                }
            });
            dialog.add(btnEdit);

            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error in show edit dialog: " + e.getMessage());
        }
    }

    private void loadBooks() {
        try {
            List<Books> books = libraryService.getAllBook();
            bookTableModel.setRowCount(0);
            for (Books book : books) {
                bookTableModel.addRow(new Object[]{
                        book.getBookId(),
                        book.getBookName(),
                        book.getAuthor(),
                        book.getCategory(),
                        book.getQuantity(),
                        book.getAvailable()
                });
            }
        } catch (Exception e) {
            System.out.println("Error in loadBook - AdminUI: " + e.getMessage());
        }
    }

    private void searchBook() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
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
                        book.getQuantity(),
                        book.getAvailable()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error in btn search: " + e.getMessage());
        }
    }

    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select book needs delete!");
            return;
        }

        int bookId = (int) bookTableModel.getValueAt(selectedRow, 0);
        String bookTitle = (String) bookTableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure :\n" + bookTitle,
                "Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (libraryService.deleteBook(bookId, currentUser)) {
                    JOptionPane.showMessageDialog(this, "Delete succcessfully !!!!");
                    loadBooks();
                } else {
                    JOptionPane.showMessageDialog(this, "Delete Failed!\n .");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }

    private void showStatus() {
        try {
            int totalBooks = libraryService.totalBook();
            int availableBooks = libraryService.getAvailableBook();
            int borrowBooks = libraryService.totalBorrowBook();
            int totalUsers = libraryService.totalUser();

            String status = String.format(
                    "THỐNG KÊ THƯ VIỆN\n\n" +
                            " Tổng số sách: %d\n" +
                            " Sách có sẵn: %d\n" +
                            " Đang được mượn: %d\n" +
                            " Tổng người dùng: %d",
                    totalBooks, availableBooks, borrowBooks, totalUsers);
            JOptionPane.showMessageDialog(this, status, "Thống kê", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error in showStatus: " + e.getMessage());
        }
    }

}
