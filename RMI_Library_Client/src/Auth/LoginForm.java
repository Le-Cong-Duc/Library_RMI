package Auth;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.*;

import Notify.NotifyClientImplement;
import Admin.AdminUI;
import Interface.LibraryService;
import Interface.Notify;
import Model.Users;
import User.UserUI;

public class LoginForm extends JFrame {

    private static final long serialVersionUID = 1L;
    JDialog loginDialog;
    JDialog registerDialog;
    private JLabel lblUsername;
    private JTextField txtUsername;
    private JLabel lblPassword;
    private JTextField txtPassword_2;
    private JPasswordField txtPassword;
    private JTextField txtEmail;
    private JTextField txtFullname;

    private LibraryService libraryService;
    private Users currentUser;
    private Notify callBack;

    public LoginForm() {
        try {
            String SERVER_IP = "192.168.63.115";
            int SERVER_PORT = 2912;

            Registry registry = LocateRegistry.getRegistry(SERVER_IP, SERVER_PORT);
            libraryService = (LibraryService) registry.lookup("LibraryService");

            System.out.println(" Connected to RMI Server");

            showLogin();
        } catch (Exception e) {
            System.out.println("Error in connect to server!!!");
            JOptionPane.showMessageDialog(this, "Can not connect to server !!!", "Error in connect to server",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showLogin() {
        loginDialog = new JDialog(this, "login", true);
        loginDialog.setTitle("Login Form");
        loginDialog.setSize(450, 450);
        loginDialog.setLocationRelativeTo(null);
        loginDialog.setLayout(null);

        JLabel lblTitle = new JLabel("Login");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(104, 45, 220, 53);
        loginDialog.add(lblTitle);

        txtUsername = new JTextField();
        txtUsername.setBounds(65, 133, 331, 46);
        txtUsername.setFont(new Font("Tahoma", Font.BOLD, 12));
        loginDialog.add(txtUsername);
        txtUsername.setColumns(10);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(65, 224, 331, 46);
        txtPassword.setFont(new Font("Tahoma", Font.BOLD, 12));
        loginDialog.add(txtPassword);

        lblUsername = new JLabel("Username :");
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblUsername.setBounds(65, 96, 103, 27);
        loginDialog.add(lblUsername);

        lblPassword = new JLabel("Password :");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPassword.setBounds(65, 190, 103, 27);
        loginDialog.add(lblPassword);

        JButton btnLogin = new JButton("Login");
        btnLogin.setForeground(new Color(0, 0, 255));
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnLogin.setBounds(65, 307, 186, 46);

        btnLogin.addActionListener(e -> login());
        loginDialog.add(btnLogin);

        JButton btnRegister = new JButton("Register");
        btnRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnRegister.setBounds(286, 307, 110, 46);

        btnRegister.addActionListener(e -> {
            loginDialog.setVisible(false);
            showRegister();
        });
        loginDialog.add(btnRegister);

        loginDialog.setVisible(true);
    }

    public void showRegister() {
        registerDialog = new JDialog(this, "register ", true);
        registerDialog.setTitle("Register Form");
        registerDialog.setSize(480, 700);
        registerDialog.setLocationRelativeTo(null);
        registerDialog.setLayout(null);

        JLabel lblTitle = new JLabel("Login");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(104, 45, 220, 53);
        registerDialog.add(lblTitle);

        txtUsername = new JTextField();
        txtUsername.setBounds(65, 133, 331, 46);
        registerDialog.add(txtUsername);
        txtUsername.setColumns(10);

        lblUsername = new JLabel("Username :");
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblUsername.setBounds(65, 96, 103, 27);
        registerDialog.add(lblUsername);

        lblPassword = new JLabel("Password :");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPassword.setBounds(65, 190, 103, 27);
        registerDialog.add(lblPassword);

        txtPassword_2 = new JTextField();
        txtPassword_2.setColumns(10);
        txtPassword_2.setBounds(65, 228, 331, 46);
        registerDialog.add(txtPassword_2);

        JLabel lblFullname = new JLabel("Fullname :");
        lblFullname.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblFullname.setBounds(65, 305, 103, 27);
        registerDialog.add(lblFullname);

        txtFullname = new JTextField();
        txtFullname.setColumns(10);
        txtFullname.setBounds(65, 343, 331, 46);
        registerDialog.add(txtFullname);

        JLabel lblEmail = new JLabel("Email :");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblEmail.setBounds(65, 412, 103, 27);
        registerDialog.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setColumns(10);
        txtEmail.setBounds(65, 450, 331, 46);
        registerDialog.add(txtEmail);

        JButton btnRegister = new JButton("Register");
        btnRegister.setForeground(new Color(0, 0, 255));
        btnRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnRegister.setBounds(65, 546, 186, 46);
        btnRegister.addActionListener(e -> register());
        registerDialog.add(btnRegister);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnLogin.setBounds(286, 546, 110, 46);
        btnLogin.addActionListener(e -> {
            registerDialog.setVisible(false);
            showLogin();
        });
        registerDialog.add(btnLogin);

        registerDialog.setVisible(true);
    }

    public void setUpRole() {
        setTitle("Library System - " + currentUser.getFullName() + " (" + currentUser.getRole() + ")");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            JTextArea notifycationArea = new JTextArea(5, 50);
            callBack = new NotifyClientImplement(notifycationArea);
            libraryService.registerCallback(currentUser.getUserName(), callBack);

            if ("ADMIN".equals(currentUser.getRole())) {
                setContentPane(new AdminUI(libraryService, currentUser, notifycationArea));
            } else {
                setContentPane(new UserUI(libraryService, currentUser, notifycationArea));
            }
        } catch (Exception e) {
            System.out.println("Error in setUpRole: " + e.getMessage());
        }

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    if (callBack != null) {
                        libraryService.unregisterCallback(currentUser.getUserName());
                    }
                } catch (Exception e2) {
                    System.out.println("Error in unregister callback!!!");
                }
            }
        });
    }

    public void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginDialog, "username or password is empty!!!");
            return;
        }

        try {
            currentUser = libraryService.login(username, password);
            if (currentUser != null) {
                loginDialog.dispose();
                setUpRole();
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Wrong username or password !!!");
            }
        } catch (Exception e) {
            System.out.println("Error in method login" + e.getMessage());
        }
    }

    public void register() {
        String username = txtUsername.getText().trim();
        String password = txtPassword_2.getText().trim();
        String fullname = txtFullname.getText().trim();
        String email = txtEmail.getText().trim();
        String role = "USER";

        try {
            Users user = new Users(username, password, fullname, email, role);

            if (libraryService.register(user)) {
                JOptionPane.showMessageDialog(registerDialog, "Register Succesfully !!!");
                registerDialog.dispose();
                loginDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(registerDialog, "Username is exists!");
            }
        } catch (Exception e) {
            System.out.println("Error in method register!!!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginForm().setVisible(true);
        });
    }
}
