package syntax_analysis.grammar_structure_creation;

import grammar_objects.*;

public class GrammarPosition{
    ProductionRule rule;
    int position;
    
    public GrammarPosition(ProductionRule rule, int position) {
        this.rule = rule;
        this.position = position;
    }

    public ProductionRule getRule() {
        return rule;
    }

    public int getPosition() {
        return position;
    }
    
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

    @Override
    public String toString() {
        String string = "";

        if(rule.nonTerminal() != null) {
            string += rule.nonTerminal().toString();
        }
        else {
            string += "null";
        }

        string += " := ";

        LexicalElement[] production = rule.productionSequence();
        for(int i = 0; i < production.length; i++) {
            if(i == position) {
                string += "•";
            }

            string += production[i].toString() + " ";
        }

        string = string.substring(0, string.length() - 1);

        if(position == production.length) {
            string += "•";
        }

        return string;
    }
}
