package grammar_objects;

public class Identifier extends Token {
    String type = null;
    String identifierName = null;

    //Untyped, no specific name
    public Identifier(String grammaticalName) {
        super(grammaticalName);
    }

    //Typed, no specific name
    public Identifier(String grammaticalName, String type) {
        super(grammaticalName);

        this.type = type;
    }

    //Named and typed
    public Identifier(String grammaticalName, String type, String identifierName) {
        super(grammaticalName);

        this.type = type;
        this.identifierName = identifierName;
    }

    public String getType() {
        return type;
    }

    public String getIdentifierName() {
        return identifierName;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Identifier)) { return false; }

        boolean grammaticallyEqual = super.equals(obj);
        if(!grammaticallyEqual) { return false; }

        Identifier otherIdentifier = (Identifier)obj;

        if(identifierName != null) {
            if(!identifierName.equals(otherIdentifier.getIdentifierName())) { return false; }
        }

        if(type != null) {
            if(!type.equals(otherIdentifier.getType())) { throw new ConflictingTypesException(type, otherIdentifier.getType()); }
        }

        return true;
    }

    public class ConflictingTypesException extends RuntimeException {
        public ConflictingTypesException(String type1, String type2) {
            super("Identifiers are equal but have different types: " + type1 + " and " + type2);
        }
    }

}
