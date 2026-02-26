package Dictionary;

public class InvalidFrequencyError extends RuntimeException{
    public InvalidFrequencyError() {
        super("Frequency Can't Be Less Than Zero");
    }
}
