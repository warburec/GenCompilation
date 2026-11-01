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
     * Grammatical equality, based on grammatical qualities, not DynamicToken value
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * Grammatical equality, based on grammatical qualities, not DynamicToken value
     */
    public boolean grammaticallyEquals(DynamicToken otherToken) {
        return super.equals(otherToken);
    }

    /**
     * Exact equality, including DynamicToken value, useful for semantic evaluation
     */
    public boolean exactlyEquals(DynamicToken otherDynamicToken) {
        if(!grammaticallyEquals(otherDynamicToken)) return false;
        if(value == null) return otherDynamicToken.getValue() == null;

        return value.equals(otherDynamicToken.getValue());
    }

    @Override
    public String toString() {
        return getName();
    }
}
