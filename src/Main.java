import controllers.AuthController;
import controllers.DictionaryController;
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

        // AUTH
        IUserRepository userRepo = new UserRepositoryImpl();
        AuthService authService = new AuthService(userRepo);
        AuthController authController = new AuthController(authService);

        // DICTIONARY
        IDictionaryRepository dictionaryRepo = new DictionaryRepository();
        DictionaryService dictionaryService = new DictionaryService(dictionaryRepo);
        DictionaryController dictionaryController = new DictionaryController(dictionaryService);



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
                    authController.login(username, password);
                    break;
                }

                case "2":
                    {
                    System.out.print("Enter word: ");
                    String word = scanner.nextLine();
                    System.out.print("Enter meaning: ");
                    String meaning = scanner.nextLine();
                    dictionaryController.addWord(word, meaning);
                    break;
                }
                case "3":
                    {
                    System.out.print("Enter word: ");
                    String search = scanner.nextLine();
                    dictionaryController.searchWord(search);
                    break;
                }

                case "4": authController.logout(); break;

                case "0" : running = false; break;
                
                default  : System.out.println("Invalid choice!"); break;    
            }         
        }

        System.out.println("Goodbye!");
        scanner.close();
    }
}