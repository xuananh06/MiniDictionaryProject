package services;

import models.word;
import repositories.DictionaryRepository;

public class DictionaryService {

    private DictionaryRepository repository;

    public DictionaryService(DictionaryRepository repository) {
        this.repository = repository;
    }

    public void addWord(String word, String meaning) {

        word newWord = new word(word, meaning);
        repository.addWord(newWord);
    }

    public String searchWord(String word) {

        return repository.findWord(word);
    }
}