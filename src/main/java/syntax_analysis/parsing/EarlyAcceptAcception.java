package syntax_analysis.parsing;

public class EarlyAcceptAcception extends Exception {

    public EarlyAcceptAcception() {
        super("A complete parse was made before the end of the input stream");
    }

}