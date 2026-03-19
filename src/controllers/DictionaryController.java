package controllers;

import annotations.AuthN;
import annotations.AuthZ;
import core.BaseController;
import services.IDictionaryService;

import java.util.Scanner;

public class DictionaryController extends BaseController {

    Scanner scanner = new Scanner(System.in);

    private final IDictionaryService dictionaryService;

    public DictionaryController(IDictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @AuthN
    @AuthZ(roles = "admin|user")
    public void addWord() {
        System.out.print("Enter word: ");
        String word = scanner.nextLine();
        System.out.print("Enter meaning: ");
        String meaning = scanner.nextLine();
        dictionaryService.addWord(word, meaning);
        System.out.println("Word added successfully!");
    }

    @AuthN
    @AuthZ(roles = "admin|user")
    public void searchWord() {
        System.out.print("Enter word: ");
        String word = scanner.nextLine();

        String meaning = dictionaryService.searchWord(word);

        if (meaning != null) {
            System.out.println("Meaning: " + meaning);
        } else {
            System.out.println("Word not found.");
        }
    }

    @AuthN
    @AuthZ(roles = "admin")
    public void deleteWord() {
        
    }

}