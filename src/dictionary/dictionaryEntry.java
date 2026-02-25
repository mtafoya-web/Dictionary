package dictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Record Class:
 * Contains all the information about a word.
 */
public class dictionaryEntry {

    private final String word;
    private final String pronounce;
    private final String definition;
    private final String example;
    private final List<String> syn;

    //Constructor (word, pron, def, exam, syn)
    public dictionaryEntry(String w, String pro, String def, String ex, List<String> syn){
        this.word = w;
        this.pronounce = pro;
        this.definition = def;
        this.example = ex;
        this.syn = getSyn(syn);
    }

    //Get synonym list
    private List<String> getSyn(List<String> words){
        List<String> list = new ArrayList<String>(4);
        for (String word : words){
            list.add(word);
            if (list.size() > 4){
                return list;
            }
        }
        return list;
    }
    public String getWord(){
        return this.word;
    }
    public String getPronounce(){
        return this.pronounce;
    }
    public String getDefinition(){
        return this.definition;
    }
    public String getExample(){
        return this.example;
    }
    //Return a copy to protect internal data
    public List<String> getSyn(){
        return new ArrayList<>(syn);
    }

}
