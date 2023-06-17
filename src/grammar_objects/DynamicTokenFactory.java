package grammar_objects;

public class DynamicTokenFactory {
    
    public static Token create(
        String type,
        String grammaticalName,
        String instanceName,
        int lineNum,
        int columnNum
    ) {
        switch(type) {
            case "Token":
                return new Token(grammaticalName, lineNum, columnNum);
            case "Identifier":
                return new Identifier(grammaticalName, instanceName, instanceName, lineNum, columnNum);
            case "Literal":
                return new Literal(grammaticalName, instanceName);
            default:
                throw new RuntimeException(type + " type not found");
        }
    }
}
