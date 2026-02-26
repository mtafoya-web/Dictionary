package dictionary;

import javax.swing.*;
import java.util.List;
import java.lang.String;

public class DictionaryController {
    private final DictionaryPanel view;
    private final DictionaryService service;

    public DictionaryController(DictionaryPanel view, DictionaryService service){
        this.view = view;
        this.service = service;
        wireEvents();
        refreshWordList();
        refreshTop5();
        view.setEditing(false);
    }

    public void wireEvents() {
        view.onFind(e ->handleFind());
        view.onAdd(e -> handleAdd());
        view.onDelete(e ->handleRemove());
        view.onExit(e -> handleExit());

        view.onEdit(e -> handleEdit());
        view.onAudio(e ->handleAudio());
        view.onClear(e -> handleClear());
        view.onSave(e->handleSave());

        view.onFreq1(e -> handleTopButton(0));
        view.onFreq2(e -> handleTopButton(1));
        view.onFreq3(e -> handleTopButton(2));
        view.onFreq4(e -> handleTopButton(3));
        view.onFreq5(e -> handleTopButton(4));
        view.onClearHist(e -> handleClearHistory());

        view.onImport(e->handleImport());
        view.onExport(e->handleExport());


        view.onWordSelection(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = view.getSelectedWord();
                if (selected != null) {
                    service.find(selected).ifPresent(entry -> {
                        view.setDetails(entry);
                        refreshTop5();
                    });
                }
            }
        });
        view.onFilterChanged(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });
    }
    private void refreshWordList() {
        List<String> words = service.sort();
        view.setSearchWordList(words);
        view.setTotalCount(words.size());
    }
    private void refreshTop5() {
        view.setTop5Buttons(service.topSearched(5));
    }
    private void handleFind() {
        String word = view.getSearchWord().trim();
        if (word.isEmpty()) return;

        service.find(word).ifPresentOrElse(
                entry -> {
                    view.setDetails(entry);
                    view.selectWordInList(entry.getWord());
                    refreshTop5();
                },
                () -> view.showError("Word not found: " + word)
        );
    }
    private void handleAdd(){
        view.clearDetails();
        view.setEditing(true);
    }
    private void handleRemove(){
        String word = view.getSelectedWord();
        if (word == null || word.isBlank()) {
            word = view.getSearchWord().trim();
        }
        if (word.isBlank()) {
            view.showError("Select or type a word to delete.");
            return;
        }

        if (!view.confirm("Delete '" + word + "'?")) return;

        boolean removed = service.delete(word);
        if (!removed) {
            view.showError("Word not found: " + word);
            return;
        }

        refreshWordList();
        view.clearDetails();
    }
    private void handleExit(){
        if(view.confirmExit()) {
            view.closeWindow();
        }
    }
    private void handleAudio(){
    }
    private void handleClear(){
        view.clearDetails();
    }
    private void handleSave(){
        dictionaryEntry entry = view.getDetailsFromFields();

        if (entry.getWord() == null || entry.getWord().isBlank()) {
            view.showError("Word cannot be empty.");
            return;
        }

        service.addOrUpdate(entry);
        refreshWordList();
        view.setEditing(false);
        view.selectWordInList(entry.getWord());
    }
    private void handleEdit(){
        if (view.getSelectedWord() == null && view.getSearchWord().isBlank()) {
            view.showError("Select a word first.");
            return;
        }
        view.setEditing(true);
    }
    private void filter() {
        view.setSearchWordList(service.searchPrefix(view.getFilterText().trim()));
    }
    private void handleTopButton(int index) {
        String word = view.getTopWordFromButton(index);
        if (word == null) return;

        service.find(word).ifPresentOrElse(
                entry -> {
                    view.setDetails(entry);
                    view.selectWordInList(entry.getWord());
                    refreshTop5();
                },
                () -> view.showError("Word not found: " + word)
        );
    }
    private void handleClearHistory() {
        service.clearFrequency();
        refreshTop5();
    }
    private void handleImport() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(view.getRoot());

        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File file = chooser.getSelectedFile();
            importFromFile(file);
            view.clearFilter();
            refreshWordList();
            refreshTop5();
        }
    }
    private void importFromFile(java.io.File file) {
        try {
            String content = java.nio.file.Files.readString(
                    file.toPath(),
                    java.nio.charset.StandardCharsets.UTF_8
            );

            // Normalize whitespace/newlines into spaces so wrapped text doesn't break records
            content = content.replace("\r", "\n");

            StringBuilder record = new StringBuilder();

            for (String chunk : content.split("\\R")) {
                String line = chunk.trim();
                if (line.isEmpty()) continue;

                // keep appending chunks (wrapped lines)
                if (record.length() > 0) record.append(" ");
                record.append(line);

                // keep extracting records as long as we can find 5 fields
                while (countPipes(record) >= 4) {
                    String one = record.toString();

                    // Split into 5 parts max (word, pron, def, example, syns+rest)
                    String[] parts = one.split("\\|", 5);
                    if (parts.length < 5) break;

                    String word = parts[0].trim();
                    String pron = parts[1].trim();
                    String def  = parts[2].trim();
                    String exAndRest = parts[3].trim();
                    String synAndRest = parts[4].trim();

                    // The tricky part: synAndRest contains: "syns banana|..."
                    // We detect start of next record by finding " <nextword>|"
                    int nextIndex = synAndRest.indexOf(" ");
                    // We'll instead search for " <something>|" pattern in synAndRest
                    int cut = findNextRecordStart(synAndRest);

                    String synPart;
                    String remaining;
                    if (cut == -1) {
                        synPart = synAndRest;
                        remaining = "";
                    } else {
                        synPart = synAndRest.substring(0, cut).trim();
                        remaining = synAndRest.substring(cut).trim();
                    }

                    List<String> syns = java.util.Arrays.asList(synPart.split("\\s*,\\s*"));
                    service.addOrUpdate(new dictionaryEntry(word, pron, def, exAndRest, syns));

                    // Prepare record buffer for remaining text (next records)
                    record.setLength(0);
                    if (!remaining.isEmpty()) record.append(remaining);
                }
            }

        } catch (Exception ex) {
            view.showError("Failed to import file: " + ex.getMessage());
        }
    }

    private int countPipes(StringBuilder sb) {
        int count = 0;
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '|') count++;
        }
        return count;
    }

    /**
     * Finds where the NEXT record likely starts inside the synonyms+rest string.
     * We look for " <word>|" (space, letters, then pipe).
     */
    private int findNextRecordStart(String s) {
        for (int i = 1; i < s.length() - 1; i++) {
            if (s.charAt(i - 1) == ' ' && s.charAt(i) != '|' ) {
                int pipe = s.indexOf('|', i);
                if (pipe != -1) {
                    // ensure there's a reasonable "word" before the pipe
                    String candidate = s.substring(i, pipe).trim();
                    if (candidate.matches("[A-Za-z][A-Za-z\\-']*")) {
                        return i - 1; // include the leading space
                    }
                }
            }
        }
        return -1;
    }
    private void handleExport() {
        java.io.File file = view.chooseExportFile();
        if (file == null) return;

        // enforce .txt extension
        if (!file.getName().toLowerCase().endsWith(".txt")) {
            file = new java.io.File(file.getParentFile(), file.getName() + ".txt");
        }

        try (java.io.PrintWriter out = new java.io.PrintWriter(file, java.nio.charset.StandardCharsets.UTF_8)) {
            for (dictionaryEntry entry : service.getAllEntriesSorted()) {
                String line = toExportLine(entry);
                out.println(line);
            }
        } catch (Exception ex) {
            view.showError("Export failed: " + ex.getMessage());
            return;
        }

    }
    private String toExportLine(dictionaryEntry e) {
        String word = safe(e.getWord());
        String pron = safe(e.getPronounce());
        String def  = safe(e.getDefinition());
        String ex   = safe(e.getExample());
        String syns = String.join(", ", e.getSyn()).replace("\n", " ").replace("\r", " ");

        // If you want to be strict, also remove '|' from fields:
        syns = syns.replace("|", "/");

        return word + "|" + pron + "|" + def + "|" + ex + "|" + syns;
    }
    private String safe(String s) {
        if (s == null) return "";
        // Keep export single-line and avoid breaking the delimiter
        return s.replace("\n", " ").replace("\r", " ").replace("|", "/").trim();
    }

}
