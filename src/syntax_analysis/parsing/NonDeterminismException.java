package syntax_analysis.parsing;

import java.util.List;

import grammar_objects.LexicalElement;
import syntax_analysis.grammar_structure_creation.GrammarPosition;

public class NonDeterminismException extends RuntimeException {
    private LexicalElement elementTraversed;
    private List<GrammarPosition> conflictingPositions;

    public NonDeterminismException(LexicalElement elementTraversed, List<GrammarPosition> conflictingPositions) {
        super("Non-Determinism found whilst traversing \"" + elementTraversed + "\"");
    }

    @Override
    public String getMessage(){
        String msg = super.getMessage();

        msg += "\nThe states causing ambiguity are:\n";

        for (GrammarPosition grammarPosition : conflictingPositions) {
            msg += "\t" + grammarPosition.toString() + "\n";
        }

        return msg;
    }

}
