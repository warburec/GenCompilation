package tests;


import static org.junit.Assert.*;
import org.junit.Test;

import helperObjects.*;

public class RegexFeatureCheckerTests {
    
    @Test
    public void constantBookends() {
        RegexFeatureChecker checker = new RegexFeatureChecker();
        String regex = "abc.*cba";

        NotEmptyTuple<String, String> actualBookends = checker.produceBookends(regex);

        NotEmptyTuple<String, String> expectedBookends = new NotEmptyTuple<String, String>(
            "abc",
            "cba"
        );

        assertEquals(expectedBookends, actualBookends);
    }
}
