package Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Notify extends Remote{
	void notifyBookReturned(String bookTitle, int bookId) throws RemoteException;

	void notifyBookAdded(String bookTitle) throws RemoteException;

	void notifyMessage(String message) throws RemoteException;
}
