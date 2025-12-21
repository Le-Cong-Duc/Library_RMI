package Admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Flow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Interface.LibraryService;
import Model.Books;
import Model.BorrowBooks;
import Model.Users;

public class AdminUI extends JPanel {

    private LibraryService libraryService;
    private Users currentUser;
    private JTextArea txtNotify;
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JTextField txtSearch;
    private JTabbedPane tabbedPane = new JTabbedPane();

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
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        tabbedPane.addTab("Quản lý sách", showBookMangerPanel());
        tabbedPane.addTab("Quản lý người dùng", showUserManagerPanel());
        tabbedPane.addTab("Quản lý mượn trả", showBorrowBookManagerPanel());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tabbedPane);
        return panel;
    }

    private JPanel showBookMangerPanel() {
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

        JButton btnBorrow = new JButton("Borrow");
        btnBorrow.setBackground(new Color(46, 204, 113));
        btnBorrow.setForeground(Color.BLACK);
        btnBorrow.setFocusPainted(false);
        btnBorrow.addActionListener(e -> showAdminBorrowBook());

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
        toolBar.add(btnBorrow);
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

    private JPanel showUserManagerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== Toolbar =====
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JTextField txtSearch = new JTextField(25);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(24, 140, 200));
        btnSearch.setFocusPainted(false);

        JButton btnAdd = new JButton("Add");
        btnAdd.setBackground(new Color(46, 204, 113)); // xanh lá
        btnAdd.setFocusPainted(false);

        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBackground(new Color(241, 196, 15)); // vàng
        btnUpdate.setFocusPainted(false);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(231, 76, 60)); // đỏ
        btnDelete.setFocusPainted(false);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(155, 89, 182)); // tím
        btnRefresh.setFocusPainted(false);

        toolbar.add(new JLabel("Search user:"));
        toolbar.add(txtSearch);
        toolbar.add(btnSearch);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(btnAdd);
        toolbar.add(btnUpdate);
        toolbar.add(btnDelete);
        toolbar.add(btnRefresh);

        // ===== Table =====
        String[] columns = {"Username", "Full Name", "Email", "Role"};
        DefaultTableModel userTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable userTable = new JTable(userTableModel);
        userTable.setRowHeight(25);
        userTable.setFont(new Font("Arial", Font.PLAIN, 13));
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        userTable.getTableHeader().setBackground(new Color(26, 188, 156));
        userTable.getTableHeader().setForeground(Color.BLACK);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(userTable);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // ===== Load data =====
        loadUsers(userTableModel);

        // ===== Events =====
        btnRefresh.addActionListener(e -> loadUsers(userTableModel));
        btnSearch.addActionListener(e -> searchUser(txtSearch.getText(), userTableModel));

        btnDelete.addActionListener(e -> deleteUser(userTable, userTableModel));

        btnAdd.addActionListener(e -> showAddUser(userTableModel));
        btnUpdate.addActionListener(e -> showEditUser(userTable, userTableModel));

        return panel;
    }

    private void showAddUser(DefaultTableModel model) {
        JDialog dialog = new JDialog();
        dialog.setSize(380, 620);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(null);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblUsername.setBounds(40, 90, 120, 20);
        dialog.add(lblUsername);

        JTextField txtUsername = new JTextField();
        txtUsername.setBounds(40, 115, 279, 38);
        dialog.add(txtUsername);

        JLabel lblFullname = new JLabel("Full Name");
        lblFullname.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblFullname.setBounds(40, 160, 120, 20);
        dialog.add(lblFullname);

        JTextField txtFullname = new JTextField();
        txtFullname.setBounds(40, 185, 279, 38);
        dialog.add(txtFullname);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblPassword.setBounds(40, 230, 120, 20);
        dialog.add(lblPassword);

        JTextField txtPassword = new JTextField();
        txtPassword.setBounds(40, 255, 279, 38);
        dialog.add(txtPassword);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblEmail.setBounds(40, 300, 120, 20);
        dialog.add(lblEmail);

        JTextField txtEmail = new JTextField();
        txtEmail.setBounds(40, 325, 279, 38);
        dialog.add(txtEmail);

        JLabel lblRole = new JLabel("Role");
        lblRole.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblRole.setBounds(40, 370, 120, 20);
        dialog.add(lblRole);

        JComboBox<String> cbRole = new JComboBox<>(new String[]{"USER", "ADMIN"});
        cbRole.setBounds(40, 395, 279, 38);
        dialog.add(cbRole);

        JLabel lblTitle = new JLabel("Add User");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setBounds(90, 30, 200, 40);
        dialog.add(lblTitle);

        JButton btnAdd = new JButton("Add");
        btnAdd.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnAdd.setBounds(40, 460, 162, 38);

        btnAdd.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            String fullname = txtFullname.getText().trim();
            String email = txtEmail.getText().trim();
            String role = cbRole.getSelectedItem().toString();
            try {
                Users user = new Users(username, password, fullname, email, role);

                if (libraryService.addUser(user, currentUser.getRole())) {
                    JOptionPane.showMessageDialog(dialog, "Add user successfully!");
                    dialog.dispose();
                    loadUsers(model);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Add user failed!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.add(btnAdd);
        dialog.setVisible(true);
    }

    private void showEditUser(JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select the user you want to edit.!!!");
            return;
        }

        String username = model.getValueAt(selectedRow, 0).toString();

        try {
            Users user = libraryService.getUserByUsername(username);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "User not found!");
                return;
            }

            JDialog dialog = new JDialog();
            dialog.setSize(380, 620);
            dialog.setLocationRelativeTo(null);
            dialog.setLayout(null);

            JLabel lblUsername = new JLabel("Username");
            lblUsername.setFont(new Font("Tahoma", Font.BOLD, 15));
            lblUsername.setBounds(40, 90, 120, 20);
            dialog.add(lblUsername);

            JTextField txtUsername = new JTextField(user.getUserName());
            txtUsername.setBounds(40, 115, 279, 38);
            txtUsername.setEditable(false);
            dialog.add(txtUsername);

            JLabel lblFullname = new JLabel("Full Name");
            lblFullname.setFont(new Font("Tahoma", Font.BOLD, 15));
            lblFullname.setBounds(40, 160, 120, 20);
            dialog.add(lblFullname);

            JTextField txtFullname = new JTextField(user.getFullName());
            txtFullname.setBounds(40, 185, 279, 38);
            dialog.add(txtFullname);

            JLabel lblPassword = new JLabel("Password");
            lblPassword.setFont(new Font("Tahoma", Font.BOLD, 15));
            lblPassword.setBounds(40, 230, 120, 20);
            dialog.add(lblPassword);

            JTextField txtPassword = new JTextField(user.getPassword());
            txtPassword.setBounds(40, 255, 279, 38);
            dialog.add(txtPassword);

            JLabel lblEmail = new JLabel("Email");
            lblEmail.setFont(new Font("Tahoma", Font.BOLD, 15));
            lblEmail.setBounds(40, 300, 120, 20);
            dialog.add(lblEmail);

            JTextField txtEmail = new JTextField(user.getEmail());
            txtEmail.setBounds(40, 325, 279, 38);
            dialog.add(txtEmail);

            JLabel lblRole = new JLabel("Role");
            lblRole.setFont(new Font("Tahoma", Font.BOLD, 15));
            lblRole.setBounds(40, 370, 120, 20);
            dialog.add(lblRole);

            JComboBox<String> cbRole = new JComboBox<>(new String[]{"USER", "ADMIN"});
            cbRole.setSelectedItem(user.getRole());
            cbRole.setBounds(40, 395, 279, 38);
            dialog.add(cbRole);

            JLabel lblTitle = new JLabel("Edit User");
            lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
            lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
            lblTitle.setBounds(90, 30, 200, 40);
            dialog.add(lblTitle);

            JButton btnEdit = new JButton("Edit");
            btnEdit.setFont(new Font("Tahoma", Font.BOLD, 15));
            btnEdit.setBounds(40, 460, 162, 38);

            btnEdit.addActionListener(e -> {
                String usernamee = txtUsername.getText().trim();
                String password = txtPassword.getText().trim();
                String fullname = txtFullname.getText().trim();
                String email = txtEmail.getText().trim();
                String role = cbRole.getSelectedItem().toString();
                try {
                    Users updateUser = new Users(usernamee, password, fullname, email, role);

                    if (libraryService.updateUser(updateUser, currentUser.getRole())) {
                        JOptionPane.showMessageDialog(dialog, "Update user successfully!");
                        dialog.dispose();
                        loadUsers(model);
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Update user failed!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
                }
            });

            dialog.add(btnEdit);
            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void showAdminBorrowBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select the user you want to borrow.!!!");
            return;
        }

        String bookName = bookTableModel.getValueAt(selectedRow, 1).toString();

        JDialog dialog = new JDialog();
        dialog.setSize(400, 420);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(null);

        JLabel lblTitle = new JLabel("Borrow Book (Admin)");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(80, 20, 240, 30);
        dialog.add(lblTitle);

        JLabel lblBook = new JLabel("Book");
        lblBook.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblBook.setBounds(40, 70, 120, 20);
        dialog.add(lblBook);

        JTextField txtBook = new JTextField(bookName);
        txtBook.setBounds(40, 95, 300, 35);
        txtBook.setEditable(false);
        dialog.add(txtBook);

        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblUser.setBounds(40, 140, 120, 20);
        dialog.add(lblUser);

        JTextField txtUsername = new JTextField();
        txtUsername.setBounds(40, 165, 300, 35);
        dialog.add(txtUsername);

        JLabel lblReturnDate = new JLabel("Return Date");
        lblReturnDate.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblReturnDate.setBounds(40, 210, 120, 20);
        dialog.add(lblReturnDate);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dataSpinner = new JSpinner(dateModel);
        dataSpinner.setEditor(new JSpinner.DateEditor(dataSpinner, "yyyy-MM-dd"));
        dataSpinner.setBounds(40, 235, 300, 35);
        dialog.add(dataSpinner);

        JButton btnConfirm = new JButton("Confirm Borrow");
        btnConfirm.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnConfirm.setBounds(110, 300, 180, 40);

        btnConfirm.addActionListener(e -> {

            int bookId = (int) bookTableModel.getValueAt(selectedRow, 0);
            int available = (int) bookTableModel.getValueAt(selectedRow, 4);

            Date selectedDate = (Date) dataSpinner.getValue();

            LocalDate returnDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (available <= 0) {
                JOptionPane.showMessageDialog(this, "Out of books!");
                return;
            }

            try {
                boolean result = libraryService.borrowBook(bookId, bookName, txtUsername.getText(), returnDate);

                if (result) {
                    JOptionPane.showMessageDialog(this, "Borrow book is successfully!!!\nVui lòng trả sách đúng hạn.");
                    loadBooks();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Borrow book is failed \n You may have already borrowed this book!!!");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error in AdminUI - borrowBook: " + ex.getMessage()
                );
            }
        });

        dialog.add(btnConfirm);
        dialog.setVisible(true);
    }


    private JPanel showBorrowBookManagerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== Toolbar =====
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JTextField txtSearch = new JTextField(25);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(24, 140, 200));
        btnSearch.setFocusPainted(false);
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(155, 89, 182));
        btnRefresh.setFocusPainted(false);

        JButton btnReturn = new JButton("Confirm Return");
        btnReturn.setBackground(new Color(46, 204, 113));
        btnReturn.setFocusPainted(false);

        toolbar.add(txtSearch);
        toolbar.add(btnSearch);
        toolbar.add(btnReturn);
        toolbar.add(btnRefresh);

        // ===== Table =====
        String[] columns = {
                "Borrow ID",
                "Username",
                "Book ID",
                "Book Title",
                "Borrow Date",
                "Return Date",
                "Status"
        };

        DefaultTableModel borrowTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable borrowTable = new JTable(borrowTableModel);
        borrowTable.setRowHeight(25);
        borrowTable.setFont(new Font("Arial", Font.PLAIN, 13));
        borrowTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        borrowTable.getTableHeader().setBackground(new Color(26, 188, 156));
        borrowTable.getTableHeader().setForeground(Color.BLACK);
        borrowTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(borrowTable);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // ===== Load data =====
        loadBorrowBooks(borrowTableModel);

        // ===== Events =====
        btnRefresh.addActionListener(e -> loadBorrowBooks(borrowTableModel));

        btnReturn.addActionListener(e -> confirmReturn(borrowTable, borrowTableModel));

        btnSearch.addActionListener(e -> searchBorrowBook(txtSearch.getText(), borrowTableModel));

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

    // method in book manager ------------------------------------
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

            JLabel lblAvailable = new JLabel("Available");
            lblAvailable.setFont(new Font("Tahoma", Font.BOLD, 15));
            lblAvailable.setBounds(40, 413, 96, 14);
            dialog.add(lblAvailable);

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
                int Id = bookId;
                String bookName = txtName.getText();
                String author = txtAuthor.getText();
                String category = txtCategory.getText();
                int quantity = Integer.parseInt(txtQuantity.getText());
                int available = book.getAvailable();
                try {
                    Books updateBook = new Books(Id, bookName, author, category, quantity, available);

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
        String bookName = (String) bookTableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure :\n" + bookName,
                "Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (libraryService.deleteBook(bookId, bookName, currentUser)) {
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
                    """
                            THỐNG KÊ THƯ VIỆN\s
                             Tổng số sách: %d
                             Sách có sẵn: %d
                             Đang được mượn: %d
                             Tổng người dùng: %d""",
                    totalBooks, availableBooks, borrowBooks, totalUsers);
            JOptionPane.showMessageDialog(this, status, "Thống kê", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error in showStatus: " + e.getMessage());
        }
    }

    // method in user manager ------------------------------------
    private void loadUsers(DefaultTableModel model) {
        try {
            model.setRowCount(0);
            for (Users user : libraryService.getAllUser()) {
                model.addRow(new Object[]{
                        user.getUserName(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getRole()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void searchUser(String key, DefaultTableModel model) {
        if (key.trim().isEmpty()) {
            loadUsers(model);
            return;
        }
        try {
            List<Users> users = libraryService.searchUser(key);
            model.setRowCount(0);
            for (Users user : users) {
                model.addRow(new Object[]{
                        user.getUserName(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getRole()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error in AdminUI -searchUser: " + e.getMessage());
        }
    }

    private void deleteUser(JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn user cần xoá!");
            return;
        }
        String username = model.getValueAt(selectedRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xoá user: " + username + " ?",
                "Xác nhận xoá",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean result = libraryService.deleteUser(username, currentUser.getRole());

                if (result) {
                    JOptionPane.showMessageDialog(this, "Xoá user thành công!");
                    loadUsers(model);
                } else {
                    JOptionPane.showMessageDialog(this, "Xoá user thất bại!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi xoá user: " + e.getMessage());
            }
        }
    }


    // method in borrow book manager ------------------------------------
    private void loadBorrowBooks(DefaultTableModel model) {
        try {
            model.setRowCount(0);
            for (BorrowBooks borrow : libraryService.getAllBorrowBook()) {
                model.addRow(new Object[]{
                        borrow.getId(),
                        borrow.getUsername(),
                        borrow.getBookId(),
                        borrow.getBookTitle(),
                        borrow.getBorrowDate(),
                        borrow.getReturnDate(),
                        borrow.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void searchBorrowBook(String keyword, DefaultTableModel model) {
        if (keyword.trim().isEmpty()) {
            loadBorrowBooks(model);
            return;
        }
        try {
            List<BorrowBooks> borrows = libraryService.searchBorrowBook(keyword);
            model.setRowCount(0);
            for (BorrowBooks borrow : borrows) {
                model.addRow(new Object[]{
                        borrow.getBookId(),
                        borrow.getUsername(),
                        borrow.getBookId(),
                        borrow.getBookTitle(),
                        borrow.getBorrowDate(),
                        borrow.getReturnDate(),
                        borrow.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error in AdminUI -searchBorrowBook: " + e.getMessage());
        }
    }

    private void confirmReturn(JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bản ghi mượn cần xác nhận trả!");
            return;
        }

        int borrowId = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
        String username = model.getValueAt(selectedRow, 1).toString();
        int bookId = Integer.parseInt(model.getValueAt(selectedRow, 2).toString());
        String bookName = model.getValueAt(selectedRow, 3).toString();
        String status = model.getValueAt(selectedRow, 6).toString();

        if (status.equalsIgnoreCase("RETURNED")) {
            JOptionPane.showMessageDialog(this, "Sách này đã được trả rồi!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Xác nhận trả sách cho lượt mượn ID: " + borrowId + " ?",
                "Confirm Return",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean result = libraryService.returnBook(bookId, bookName, username);

                if (result) {
                    JOptionPane.showMessageDialog(this, "Xác nhận trả sách thành công!");
                    loadBorrowBooks(model);
                } else {
                    JOptionPane.showMessageDialog(this, "Xác nhận trả sách thất bại!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi confirm return: " + e.getMessage());
            }
        }
    }
}
