import Interface.LibraryService;
import Service.LibraryServiceImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainServer {
    public static void main(String[] args) {
        int PORT = 2912;
        try {
            Registry registry = LocateRegistry.createRegistry(PORT);
            LibraryService libraryService = new LibraryServiceImpl();
            registry.rebind("Interface.LibraryService", libraryService);
            System.out.println("Server is running in port : " + PORT);
        } catch (Exception e) {
            System.out.println("Lỗi server: " + e.getMessage());
        }
    }
}
