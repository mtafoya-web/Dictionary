package dictionary;

import javax.swing.*;
import java.util.List;

/**
 * Controller layer of the Dictionary application.
 *
 * Responsible for:
 * - Wiring UI events to business logic
 * - Coordinating interactions between the View and Service
 * - Handling user input validation
 * - Managing application state transitions
 *
 * This class follows the MVC pattern and contains no UI layout logic
 * and no direct data storage logic.
 */
public class DictionaryController {

    /** Reference to the UI layer */
    private final DictionaryPanel view;

    /** Reference to the business logic layer */
    private final DictionaryService service;

    /**
     * Constructs the controller and initializes event wiring.
     *
     * @param view    the DictionaryPanel (UI layer)
     * @param service the DictionaryService (business logic layer)
     */
    public DictionaryController(DictionaryPanel view, DictionaryService service){
        this.view = view;
        this.service = service;

        wireEvents();
        refreshWordList();
        refreshTop5();
        view.setEditing(false);
    }

    /**
     * Connects all UI event listeners to their respective handler methods.
     */
    public void wireEvents() {

        // Main CRUD actions
        view.onFind(e -> handleFind());
        view.onAdd(e -> handleAdd());
        view.onDelete(e -> handleRemove());
        view.onExit(e -> handleExit());
        view.onEdit(e -> handleEdit());
        view.onAudio(e -> handleAudio());
        view.onClear(e -> handleClear());
        view.onSave(e -> handleSave());

        // Frequency buttons
        view.onFreq1(e -> handleTopButton(0));
        view.onFreq2(e -> handleTopButton(1));
        view.onFreq3(e -> handleTopButton(2));
        view.onFreq4(e -> handleTopButton(3));
        view.onFreq5(e -> handleTopButton(4));
        view.onClearHist(e -> handleClearHistory());

        // Import / Export
        view.onImport(e -> handleImport());
        view.onExport(e -> handleExport());

        // List selection
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

        // Prefix filter listener
        view.onFilterChanged(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });
    }

    /**
     * Refreshes the full dictionary word list in sorted order.
     */
    private void refreshWordList() {
        List<String> words = service.sort();
        view.setSearchWordList(words);
        view.setTotalCount(words.size());
    }

    /**
     * Updates the Top 5 most searched words display.
     */
    private void refreshTop5() {
        view.setTop5Buttons(service.topSearched(5));
    }

    /**
     * Handles search operation for a specific word.
     * Displays word details if found.
     */
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

    /**
     * Clears details and enables editing mode for adding a new word.
     */
    private void handleAdd(){
        view.clearDetails();
        view.setEditing(true);
    }

    /**
     * Handles word deletion with confirmation dialog.
     */
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

    /**
     * Handles application exit request.
     */
    private void handleExit(){
        if(view.confirmExit()) {
            view.closeWindow();
        }
    }

    /**
     * Placeholder for audio pronunciation feature.
     * Currently not implemented.
     */
    private void handleAudio(){
        // Future enhancement: integrate TTS API
    }

    /**
     * Clears the detail input fields.
     */
    private void handleClear(){
        view.clearDetails();
    }

    /**
     * Saves a new or edited dictionary entry.
     */
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

    /**
     * Enables editing mode for an existing word.
     */
    private void handleEdit(){
        if (view.getSelectedWord() == null && view.getSearchWord().isBlank()) {
            view.showError("Select a word first.");
            return;
        }
        view.setEditing(true);
    }

    /**
     * Applies prefix filtering to the dictionary word list.
     */
    private void filter() {
        view.setSearchWordList(service.searchPrefix(view.getFilterText().trim()));
    }

    /**
     * Handles selection of a Top 5 frequency button.
     *
     * @param index button index (0–4)
     */
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

    /**
     * Clears stored search frequency history.
     */
    private void handleClearHistory() {
        service.clearFrequency();
        refreshTop5();
    }

    /**
     * Opens file chooser and imports dictionary entries.
     */
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

    /**
     * Imports dictionary entries from a formatted text file.
     *
     * @param file the file to import from
     */
    private void importFromFile(java.io.File file) {
        try {
            String content = java.nio.file.Files.readString(
                    file.toPath(),
                    java.nio.charset.StandardCharsets.UTF_8
            );

            content = content.replace("\r", "\n");
            StringBuilder record = new StringBuilder();

            for (String chunk : content.split("\\R")) {
                String line = chunk.trim();
                if (line.isEmpty()) continue;

                if (record.length() > 0) record.append(" ");
                record.append(line);

                while (countPipes(record) >= 4) {
                    String one = record.toString();
                    String[] parts = one.split("\\|", 5);
                    if (parts.length < 5) break;

                    String word = parts[0].trim();
                    String pron = parts[1].trim();
                    String def  = parts[2].trim();
                    String ex   = parts[3].trim();
                    String synAndRest = parts[4].trim();

                    List<String> syns =
                            java.util.Arrays.asList(synAndRest.split("\\s*,\\s*"));

                    service.addOrUpdate(new dictionaryEntry(word, pron, def, ex, syns));
                    record.setLength(0);
                }
            }

        } catch (Exception ex) {
            view.showError("Failed to import file: " + ex.getMessage());
        }
    }

    /**
     * Counts the number of '|' characters in a StringBuilder.
     *
     * @param sb string builder
     * @return number of pipe characters
     */
    private int countPipes(StringBuilder sb) {
        int count = 0;
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '|') count++;
        }
        return count;
    }

    /**
     * Exports dictionary entries to a user-selected file.
     */
    private void handleExport() {
        java.io.File file = view.chooseExportFile();
        if (file == null) return;

        if (!file.getName().toLowerCase().endsWith(".txt")) {
            file = new java.io.File(file.getParentFile(), file.getName() + ".txt");
        }

        try (java.io.PrintWriter out =
                     new java.io.PrintWriter(file, java.nio.charset.StandardCharsets.UTF_8)) {

            for (dictionaryEntry entry : service.getAllEntriesSorted()) {
                out.println(toExportLine(entry));
            }

        } catch (Exception ex) {
            view.showError("Export failed: " + ex.getMessage());
        }
    }

    /**
     * Converts a dictionary entry to export file format.
     */
    private String toExportLine(dictionaryEntry e) {
        String word = safe(e.getWord());
        String pron = safe(e.getPronounce());
        String def  = safe(e.getDefinition());
        String ex   = safe(e.getExample());
        String syns = String.join(", ", e.getSyn()).replace("\n", " ").replace("\r", " ");
        syns = syns.replace("|", "/");

        return word + "|" + pron + "|" + def + "|" + ex + "|" + syns;
    }

    /**
     * Sanitizes string data before file export.
     */
    private String safe(String s) {
        if (s == null) return "";
        return s.replace("\n", " ")
                .replace("\r", " ")
                .replace("|", "/")
                .trim();
    }
}