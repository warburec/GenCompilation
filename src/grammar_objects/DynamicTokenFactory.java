package grammar_objects;

import static java.util.Map.entry;
import java.util.Map;

public class DynamicTokenFactory {

    private static Map<Class<? extends Token>, FactoryFunction> functionMap = Map.ofEntries(
        entry(Token.class, (type, grammaticalName, instanceName, lineNum, columnNum) -> new Token(grammaticalName, lineNum, columnNum)),
        entry(Identifier.class, (type, grammaticalName, instanceName, lineNum, columnNum) -> new Identifier(grammaticalName, instanceName, instanceName, lineNum, columnNum)), //TODO: Add type
        entry(Literal.class, (type, grammaticalName, instanceName, lineNum, columnNum) -> new Literal(grammaticalName, instanceName, lineNum, columnNum))
    );

    private static interface FactoryFunction {
        public Token run(
            Class<? extends Token> type,
            String grammaticalName,
            String instanceName,
            int lineNum,
            int columnNum
        );
    }
    
    public static Token create(
        Class<? extends Token> type,
        String grammaticalName,
        String instanceName,
        int lineNum,
        int columnNum
    ) {
        FactoryFunction function = functionMap.get(type);

        if(function == null) { throw new RuntimeException(type + " type not found"); }

        return function.run(type, grammaticalName, instanceName, lineNum, columnNum);
    }
}
