package grammar_objects;

public class Token extends LexicalElement {

    private Integer lineNumber;
    private Integer columnNumber;

    private Integer symbolNumber;

    private PositionType positionType;

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

}
