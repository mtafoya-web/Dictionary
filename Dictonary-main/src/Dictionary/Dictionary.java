package Dictionary;

import javax.swing.*;
import Dictionary.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Collections;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;

public class Dictionary{
    /**
     * The Panel The Dictionary Components will Be Placed On
     */
    public JPanel Dictonary;
    /**
     * Display Letting User Know To Enter Information
     */
    public JLabel TextOne;
    /**
     * Clears All Text From The Textboxes
     */
    public JButton CLEARButton;
    /**
     * Removes A Word From The Dictionary
     */
    public JButton REMOVEButton;
    /**
     * Finds A Word In The Dictionary
     */
    public JButton FINDButton;
    /**
     * Dipslays Text Such As Error Messages and Word Defenitions
     */
    public JTextArea TextArea;
    /**
     * Adds Words To The Dictionary
     */
    public JButton ADDButton;
    /**
     * Imports Words To The Dictionary From A File
     */
    public JButton IMPORTButton;
    /**
     * Exports Words From The Dictionary to A Text File
     */
    public JButton EXPORTButton;
    /**
     * Field That Will Process The Word
     */
    public JTextField TextWord;
    /**
     * Field That Will Process the Frequency
     */
    public JTextField TextFrequency;
    /**
     * Field that will Display The Most Frequent Word
     */
    public JTextField TextFreqWord1;
    /**
     * Field that will Display The Second Most Frequent Word
     */
    public JTextField TextFreqWord2;
    /**
     * Field That Will Display the Third Most Frequent Word
     */
    public JTextField TextFreqWord3;
    /**
     * Field That Will Display The Text To File path
     */
    public JTextField TextFilePath;

    /**
     * Method To Set Check if a substring is In a String
     * @param F
     * @param map
     * @return boolean
     */
    public Boolean IsSubstring(String F,HashMap<String, ArrayList<String>> map){
        for(String key: map.keySet())
        {
            if(key.contains(F));
            return true;
        }
        return false;
    }

    /**
     * Method To Check If A String Is In The HashMap
     * @param F
     * @param map
     * @return
     */
    public boolean IsStored(String F, HashMap<String, ArrayList<String>> map){
        for(String Key: map.keySet()){
            if(Key.equals(F))
                return true;
        }
        return false;
    }
    /**
     * Method To Check If The Testfield Is A Word
     * @param F
     * @return boolean
     */
    public boolean IsValidWord(String F){
        if(F.matches("[a-zA-Z]+")){
            return true;
        }
        return false;
    }
    /**
     * Method That Checks If Frequency Is Empty Returns 1 If True and String If False
     * @param F
     * @return Str
     */
    public String CheckFreq(String F)
    {
        String Str = "";
        if(F.equals(Str)){
            Str = "1";
        }else{
            Str += F;
        }
        return Str;
    }

    /**
     * Method To Check If The Freqency Is NonNegative
     * @param F
     * @return boolean
     */
    public boolean IsValidFreq(int F)
    {
        if (F < 0){
            return false;
        }
        return true;
    }

    public boolean IsDuplicate(String F, HashMap<String, ArrayList<String>> map)
    {
        for(String Key: map.keySet()){
            if(Key.equals(F))
                return true;
        }
        return false;
    }
    /**
     * Creates a innerClass object to handle events
     */
    eventHandler handler = new eventHandler();

    /**
     * Initializes Action Listener For All The Buttons On The Subclass EventHandler
     */
    public Dictionary(){
        FINDButton.addActionListener(handler);
        ADDButton.addActionListener(handler);
        REMOVEButton.addActionListener(handler);
        CLEARButton.addActionListener(handler);
        IMPORTButton.addActionListener(handler);
        EXPORTButton.addActionListener(handler);
    }
    /**
     *  Hashmap That Stores The Data In Key -> Value Pairs Format: (Word, List(Frequency, Def)
     */
    HashMap<String, ArrayList<String>> Storage = new HashMap<String, ArrayList<String>>();

    /**
     * Strings That Store The Immeditate Information For The User
     */
    String Def = new String("");
    String Word = new String("");
    String Freq = new String("");

    int freq = 0;

