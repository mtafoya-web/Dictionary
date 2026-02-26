package dictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model class representing a dictionary entry.
 * Stores all metadata for a word:
 * - word text
 * - pronunciation
 * - definition
 * - example usage
 * - up to 4 synonyms
 * This class is immutable (all fields are final) and defensively copies
 * mutable data (synonym list) to protect internal state.
 */
public class dictionaryEntry {

    /** The dictionary word (as entered) */
    private final String word;

    /** Pronunciation text for the word */
    private final String pronounce;

    /** Definition of the word */
    private final String definition;

    /** Example usage sentence */
    private final String example;

    /** List of synonyms (maximum of 4) */
    private final List<String> syn; // max 4

    /**
     * Constructs a dictionary entry.
     *
     * @param w   the word text
     * @param pro pronunciation string
     * @param def definition string
     * @param ex  example usage string
     * @param syn list of synonyms (will be sanitized and limited to 4)
     */
    public dictionaryEntry(String w, String pro, String def, String ex, List<String> syn) {
        this.word = w;
        this.pronounce = pro;
        this.definition = def;
        this.example = ex;
        this.syn = normalizeSynonyms(syn);
    }

    /**
     * Normalizes the synonym list by:
     * - handling null lists safely
     * - trimming whitespace
     * - removing blank/null entries
     * - limiting the list to a maximum of 4 items
     *
     * @param words raw synonym list
     * @return sanitized list of synonyms (size <= 4)
     */
    private List<String> normalizeSynonyms(List<String> words) {
        if (words == null) return Collections.emptyList();

        List<String> list = new ArrayList<>(4);
        for (String s : words) {
            if (s == null) continue;

            String trimmed = s.trim();
            if (trimmed.isEmpty()) continue;

            if (list.size() == 4) break;
            list.add(trimmed);
        }
        return list;
    }

    /**
     * Returns the word text.
     *
     * @return word
     */
    public String getWord() { return word; }

    /**
     * Returns the pronunciation string.
     *
     * @return pronunciation
     */
    public String getPronounce() { return pronounce; }

    /**
     * Returns the definition string.
     *
     * @return definition
     */
    public String getDefinition() { return definition; }

    /**
     * Returns the example usage string.
     *
     * @return example sentence
     */
    public String getExample() { return example; }

    /**
     * Returns a defensive copy of the synonym list to protect immutability.
     *
     * @return copy of synonyms (max size 4)
     */
    public List<String> getSyn() { return new ArrayList<>(syn); }
}