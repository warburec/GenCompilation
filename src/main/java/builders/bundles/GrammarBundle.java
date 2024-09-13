package builders.bundles;

import grammar_objects.*;
import lexical_analysis.DynamicTokenRegex;

public interface GrammarBundle {

    public Grammar getGrammar();
    public RuleConvertor getRuleConvertor();
    public String[] getWhitespaceDelimiters();
    public String[] getStronglyReservedWords();
    public String[] getWeaklyReservedWords();
    public DynamicTokenRegex[] getDynamicTokenRegex();

}
