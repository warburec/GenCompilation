package grammar_objects;

import static helper_objects.ToStringFormatting.indentFormat;

import java.util.Set;

public record GrammarParts(
    Set<Token> tokens, 
    Set<NonTerminal> nonTerminals, 
    Set<ProductionRule> productionRules, 
    NonTerminal sentinal
) {

    @Override
    public final String toString() {
        String out = "";

        out += "Tokens:\n" + indentFormat(tokens) + "\n\n";
        out += "NonTerminals:\n" + indentFormat(nonTerminals) + "\n\n";
        out += "Production Rules:\n" + indentFormat(productionRules) + "\n\n";
        out += "Sentinal: " + sentinal.toString() + "\n";

        return out;
    }
}
