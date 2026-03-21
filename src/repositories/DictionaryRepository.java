package repositories;

import models.Word;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class DictionaryRepository implements IDictionaryRepository {

    private final String FILE_PATH = "data/dictionary.txt";
    


    @Override
    public void addWord(Word word) {

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(FILE_PATH, true), StandardCharsets.UTF_8))) {

            bw.write(word.getWord() + "|" + word.getMeaning());
            bw.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String findWord(String inputWord) {

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(FILE_PATH), StandardCharsets.UTF_8))) {

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

    @Override
    public List<String> suggestWords(String keyword) {
        // Return unique words that contain the keyword (case-insensitive).
        List<String> suggestions = new ArrayList<>();
        String normalizedKeyword = keyword == null ? "" : keyword.trim().toLowerCase();
        if (normalizedKeyword.isEmpty()) {
            return suggestions;
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(FILE_PATH), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 2) {
                    continue;
                }

                String word = parts[0].trim();
                if (word.toLowerCase().contains(normalizedKeyword) && !suggestions.contains(word)) {
                    suggestions.add(word);
                }
            }
        } catch (IOException e) {
            System.out.println("[DictionaryRepository] Failed to suggest words: " + e.getMessage());
        }

        return suggestions;
    }

    @Override
    public void deleteWord(String word) {
        File inputFile = new File(FILE_PATH);
        if (!inputFile.exists()) {
            System.out.println("[DictionaryRepository] Cannot delete: file not found: " + FILE_PATH);
            return;
        }

        String normalizedInput = word == null ? "" : word.trim();
        if (normalizedInput.isEmpty()) {
            System.out.println("[DictionaryRepository] Cannot delete: empty word.");
            return;
        }

        // Delete by rewriting the file to a temp file, skipping matching records.
        String tempPath = FILE_PATH + ".tmp-" + System.nanoTime();
        File tempFile = new File(tempPath);

        int removed = 0;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8));
             BufferedWriter bw = new BufferedWriter(
                     new OutputStreamWriter(new FileOutputStream(tempFile), StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    bw.newLine();
                    continue;
                }

                String[] parts = trimmed.split("\\|");
                if (parts.length >= 2) {
                    String currentWord = parts[0].trim();
                    if (currentWord.equalsIgnoreCase(normalizedInput)) {
                        removed++;
                        continue; // skip this record
                    }
                }

                // Keep original line if it's malformed or not matched.
                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("[DictionaryRepository] Failed to delete word: " + e.getMessage());
            return;
        }

        // Replace original file with temp file
        if (!inputFile.delete()) {
            System.out.println("[DictionaryRepository] Failed to delete original file.");
            return;
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("[DictionaryRepository] Failed to rename temp file.");
        }

        if (removed == 0) {
            System.out.println("Word not found (nothing deleted).");
        } else {
            System.out.println("Deleted " + removed + " record(s).");
        }
    }
}