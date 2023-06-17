package grammar_objects;

public class Identifier extends Token {
    String type = null;
    String identifierName = null;


    //TODO: Consider if all constructors are necessary
    
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

    //Untyped, no specific name
    public Identifier(String grammaticalName, int lineNum, int columnNum) {
        super(grammaticalName, lineNum, columnNum);
    }

    //Typed, no specific name
    public Identifier(String grammaticalName, String type, int lineNum, int columnNum) {
        super(grammaticalName, lineNum, columnNum);

        this.type = type;
    }

    //Named and typed
    public Identifier(String grammaticalName, String type, String identifierName, int lineNum, int columnNum) {
        super(grammaticalName, lineNum, columnNum);

        this.type = type;
        this.identifierName = identifierName;
    }

    public String getType() {
        return type;
    }

    public String getIdentifierName() {
        return identifierName;
    }
    

    /**
     * Grammatical equality, based on grammatical qualities, not identfier name and type
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Identifier)) { return false; }

        return grammaticallyEquals((Identifier)obj);
    }

    /**
     * Grammatical equality, based on grammatical qualities, not identfier name and type
     */
    public boolean grammaticallyEquals(Identifier otherIdentifier) {
        boolean grammaticallyEqual = super.equals(otherIdentifier);
        if(!grammaticallyEqual) { return false; }

        return true;
    }

    /**
     * Exact equality, including identfier name and type, useful for semantic evaluation
     */
    public boolean exactlyEquals(Identifier otherIdentifier) {
        if(!grammaticallyEquals(otherIdentifier)) { return false; }

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
