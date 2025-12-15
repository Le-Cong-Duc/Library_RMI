package Notify;

import Interface.Notify;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JTextArea;

public class NotifyClientImplement extends UnicastRemoteObject implements Notify {
	private JTextArea txtNotification;
	
	public NotifyClientImplement(JTextArea txtNotification) throws RemoteException{
		super();
		this.txtNotification = txtNotification;
	}

	@Override
	public void notifyBookReturned(String bookTitle, int bookId) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyBookAdded(String bookTitle) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyMessage(String message) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
