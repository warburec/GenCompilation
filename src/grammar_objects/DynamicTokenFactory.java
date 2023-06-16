package grammar_objects;

public class DynamicTokenFactory {
    
    public Token create(
        String type,
        String grammaticalName,
        String instanceName,
        int lineNum,
        int columNum
    ) {
        switch(type) {
            case "Token":
                return new Token(grammaticalName, lineNum, columNum);
            case "Identifier":
                return new Identifier(grammaticalName, instanceName, instanceName);
            case "Literal":
                return new Literal(grammaticalName, instanceName);
            default:
                throw new RuntimeException(type + " type not found");
        }
    }
}