    /**
     *An inner class that inherits from the outer class and defines the action listener
     */
    public class eventHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == CLEARButton) {
                TextWord.setText("");
                TextFrequency.setText("");
                TextFreqWord1.setText("");
                TextFreqWord2.setText("");
                TextFreqWord3.setText("");
                TextFilePath.setText("");
                TextArea.setText("");

                //Clear Strings
                Word = "";
                Freq = "";
                freq = 0;
                Def = "";
            }
            if (event.getSource() == ADDButton) {
                //Get Word, Freq, Def
                Word += TextWord.getText();
                Freq += TextFrequency.getText();
                try {
                    freq = Integer.parseInt(Freq);
                }catch (Exception e)
                {

                }
                Def += TextArea.getText();

                if (!IsValidWord(Word)) {
                    TextArea.setText("Invalid Word Error: Please Enter A Valid Word");
                    throw new InvalidWordError();
                }

                //Check if the Frequency is empty
                Freq = CheckFreq(Freq);
                //Check if the frequency is valid
                if (!IsValidFreq(freq)) {
                    TextArea.setText("Invalid Frequency Error: Please Enter A Valid Frequency");
                    throw new InvalidFrequencyError();
                }

                //Check if the Word Is Duplicate
                if (IsDuplicate(Word, Storage)) {
                    TextArea.setText("Word Duplicate Error: Word Already Exists");
                    throw new WordDuplicatedError();
                }
                if (IsValidWord(Word) && IsValidFreq(freq) && !IsDuplicate(Word, Storage)) {
                    //Add Info To HashMap
                    Storage.put(Word, new ArrayList<>());
                    Storage.get(Word).add(Freq);
                    Storage.get(Word).add(Def);
                }

                //Clear Strings
                Word = "";
                Freq = "";
                freq = 0;
                Def = "";
            }
            if (event.getSource() == REMOVEButton) {
                Word += TextWord.getText();
                //Check If The Word Is In My HashMap
                if (!IsStored(Word, Storage)) {
                    TextArea.setText("Word Is Not Stored In Dictionary");
                    throw new WordNotFoundError("ERROR Word Not Found");
                } else Storage.remove("Word");

                Word = "";
            }

            if (event.getSource() == FINDButton) {

                HashMap<String, ArrayList<String>> Temp = new HashMap<>();
                ArrayList<Integer> FreqCheck = new ArrayList<>();
                ArrayList<String> WordCheck = new ArrayList<>();
                ArrayList<String> DefCheck = new ArrayList<>();
                String AreaText1 = "";
                String AreaText2 = "";
                String AreaText3 = "";
                int Max = 0;
                int index = 0;
                TextArea.setText("");

                Temp.putAll(Storage);
                Word = TextWord.getText();

                if (!IsSubstring(Word, Storage)) {
                    TextArea.setText("No Words Stored In The Dictionary");
                    throw new WordNotFoundError("ERROR Word Not Found");
                } else {
                    for (String key : Temp.keySet()) {
                        if (key.contains(Word)) {
                            WordCheck.add(key);
                            FreqCheck.add(Integer.parseInt(Temp.get(key).get(0)));
                            DefCheck.add(Temp.get(key).get(1));
                        }
                    }

                    try {
                        Max = Collections.max(FreqCheck);
                        index = FreqCheck.indexOf(Max);
                        TextFreqWord1.setText(WordCheck.get(index));
                        AreaText1 += DefCheck.get(index);

                        FreqCheck.remove(index);
                        DefCheck.remove(index);
                        WordCheck.remove(index);
                    }catch(Exception e){
                        if(AreaText1.equals("")){
                            TextArea.setText("No Defenition Provided");
                        }else {
                            TextArea.setText("No valid Words Will leave frequency1 empty");
                        }
                    }
                    try {
                        Max = Collections.max(FreqCheck);
                        index = FreqCheck.indexOf(Max);
                        TextFreqWord2.setText(WordCheck.get(index));
                        AreaText2 += DefCheck.get(index);

                        FreqCheck.remove(index);
                        DefCheck.remove(index);
                        WordCheck.remove(index);
                    }catch(Exception e){
                        if(AreaText2.equals("")){
                            TextArea.setText("No Defenition Provided");
                        }else {
                            TextArea.setText("No valid Words Will leave frequency2 empty");
                        }
                    }

                    try {
                        Max = Collections.max(FreqCheck);
                        index = FreqCheck.indexOf(Max);
                        TextFreqWord3.setText(WordCheck.get(index));
                        AreaText3 += DefCheck.get(index);

                        FreqCheck.remove(index);
                        DefCheck.remove(index);
                        WordCheck.remove(index);

                    }catch(Exception e){
                        if(AreaText3.equals("")){
                            TextArea.setText("No Defenition Provided");
                        }else {
                            TextArea.setText("No valid Words Will leave frequency3 empty");
                        }
                    }
                    TextArea.setText(AreaText1+"\n\n"+AreaText2+"\n\n"+AreaText3);

                    Max = 0;
                    index = 0;
                }
            }

            if(event.getSource() == IMPORTButton) {
                try {
                    File readFile = new File(TextFilePath.getText());
                    Scanner reader = new Scanner(readFile);

                    while (reader.hasNextLine()) {
                        Word = reader.nextLine();
                        Word = Word.replaceAll("\\s", "");
                        Freq = reader.nextLine();
                        Freq = Freq.replaceAll("\\s", "");
                        Def = reader.nextLine();
                        if (!IsValidWord(Word)) {
                            TextArea.setText("Invalid Word Error: Please Enter A Valid Word");
                        }

                        //Check if the Frequency is empty
                        Freq = CheckFreq(Freq);
                        //Check if the frequency is valid
                        if (!IsValidFreq(freq)) {
                            TextArea.setText("Invalid Frequency Error: Please Enter A Valid Frequency");
                        }

                        //Check if the Word Is Duplicate
                        if (IsDuplicate(Word, Storage)) {
                            TextArea.setText("Word Duplicate Error: Word Already Exists");
                            throw new WordDuplicatedError();
                        }
                        if (IsValidWord(Word) && IsValidFreq(freq) && !IsDuplicate(Word, Storage)) {
                            //Add Info To HashMap
                            Storage.put(Word, new ArrayList<>());
                            Storage.get(Word).add(Freq);
                            Storage.get(Word).add(Def);
                        }
                        if (reader.hasNextLine()) {
                            String BlankSpace = reader.nextLine();
                        }

                    }

                } catch (FileNotFoundException e) {
                    TextArea.setText("File Not Found Please Enter Valid File");
                    throw new FileNotFoundError("File Not Found Please Enter Valid File");
                }
                System.out.println(Storage);
            }

            if(event.getSource() == EXPORTButton) {
                HashMap<String, ArrayList<String>> Temp = new HashMap<>();
                ArrayList<Integer> FreqCheck = new ArrayList<>();
                ArrayList<String> WordCheck = new ArrayList<>();
                ArrayList<String> DefCheck = new ArrayList<>();
                int Max = 0;
                int index = 0;

                Temp.putAll(Storage);
                for (String key : Temp.keySet()) {
                    WordCheck.add(key);
                    FreqCheck.add(Integer.parseInt(Temp.get(key).get(0)));
                    DefCheck.add(Temp.get(key).get(1));
                }
                Temp.clear();
                int Flag = 0;
                while (Flag < FreqCheck.size()) {
                    Max = Collections.max(FreqCheck);
                    index = FreqCheck.indexOf(Max);
                    Temp.put(WordCheck.get(index), new ArrayList<>());
                    Temp.get(WordCheck.get(index)).add(Integer.toString(Max));
                    Temp.get(WordCheck.get(index)).add(DefCheck.get(index));
                    FreqCheck.set(index, 0);
                    Flag++;
                }
                System.out.println(Temp);
                PrintWriter outputStream = null;
                String outputFile = TextFilePath.getText();
                try {
                    outputStream = new PrintWriter(new FileOutputStream(outputFile));
                    for(String key: Temp.keySet()){
                        outputStream.println(key);
                        outputStream.println(Temp.get(key).get(0));
                        outputStream.println(Temp.get(key).get(1));
                        outputStream.println("\n");
                    }
                    outputStream.close();
                } catch (FileNotFoundException e) {
                    TextArea.setText("File Not Found Please Enter Valid File");
                    throw new FileNotFoundError("File Not Found Please Enter Valid File");
                }

            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dictionary");
        frame.setContentPane(new Dictionary().Dictonary);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}

