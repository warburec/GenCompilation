package grammar_objects;

public class Token extends LexicalElement {

    private Integer lineNumber;
    private Integer columnNumber;

    private Integer symbolNumber;

    private PositionType positionType;

    /**
     * A constructor for grammar description, not for use within sentences/inputs to syntax analysis.
     * @param grammarName The name of this Token used within the grammar.
     */
    public Token(String name) {
        super(name);
    }

    public Token(String name, int symbolNumber) {
        super(name);

        this.symbolNumber = symbolNumber;
        positionType = PositionType.Symbol;
    }

    public Token(String name, Integer lineNumber, int columnNumber) {
        super(name);

        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;

        if(lineNumber == null) {
            positionType = PositionType.ColumnOnly;
        }
        else {
            positionType = PositionType.LineAndColumn;
        }
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public Integer getColumnNumber() {
        return columnNumber;
    }
    
    public String getPositionString() {
        if(positionType == null) {
            return null;
        }

        switch(positionType) {
            case LineAndColumn:
                return "line: " + lineNumber + ", column: " + columnNumber;

            case ColumnOnly:
                return "column " + columnNumber;

            case Symbol:
                return "symbol " + symbolNumber;

            default:
                return null;
        }
    }


    private enum PositionType {
        LineAndColumn,
        ColumnOnly,
        Symbol
    }

    @Override
    public String toString() {
        return "\"" + getName() + "\"";
    }

}
