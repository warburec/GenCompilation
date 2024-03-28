package grammar_objects;

public class Literal extends DynamicToken {

    /**
     * A constructor for grammar description, not for use within sentences/inputs to syntax analysis.
     * @param grammarName The name of this Literal used within the grammar.
     */
    public Literal(String name) {
        super(name);
    }

    public Literal(String grammarName, String value) {
        super(grammarName, value);
    }

    public Literal(String name, String value, int symbolNumber) {
        super(name, value, symbolNumber);
    }

    public Literal(String grammaticalName, String value, int lineNum, int columnNum) {
        super(grammaticalName, value, lineNum, columnNum);
    }

}
