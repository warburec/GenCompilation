package syntax_analysis.parsing;

import grammar_objects.GrammarStructure;
import syntax_analysis.grammar_structure_creation.State;

public interface ParseState extends GrammarStructure {
    public State state();

    @Override
    String toString();
}
