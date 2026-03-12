import controllers.DictionaryController;
import repositories.DictionaryRepository;
import services.DictionaryService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        DictionaryRepository repository = new DictionaryRepository();
        DictionaryService service = new DictionaryService(repository);
        DictionaryController controller = new DictionaryController(service);

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("===== DICTIONARY =====");
            System.out.println("1. Add word");
            System.out.println("2. Search word");
            System.out.println("3. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter word: ");
                    String word = sc.nextLine();

                    System.out.print("Enter meaning: ");
                    String meaning = sc.nextLine();

                    controller.addWord(word, meaning);
                    break;

                case 2:

                    System.out.print("Enter word: ");
                    String search = sc.nextLine();

                    controller.searchWord(search);
                    break;

                case 3:
                    return;
            }
        }
    }
}