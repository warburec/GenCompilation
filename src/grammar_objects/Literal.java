package grammar_objects;

public class Literal extends Token {
    private String type;
    private String value;

    public Literal(String grammaticalName, String type, String value) {
        super(grammaticalName);

        this.type = type;
        this.value = value;
    }
    
    public Literal(String grammaticalName, String value) {
        super(grammaticalName);

        this.type = null;
        this.value = value;
    }

    public Literal(String grammaticalName) {
        super(grammaticalName);

        this.type = null;
        this.value = null;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
