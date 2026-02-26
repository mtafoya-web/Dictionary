package dictionary;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionListener;
import java.util.List;

public class DictionaryPanel {

    private JPanel app;
    //North Panel
    private JTextField searchWordTextField;
    private JButton btn_find;
    private JButton btn_add;
    private JButton btn_delete;
    private  JLabel searchWord;
    private JButton btn_exit;

    //Center panel
    private JPanel search_panel;
    private JSplitPane splitMain;
    private JPanel listPanel;
    private JPanel detailsPanel;
    private JLabel word;
    private JTextField wordText;
    private JLabel pron;
    private JTextField pron_text_field;
    private JButton btn_audio;
    private JPanel pronPanel;
    private JLabel example;
    private JLabel def;
    private JLabel syn;
    private JScrollPane defScroll;
    private JTextArea defTextArea;
    private JScrollPane examScroll;
    private JTextArea examTextArea;
    private JScrollPane synScroll;
    private JTextArea synTextArea;
    private JPanel history;
    private JPanel detailsBtn;
    private JButton btn_edit;
    private JButton btn_clear;
    private JButton btn_save;
    private JScrollPane scroll;
    private JList<String> lstWords;
    private JPanel list_search;
    private JPanel fullListPanel;
    private JLabel searchWordList;
    private JTextField filter;
    //South Panel
    private JPanel historyPanel;
    private JLabel hist;
    private JButton btn_freq2;
    private JButton btn_clearHistory;
    private JButton btn_freq5;
    private JButton btn_freq4;
    private JButton btn_freq3;
    private JButton btn_freq1;
    private JLabel count;
    private JButton btn_export;
    private JButton btn_import;

    //Expose root panel so JFrame can show it
    public JPanel getRoot(){return app;}

    //Listener hooks: Does nothing except attach whatever listener someone gives
    public void onFind(ActionListener l) { btn_find.addActionListener(l); }
    public void onAdd(ActionListener l) { btn_add.addActionListener(l); }
    public void onDelete(ActionListener l) { btn_delete.addActionListener(l); }
    public void onExit(ActionListener l) { btn_exit.addActionListener(l); }

    public void  onAudio(ActionListener l) { btn_audio.addActionListener(l); }
    public void  onEdit(ActionListener l) { btn_edit.addActionListener(l); }
    public void  onClear(ActionListener l) { btn_clear.addActionListener(l); }
    public void  onSave(ActionListener l) { btn_save.addActionListener(l); }

    public void onFreq1(ActionListener l) {btn_freq1.addActionListener(l);}
    public void onFreq2(ActionListener l) {btn_freq2.addActionListener(l);}
    public void onFreq3(ActionListener l) {btn_freq3.addActionListener(l);}
    public void onFreq4(ActionListener l) {btn_freq4.addActionListener(l);}
    public void onFreq5(ActionListener l) {btn_freq5.addActionListener(l);}
    public void  onClearHist(ActionListener l) { btn_clearHistory.addActionListener(l); }

    public void onImport(ActionListener l){btn_import.addActionListener(l);}
    public void onExport(ActionListener l){btn_export.addActionListener(l);}


    //Jlist Selection
    public void onWordSelection(ListSelectionListener l){
        lstWords.addListSelectionListener(l);
    }
    //Filter box text changes
    public void onFilterChanged(DocumentListener l){
        filter.getDocument().addDocumentListener(l);
    }

    //Getters for inputs
    public String getSearchWord(){return searchWordTextField.getText();}
    public String getFilterText(){return filter.getText();}
    public String getSelectedWord(){return lstWords.getSelectedValue();}

    //Setters
    public void setTotalCount(int n){count.setText("Total Words: " + n);}
    public void setSearchWordList(List<String> words){
        DefaultListModel<String> model = new DefaultListModel<>();
        for(String word: words){
            model.addElement(word);
        }
        lstWords.setModel(model);
    }
    public void selectWordInList(String word){
        ListModel<String> model = lstWords.getModel();
        for(int i= 0; i<model.getSize(); i++){
            if(word.equalsIgnoreCase(model.getElementAt(i))){
                lstWords.setSelectedIndex(i);
                lstWords.ensureIndexIsVisible(i);
                return;
            }
        }
    }
    public void setDetails(dictionaryEntry e){
        wordText.setText(e.getWord());
        pron_text_field.setText(e.getPronounce());
        defTextArea.setText(e.getDefinition());
        examTextArea.setText(e.getExample());
        synTextArea.setText(String.join(", ",e.getSyn()));
    }
    public void setTop5Buttons(List<String> topWords){
        JButton[] btns = { btn_freq1, btn_freq2, btn_freq3, btn_freq4, btn_freq5 };

        for (int i = 0; i < btns.length; i++) {
            if (topWords != null && i < topWords.size()) {
                btns[i].setText(topWords.get(i));
                btns[i].setEnabled(true);
            } else {
                btns[i].setText("-");
                btns[i].setEnabled(false);
            }
        }
    }
    public String getTopWordFromButton(int index) {
        JButton[] btns = { btn_freq1, btn_freq2, btn_freq3, btn_freq4, btn_freq5 };
        if (index < 0 || index >= btns.length) return null;
        String text = btns[index].getText();
        if (text == null || text.isBlank() || text.equals("-")) return null;
        return text.trim();
    }
    public dictionaryEntry getDetailsFromFields(){
       List<String> syns = List.of(synTextArea.getText().split("\\s*,\\s*"));
        return new dictionaryEntry(
                wordText.getText().trim(),
                pron_text_field.getText().trim(),
                defTextArea.getText().trim(),
                examTextArea.getText().trim(),
                syns
        );
    }
    public void clearDetails() {
        wordText.setText("");
        pron_text_field.setText("");
        defTextArea.setText("");
        examTextArea.setText("");
        synTextArea.setText("");
    }
    //Method to dictate if user can edit the fields
    public void setEditing(boolean editing){
        wordText.setEditable(editing);
        pron_text_field.setEditable(editing);
        defTextArea.setEditable(editing);
        examTextArea.setEditable(editing);
        synTextArea.setEditable(editing);

    }
    //Method to show confirm panel
    public boolean confirm(String message) {
        return JOptionPane.showConfirmDialog(app, message, "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    //method to output an error message
    public void showError(String message) {
        JOptionPane.showMessageDialog(app, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    //method to confirm exit
    public boolean confirmExit() {
        return JOptionPane.showConfirmDialog(
                getRoot(),
                "Exit the application?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
    }
    //Method to close the window
    public void closeWindow() {
        // closes the top-level window that contains this panel
        java.awt.Window w = SwingUtilities.getWindowAncestor(getRoot());
        if (w != null) w.dispose();
    }
    public java.io.File chooseExportFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export Dictionary");

        int result = chooser.showSaveDialog(getRoot());
        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    public void clearFilter() {
        filter.setText("");
    }
}
