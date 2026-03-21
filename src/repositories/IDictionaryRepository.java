package repositories;

import models.Word;
import java.util.List;

public interface IDictionaryRepository {

    // add word to  dictionary 
    void addWord(Word word);

    //  find word, return null if not found 
    String findWord(String inputWord);

    // suggest words by partial keyword
    List<String> suggestWords(String keyword);

    // delete word from dictionary
    void deleteWord(String word);
}