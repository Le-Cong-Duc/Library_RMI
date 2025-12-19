package Notify;

import Interface.Notify;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.*;

public class NotifyClientImplement extends UnicastRemoteObject implements Notify {
	private JTextArea txtNotification;
	
	public NotifyClientImplement(JTextArea txtNotification) throws RemoteException{
		super();
		this.txtNotification = txtNotification;
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
	@Override
	public void notifyMessage(String message) throws RemoteException {
		SwingUtilities.invokeLater(() -> {
			txtNotification.append(message + "\n");
		});
		System.out.println("Notification: " + message);
	}

}
