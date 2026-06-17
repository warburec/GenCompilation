package storage.value_formatters;

import java.util.*;
import java.util.Map.Entry;

import storage.storage_value_adapters.UnsupportedValueException;
import storage.storage_values.*;

/**
 * A simple and limited ValueFormatter, not intended for storage of complex data
 * Parses and produces strings in the form { "entry1":value1, ..., "entryN":value }
 */
public class ValueToStringFormatter implements ValueFormatter<String> {

    protected static final String INDENT = "    ";
    protected static final String ANY_WHITESPACE = "[\n\t ]*";

    @Override
    public String format(StorageValue<?> value) throws UnsupportedValueException {
        if (value instanceof ListStorageValue) return formatValue((ListStorageValue)value);
        if (value instanceof MapStorageValue) return formatValue((MapStorageValue)value);
        if (value instanceof StringStorageValue) return formatValue((StringStorageValue)value);
        if (value instanceof IntegerStorageValue) return formatValue((IntegerStorageValue)value);

        //TODO: Add more value types and ensure types are differentiable from their stored formats
        throw new UnsupportedValueException(value);
    }

    /**
     * Produces a StorageValue representation of the provided data.
     * Note: Errors will not be thrown for incorrect values, ensure the input data is correctly formed.
     * @param formattedData The formatted data to be parsed.
     * @return The StorageValue representation of the input data.
     */
    @Override
    public StorageValue<?> parse(String formattedData) throws UnsupportedValueException {
        if (formattedData.startsWith("[")) return parseListFormat(formattedData);
        if (formattedData.startsWith("{")) return parseMapFormat(formattedData);
        if (formattedData.startsWith("\"")) return parseStringFormat(formattedData);
        if (formattedData.matches("[0-9]+.*")) return parseIntegerFormat(formattedData);
        
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

    private String formatValue(ListStorageValue value) {
        String out = "";
        List<StorageValue<?>> entries = value.getValue();

        if (entries.isEmpty()) return "[]";

        for (StorageValue<?> entry : entries) {
            out += format(entry) + ",\n";
        }

        out = out.substring(0, out.length() - 2); //Remove trailing ",\n"

        return "[\n" + indent(out) + "\n]";
    }

    private ListStorageValue parseListFormat(String formattedData) {
        formattedData = formattedData.substring(1, formattedData.length() - 1).trim();
        List<StorageValue<?>> list = new ArrayList<>();
        int i = 0;

        while (i < formattedData.length()) {
            int endOfValue = findNextElementBoundary(formattedData, i);
            list.add(parse(formattedData.substring(i, endOfValue).trim()));
            i = endOfValue + 1; // Skip the comma
        }

        return new ListStorageValue(list);
    }

    private String formatValue(MapStorageValue value) {
        String out = "";
        Set<Entry<String, StorageValue<?>>> mapEntries = value.getValue().entrySet();

        if (mapEntries.isEmpty()) return "{}";

        for (Entry<String, StorageValue<?>> entry : mapEntries) {
            out += "\"" + entry.getKey() + "\":" + format(entry.getValue()) + ",\n";
        }

        out = out.substring(0, out.length() - 2); //Remove trailing ",\n"

        return "{\n" + indent(out) + "\n}";
    }

    private MapStorageValue parseMapFormat(String formattedData) {
        if (!formattedData.endsWith("}")) throw new RuntimeException("The map " + formattedData + " must end with }");
        if (formattedData.matches("\\{" + ANY_WHITESPACE + "\\}")) return new MapStorageValue(Map.of());

        formattedData = formattedData.substring(1, formattedData.length() - 1).trim();
        Map<String, StorageValue<?>> map = new HashMap<>();
        int i = 0;

        while (i < formattedData.length()) {
            int colonIndex = formattedData.indexOf(':', i);
            String key = formattedData.substring(i, colonIndex).trim().replace("\"", "");
            
            int startOfValue = colonIndex + 1;
            int endOfValue = findNextElementBoundary(formattedData, startOfValue);
            
            map.put(key, parse(formattedData.substring(startOfValue, endOfValue).trim()));
            i = endOfValue + 1; // Skip the comma
        }

        return new MapStorageValue(map);
    }

    private int findNextElementBoundary(String formattedData, int start) {
        int depth = 0;
        boolean inQuotes = false;

        for (int i = start; i < formattedData.length(); i++) {
            char c = formattedData.charAt(i);

            if (c == '"' && (i == 0 || formattedData.charAt(i - 1) != '\\')) inQuotes = !inQuotes;
            if (inQuotes) continue;

            if (c == '{' || c == '[') depth++;
            else if (c == '}' || c == ']') depth--;
            else if (c == ',' && depth == 0) return i;
        }

        return formattedData.length();
    }

    private String indent(String string) {
        return INDENT + string.replace("\n", "\n" + INDENT);
    }

    protected record ValueFormat(String startDelimiter, String internalRegex, String endDelimiter) {}
}
