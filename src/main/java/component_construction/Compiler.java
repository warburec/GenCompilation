package component_construction;
import syntax_analysis.parsing.ParseFailedException;

public interface Compiler {
    public String compile(String input) throws ParseFailedException;
}
