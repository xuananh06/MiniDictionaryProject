package services;

import java.util.List;

public interface IDictionaryService {

    
    void addWord(String word, String meaning);

    String searchWord(String word);

    List<String> suggestWords(String keyword);
    
    void deleteWord(String word);
}
