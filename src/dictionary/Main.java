package dictionary;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private static final Path APP_DIR =
            Paths.get(System.getProperty("user.home"), ".dictionary_app");

    private static final Path DICT_FILE =
            APP_DIR.resolve("dictionary.txt");
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) { }

            // Create MVC pieces
            FlatDarkLaf.setup();
            DictionaryPanel view = new DictionaryPanel();
            DictionaryService service = new DictionaryService();
            System.out.println("Saving/Loading from: " + DICT_FILE.toAbsolutePath());


            try {
                Files.createDirectories(APP_DIR);
                service.loadFromFile(DICT_FILE);
            } catch (Exception e) {
                // optional: show error popup instead
                System.err.println("Load failed: " + e.getMessage());
            }
            new DictionaryController(view, service);

            // Build window
            JFrame frame = new JFrame("Dictionary");
            frame.setContentPane(view.getRoot());
            frame.pack();
            frame.setLocationRelativeTo(null); // center
            frame.setVisible(true);


            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    if (!view.confirmExit()) return;

                    try {
                        service.saveToFile(DICT_FILE);
                    } catch (Exception ex) {
                        view.showError("Save failed: " + ex.getMessage());
                        return; // up to you: still exit or not
                    }

                    frame.dispose();
                }
            });
        });
    }
}