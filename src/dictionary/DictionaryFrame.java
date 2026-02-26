package dictionary;

import javax.swing.*;

//Display the panel
public class DictionaryFrame extends JFrame {
    public DictionaryFrame(DictionaryPanel panel){
        super("Dictionary");
        setContentPane(panel.getRoot());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }
}
