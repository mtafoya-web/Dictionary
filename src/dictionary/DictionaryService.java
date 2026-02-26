package dictionary;

import java.util.*;

public class DictionaryService {
    private final Map<String, dictionaryEntry> Dictionary = new HashMap<>();
    private final Map<String, Integer> frequency = new HashMap<>();
    /**
     * Adds or Updates an Entry
     * @param entry : The dictionary we want to edit
     */
    public void addOrUpdate(dictionaryEntry entry){
        String word = entry.getWord().toLowerCase();
        Dictionary.put(word, entry);
    }
    /**
     *
     * @param word
     * @return null or he lower case word
     */
    public Optional<dictionaryEntry> find(String word){
        if (word == null) return Optional.empty();
        String key = word.toLowerCase();

        dictionaryEntry entry = Dictionary.get(key);
        if (entry != null) {
            frequency.put(key, frequency.getOrDefault(key, 0) + 1);
        }

        return Optional.ofNullable(entry);
    }
    /**
     *
     * @param word
     * @return boolean: False if null, True otherwise
     */
    public boolean delete(String word){
        if (word == null) return false;
        String key = word.toLowerCase();
        dictionaryEntry removed = Dictionary.remove(key);
        frequency.remove(key);
        return removed != null;
    }
    /**
     *
     * @return List: a list of the sorted words
     */
    public List<String> sort(){
        List<String> words = new ArrayList<>(Dictionary.keySet());
        Collections.sort(words);
        return words;
    }
    /**
     *
     * @param prefix
     * @return list: Contains the words with the input prefix
     */
    public List<String> searchPrefix(String prefix){
        String lower = prefix.toLowerCase();
        List<String> results = new ArrayList<>();
        for(String word : Dictionary.keySet()){
            if(word.startsWith(lower)){
                results.add(word);
            }
        }
        Collections.sort(results);
        return results;
    }
    /**
     *
     * @return count: number of items in the dictionary
     */
    public int count(){
        return Dictionary.size();
    }
    public List<String> topSearched(int n) {
        return frequency.entrySet().stream()
                .sorted((a, b) -> {
                    int cmp = Integer.compare(b.getValue(), a.getValue()); // desc count
                    if (cmp != 0) return cmp;
                    return a.getKey().compareTo(b.getKey());
                })
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }
    public void clearFrequency() {
        frequency.clear();

    }
    public List<dictionaryEntry> getAllEntriesSorted() {
        List<String> words = sort(); // your existing sorted word list (keys)
        List<dictionaryEntry> entries = new ArrayList<>();

        for (String w : words) {
            dictionaryEntry e = Dictionary.get(w); // keys already lowercase
            if (e != null) entries.add(e);
        }
        return entries;
    }

    public void saveToFile(java.nio.file.Path path) throws java.io.IOException {
        try (java.io.BufferedWriter writer =
                     java.nio.file.Files.newBufferedWriter(path, java.nio.charset.StandardCharsets.UTF_8)) {

            for (dictionaryEntry entry : getAllEntriesSorted()) {

                String word = safe(entry.getWord());
                String pron = safe(entry.getPronounce());
                String def  = safe(entry.getDefinition());
                String ex   = safe(entry.getExample());
                String syns = String.join(", ", entry.getSyn()).replace("|", "/");

                writer.write(word + "|" + pron + "|" + def + "|" + ex + "|" + syns);
                writer.newLine();
            }
        }
    }

    public void loadFromFile(java.nio.file.Path path) throws java.io.IOException {

        if (!java.nio.file.Files.exists(path)) return;

        java.util.List<String> lines =
                java.nio.file.Files.readAllLines(path, java.nio.charset.StandardCharsets.UTF_8);

        for (String line : lines) {

            if (line == null || line.isBlank()) continue;

            String[] parts = line.split("\\|", -1);
            if (parts.length < 5) continue;

            String word = parts[0].trim();
            String pron = parts[1].trim();
            String def  = parts[2].trim();
            String ex   = parts[3].trim();
            java.util.List<String> syns =
                    java.util.Arrays.asList(parts[4].split("\\s*,\\s*"));

            addOrUpdate(new dictionaryEntry(word, pron, def, ex, syns));
        }
    }

    private String safe(String s) {
        if (s == null) return "";

        return s.replace("\r", " ")
                .replace("\n", " ")
                .replace("|", "/")
                .trim();
    }
}
