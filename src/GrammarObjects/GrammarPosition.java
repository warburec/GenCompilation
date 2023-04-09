package GrammarObjects;

public record GrammarPosition(ProductionRule rule, int position) {
    
    public boolean isClosed() {
        return position == rule.productionSequence().length;
    }

    public GrammarPosition getNextPosition() {
        return new GrammarPosition(rule, position + 1);
    }

    public LexicalElement getNextElement() {
        return rule.productionSequence()[position];
    }
    
}
