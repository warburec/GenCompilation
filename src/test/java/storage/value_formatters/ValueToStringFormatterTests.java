package storage.value_formatters;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

import helper_objects.NotEmptyTuple;
import helper_objects.Tuple;
import storage.storage_value_adapters.UnsupportedValueException;
import storage.storage_values.*;

public class ValueToStringFormatterTests {

    //NOTE: No exceptions for incorrectly formatted data were made. This will be left to more sophisticated ValueFormatters

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

    //TODO: Fomatting of StorageValues

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
}
