package Dictionary;

public class InvalidWordError extends RuntimeException{
    public InvalidWordError(){
        super("Invalid Word Error");
    }
}
