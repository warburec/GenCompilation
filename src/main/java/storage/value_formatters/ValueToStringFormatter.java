package storage.value_formatters;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.*;

import storage.storage_value_adapters.UnsupportedValueException;
import storage.storage_values.*;

public class ValueToStringFormatter implements ValueFormatter<String> {

    protected static final String KEY_VALUE_SEPERATOR = " : ";
    protected static final String INDENT = "  ";

    protected static final String ANY_WHITESPACE = "[\n\t ]*";

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
        if (formattedData.startsWith("\"")) return parseStringFormat(formattedData);
        if (formattedData.matches("[0-9]+.*")) return parseIntegerFormat(formattedData);
        if (formattedData.startsWith("{")) return parseMapFormat(formattedData);

        //TODO: Add more value types
        throw new UnsupportedValueException();
    }


    private String formatValue(StringStorageValue value) {
        return "\"" + 
            value.getValue().replace("\"", "\\\"").replace("\\", "\\\\") + 
            "\"";
    }

    private StringStorageValue parseStringFormat(String formattedData) {
        if (!formattedData.endsWith("\"")) throw new RuntimeException("The string " + formattedData + " must end with \"");

        formattedData = formattedData.substring(1, formattedData.length() - 1);
        formattedData = formattedData.replace("\\\\", "\\");

        return new StringStorageValue(formattedData);
    }

    private String formatValue(IntegerStorageValue value) {
        return String.valueOf(value.getValue());
    }

    private IntegerStorageValue parseIntegerFormat(String formattedData) {
        return new IntegerStorageValue(Integer.parseInt(formattedData));
    }
    
    private String formatValue(MapStorageValue value) {
        String out = "";
        Set<Entry<String, StorageValue<?>>> mapEntries = value.getValue().entrySet();

        if (mapEntries.isEmpty()) return "{}";

        for (Entry<String, StorageValue<?>> entry : mapEntries) {
            out += "\"" + entry.getKey() + ":" + entry.getValue() + ",\n";
        }

        out = out.substring(0, out.length() - 3); //Remove trailing ",\n"

        return "{\n" + indent(out) + "\n}";
    }

    private MapStorageValue parseMapFormat(String formattedData) {
        if (!formattedData.endsWith("}")) throw new RuntimeException("The map " + formattedData + " must end with }");
        if (formattedData.matches("\\{" + ANY_WHITESPACE + "\\}")) return new MapStorageValue(Map.of());

        formattedData = formattedData.substring(1, formattedData.length() - 1);
        formattedData = formattedData.strip();

        Map<String, StorageValue<?>> map = new HashMap<>();

        Pattern entryPattern = Pattern.compile(
            "\\\"(?<key>[a-zA-Z0-9_\\-]*?)\\\"" + ANY_WHITESPACE + ":" + ANY_WHITESPACE + "(?<value>\\{.*\\}|\\\".*?[^\\\\]\\\"|[0-9]+)(?=" + ANY_WHITESPACE + "(?:,|$))", 
            Pattern.DOTALL | Pattern.MULTILINE
        );
        Matcher entryMatcher = entryPattern.matcher(formattedData);

        while (entryMatcher.find()) {
            map.put(
                entryMatcher.group("key"), 
                parse(entryMatcher.group("value"))
            );
        }

        return new MapStorageValue(map);
    }

    private String indent(String string) {
        return INDENT + string.replace("\n", INDENT);
    }

    protected record ValueFormat(String startDelimiter, String internalRegex, String endDelimiter) {}
}
