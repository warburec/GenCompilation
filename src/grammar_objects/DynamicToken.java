package grammar_objects;

public class DynamicToken extends Token {

    protected String value = null;

    /**
     * A constructor for grammar description, not for use within sentences/inputs to syntax analysis.
     * @param grammarName The name of this dynamic token used within the grammar.
     */
    public DynamicToken(String grammarName) {
        super(grammarName);
    }

    public DynamicToken(String grammarName, String value) {
        super(grammarName);

        this.value = value;
    }

    public DynamicToken(String grammarName, String value, int symbolNumber) {
        super(grammarName, symbolNumber);

        this.value = value;
    }

    public DynamicToken(String grammarName, String value, Integer lineNumber, int columnNumber) {
        super(grammarName, lineNumber, columnNumber);

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Grammatical equality, based on grammatical qualities, not identfier name and type
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DynamicToken)) { return false; }

        return grammaticallyEquals((DynamicToken)obj);
    }

    /**
     * Grammatical equality, based on grammatical qualities, not value
     */
    public boolean grammaticallyEquals(DynamicToken otherToken) {
        boolean grammaticallyEqual = super.equals(otherToken);
        if(!grammaticallyEqual) { return false; }

        return true;
    }

    /**
     * Exact equality, including DnamicToken value, useful for semantic evaluation
     */
    public boolean exactlyEquals(DynamicToken otherIdentifier) {
        if(!grammaticallyEquals(otherIdentifier)) { return false; }

        if(value != null) {
            if(!value.equals(otherIdentifier.getValue())) { return false; }
        }

        return true;
    }

    @Override
    public String toString() {
        return getName();
    }
}
