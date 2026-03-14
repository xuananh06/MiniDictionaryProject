import controllers.DictionaryController;
import repositories.DictionaryRepository;
import services.DictionaryService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        DictionaryRepository repository = new DictionaryRepository();
        DictionaryService service = new DictionaryService(repository);
        DictionaryController controller = new DictionaryController(service);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("===== DICTIONARY =====");
            System.out.println("1. Add word");
            System.out.println("2. Search word");
            System.out.println("3. Exit");
            System.out.print("Your choice:");

            String choice = scanner.nextLine().trim();


            switch (choice) {

                case "1":
                    {
                    System.out.print("Enter word: ");
                    String word = scanner.nextLine();
                    System.out.print("Enter meaning: ");
                    String meaning = scanner.nextLine();
                    controller.addWord(word, meaning);
                    break;
                }
                case "2":
                    {
                    System.out.print("Enter word: ");
                    String search = scanner.nextLine();
                    controller.searchWord(search);
                    break;
                }
                case "3" : running = false; break;
                default  : System.out.println("Invalid choice!"); break;    
            }         
        }

        System.out.println("Goodbye!");
        scanner.close();
    }
}