package Dictionary;

public class WordNotFoundError extends RuntimeException{
    public WordNotFoundError(String message){
        super(message);
    }
}
