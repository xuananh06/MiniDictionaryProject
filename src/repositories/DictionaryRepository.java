package repositories;

import models.Word;

import java.io.*;

public class DictionaryRepository implements IDictionaryRepository {

    private final String FILE_PATH = "data/dictionary.txt";

    @Override
    public void addWord(Word word) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {

            bw.write(word.getWord() + "|" + word.getMeaning());
            bw.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String findWord(String inputWord) {

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] parts = line.split("\\|");

                String word = parts[0];
                String meaning = parts[1];

                if (word.equalsIgnoreCase(inputWord)) {
                    return meaning;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}