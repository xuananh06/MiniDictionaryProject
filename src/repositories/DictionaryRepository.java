package repositories;

import models.word;

import java.io.*;

public class DictionaryRepository {

    private final String FILE_PATH = "data/dictionary.txt";

    public void addWord(word word) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {

            bw.write(word.getWord() + "|" + word.getMeaning());
            bw.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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