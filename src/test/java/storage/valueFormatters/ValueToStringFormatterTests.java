package storage.valueFormatters;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

import storage.storageValueAdapters.UnsupportedValueException;
import storage.storageValues.*;

public class ValueToStringFormatterTests {
    
    @Test
    public void parseFormattedString() {
        String str = "Java";
        String formattedString = "str:" + str;
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
        String formattedString = "int:" + Integer.toString(integer);
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        StorageValue<?> actualValue = valueFormatter.parse(formattedString);

        IntegerStorageValue expectedValue = new IntegerStorageValue(10);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void parseUnformattedInteger() {
        String str = "10";
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        assertThrows(UnsupportedValueException.class, () -> valueFormatter.parse(str));
    }

    @Test
    public void parseFormattedEmptyMap() {
        String formattedString1 = "map:{}";
        String formattedString2 = "map:{\n}";
        ValueFormatter<String> valueFormatter = new ValueToStringFormatter();

        StorageValue<?> actualValue1 = valueFormatter.parse(formattedString1);
        StorageValue<?> actualValue2 = valueFormatter.parse(formattedString2);

        MapStorageValue expectedValue = new MapStorageValue(Map.of());
        assertEquals(expectedValue, actualValue1);
        assertEquals(expectedValue, actualValue2);
    }

    //TODO: Test MapStorageValue with various contents (including nested maps)
    //TODO: Fomatting of StorageValues
}
