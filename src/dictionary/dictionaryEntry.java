package dictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class dictionaryEntry {
    private final String word;
    private final String pronounce;
    private final String definition;
    private final String example;
    private final List<String> syn; // max 4

    public dictionaryEntry(String w, String pro, String def, String ex, List<String> syn) {
        this.word = w;
        this.pronounce = pro;
        this.definition = def;
        this.example = ex;
        this.syn = normalizeSynonyms(syn);
    }

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

    public String getWord() { return word; }
    public String getPronounce() { return pronounce; }
    public String getDefinition() { return definition; }
    public String getExample() { return example; }
    public List<String> getSyn() { return new ArrayList<>(syn); }
}