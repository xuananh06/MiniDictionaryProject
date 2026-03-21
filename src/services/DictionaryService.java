package services;

import models.Word;
import repositories.IDictionaryRepository;
import java.util.List;

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

    @Override
    public List<String> suggestWords(String keyword) {
        return repository.suggestWords(keyword);
    }

    @Override
    public void deleteWord(String word) {
        repository.deleteWord(word);
    }
}