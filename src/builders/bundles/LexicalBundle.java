package builders.bundles;

import lexical_analysis.DynamicTokenRegex;

/**
 * All attributes may be null
 */
public interface LexicalBundle {

    public String[] getWhitespaceDelimiters();
    public String[] getStronglyReservedWords();
    public String[] getWeaklyReservedWords();
    public DynamicTokenRegex[] getDynamicTokenRegex();
    
}
