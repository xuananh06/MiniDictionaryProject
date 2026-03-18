package repositories;

import models.Word;

public interface IDictionaryRepository {

    // add word to  dictionary 
    void addWord(Word word);

    //  find word, return null if not found 
    String findWord(String inputWord);
}