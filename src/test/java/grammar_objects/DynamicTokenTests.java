package grammar_objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DynamicTokenTests {
    
    @Test
    public void equality() {
        DynamicToken dynamicToken1 = new DynamicToken("A");
        DynamicToken dynamicToken2 = new DynamicToken("A");
        Token token = new Token("A");

        assertEquals(dynamicToken1, dynamicToken2);
        assertEquals(dynamicToken2, dynamicToken1);
        assertEquals(dynamicToken1, token);
        assertEquals(token, dynamicToken1);
    }

}
