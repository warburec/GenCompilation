package storage.valueFormatters;

import java.util.*;
import java.util.Map.Entry;

import storage.storageValueAdapters.UnsupportedValueException;
import storage.storageValues.*;
import helper_objects.ValueEnum;

public class ValueToStringFormatter implements ValueFormatter<String> {

    protected static final String KEY_VALUE_SEPERATOR = " : ";
    protected static final String INDENT = "  ";

    protected static enum PREFIXES implements ValueEnum<String> {
        STRING("str:"),
        INTEGER("int:"),
        MAP("map:");
        
        private String value;
        
        private PREFIXES(String value) {
            this.value = value;
        }
        
        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    boolean storageUsed = false;
    List<String> existingKeys = new ArrayList<String>();

    @Override
    public String format(StorageValue<?> value) throws UnsupportedValueException {
        if (value instanceof StringStorageValue) return formatValue((StringStorageValue)value);
        if (value instanceof IntegerStorageValue) return formatValue((IntegerStorageValue)value);
        if (value instanceof MapStorageValue) return formatValue((MapStorageValue)value);

        //TODO: Add more value types and ensure types are differentiatable from their stored formats
        throw new UnsupportedValueException(value);
    }

    @Override
    public StorageValue<?> parse(String formattedData) throws UnsupportedValueException {
        if (formattedData.startsWith(PREFIXES.STRING.getValue())) return parseStringFormat(formattedData);
        if (formattedData.startsWith(PREFIXES.INTEGER.getValue())) return parseIntegerFormat(formattedData);
        if (formattedData.startsWith(PREFIXES.MAP.getValue())) return parseMapFormat(formattedData);

        //TODO: Add more value types
        throw new UnsupportedValueException();
    }


    private String formatValue(StringStorageValue value) {
        String formattedString = PREFIXES.STRING.getValue();
        formattedString += value.getValue().replace("\\", "\\\\");
        
        return formattedString;
    }

    private StringStorageValue parseStringFormat(String formattedData) {
        formattedData = formattedData.replaceFirst(PREFIXES.STRING.getValue(), "");
        formattedData = formattedData.replace("\\\\", "\\");
        return new StringStorageValue(formattedData);
    }

    private String formatValue(IntegerStorageValue value) {
        return PREFIXES.INTEGER + String.valueOf(value.getValue());
    }

    private IntegerStorageValue parseIntegerFormat(String formattedData) {
        formattedData = formattedData.replaceFirst(PREFIXES.INTEGER.getValue(), "");
        return new IntegerStorageValue(Integer.parseInt(formattedData));
    }
    
    private String formatValue(MapStorageValue value) {
        Set<Entry<String, StorageValue<?>>> mapEntries = value.getValue().entrySet();
        if (mapEntries.isEmpty()) return "{}";

        String out = PREFIXES.MAP + "{\n";

        for (Entry<String, StorageValue<?>> entry : mapEntries) {
            out += INDENT + entry.getKey() + ":" + entry.getValue() + ",\n"; //TODO: Test for values containing "",\n"
        }

        out = out.substring(0, out.length() - 3); //Remove trailing ",\n"

        return out + "}";
    }

    private MapStorageValue parseMapFormat(String formattedData) {
        formattedData = formattedData.replaceFirst(PREFIXES.MAP.getValue(), "");

        if (formattedData.equals("{}") || formattedData.equals("{\n}"))
            return new MapStorageValue(Map.of());

        formattedData = formattedData.replaceFirst(PREFIXES.MAP.getValue() + "{\n", "");
        formattedData = formattedData.substring(0, formattedData.length() - 2); // Remove ending "}"

        Map<String, StorageValue<?>> map = new HashMap<>();

        for (String entry : formattedData.split(",\n")) {
            String[] parts = entry.split(":", 2);

            map.put(
                parts[0], 
                this.parseMapFormat(parts[1])
            );
        }

        return new MapStorageValue(map);
    }
}
