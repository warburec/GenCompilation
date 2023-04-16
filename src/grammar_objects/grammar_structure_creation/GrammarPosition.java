package grammar_objects.grammar_structure_creation;

import grammar_objects.fundamentals.*;

public record GrammarPosition(ProductionRule rule, int position) {
    
    public boolean isClosed() {
        return position == rule.productionSequence().length;
    }

    public GrammarPosition getNextPosition() {
        return new GrammarPosition(rule, position + 1);
    }

    public LexicalElement getNextElement() {
        if(isClosed()) { throw new ClosedRuleException(); }
        return rule.productionSequence()[position];
    }
    
    public LexicalElement getLastElementRead() {
        if(position == 0) { return null; }
        return rule.productionSequence()[position - 1];
    }
    
    public class ClosedRuleException extends RuntimeException {
        public ClosedRuleException() {
            super("A next element could not be found as this position is closed");
        }
    }
    
    @Override
    public int hashCode() {
        int hashCode = position;
        return hashCode;
    }

}
