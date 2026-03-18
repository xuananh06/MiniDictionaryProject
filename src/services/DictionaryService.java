package services;

import models.Word;

import repositories.IDictionaryRepository;

public class DictionaryService implements IDictionaryService {

    private IDictionaryRepository repository;

    public DictionaryService(IDictionaryRepository repository) {
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