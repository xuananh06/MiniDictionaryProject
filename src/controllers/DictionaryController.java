package controllers;

import annotations.AuthN;
import annotations.AuthZ;
import core.BaseController;
import services.IDictionaryService;
import java.util.List;
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
        String word = pickSuggestedWord("Enter keyword to search: ");
        if (word == null) {
            return;
        }

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
        String wordToDelete = pickSuggestedWord("Enter keyword to delete: ");
        if (wordToDelete == null) {
            return;
        }
        dictionaryService.deleteWord(wordToDelete);
    }

    // Flow: input keyword -> fetch suggestions -> choose index -> return selected word.
    private String pickSuggestedWord(String prompt) {
        System.out.print(prompt);
        String keyword = scanner.nextLine();
        if (keyword == null) {
            System.out.println("Invalid input.");
            return null;
        }

        keyword = keyword.trim();
        if (keyword.isEmpty()) {
            System.out.println("Keyword cannot be empty.");
            return null;
        }

        List<String> suggestions = dictionaryService.suggestWords(keyword);
        if (suggestions.isEmpty()) {
            System.out.println("No suggestions found.");
            return null;
        }

        int limit = Math.min(suggestions.size(), 10);
        System.out.println("Suggestions:");
        for (int i = 0; i < limit; i++) {
            System.out.println((i + 1) + ". " + suggestions.get(i));
        }

        System.out.print("Choose number (1-" + limit + "): ");
        String chooseRaw = scanner.nextLine();
        int chosen;
        try {
            chosen = Integer.parseInt(chooseRaw.trim());
        } catch (Exception e) {
            System.out.println("Invalid choice.");
            return null;
        }

        if (chosen < 1 || chosen > limit) {
            System.out.println("Choice out of range.");
            return null;
        }

        return suggestions.get(chosen - 1);
    }
}