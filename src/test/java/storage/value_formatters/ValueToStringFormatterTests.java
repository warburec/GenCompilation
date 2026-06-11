package storage.value_formatters;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

import helper_objects.*;
import storage.storage_value_adapters.UnsupportedValueException;
import storage.storage_values.*;

public class ValueToStringFormatterTests {

    //NOTE: No exceptions for incorrectly formatted data were made. This will be left to more sophisticated ValueFormatters

    //#region parsing

    @Test
    public void parseFormattedString() {
        String str = "Java";
        String formattedString = "\"" + str + "\"";
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        StorageValue<?> actualValue = valueFormatter.parse(formattedString);

        StringStorageValue expectedValue = new StringStorageValue(str);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void parseUnformattedString() {
        String str = "Java";
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        assertThrows(UnsupportedValueException.class, () -> valueFormatter.parse(str));
    }

    @Test
    public void parseFormattedInteger() {
        Integer integer = 10;
        String formattedString = Integer.toString(integer);
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        StorageValue<?> actualValue = valueFormatter.parse(formattedString);

        IntegerStorageValue expectedValue = new IntegerStorageValue(10);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void parseFormattedEmptyMap() {
        String formattedString1 = "{}";
        String formattedString2 = "{\n}";
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        StorageValue<?> actualValue1 = valueFormatter.parse(formattedString1);
        StorageValue<?> actualValue2 = valueFormatter.parse(formattedString2);

        MapStorageValue expectedValue = new MapStorageValue(Map.of());
        assertEquals(expectedValue, actualValue1);
        assertEquals(expectedValue, actualValue2);
    }

    @Test
    public void parseFormattedSimpleMap() {
        Tuple<String, String> testString = new NotEmptyTuple<>("testStringKey", "testString");
        Tuple<String, Integer> testInt = new NotEmptyTuple<>("testIntKey", 30);
        String formattedString1 = "{\n" + 
        "    \"" + testString.value1() + "\":\"" + testString.value2() + "\",\n" +
        "    \"" + testInt.value1() + "\":" + testInt.value2() + "\n" +
        "}";
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        StorageValue<?> actualValue1 = valueFormatter.parse(formattedString1);

        MapStorageValue expectedValue = new MapStorageValue(Map.ofEntries(
            Map.entry(testString.value1(), new StringStorageValue(testString.value2())),
            Map.entry(testInt.value1(), new IntegerStorageValue(testInt.value2()))
        ));
        assertEquals(expectedValue, actualValue1);
    }

    @Test
    public void parseNestedMap() {
        Tuple<String, String> testString1 = new NotEmptyTuple<>("testStringKey1", "testString1");
        Tuple<String, String> testString2 = new NotEmptyTuple<>("testStringKey2", "testString2");
        Tuple<String, Integer> testInt = new NotEmptyTuple<>("testIntKey", 30);
        String formattedString1 = "{\n" + 
        "    \"innerMap\":{\n" +
        "        \"" + testString1.value1() + "\":\"" + testString1.value2() + "\",\n" +
        "        \"" + testInt.value1() + "\":" + testInt.value2() + "\n" +
        "    },\n" +
        "    \"" + testString2.value1() + "\":\"" + testString2.value2() + "\"\n" +
        "}";
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        StorageValue<?> actualValue1 = valueFormatter.parse(formattedString1);

        MapStorageValue expectedValue = new MapStorageValue(Map.ofEntries(
            Map.entry("innerMap", new MapStorageValue(Map.ofEntries(
                Map.entry(testString1.value1(), new StringStorageValue(testString1.value2())),
                Map.entry(testInt.value1(), new IntegerStorageValue(testInt.value2()))
            ))),
            Map.entry(testString2.value1(), new StringStorageValue(testString2.value2()))
        ));
        assertEquals(expectedValue, actualValue1);
    }

    @Test
    public void parseFormattedEmptyList() {
        String formattedString1 = "[]";
        String formattedString2 = "[\n]";
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        StorageValue<?> actualValue1 = valueFormatter.parse(formattedString1);
        StorageValue<?> actualValue2 = valueFormatter.parse(formattedString2);

        ListStorageValue expectedValue = new ListStorageValue(List.of());
        assertEquals(expectedValue, actualValue1);
        assertEquals(expectedValue, actualValue2);
    }

    @Test
    public void parseFormattedSimpleList() {
        String testString1 = "testString1";
        String testString2 = "testString2";
        Integer testInt = 30;
        String formattedString1 = "[\n" + 
        "    \"" + testString1 + "\",\n" +
        "    \"" + testString2 + "\",\n" +
        "    " + testInt + "\n" +
        "]";
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        StorageValue<?> actualValue1 = valueFormatter.parse(formattedString1);

        ListStorageValue expectedValue = new ListStorageValue(List.of(
            new StringStorageValue(testString1), 
            new StringStorageValue(testString2),
            new IntegerStorageValue(testInt)
        ));
        assertEquals(expectedValue, actualValue1);
    }

    @Test
    public void parseFormattedNestedList() {
        String testString1 = "testString1";
        String testString2 = "testString2";
        Integer testInt = 30;
        String formattedString1 = "[\n" + 
        "    [\n" +
        "    \"" + testString1 + "\",\n" +
        "    \"" + testString2 + "\"\n" +
        "    ],\n" +
        "    " + testInt + "\n" +
        "]";
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        StorageValue<?> actualValue1 = valueFormatter.parse(formattedString1);

        ListStorageValue expectedValue = new ListStorageValue(List.of(
            new ListStorageValue(List.of(
                new StringStorageValue(testString1), 
                new StringStorageValue(testString2)
            )),
            new IntegerStorageValue(testInt)
        ));
        assertEquals(expectedValue, actualValue1);
    }

    @Test
    public void parseFormattedNestedMapAndList() {
        Tuple<String, List<String>> testList = new NotEmptyTuple<>(
            "testStringKey1", 
            List.of(
                "testString2",
                "testString3"
            )
        );
        Tuple<String, Integer> testInt = new NotEmptyTuple<>("testIntKey", 30);
        String formattedString1 = "{\n" + 
        "    \"" + testList.value1() + "\":[\n" +
        "        \"" + testList.value2().get(0) + "\",\n" +
        "        \"" + testList.value2().get(1)+ "\",\n" +
        "    ],\n" +
        "    \"" + testInt.value1() + "\":\"" + testInt.value2() + "\"\n" +
        "}";
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        StorageValue<?> actualValue1 = valueFormatter.parse(formattedString1);

        MapStorageValue expectedValue = new MapStorageValue(Map.ofEntries(
            Map.entry(testList.value1(), new ListStorageValue(List.of(
                new StringStorageValue(testList.value2().get(0)),
                new StringStorageValue(testList.value2().get(1))
            ))),
            Map.entry(testInt.value1(), new IntegerStorageValue(testInt.value2()))
        ));
        assertEquals(expectedValue, actualValue1);
    }

    @Test
    public void parseFormattedNestedListAndMap() {
        Tuple<String, String> testString1 = new NotEmptyTuple<>("testStringKey1", "testString1");
        Tuple<String, Integer> testInt1 = new NotEmptyTuple<>("testStringKey2", 50);
        Integer testInt2 = 30;
        String formattedString1 = "[\n" + 
        "    {\n" +
        "        \"" + testString1.value1() + "\":\"" + testString1.value2() + "\",\n" +
        "        \"" + testInt1.value1() + "\":" + testInt1.value2() + "\n" +
        "    },\n" +
        "    " + testInt2 + "\n" +
        "]";
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        StorageValue<?> actualValue1 = valueFormatter.parse(formattedString1);

        ListStorageValue expectedValue = new ListStorageValue(List.of(
            new MapStorageValue(Map.ofEntries(
                Map.entry(testString1.value1(), new StringStorageValue(testString1.value2())),
                Map.entry(testInt1.value1(), new IntegerStorageValue(testInt1.value2()))
            )),
            new IntegerStorageValue(testInt2)
        ));
        assertEquals(expectedValue, actualValue1);
    }

    //#endregion

    //#region formatting

    @Test
    public void formatString() {
        String str = "Java";
        StringStorageValue stringValue = new StringStorageValue(str);
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        String actualValue = valueFormatter.format(stringValue);

        String expectedValue = "\"" + str + "\"";
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void formatInteger() {
        Integer integer = 10;
        IntegerStorageValue integerValue = new IntegerStorageValue(10);
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        String actualValue = valueFormatter.format(integerValue);

        String expectedValue = Integer.toString(integer);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void formatEmptyMap() {
        MapStorageValue mapValue = new MapStorageValue(Map.of());
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        String actualValue = valueFormatter.format(mapValue);

        String expectedValue = "{}";
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void formatSimpleMap() {
        Tuple<String, String> testString = new NotEmptyTuple<>("testStringKey", "testString");
        Tuple<String, Integer> testInt = new NotEmptyTuple<>("testIntKey", 30);
        MapStorageValue mapValue = new MapStorageValue(Map.ofEntries(
            Map.entry(testString.value1(), new StringStorageValue(testString.value2())),
            Map.entry(testInt.value1(), new IntegerStorageValue(testInt.value2()))
        ));
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        String actualValue = valueFormatter.format(mapValue);

        assertTrue(List.of(
                "{\n" +
                "    \"" + testString.value1() + "\":\"" + testString.value2() + "\",\n" +
                "    \"" + testInt.value1() + "\":" + testInt.value2() + "\n" +
                "}",

                "{\n" +
                "    \"" + testInt.value1() + "\":" + testInt.value2() + ",\n" +
                "    \"" + testString.value1() + "\":\"" + testString.value2() + "\"\n" +
                "}"
            )
            .contains(actualValue)
        );
    }

    @Test
    public void formatNestedMap() {
        Tuple<String, String> testString1 = new NotEmptyTuple<>("testStringKey1", "testString1");
        Tuple<String, String> testString2 = new NotEmptyTuple<>("testStringKey2", "testString2");
        Tuple<String, Integer> testInt = new NotEmptyTuple<>("testIntKey", 30);
        MapStorageValue mapValue = new MapStorageValue(Map.ofEntries(
            Map.entry("innerMap", new MapStorageValue(Map.ofEntries(
                Map.entry(testString1.value1(), new StringStorageValue(testString1.value2())),
                Map.entry(testInt.value1(), new IntegerStorageValue(testInt.value2()))
            ))),
            Map.entry(testString2.value1(), new StringStorageValue(testString2.value2()))
        ));
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        String actualValue = valueFormatter.format(mapValue);

        assertTrue(List.of(
                "{\n" + 
                "    \"innerMap\":{\n" +
                "        \"" + testString1.value1() + "\":\"" + testString1.value2() + "\",\n" +
                "        \"" + testInt.value1() + "\":" + testInt.value2() + "\n" +
                "    },\n" +
                "    \"" + testString2.value1() + "\":\"" + testString2.value2() + "\"\n" +
                "}",

                "{\n" + 
                "    \"innerMap\":{\n" +
                "        \"" + testInt.value1() + "\":" + testInt.value2() + ",\n" +
                "        \"" + testString1.value1() + "\":\"" + testString1.value2() + "\"\n" +
                "    },\n" +
                "    \"" + testString2.value1() + "\":\"" + testString2.value2() + "\"\n" +
                "}",

                "{\n" + 
                "    \"" + testString2.value1() + "\":\"" + testString2.value2() + "\",\n" +
                "    \"innerMap\":{\n" +
                "        \"" + testString1.value1() + "\":\"" + testString1.value2() + "\",\n" +
                "        \"" + testInt.value1() + "\":" + testInt.value2() + "\n" +
                "    }\n" +
                "}",

                "{\n" + 
                "    \"" + testString2.value1() + "\":\"" + testString2.value2() + "\",\n" +
                "    \"innerMap\":{\n" +
                "        \"" + testInt.value1() + "\":" + testInt.value2() + ",\n" +
                "        \"" + testString1.value1() + "\":\"" + testString1.value2() + "\"\n" +
                "    }\n" +
                "}"
            )
            .contains(actualValue)
        );
    }

    @Test
    public void formatEmptyList() {
        ListStorageValue mapValue = new ListStorageValue(List.of());
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        String actualValue = valueFormatter.format(mapValue);

        String expectedValue = "[]";
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void formatSimpleList() {
        String testString1 = "testString1";
        String testString2 = "testString2";
        Integer testInt = 30;
        ListStorageValue listValue = new ListStorageValue(List.of(
            new StringStorageValue(testString1), 
            new StringStorageValue(testString2),
            new IntegerStorageValue(testInt)
        ));
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        String actualValue = valueFormatter.format(listValue);

        String expectedValue = "[\n" + 
        "    \"" + testString1 + "\",\n" +
        "    \"" + testString2 + "\",\n" +
        "    " + testInt + "\n" +
        "]";
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void formatNestedList() {
        String testString1 = "testString1";
        String testString2 = "testString2";
        Integer testInt = 30;
        ListStorageValue listValue = new ListStorageValue(List.of(
            new ListStorageValue(List.of(
                new StringStorageValue(testString1), 
                new StringStorageValue(testString2)
            )),
            new IntegerStorageValue(testInt)
        ));
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        String actualValue = valueFormatter.format(listValue);

        String expectedValue = "[\n" + 
        "    [\n" +
        "    \"" + testString1 + "\",\n" +
        "    \"" + testString2 + "\"\n" +
        "    ],\n" +
        "    " + testInt + "\n" +
        "]";
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void formatNestedMapAndList() {
        Tuple<String, List<String>> testList = new NotEmptyTuple<>(
            "testStringKey1", 
            List.of(
                "testString2",
                "testString3"
            )
        );
        Tuple<String, Integer> testInt = new NotEmptyTuple<>("testIntKey", 30);
        MapStorageValue mapValue = new MapStorageValue(Map.ofEntries(
            Map.entry(testList.value1(), new ListStorageValue(List.of(
                new StringStorageValue(testList.value2().get(0)),
                new StringStorageValue(testList.value2().get(1))
            ))),
            Map.entry(testInt.value1(), new IntegerStorageValue(testInt.value2()))
        ));
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        String actualValue = valueFormatter.format(mapValue);

        assertTrue(List.of(
                "{\n" + 
                "    \"" + testList.value1() + "\":[\n" +
                "        \"" + testList.value2().get(0) + "\",\n" +
                "        \"" + testList.value2().get(1)+ "\",\n" +
                "    ],\n" +
                "    \"" + testInt.value1() + "\":\"" + testInt.value2() + "\"\n" +
                "}",

                "{\n" + 
                "    \"" + testInt.value1() + "\":\"" + testInt.value2() + "\",\n" +
                "    \"" + testList.value1() + "\":[\n" +
                "        \"" + testList.value2().get(0) + "\",\n" +
                "        \"" + testList.value2().get(1)+ "\",\n" +
                "    ]\n" +
                "}"
            )
            .contains(actualValue)
        );
    }

    @Test
    public void formatNestedListAndMap() {
        Tuple<String, String> testString1 = new NotEmptyTuple<>("testStringKey1", "testString1");
        Tuple<String, Integer> testInt1 = new NotEmptyTuple<>("testStringKey2", 50);
        Integer testInt2 = 30;
        ListStorageValue listValue = new ListStorageValue(List.of(
            new MapStorageValue(Map.ofEntries(
                Map.entry(testString1.value1(), new StringStorageValue(testString1.value2())),
                Map.entry(testInt1.value1(), new IntegerStorageValue(testInt1.value2()))
            )),
            new IntegerStorageValue(testInt2)
        ));
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        String actualValue = valueFormatter.format(listValue);

        assertTrue(List.of(
                "[\n" + 
                "    {\n" +
                "        \"" + testString1.value1() + "\":\"" + testString1.value2() + "\",\n" +
                "        \"" + testInt1.value1() + "\":" + testInt1.value2() + "\n" +
                "    },\n" +
                "    " + testInt2 + "\n" +
                "]",

                "[\n" + 
                "    {\n" +
                "        \"" + testInt1.value1() + "\":" + testInt1.value2() + ",\n" +
                "        \"" + testString1.value1() + "\":\"" + testString1.value2() + "\"\n" +
                "    },\n" +
                "    " + testInt2 + "\n" +
                "]"
            )
            .contains(actualValue)
        );
    }

    //#endregion
}
