import Interface.LibraryService;
import Service.LibraryServiceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainServer {
    public static void main(String[] args) throws RemoteException {
        LibraryServiceImpl libraryService = null;
        int PORT = 2912;
        try {
            Registry registry = LocateRegistry.createRegistry(PORT);
            libraryService = new LibraryServiceImpl();
            registry.rebind("Interface.LibraryService", libraryService);
            System.out.println("Server is running in port : " + PORT);
        } catch (Exception e) {
            System.out.println("Lỗi server: " + e.getMessage());
        }

        LibraryServiceImpl finalService = libraryService;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (finalService != null) {
                finalService.shutdownServer();
            }
            System.out.println("Shutdown hook executed");
        }));
    }
}
