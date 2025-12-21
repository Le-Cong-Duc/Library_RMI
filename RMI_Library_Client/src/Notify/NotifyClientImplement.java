package Notify;

import Interface.Notify;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.*;

public class NotifyClientImplement extends UnicastRemoteObject implements Notify {
    private JTextArea txtNotification;

    public void notifyUserAdded(String username, String role) throws RemoteException {
        if (role.equals("ADMIN")) {
            SwingUtilities.invokeLater(() -> {
                txtNotification.append(" User : '" + username + "đã được theem!\n");
            });
        }
        System.out.println("Notification: User is added - " + username);
    }

    public void notifyUserDelete(String username, String role) throws RemoteException{
        if (role.equals("ADMIN")) {
            SwingUtilities.invokeLater(() -> {
                txtNotification.append(" User : '" + username + "is deleted!\n");
            });
        }
        System.out.println("Notification: User is deleted - " + username);
    }
    public void notifyUserUpdated(String username, String role) throws RemoteException{
        if (role.equals("ADMIN")) {
            SwingUtilities.invokeLater(() -> {
                txtNotification.append(" User : '" + username + "is edited!\n");
            });
        }
        System.out.println("Notification: User is edited - " + username);
    }

    public NotifyClientImplement(JTextArea txtNotification) throws RemoteException {
        super();
        this.txtNotification = txtNotification;
    }

    @Override
    public void notifyBookBorrowed(String bookTitle, int bookId) throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            txtNotification.append(" Sách '" + bookTitle + "' (ID: " + bookId + ")  đã được mượn!\n");
        });
        System.out.println("Notification: Book is borrowed - " + bookTitle);
    }

    @Override
    public void notifyBookReturned(String bookTitle, int bookId) throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            txtNotification.append(" Sách '" + bookTitle + "' (ID: " + bookId + ") đã được trả!\n");
        });
        System.out.println("Notification: Book returned - " + bookTitle);
    }

    @Override
    public void notifyBookAdded(String bookTitle) throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            txtNotification.append("New Book: '" + bookTitle + "' is added!\n");
        });
        System.out.println("Notification: New book added - " + bookTitle);
    }

    public void notifyBookDeleted(String bookTitle) throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            txtNotification.append(bookTitle + "' is deleted !!!\n");
        });
        System.out.println("Notification: " + bookTitle + " is deleted!");
    }

    public void notifyBookEdit(String bookTitle) throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            txtNotification.append(bookTitle + "' is edited!\n");
        });
        System.out.println("Notification: " + bookTitle + " is edited!");
    }

    @Override
    public void notifyMessage(String message) throws RemoteException {
        SwingUtilities.invokeLater(() -> {
            txtNotification.append(message + "\n");
        });
        System.out.println("Notification: " + message);
    }

}
