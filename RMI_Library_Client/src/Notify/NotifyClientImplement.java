package Notify;

import java.rmi.RemoteException;

import javax.swing.JTextArea;

import Interface.NotifyClient;

public class NotifyClientImplement implements NotifyClient{
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
