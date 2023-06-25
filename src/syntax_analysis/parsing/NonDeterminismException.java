package syntax_analysis.parsing;

import java.util.List;

import grammar_objects.LexicalElement;
import grammar_objects.ProductionRule;
import syntax_analysis.grammar_structure_creation.*;

public class NonDeterminismException extends RuntimeException {
    private List<GrammarPosition> conflictingPositions;
    private List<ProductionRule> conflictingRules;
    private DisplayType displayType;

    private State failingState;

    public NonDeterminismException(LexicalElement elementTraversed, List<GrammarPosition> conflictingPositions, State failingState) {
        super("Non-Determinism found whilst traversing \"" + elementTraversed + "\" ");

        this.conflictingPositions = conflictingPositions;
        this.failingState = failingState;
        this.displayType = DisplayType.Positions;
    }

    public NonDeterminismException(List<ProductionRule> conflictingRules, State failingState) {
        this.failingState = failingState;
        this.conflictingRules = conflictingRules;
        this.displayType = DisplayType.Rules;
    }

    @Override
    public String getMessage(){
        switch(displayType) {
            case Positions:
                return getPositionMessage();
            case Rules:
                return getRulesMessage();
            default:
                return super.getMessage();
        }
    }

    private String getPositionMessage() {
        String msg = super.getMessage();

        msg += "\nThe states causing ambiguity are:\n";

        for (GrammarPosition grammarPosition : conflictingPositions) {
            msg += "\t" + grammarPosition.toString() + "\n";
        }

        return msg;
    }

    private String getRulesMessage() {
        String msg = "Non-determinism was found in the rules :\n";

        for (ProductionRule rule : conflictingRules) {
            msg += "\t" + rule.toString() + "\n";
        }

        return msg;
    }

    public State getFailingState() {
        return failingState;
    }

    private enum DisplayType {
        Positions,
        Rules
    }
}
