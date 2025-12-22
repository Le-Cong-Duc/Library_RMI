import Interface.LibraryService;
import Service.LibraryServiceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainServer {
    public static void main(String[] args) throws RemoteException {
        LibraryServiceImpl libraryService = null;
        String SERVER_IP = "172.26.21.242";
        int PORT = 2912;
        try {
            System.setProperty("java.rmi.server.hostname", SERVER_IP);
            Registry registry = LocateRegistry.createRegistry(PORT);
            libraryService = new LibraryServiceImpl();
            registry.rebind("LibraryService", libraryService);
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
