package Dictionary;

public class WordDuplicatedError extends RuntimeException{
    public WordDuplicatedError(){
        super("Duplicate Word In The Dictionary");
    }
}
