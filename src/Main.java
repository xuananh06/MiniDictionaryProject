import controllers.AuthController;
import controllers.DictionaryController;
import core.DIManager;
import core.Session;
import repositories.IDictionaryRepository;
import repositories.DictionaryRepository;
import repositories.IUserRepository;
import repositories.UserRepositoryImpl;
import services.AuthService;
import services.DictionaryService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //DI wiring 
        DIManager di = new DIManager();

        // DAL — manual: no dependencies
        di.register(IUserRepository.class, new UserRepositoryImpl());
        di.register(IDictionaryRepository.class, new DictionaryRepository());

        // Services — auto-wire by class
        di.register(AuthService.class);
        di.register(DictionaryService.class);
    

        // Controllers — package scan (or register explicitly, e.g. di.register(AuthController.class))
        di.register("controllers.*");
        
        
        AuthController authController = di.resolve(AuthController.class);
        DictionaryController    dictionaryController    = di.resolve(DictionaryController.class);
       



        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("===== DICTIONARY =====");
            System.out.println("1. Login");
            System.out.println("2. Add word");
            System.out.println("3. Search word");
            System.out.println("4. Logout");
            System.out.println("0. Exit");

            if (Session.isLoggedIn()) {
                System.out.println(">> Logged in as: " + Session.getCurrentUser());
            } else {
                System.out.println(">> Not logged in");
            }

            System.out.print("Your choice:");

            String choice = scanner.nextLine().trim();


            switch (choice) {
                case "1" : 
                {
                    System.out.print("Username: ");
                    String username = scanner.nextLine().trim();
                    System.out.print("Password: ");
                    String password = scanner.nextLine().trim();
                    authController.execute("login", username, password);
                    break;
                }
                
                case "2": dictionaryController.execute("addWord"); break;
                case "3": dictionaryController.execute("searchWord"); break;
                case "4": authController.execute("logout"); break;
                case "0" : running = false; break;
                
                default  : System.out.println("Invalid choice!"); break;    
            }         
        }

        System.out.println("Goodbye!");
        scanner.close();
    }
}