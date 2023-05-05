package syntax_analysis.parsing;

public class IncompleteParseException extends Exception {

    public IncompleteParseException() {
        super("The input ended before a complete parse could be made");
    }

}
