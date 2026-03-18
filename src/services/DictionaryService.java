package services;

import models.Word;
import repositories.DictionaryRepository;

public class DictionaryService implements IDictionaryService {

    private DictionaryRepository repository;

    public DictionaryService(DictionaryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addWord(String word, String meaning) {

        Word newWord = new Word(word, meaning);
        repository.addWord(newWord);
    }

    @Override
    public String searchWord(String word) {

        return repository.findWord(word);
    }
}