package services;

public interface IDictionaryService {

    
    void addWord(String word, String meaning);

    String searchWord(String word);
    
    void deleteWord(String word);
}
