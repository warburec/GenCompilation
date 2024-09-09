package grammar_objects;

public class Identifier extends DynamicToken {

    /**
     * A constructor for grammar description, not for use within sentences/inputs to syntax analysis.
     * @param grammarName The name of this Identifier used within the grammar.
     */
    public Identifier(String grammaticalName) {
        super(grammaticalName);
    }

    public Identifier(String grammaticalName, String name) {
        super(grammaticalName, name);
    }

    public Identifier(String grammaticalName, String name, int symbolNumber) {
        super(grammaticalName, name, symbolNumber);
    }

    public Identifier(String grammaticalName, String name, int lineNum, int columnNum) {
        super(grammaticalName, name, lineNum, columnNum);
    }

    public String getIdentifierName() {
        return this.value;
    }

}
