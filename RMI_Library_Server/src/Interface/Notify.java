package Interface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Notify  extends Remote{
	public void notifyLogin(String username) throws RemoteException;
	public void notifyUserAdded(String username, String role) throws RemoteException;
	public void notifyUserDelete(String username, String role) throws RemoteException;
	public void notifyUserUpdated(String username, String role) throws RemoteException;
	public void notifyBookBorrowed(String bookTitle, int bookId) throws RemoteException;
	public void notifyBookReturned(String bookTitle, int bookId) throws RemoteException;
	public void notifyBookAdded(String bookTitle) throws RemoteException;
	public void notifyBookDeleted(String bookTitle) throws RemoteException;
	public void notifyBookEdit(String bookTitle) throws RemoteException;
	public void notifyMessage(String message) throws RemoteException;
	void onServerShutdown() throws RemoteException;
}
