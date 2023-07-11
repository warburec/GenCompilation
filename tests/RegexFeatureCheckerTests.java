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

    @Test
    public void escapedCharacters() {
        RegexFeatureChecker checker = new RegexFeatureChecker();
        String regex = "a\\*\\]?ccba";

        NotEmptyTuple<String, String> actualBookends = checker.produceBookends(regex);

        NotEmptyTuple<String, String> expectedBookends = new NotEmptyTuple<String, String>(
            "a\\*",
            "ccba"
        );

        assertEquals(expectedBookends, actualBookends);
    }

    @Test
    public void brackets() {
        RegexFeatureChecker checker = new RegexFeatureChecker();
        String regex = "(abc)(def)*(ghi)+";

        NotEmptyTuple<String, String> actualBookends = checker.produceBookends(regex);

        NotEmptyTuple<String, String> expectedBookends = new NotEmptyTuple<String, String>(
            "(abc)",
            "(ghi)"
        );

        assertEquals(expectedBookends, actualBookends);
    }

    @Test
    public void bracketsInBookends() {
        RegexFeatureChecker checker = new RegexFeatureChecker();
        String regex = "[abc](def)+[123]?(ghi)+";

        NotEmptyTuple<String, String> actualBookends = checker.produceBookends(regex);

        NotEmptyTuple<String, String> expectedBookends = new NotEmptyTuple<String, String>(
            "[abc](def)",
            "(ghi)"
        );

        assertEquals(expectedBookends, actualBookends);
    }

    @Test
    public void multipleDefiniteGroups() {
        RegexFeatureChecker checker = new RegexFeatureChecker();
        String regex = "(abc){1}(def)[ghi][123]{1}.*z";

        NotEmptyTuple<String, String> actualBookends = checker.produceBookends(regex);

        NotEmptyTuple<String, String> expectedBookends = new NotEmptyTuple<String, String>(
            "(abc)(def)[ghi][123]",
            "z"
        );

        assertEquals(expectedBookends, actualBookends);
    }

    @Test
    public void noStartBookend() {
        RegexFeatureChecker checker = new RegexFeatureChecker();
        String regex = "a?+[123]?(ghi)+";

        NotEmptyTuple<String, String> actualBookends = checker.produceBookends(regex);

        NotEmptyTuple<String, String> expectedBookends = null;

        assertEquals(expectedBookends, actualBookends);
    }

    @Test
    public void noEndBookend() {
        RegexFeatureChecker checker = new RegexFeatureChecker();
        String regex = "(def)+[123]?(ghi)*";

        NotEmptyTuple<String, String> actualBookends = checker.produceBookends(regex);

        NotEmptyTuple<String, String> expectedBookends = null;

        assertEquals(expectedBookends, actualBookends);
    }

    @Test
    public void nestedBrackets() {
        RegexFeatureChecker checker = new RegexFeatureChecker();
        String regex = "[a[b|c[ef]]][1[2.*][3]4](a[4(bc.*d)])d";

        NotEmptyTuple<String, String> actualBookends = checker.produceBookends(regex);

        NotEmptyTuple<String, String> expectedBookends = new NotEmptyTuple<String, String>(
            "[a[b|c[ef]]]",
            "d"
        );

        assertEquals(expectedBookends, actualBookends);
    }

    @Test
    public void definiteRangeInBrackets() {
        RegexFeatureChecker checker = new RegexFeatureChecker();
        String regex = "(ab|(cd){3}).*end";

        NotEmptyTuple<String, String> actualBookends = checker.produceBookends(regex);

        NotEmptyTuple<String, String> expectedBookends = new NotEmptyTuple<String, String>(
            "(ab|(cd){3})",
            "end"
        );

        assertEquals(expectedBookends, actualBookends);
    }

    @Test
    public void indefiniteRangeInBrackets() {
        RegexFeatureChecker checker = new RegexFeatureChecker();
        String regex = "(ab|(cd){3,7}).*end";

        NotEmptyTuple<String, String> actualBookends = checker.produceBookends(regex);

        NotEmptyTuple<String, String> expectedBookends = new NotEmptyTuple<String, String>(
            "(ab|(cd){3})",
            "end"
        );

        assertEquals(expectedBookends, actualBookends);
    }
}
