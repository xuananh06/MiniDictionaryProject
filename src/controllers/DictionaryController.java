package controllers;

import services.DictionaryService;

public class DictionaryController {

    private DictionaryService service;

    public DictionaryController(DictionaryService service) {
        this.service = service;
    }

    public void addWord(String word, String meaning) {

        service.addWord(word, meaning);
        System.out.println("Word added successfully!");
    }

    public void searchWord(String word) {

        String meaning = service.searchWord(word);

        if (meaning != null) {
            System.out.println("Meaning: " + meaning);
        } else {
            System.out.println("Word not found.");
        }
    }
}