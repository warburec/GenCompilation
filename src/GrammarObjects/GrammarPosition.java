package GrammarObjects;

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
    
    public class ClosedRuleException extends RuntimeException {
        public ClosedRuleException() {
            super("A next element could not be found as this position is closed");
        }
    }
    
}
