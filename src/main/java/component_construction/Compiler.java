package component_construction;
import syntax_analysis.parsing.ParseFailedException;

public interface Compiler {

    /**
     * Compiles an input sentence
     * @param input The sentence to be compiled
     * @return The output of compilation
     * @throws ParseFailedException An error occurred when parsing the sentence
     */
    public String compile(String input) throws ParseFailedException;

}
